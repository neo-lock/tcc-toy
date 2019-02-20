package com.lockdown.tcctoy.core;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lockdown.tcctoy.api.exception.MethodSignatureMismatchException;
import com.lockdown.tcctoy.core.util.TccSignatureUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;


public class TccRequestInterceptor implements RequestInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void apply(RequestTemplate template) {
		TransactionPermit permit = TccTransactionManager.getRemotePermit();
		if(!TccTransactionManager.hasTransaction() || null == permit) {
			return ;
		}
		
		String remoteSignature = TccSignatureUtils.remoteSignature(getRequestUri(template.request().url()), template.request().httpMethod().name());
		logger.info(" {} remote signature {}"+remoteSignature);
		if(!remoteSignature.equals(permit.getRemoteSignature())) {
			throw new MethodSignatureMismatchException(" permit signature mismatch !");
		}
		template.header(TransactionPermit.class.getSimpleName(),JSON.toJSONString(permit));
	}
	
	
	private String getRequestUri(String url) {
		return url.substring(0, url.indexOf("?"));
	}

}
