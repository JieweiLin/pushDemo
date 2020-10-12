package com.example.pushdemo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.List;

/**
 * @author 林杰炜 Linjw
 * @Title TODO 类描述
 * @date 2020/5/21 16:47
 */
public class JsonUtils {
    public JsonUtils() {
    }

    public static String getJson(Object obj) {
        return JSON.toJSONString(obj, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect});
    }

    public static <T> T readValue(String json, Class<? extends T> cls) {
        try {
            return JSON.parseObject(json, cls);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static List readJson2List(String json, Class cls) {
        try {
            return JSON.parseArray(json, cls);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> readJson2Array(String json, Class<T> clazz) {
        try {
            return JSON.parseArray(json, clazz);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String getJsonExcludeProperties(Object obj, final String... params) {
        PropertyFilter filter = new PropertyFilter() {
            public boolean apply(Object source, String name, Object value) {
                for (int i = 0; i < params.length; ++i) {
                    if (params[i].equals(name)) {
                        return false;
                    }
                }

                return true;
            }
        };
        return JSON.toJSONString(obj, filter, new SerializerFeature[0]);
    }

    public static String getJsonIncludeProperties(Object obj, String... params) {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(obj.getClass(), params);
        return JSON.toJSONString(obj, filter, new SerializerFeature[0]);
    }
}
