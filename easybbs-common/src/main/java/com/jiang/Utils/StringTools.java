package com.jiang.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

//验证参数
public class StringTools {
    public static Boolean isEmpty(String str){
        if(null==str||"".equals(str.trim())||"null".equalsIgnoreCase(str)){
            return true;
        }else{
            return false;
        }
    }

    public static String getRandomString(Integer count){
        return RandomStringUtils.random(count,true,true);
    }

    public static String getRandomNumber(Integer count){
        return RandomStringUtils.random(count,false,true);
    }

    /*密码加密*/
    public static String encodeMd5(String sourceStr){
        /*apache里面的md5加密*/
        return StringTools.isEmpty(sourceStr)?null: DigestUtils.md5Hex(sourceStr);
    }
}
