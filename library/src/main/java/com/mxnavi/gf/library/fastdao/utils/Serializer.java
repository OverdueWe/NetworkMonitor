package com.mxnavi.gf.library.fastdao.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 描述 ：序列化工具
 *
 * @author Mark
 * @date 2019.04.25
 */
public class Serializer {

    /**
     * 这里由两个方法，一个是对象序列化为字节数组，一个是字节数组序列化为对象，这里需要注意的是对象的serialVersionUID
     * 必须设置为一样才能序列化，否则哪怕两个类长得一模一样都会报错。
     *
     * @param obj
     * @return
     */
    public static byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();

            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }


    /**
     * byte转对象
     *
     * @param bytes
     * @return
     */
    public static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);


            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 这里选择了fastJson来序列化对象，实现方式如下：
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */

    public static <T> T get(String data, Class<T> clazz) {
        T body = JSON.parseObject(data, clazz);
        return body;
    }


    public static String toJson(Object o) {
        return JSONObject.toJSONString(o);
    }

    /***********************************************************
     下面对性能进行了对比：
     类别	    1000次	5000次	10000次	100000次	 1000000次
     byte数组	 82ms	147ms	 213ms	 827ms	   5707ms
     fastJson	114ms	 157ms	 146ms	 174ms	    412ms
     ***********************************************************/

}
