package com.jiang.aspect;


import com.jiang.Enums.ResponseCodeEnum;

import com.jiang.Exception.BusinessException;

import com.jiang.Utils.StringTools;

import com.jiang.Utils.VerifyUtils;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;

import com.jiang.entity.constants.Constants;
import com.jiang.entity.vo.ResponseVO;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


@Component
@Aspect
public class OperationAspect {
    public static final Logger logger = LoggerFactory.getLogger(OperationAspect.class);
    private static final String[] TYPE_BASE = {"jiang.lang.String","jiang.lang.Integer","jiang.lang.Long"};
    //导入切面，定义切点，添加这个注解才拦截
    //放在不是同一个包下拿不到
    @Pointcut("@annotation(com.jiang.annotation.GlobalInterceptor)")
    private void requestInterceptor(){

    }
    //切面的校验方法,环绕通知会让确定方法是否最终返回
    //前置通知主要用于在方法调用之前进行一些准备工作，最终方法还会执行
    @Before("requestInterceptor()")
    public void interceptorDo(JoinPoint point) throws Exception {
        try{
            //拿到对象
            Object target = point.getTarget();
            //拿到参数名
            Object[] arguments = point.getArgs();
            //拿到方法名
            String methodName = point.getSignature().getName();
            //方法的参数类型。
            Class<?>[] parameterTypes = ((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
            //通过反射机制拿到里面的方法
            Method method = target.getClass().getMethod(methodName,parameterTypes);
            //获取到方法上面的注解
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if(null == interceptor){
                return ;
            }
            //校验参数
            if(interceptor.checkParams()){
                validateParams(method,arguments);
            }
            //上面成功后，切面执行原来的方法，继续走下去，返回方法执行结果
        }catch(BusinessException e){
            throw e;
        }catch(Exception e){
            logger.error("全局拦截器异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        } catch (Throwable e) {
            logger.error("全局拦截器异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }

    private void validateParams(Method method,Object[] arguments){
        //获取方法参数类型
        Parameter[] parameters = method.getParameters();
        for(int i = 0;i<parameters.length;i++){
            Parameter parameter = parameters[i];
            Object value= arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
            if(verifyParam==null){
                continue;
            }
            /**
             * 类型匹配,如果类型是这几种，就做验证
             */
            if(ArrayUtils.contains(TYPE_BASE,parameter.getParameterizedType().getTypeName())){
                    checkValue(value,verifyParam);
            }
        }
    }

    public void checkValue(Object value,VerifyParam verifyParam){
        //为空判断
        Boolean isEmpty = value==null|| StringTools.isEmpty(value.toString());
        Integer length = value==null?0:value.toString().length();

        /**
         * 校验空
         */
        if(isEmpty&&verifyParam.required()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        /**
         * 校验长度
         * 不为空,不为-1，这代表不校验
         */
        if(!isEmpty&&((verifyParam.max()!=-1 && verifyParam.max()>=length) || (verifyParam.min()!=-1&&verifyParam.min()<=length) )){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        /**
         * 校验正则
         */
        if(!isEmpty&& !StringTools.isEmpty(verifyParam.regex().getRegex()) &&
                !VerifyUtils.verify(verifyParam.regex(),String.valueOf(value))){
            throw new  BusinessException(ResponseCodeEnum.CODE_600);
        }
    }














}
