package com.lockdown.tcctoy.core.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.lockdown.tcctoy.core.TransactionPermit;

public class RequestUtils {
	
	
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
	}
	
	
	public static boolean hasPermit() {
		String value = getRequest().getHeader(TransactionPermit.class.getSimpleName());
		return !StringUtils.isEmpty(value);
	}
	
	public static TransactionPermit getRequestPermit() {
		HttpServletRequest request = getRequest();
		String json = request.getHeader(TransactionPermit.class.getSimpleName());
		if(null==json) {
			return null;
		}
		try {
			return JSON.parseObject(json,TransactionPermit.class);
		}catch(Exception ex) {
			return null;
		}	
	}
	
	public static TransactionPermit getValidRequestPermit() {
		HttpServletRequest request = getRequest();
		String json = request.getHeader(TransactionPermit.class.getSimpleName());
		 
		if(null==json || StringUtils.isEmpty(json)) {
			return TransactionPermit.empty();
		}
		try {
			TransactionPermit permit =  JSON.parseObject(json,TransactionPermit.class);
			String remoteSignature = TccSignatureUtils.remoteSignature(request.getRequestURI(), request.getMethod());
			if(null!=permit && remoteSignature.equals(permit.getRemoteSignature())) {
				return permit;
			}else {
				return TransactionPermit.empty();
			}
		}catch(Exception ex) {
			return TransactionPermit.empty();
		}	
	}

}
