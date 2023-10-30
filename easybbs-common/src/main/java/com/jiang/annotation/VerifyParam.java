package com.jiang.annotation;

import com.jiang.Enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//FIELD放在属性上面
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyParam {
    //是否传值,true要传值
    boolean required() default false;
    int max() default -1;
    int min() default -1;
    /**
     * 正则
     */
    VerifyRegexEnum regex() default  VerifyRegexEnum.NO;





















}
