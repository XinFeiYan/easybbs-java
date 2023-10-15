package com.jiang.Exception;

import com.jiang.Enums.ResponseCodeEnum;


public class BusinessException extends RuntimeException {
    private ResponseCodeEnum codeEnum;
    private Integer code;
    private String msg;
    public BusinessException(String msg,Throwable e){
        super(msg,e);
        this.msg=msg;
    }
    public BusinessException(String msg){
        super(msg);
        this.msg=msg;
    }
    public BusinessException(ResponseCodeEnum codeEnum){
        super(codeEnum.getMsg());
        this.msg=codeEnum.getMsg();
        this.code=codeEnum.getCode();
        this.codeEnum=codeEnum;
    }
    public BusinessException(Throwable e){
        super(e);
    }
    public BusinessException(Integer code,String msg){
        super(msg);
        this.code=code;
        this.msg=msg;
    }
    public  ResponseCodeEnum getCodeEnum(){return  codeEnum;}
    public Integer getCode(){return  code;}
    @Override
    public String getMessage(){return msg;}
    /**
     *
     */
    @Override
    public Throwable fillInStackTrace(){return  this;}
}
