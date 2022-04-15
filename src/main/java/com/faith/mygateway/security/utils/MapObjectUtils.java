package com.faith.mygateway.security.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * JedisCluster 用的 jdk 自带的序列化，本来想换成阿里的 fastJson，写了一段代码，在反序列化 Map<byte[],byte[])的时候太麻烦了，拉到了，这个类没用
 *
 * @author Leeko
 * @date 2022/3/28
 **/
public class MapObjectUtils {

    /**
     * 用 fastJson 把对象转为字节数组的 map
     *
     * @param obj
     * @return
     */
    public Map<byte[], byte[]> beanToByteMapViaFastJson(Object obj) {

        Map<byte[], byte[]> map = new HashMap<>();

        beanToMapSkipNullValue(obj).forEach((key, value) -> {
            map.put(SerializationByFastJson.stringToBytes(key), SerializationByFastJson.objectToBytes(value));
        });
        return map;
    }

//    public static <T> T byteMapToBean(Map<byte[], byte[]> map, Class<T> clazz) {
//        Map<String,Object>
//    }

    /**
     * 对象转成 map 属性名是 key，属性值是 value 的数组，跳过 value 为 null 的 key-value 对
     *
     * @param bean bean
     * @return Map<String, Object>
     */
    public static Map<String, Object> beanToMapSkipNullValue(Object bean) {

        Map<String, Object> map = new HashMap<>();

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        assert beanInfo != null;
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            String propertyName = descriptor.getName();
            Object propertyValue = null;
            try {
                propertyValue = descriptor.getReadMethod().invoke(bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            if ("class".equals(propertyName) || propertyValue == null) {
                continue;
            }
            map.put(propertyName, propertyValue);
        }
        return map;
    }

    /**
     * value存在空 java类转url
     *
     * @param bean
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String convertBeanToUrl(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                sb.append(propertyName + "=");
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    sb.append(result + "&");
                } else {
                    sb.append("&");
                }
            }
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new HashMap<String, String>(0);
        if (StringUtils.isEmpty(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将str字符串转换为json对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, String> toMapStr(String jsonStr) {
        Map<String, String> map = new HashMap<>();
        JSONObject parseObject = JSON.parseObject(jsonStr);
        for (String key : parseObject.keySet()) {
            map.put(key, StringUtils.isEmpty(parseObject.get(key)) ? "" : parseObject.get(key).toString());
        }
        return map;
    }

    /**
     * map 转换url请求参数
     *
     * @param map
     * @return
     */
    public static String buildMap(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                sb.append(key + "=");
                if (StringUtils.isEmpty(map.get(key))) {
                    sb.append("&");
                } else {
                    sb.append(map.get(key) + "&");
                }
            }
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
