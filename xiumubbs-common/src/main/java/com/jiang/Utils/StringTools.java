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

    /*获取后置，包括.*/
    public static String getFileSuffix(String fileName){

        return fileName.substring(fileName.lastIndexOf("."));
    }
    public static String getFileName(String fileName){
        return fileName.substring(0,fileName.lastIndexOf("."));
    }
    /*转义内容*/
    public static String eecpapeHtml(String content){
        if(StringTools.isEmpty(content)){
            return content;
        }

        content = content.replace("<","&lt;");
        content = content.replace(" ","&nbsp;");
        content = content.replace("\n","<br>");

        return content;
    }
}
