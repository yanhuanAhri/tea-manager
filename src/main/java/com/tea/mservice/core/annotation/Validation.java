package com.tea.mservice.core.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Validation {

	String name() default "";

	long min() default Long.MIN_VALUE;

	long max() default Long.MAX_VALUE;

	boolean email() default false;

	boolean nullable() default true;

	@SuppressWarnings("rawtypes")
	Class<? extends Enum> enumClass() default Enum.class;

	String regex() default "";
}
