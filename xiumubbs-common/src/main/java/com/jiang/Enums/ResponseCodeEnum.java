package com.jiang.Enums;

public enum ResponseCodeEnum {
    CODE_200(200,"请求成功"),
    CODE_400(400,"请求参数错误"),
    CODE_404(404,"请求地址不存在"),
    CODE_409(409,"信息已存在" ),
    CODE_600(600,"请求参数错误"),
    CODE_601(601,"信息已存在"),
    CODE_602(602,"上传超过限制"),
    CODE_500(500,"服务器返回错误,请联系管理员"),
    CODE_900(900,"服务器请求超时"),
    CODE_901(901,"登录超时"),
    CODE_902(902,"请登录");
    private Integer code;
    private String msg;
    ResponseCodeEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
