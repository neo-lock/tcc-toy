package com.lockdown.tcctoy.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ATccTransaction  {
	
	Class<?> confirmClass() default Void.class;
	
	Class<?> cancelClass() default Void.class;
	
	String confirmMethodName();
	
	String cancelMethodName();
	
}
