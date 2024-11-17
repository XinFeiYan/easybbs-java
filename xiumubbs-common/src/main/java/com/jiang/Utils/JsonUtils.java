package com.jiang.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonUtils {
    public static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 对象转json
     */
    public static String convertObj2Json(Object obj){
        return JSON.toJSONString(obj);
    }

    /**
     * json转对象，可以转map
     */
    public static <T> T convertJson2Obj(String json,Class<T> classz){

        return JSONObject.parseObject(json,classz);
    }

    /**
     * 字符串数组转集合
     */
    public static <T> List<T> convertJsonArray2List(String json,Class<T> classz){
        return JSONArray.parseArray(json,classz);
    }
}
