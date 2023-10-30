package com.jiang.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
//生命周期
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GlobalInterceptor {
    /**
     * 是否需要登录
     */
    boolean checkLogin() default false;

    /**
     * 是否需要校验参数
     */
    boolean checkParams() default false;
    /**
     * 校验频次
     */


}
