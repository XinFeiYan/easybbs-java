package com.jiang.aspect;

import com.jiang.Enums.DateTimePatternEnum;
import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Enums.UserOperFrequencyTypeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.*;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;

import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.dto.SysSettingDto;
import com.jiang.entity.query.ForumArticleQuery;
import com.jiang.entity.query.ForumCommentQuery;
import com.jiang.entity.query.LikeRecordQuery;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.service.ForumArticleService;
import com.jiang.service.ForumCommentService;
import com.jiang.service.LikeRecordService;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

@Component
@Aspect
public class OperationAspect {
    public static final Logger logger = LoggerFactory.getLogger(OperationAspect.class);
    private static final String[] TYPE_BASE = {"jiang.lang.String","jiang.lang.Integer","jiang.lang.Long"};
    @Resource
    private ForumArticleService forumArticleService;
    @Resource
    private ForumCommentService forumCommentService;
    @Resource
    private LikeRecordService likeRecordService;
    //定义切点，添加这个注解才拦截， 指定了切点表达式，表示要匹配所有带有 @GlobalInterceptor 注解的方法
    //放在不是同一个包下拿不到，定义一个公用切点
    @Pointcut("@annotation(com.jiang.annotation.GlobalInterceptor)")
    private void requestInterceptor(){

    }
    //导入切点使用，
    @Around("requestInterceptor()")
    public Object interceptorDo(ProceedingJoinPoint point) throws Exception {
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
                return null;
            }
            //校验登录
            if(interceptor.checkLogin()){
                checkLogin();
            }
            //校验参数
            if(interceptor.checkParams()){
                validateParams(method,arguments);
            }
            /**
             * 频次校验
             */
            this.checkFrequency(interceptor.frequencyType());
            //上面成功后，切面执行原来的方法，继续走下去，返回方法执行结果
            Object pointResult = point.proceed();
            if(pointResult instanceof ResponseVO){
                ResponseVO responseVO = (ResponseVO)pointResult;
                if(Constants.STATUS_SUCCESS.equals(responseVO.getStatus())){
                    this.addOpCount(interceptor.frequencyType());
                }
            }
            return pointResult;
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
    private void checkFrequency(UserOperFrequencyTypeEnum typeEnum){
        if(typeEnum==null||typeEnum==UserOperFrequencyTypeEnum.NO_CHECK){
            return;
        }
        //获取到session,通过全局变量
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = httpServletRequest.getSession();
        //SessionWebUserDto webUserDto = (SessionWebUserDto)session.getAttribute(Constants.SESSION_KEY);
        SessionWebUserDto webUserDto = UserHolder.getUser();
        //通过session，存入当前的一天的数量限制
        String curDate = DateUtil.format(new Date(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
        String sessionKey = Constants.SESSION_KEY_FREQUENCY+curDate+typeEnum.getOperType();
        Integer count = (Integer)session.getAttribute(sessionKey);
        SysSettingDto sysSettingDto = SysCacheUtils.getSysSetting();

        switch(typeEnum){
            case POST_ARTICLE:
                if(count==null){
                    ForumArticleQuery forumArticleQuery =new ForumArticleQuery();
                    forumArticleQuery.setUserId(webUserDto.getUserId());
                    forumArticleQuery.setPostTimeStart(curDate);
                    forumArticleQuery.setPostTimeEnd(curDate);
                    count = forumArticleService.findCountByParam(forumArticleQuery);
                }
                if(count>=sysSettingDto.getPostSetting().getPostDayCountThreshold()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
                break;
            case POST_COMMENT:
                if(count==null){
                    ForumCommentQuery forumCommentQuery =new ForumCommentQuery();
                    forumCommentQuery.setUserId(webUserDto.getUserId());
                    forumCommentQuery.setPostTimeStart(curDate);
                    forumCommentQuery.setPostTimeEnd(curDate);
                    count = forumCommentService.findCountByParam(forumCommentQuery);
                }
                if(count>=sysSettingDto.getCommentSetting().getCommentDayCountThreshold()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
                break;
            case DO_LIKE:
                if(count==null){
                    LikeRecordQuery likeRecordQuery =new LikeRecordQuery();
                    likeRecordQuery.setUserId(webUserDto.getUserId());
                    likeRecordQuery.setCreateTimeStart(curDate);
                    likeRecordQuery.setCreateTimeEnd(curDate);
                    count = likeRecordService.findCountByParam(likeRecordQuery);
                }
                if(count>=sysSettingDto.getLikeSetting().getLikeDayCountThreshold()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
                break;
            //没有使用redis做缓存，如果重启项目图片会情况
            case IMAGE_UPLOAD:
                if(count==null){
                   count=0;
                }
                if(count>=sysSettingDto.getPostSetting().getDayImageUploadCount()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
                break;
        }
        session.setAttribute(sessionKey,count);

    }
    //验证成功后，存在session中的count加1
    private void addOpCount(UserOperFrequencyTypeEnum typeEnum){
        if(typeEnum==null||typeEnum==UserOperFrequencyTypeEnum.NO_CHECK){
            return;
        }

        //获取到session,通过全局变量
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = httpServletRequest.getSession();
        //通过session，加1
        String curDate = DateUtil.format(new Date(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
        String sessionKey = Constants.SESSION_KEY_FREQUENCY+curDate+typeEnum.getOperType();
        Integer count = (Integer)session.getAttribute(sessionKey);
        session.setAttribute(sessionKey,count+1);
    }
    //登录检测
    private void checkLogin(){
        //全部变量，获取到request的
        /*HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Object obj = session.getAttribute(Constants.SESSION_KEY);*/
        Object obj = UserHolder.getUser();
        if(obj==null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
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
