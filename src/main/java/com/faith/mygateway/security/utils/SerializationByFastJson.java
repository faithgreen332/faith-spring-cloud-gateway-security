package com.faith.mygateway.security.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Leeko
 * @date 2022/3/28
 **/
public class SerializationByFastJson {

    /**
     * QuoteFieldNames	输出key时是否使用双引号,默认为true
     * UseSingleQuotes	使用单引号而不是双引号,默认为false
     * WriteMapNullValue	是否输出值为null的字段,默认为false
     * WriteEnumUsingToString	Enum输出name()或者original,默认为false
     * UseISO8601DateFormat	Date使用ISO8601格式输出，默认为false
     * WriteNullListAsEmpty	List字段如果为null,输出为[],而非null
     * WriteNullStringAsEmpty	字符类型字段如果为null,输出为”“,而非null
     * WriteNullNumberAsZero	数值字段如果为null,输出为0,而非null
     * WriteNullBooleanAsFalse	Boolean字段如果为null,输出为false,而非null
     * SkipTransientField	如果是true，类中的Get方法对应的Field是transient，序列化时将会被忽略。默认为true
     * SortField	按字段名称排序后输出。默认为false
     * WriteTabAsSpecial	把\t做转义输出，默认为false	不推荐
     * PrettyFormat	结果是否格式化,默认为false
     * WriteClassName	序列化时写入类型信息，默认为false。反序列化是需用到
     * DisableCircularReferenceDetect	消除对同一对象循环引用的问题，默认为false
     * WriteSlashAsSpecial	对斜杠’/’进行转义
     * BrowserCompatible	将中文都会序列化为 unocode 格式，字节数会多一些，但是能兼容IE 6，默认为false
     * WriteDateUseDateFormat	全局修改日期格式,默认为false。JSON.DEFFAULT_DATE_FORMAT = “yyyy-MM-dd”;JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
     * DisableCheckSpecialChar	一个对象的字符串属性中如果有特殊字符如双引号，将会在转成json时带有反斜杠转移符。如果不需要转义，可以使用这个属性。默认为false
     */

    /**
     * 字符串序列化为字节数组
     *
     * @param str str
     * @return byte[]
     */
    public static byte[] stringToBytes(String str) {
        return JSONObject.toJSONBytes(str, SerializerFeature.WriteNullStringAsEmpty);

    }

    /**
     * 序列化对象为字节数组，用默认的就行，只要反序列化还是一样的，就没问题
     *
     * @param obj obj
     * @return byte[]
     */
    public static byte[] objectToBytes(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    /**
     * 字符串转化成字节数组
     *
     * @param bytes
     * @return
     */
    public static String bytesToStr(byte[] bytes) {
        return JSONObject.toJSONString(new String(bytes));
    }

    public static <T> T byteToObj(byte[] bytes, Class<T> clazz) {
        return JSONObject.parseObject(JSONArray.parseObject(JSONArray.toJSONString(bytes), byte[].class), clazz);
    }

    public static <T> T requestBodyToBean(Flux<DataBuffer> body, Class<T> type) {
        CountDownLatch downLatch = new CountDownLatch(1);
        AtomicReference<String> str = new AtomicReference<>();
        body.doOnComplete(downLatch::countDown).subscribe(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            str.set(new String(bytes, StandardCharsets.UTF_8));
        });
        //  使用 coutdownLatch去阻塞让subscirbe订阅完
        try {
            downLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(str.get(), type);
    }
}
