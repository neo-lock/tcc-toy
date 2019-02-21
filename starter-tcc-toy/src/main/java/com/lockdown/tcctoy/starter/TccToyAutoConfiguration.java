package com.lockdown.tcctoy.starter;

import javax.sql.DataSource;

import com.lockdown.tcctoy.support.SchedulerFactoryBeanListener;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
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
import org.terracotta.quartz.wrappers.JobFacade;

import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties({StarterTccProperties.class,QuartzRecoverProperties.class})
public class TccToyAutoConfiguration {
	
	@Autowired
	private StarterTccProperties properties;

	@Autowired
	private QuartzRecoverProperties quartzRecoverProperties;


	private Logger logger = LoggerFactory.getLogger(getClass());

	
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


	/**
	 * 如果不开启enable,可以不需要当前properties
	 * @return
	 */
	@Bean
	@Primary
	@ConditionalOnProperty(name = "tcc.transaction.recovery.quartz.enable",havingValue = "true",matchIfMissing = true)
	@ConfigurationProperties("spring.quartz")
	public QuartzProperties quartzProperties(){
		return new QuartzProperties();
	}



	@Bean("recoverJobDetailFactoryBean")
	public JobDetailFactoryBean jobDetailFactoryBean(ApplicationContext applicationContext){
		JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
		jobDetailFactoryBean.setName(quartzRecoverProperties.getJobDetailName());
		jobDetailFactoryBean.setGroup(properties.getDomain());
		jobDetailFactoryBean.setApplicationContext(applicationContext);
		jobDetailFactoryBean.setDurability(true);
		jobDetailFactoryBean.setJobClass(QuartzTransactionRecovery.class);
		return jobDetailFactoryBean;
	}

	@Bean("recoverTriggerFactoryBean")
	public SimpleTriggerFactoryBean simpleTriggerFactoryBean(@Qualifier("recoverJobDetailFactoryBean") JobDetailFactoryBean recoverJobDetailFactoryBean){
		SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
		simpleTriggerFactoryBean.setGroup(properties.getDomain());
		simpleTriggerFactoryBean.setJobDetail(Objects.requireNonNull(recoverJobDetailFactoryBean.getObject()));
		simpleTriggerFactoryBean.setName(quartzRecoverProperties.getJobTriggerName());
		simpleTriggerFactoryBean.setRepeatInterval(properties.getRecoverIntervalSeconds()*1000);
		return simpleTriggerFactoryBean;
	}


	@Bean
	public JobFactory jobFactory(){
		return new TccQuartzJobBeanFactory();
	}

	@Bean
	@Primary
	public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties quartzProperties,DataSource dataSource,ApplicationContext applicationContext,JobFactory jobFactory) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setWaitForJobsToCompleteOnShutdown(false);
		factory.setOverwriteExistingJobs(true);
		factory.setSchedulerName(quartzRecoverProperties.getSchedulerName());
		factory.setJobFactory(jobFactory);
		factory.setApplicationContext(applicationContext);
		logger.info(" schedulerFactoryBean set properties !");
		Properties properties = new Properties();
		quartzProperties.getProperties().forEach(properties::setProperty);
		factory.setQuartzProperties(properties);
		logger.info(" schedulerFactoryBean set datasource !");
		factory.setDataSource(dataSource);
		applicationContext.getBeansOfType(SchedulerFactoryBeanListener.class).values().forEach(schedulerFactoryBeanListener -> schedulerFactoryBeanListener.schedulerFactoryBeanCreated(factory));
		return factory;
	}

	@Bean
	public SchedulerFactoryBeanListener recoverSchedulerListener(@Qualifier("recoverTriggerFactoryBean") SimpleTriggerFactoryBean simpleTriggerFactoryBean){
		return bean -> {
			bean.setAutoStartup(quartzRecoverProperties.isEnable());
			if(quartzRecoverProperties.isEnable()){
				bean.setTriggers(simpleTriggerFactoryBean.getObject());
			}
		};
	}













	
}
