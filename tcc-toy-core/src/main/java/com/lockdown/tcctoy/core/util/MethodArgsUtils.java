package com.lockdown.tcctoy.core.util;

import com.alibaba.fastjson.JSON;

public class MethodArgsUtils {
	
	
	public static Object[] convertArgs(Class<?>[] argTypes,Object[] argValues) {
		if(argTypes.length != argValues.length) {
			throw new IllegalArgumentException(" args type length not equal to values length !");
		}
		if(argTypes.length == 0 ) {
			return argValues;
		}
		
		Object[] converted = new Object[argValues.length];
		
		for(int i=0;i<argValues.length;i++) {
			converted[i] = JSON.parseObject(JSON.toJSONString(argValues[i]),argTypes[i]);
		}
		return converted;
	}

}
