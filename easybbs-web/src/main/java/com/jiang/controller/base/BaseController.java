package com.jiang.controller.base;

import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.entity.vo.ResponseVO;


public class BaseController<T> {
    protected static final String STATIC_SUCCESS = "success";
    protected static final String STATIC_ERROR = "error";

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
}
