package com.lockdown.tcctoy.core;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.util.ClassUtils;

import com.lockdown.tcctoy.core.util.SpringBeanUtils;

public class MethodInvocation implements Serializable {
	
	private static final long serialVersionUID = 2336794508145284641L;

	private Class<?> targetClass;
	
	private String methodName;
	
	private Class<?>[] parameterTypes;
	
	private Object[] args;
	
	
	

	public MethodInvocation() {
	}



	public MethodInvocation(Class<?> targetClass, String methodName, Class<?>[] parameterTypes,
			Object[] args) {
		this.targetClass = targetClass;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.args = args;
	}
	
	



	public Class<?> getTargetClass() {
		return targetClass;
	}



	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}


	public String getMethodName() {
		return methodName;
	}



	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}



	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}



	public Object[] getArgs() {
		return args;
	}



	public void setArgs(Object[] args) {
		this.args = args;
	}



	public void invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object targetObj = SpringBeanUtils.getBean(targetClass);
		Method method;
		if(null==parameterTypes||parameterTypes.length==0) {
			method = ClassUtils.getMethod(targetObj.getClass(), methodName);
		}else {
			method = ClassUtils.getMethod(targetObj.getClass(), methodName, parameterTypes);
		}
		//method.invoke(targetObj, MethodArgsUtils.convertArgs(parameterTypes, args));
		method.invoke(targetObj, args);
	}

}
