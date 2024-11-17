package com.jiang.Utils;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.jiang.Enums.VerifyRegexEnum;
import com.jiang.annotation.VerifyParam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtils {
    public static Boolean verify(String regs,String value){
        if(StringTools.isEmpty(value)){
            return false;
        }
        Pattern pattern = Pattern.compile(regs);
        //校验正则
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static Boolean verify(VerifyRegexEnum verifyRegexEnum, String value){
        return verify(verifyRegexEnum.getRegex(),value);
    }
}
