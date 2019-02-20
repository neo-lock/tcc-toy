package com.lockdown.tcctoy.starter;

import javax.sql.DataSource;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import com.lockdown.tcctoy.support.JdbcTransactionRepository;
import com.lockdown.tcctoy.support.QuartzTransactionRecovery;
import com.lockdown.tcctoy.api.support.TccTransactionRecovery;
import com.lockdown.tcctoy.api.support.TccTransactionRepository;
import com.lockdown.tcctoy.api.support.TccTransactionSerializer;
import com.lockdown.tcctoy.core.ProtostuffTccTransactionSerializer;
import com.lockdown.tcctoy.core.SimpleOptimisticLockManager;
import com.lockdown.tcctoy.core.SimpleTransactionRecovery;
import com.lockdown.tcctoy.core.TccRequestInterceptor;
import com.lockdown.tcctoy.core.TccTransactionManager;
import com.lockdown.tcctoy.core.aspectj.TccRemoteParticipantAspect;
import com.lockdown.tcctoy.core.aspectj.TccTransactionAspect;
import com.lockdown.tcctoy.core.processor.BranchTransactionProcessor;
import com.lockdown.tcctoy.core.processor.RootTransactionProcessor;
import com.lockdown.tcctoy.core.processor.TccTransactionTypeProcessor;
import com.lockdown.tcctoy.core.processor.TransactionTypeProcessor;
import com.lockdown.tcctoy.core.util.SpringBeanUtils;

import feign.RequestInterceptor;

@Configuration
@EnableConfigurationProperties({StarterTccProperties.class})
public class TccToyAutoConfiguration {
	
	@Autowired
	private StarterTccProperties properties;
	
	@Bean
	@Primary
    @ConfigurationProperties(prefix="spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	
	@Bean(name = "tccDataSource")
    @Qualifier("tccDataSource")
    @ConfigurationProperties(prefix="tcc.transaction.datasource")
    public DataSource tccDataSource() {
        return DataSourceBuilder.create().build();
    }
	
	@Bean
	@ConditionalOnMissingBean
	public TccTransactionRepository repository(@Qualifier("tccDataSource")DataSource datasource,TccTransactionSerializer transactionSerializer) {
		if(StringUtils.isEmpty(properties.getTableName())) {
			throw new IllegalArgumentException(" tcc.transaction.table-name cannot be empty !");
		}
		JdbcTransactionRepository repository  = new JdbcTransactionRepository(transactionSerializer,datasource,properties.getTableName());
		repository.setDomain(properties.getDomain());
		repository.setDatasource(datasource);
		TccTransactionRepository manager = new SimpleOptimisticLockManager(repository);
		manager.setDomain(properties.getDomain());
		return manager;
	}
	
	
	
	/**
	 * 当前对象本不必实例化,
	 * 1.如果有必要，可以额外控制对象的变量或者生命周期
	 * 2.可以检查对象是否配置完毕
	 * @param repository
	 * @return
	 */
	@Bean
	public TccTransactionManager tccTransactionManager(TccTransactionRepository repository,TccTransactionRecovery recovery) {
		TccTransactionManager.setRepository(repository);
		return new TccTransactionManager();
	}
	
	@Bean
	public TccTransactionRecovery tccTransactionRecovery(TccTransactionRepository repository) {
		return new SimpleTransactionRecovery(repository, properties);
	}
	
	
	
	@Bean
	public TransactionTypeProcessor branchTransactionProcessor() {
		return new BranchTransactionProcessor();
	}
	@Bean
	public TransactionTypeProcessor rootTransactionProcessor() {
		return new RootTransactionProcessor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public TccTransactionTypeProcessor tccTransactionTypeProcessor() {
		return  new TccTransactionTypeProcessor();
	}

	
	@Bean
	@ConditionalOnMissingBean
	public TccTransactionAspect tccTransactionAspect(TccTransactionTypeProcessor processor) {
		return new TccTransactionAspect(processor);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public TccTransactionSerializer tccTransactionSerializer() {
		//return new SimpleTccTransactionSerializer();
		return new ProtostuffTccTransactionSerializer();
	}
	
	
	@Bean
	@ConditionalOnMissingBean
	public TccRemoteParticipantAspect tccRemoteParticipantAspect() {
		return new TccRemoteParticipantAspect();
	}
	
	
	@Bean
	@ConditionalOnMissingBean
	public RequestInterceptor tccRequestInterceptor() {
		return new TccRequestInterceptor();
	}
	
	@Bean
	public SpringBeanUtils springBeanUtils() {
		return new SpringBeanUtils();
	}


	
	@Bean
    public JobDetail myJobDetail(){
        JobDetail jobDetail = JobBuilder.newJob(QuartzTransactionRecovery.class)
                .withIdentity("recoveryJob","RecoveryTransaction")
                .storeDurably()
                .build();
        return jobDetail;
    }
	
	
	
    @Bean
	@ConditionalOnProperty(prefix = "tcc.transaction",name = "enable-recovery",havingValue = "true",matchIfMissing = false)
    public Trigger myTrigger(){
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(myJobDetail())
                .withIdentity("recoveryTrigger","RecoveryTransaction")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(properties.getRecoverIntervalSeconds()).repeatForever())
                .build();
        return trigger;
    }
    
    
	
	
}
