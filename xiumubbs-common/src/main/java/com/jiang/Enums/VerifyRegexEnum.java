package com.jiang.Enums;

public enum VerifyRegexEnum {
    NO("", "不校验"),
    EMAIL("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}", "邮箱"),
    IP("((?:(?:25[0-5]|2[0-4]//d|[01]?//d?//d)//.){3}(?:25[0-5]|2[0-4]//d|[01]?//d?//d))","IP地址"),
    PHONE("(1[0-9])\\d{9}$","手机号码"),
    ACCOUNT("^[0-9a-zA-Z_]{1,}$","字母开头，由数字、英文字母或者下划线组成"),
    PASSWORD("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,16}$", "密码由八-十六位组成,至少包含数字大写和小写字母");

    private String regex;
    private String desc;

    VerifyRegexEnum(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }

    public String getRegex() {
        return regex;
    }

    public String getDesc() {
        return desc;
    }
}
