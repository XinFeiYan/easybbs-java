package com.jiang.annotation;

import com.jiang.Enums.UserOperFrequencyTypeEnum;

import java.lang.annotation.*;

//注解放在什么上面
@Target({ElementType.METHOD,ElementType.TYPE})
//生命周期，执行时参加编译
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
    UserOperFrequencyTypeEnum frequencyType() default UserOperFrequencyTypeEnum.NO_CHECK;

}
