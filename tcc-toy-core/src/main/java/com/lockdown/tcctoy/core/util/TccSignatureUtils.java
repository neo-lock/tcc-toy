package com.lockdown.tcctoy.core.util;

import java.lang.reflect.Method;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

public class TccSignatureUtils {
	
	
	public static String remoteSignature(Method method) {
		RequestMapping annotation = method.getAnnotation(RequestMapping.class);
		return remoteSignature(annotation.value()[0],annotation.method()[0].name());
	}

	public static String remoteSignature(String requestUrl,String requestMethod) {
		return DigestUtils.md5Hex(requestMethod+requestUrl);
	}
	
	
	public static String methodSignature(Method method) {
		return DigestUtils.md5Hex(method.toGenericString());
	}
}
