package com.ijson.platform.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * description:  负责反射数据库的一切事物,主要辅助Hibernate
 *
 * @author cuiyongxu 创建时间：Dec 14, 2015
 */
@Slf4j
public class ReflectDB {

    private static ReflectDB instance;

    private ReflectDB() {
    }

    public static ReflectDB getInstance() {
        if (null == instance) {
            instance = new ReflectDB();
        }
        return instance;

    }

    /**
     * description: 设置对象属性值,暂支持string,Integer
     *
     * @param obj  值对象,内部存在值
     * @param obj2 对象-要转的对象,为空对象
     * @return
     * @author cuiyongxu
     * @update Dec 14, 2015
     */
    public Object cloneObj(Object obj, Object obj2) {
        if (obj == null) {
            log.error("未查询到结果集");
            return null;
        }
        Field[] field = obj2.getClass().getDeclaredFields();
        Object[] obn = (Object[]) obj;
        try {
            for (int j = 0; j < field.length; j++) {
                String name = field[j].getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                String type = field[j].getGenericType().toString();
                if (type.equals("class java.lang.String")) {
                    Method m = obj2.getClass().getMethod("set" + name, String.class);
                    String value = (String) obn[j];
                    m.invoke(obj2, value);
                } else if (type.equals("class java.lang.Integer") || type.equals("int")) {
                    Method m = obj2.getClass().getMethod("set" + name, Integer.class);
                    Integer value = (Integer) obn[j];
                    m.invoke(obj2, value);
                } else if (type.equals("class java.util.Date")) {
                    Method m = obj2.getClass().getMethod("set" + name, Date.class);
                    Date value = (Date) obn[j];
                    m.invoke(obj2, value);
                } else if (type.equals("class java.lang.Boolean") || type.equals("boolean")) {
                    Method m = obj2.getClass().getMethod("set" + name, Boolean.class);
                    Boolean value = (Boolean) obn[j];
                    m.invoke(obj2, value);
                } else if (type.equals("class java.lang.Character") || type.equals("char")) {
                    Method m = obj2.getClass().getMethod("set" + name, Character.class);
                    Character value = (Character) obn[j];
                    m.invoke(obj2, value);
                } else if (type.equals("class java.lang.Long") || type.equals("long")) {
                    Method m = obj2.getClass().getMethod("set" + name, Long.class);
                    Long value = (Long) obn[j];
                    m.invoke(obj2, value);
                } else if (type.equals("class java.lang.Float") || type.equals("float")) {
                    Method m = obj2.getClass().getMethod("set" + name, Float.class);
                    Float value = (Float) obn[j];
                    m.invoke(obj2, value);
                } else if (type.equals("class java.lang.Double") || type.equals("double")) {
                    Method m = obj2.getClass().getMethod("set" + name, Double.class);
                    Double value = (Double) obn[j];
                    m.invoke(obj2, value);
                } else {
                    if (type.contains("class ")) {
                        String className = type.substring(6, type.length());
                        Class clazz = Class.forName(className);
                        Object clazzObj = clazz.newInstance();
                        Method m = obj2.getClass().getMethod("set" + name, clazzObj.getClass());
                        m.invoke(obj2, obn[j]);
                    } else {
                        log.error("该类型暂未检索:" + type + ",请联系平台组!");
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | SecurityException | InstantiationException | ClassNotFoundException e) {
            log.error("错误:", e);
        }
        return obj2;
    }
}
