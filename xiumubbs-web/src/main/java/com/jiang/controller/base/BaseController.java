package com.jiang.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.*;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.SessionWebUserDto;
import com.jiang.entity.vo.PaginationResultVO;
import com.jiang.entity.vo.ResponseVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class BaseController<T> {
    protected static final String STATIC_SUCCESS = "success";
    protected static final String STATIC_ERROR = "error";
    @Resource
    private RedisTemplate redisTemplate;

    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> ResponseVO = new ResponseVO<>();
        ResponseVO.setStatus(STATIC_SUCCESS);
        ResponseVO.setCode((ResponseCodeEnum.CODE_200.getCode()));
        ResponseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        ResponseVO.setData(t);
        return ResponseVO;
    }

    protected <T> ResponseVO getErrorResponseVO(T t) {
        ResponseVO<T> ResponseVO = new ResponseVO<>();
        ResponseVO.setStatus(STATIC_ERROR);
        ResponseVO.setCode((ResponseCodeEnum.CODE_400.getCode()));
        ResponseVO.setInfo(ResponseCodeEnum.CODE_400.getMsg());
        ResponseVO.setData(t);
        return ResponseVO;
    }
    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected  SessionWebUserDto getUserInfo4TL(){
        return UserHolder.getUser();
    }
    protected SessionWebUserDto getUserInfo4Session(HttpSession session){
        /**
         *做一个登录判断
         */
        /*String sessionId = CookieUtil.readLoginToken(request);
        if(StringTools.isEmpty(sessionId)){
            return null;
        }
        //可能会返回null
        SessionWebUserDto sessionWebUserDto  = JsonUtils.convertJson2Obj((String)redisTemplate.opsForValue().get(sessionId),SessionWebUserDto.class);
        if(sessionWebUserDto!=null){
            redisTemplate.expire(sessionId, 30,TimeUnit.MINUTES);
        }*/
        //SessionWebUserDto sessionWebUserDto = (SessionWebUserDto)session.getAttribute(Constants.SESSION_KEY);
        SessionWebUserDto sessionWebUserDto = getUserInfo4TL();
        return sessionWebUserDto;
    }

    protected SessionWebUserDto getUserInfo4Token(String token){
        if(StringTools.isEmpty(token)){
            throw new BusinessException("token失败");
        }
        Map<String,Object> stringObjectMap = JWTUtil.checkToken(token);
        if(stringObjectMap==null){
            throw new BusinessException("token无效");
        }

        String userJson = (String)redisTemplate.opsForValue().get("TOKEN_"+token);
        if(StringTools.isEmpty(userJson)){
            throw new BusinessException("token过期了");
        }

        return JSON.parseObject(userJson,SessionWebUserDto.class);
    }
    protected <S, T> PaginationResultVO<T> convert2PaginationVO(PaginationResultVO<S> result, Class<T> classz) {
        PaginationResultVO<T> resultVO = new PaginationResultVO<>();
        resultVO.setList(CopyTools.copyList(result.getList(),classz));
        resultVO.setPageNo(result.getPageNo());
        resultVO.setPageSize(result.getPageSize());
        resultVO.setPageTotal(result.getPageTotal());
        resultVO.setTotalCount(result.getTotalCount());
        return resultVO;
    }
}
