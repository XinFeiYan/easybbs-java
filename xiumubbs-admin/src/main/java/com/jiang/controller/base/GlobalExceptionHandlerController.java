package com.jiang.controller.base;

import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.entity.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandlerController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler(value = Exception.class)
    Object HandleException(Exception e, HttpServletRequest request) {
        logger.info("请求错误，请求地址{},错误信息：", request.getRequestURI(), e);
        ResponseVO ajaxResponse = new ResponseVO();
        //404
        if (e instanceof NoHandlerFoundException) {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_404.getMsg());
            ajaxResponse.setStatus(STATIC_ERROR);
        } else if (e instanceof BusinessException) {
            //业务错误
            BusinessException biz = (BusinessException) e;
            ajaxResponse.setCode(biz.getCode());
            ajaxResponse.setInfo(biz.getMessage());
            ajaxResponse.setStatus(STATIC_ERROR);
        } else if (e instanceof BindException) {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_400.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_400.getMsg());
            ajaxResponse.setStatus(STATIC_ERROR);
        } else if (e instanceof DuplicateKeyException) {
            //主键冲突
            ajaxResponse.setCode(ResponseCodeEnum.CODE_409.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_409.getMsg());
            ajaxResponse.setStatus(STATIC_ERROR);
        } else {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_500.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_500.getMsg());
            ajaxResponse.setStatus(STATIC_ERROR);
        }
        return ajaxResponse;
    }
}
