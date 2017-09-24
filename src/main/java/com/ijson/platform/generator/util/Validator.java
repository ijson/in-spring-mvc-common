package com.ijson.platform.generator.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * description:  验证工具类
 *
 * @author cuiyongxu 创建时间：Oct 8, 2015
 */
@Slf4j
public class Validator {

    public static final String BLANK = "";

    public static final String NULL = "null";

    /**
     * description:获取UUID主键生成器的方法
     * 生成规则：32位字符串由本机的IP+时间无符号右移8位（当前中时间）+当前短时间+当前长时间+计数器 组成
     *
     * @return 返回UUID32位字符串
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public synchronized static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * description: 判断对象数组是否为空
     *
     * @param object 对象数组
     * @return 如果object为空返回true，不为空返回false
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static boolean isNull(Object[] object) {
        if (object == null || object.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * description: 判断一个字符串是否为空,为空则返回默认字符串
     *
     * @param str        传入的字符串
     * @param defaultStr 默认字符串
     * @return 如果str为空返回defaultStr，不为空返回str
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static String isNull(String str, String defaultStr) {
        if (isNull(str))
            return defaultStr;
        return str;
    }

    /**
     * description: 判断一个字符串是否为空
     *
     * @param str 传入的字符串
     * @return 如果str为空返回true，不为空返回false
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static boolean isNull(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        if ((str.equals(NULL)) || (str.equals(BLANK))) {
            return true;
        }
        return false;
    }

    /**
     * description:  判断字符串不能为空
     *
     * @param str 传入的字符串
     * @return 如果str不为空返回true，为空返回false
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    /**
     * description:  description: 判断字符串是否为空，如果为空取给定的默认值
     *
     * @param text       传入的字符串
     * @param defaultStr 给定的默认值
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static String getDefaultStr(String text, String defaultStr) {
        if (Validator.isNull(text))
            return defaultStr;
        return text;
    }

    /**
     * description:判断对象是否为空
     *
     * @param obj 传入的对象
     * @return 如果obj为空返回true，不为空返回false
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    /**
     * description:   判断一个list集合是否为空
     *
     * @param list 传入的list
     * @return 如果list为空或者长度为0返回true，不为空返回false
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static boolean isEmpty(@SuppressWarnings("rawtypes") List list) { //
        return list == null || list.size() == 0;
    }

    /**
     * description: 判断一个list集合是否为空
     *
     * @param collection 传入的集合
     * @return 如果collection为空或者长度为0返回true，不为空返回false
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static boolean isEmpty(@SuppressWarnings("rawtypes") Collection collection) { //
        return collection == null || collection.isEmpty();
    }

    /**
     * description: 获取字符串字节长度
     *
     * @param str 传入的字符串
     * @return 如果字符串为空返回长度为0，否则返回字符串的字节长度
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static int getStrByteLength(String str) {
        if (isNull(str)) {
            return 0;
        } else {
            return str.getBytes().length;
        }
    }

    /**
     * description: 获取Poperties文件
     *
     * @param filePath 资源文件路径
     * @return
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static Properties getProperties(String filePath, String fileName) {
        Properties prop = null;
        FileInputStream fileInput = null;
        try {
            File file = new File(filePath + fileName);
            if (file.exists()) {// 判断文件夹是否存在
                prop = new Properties();
                fileInput = new FileInputStream(filePath + fileName);
                prop.load(fileInput);
            }
        } catch (IOException e) {
            try {
                InputStream in = Validator.class.getClass().getResourceAsStream(fileName);
                prop.load(in);
                in.close();
            } catch (IOException e1) {
               log.error("ERROR:",e);
            }
        } finally {
            try {
                if (null != fileInput)
                    fileInput.close();
            } catch (Exception e) {
                log.error("ERROR:",e);
            }
        }
        return prop;
    }

    /**
     * description:  返回classes的绝对路径
     *
     * @author heppy1.com
     * @update Jun 29, 2015
     */
    public static String getClassLoaderPath() {
        String path = "";
        URL classLoaderUrl = Thread.currentThread().getContextClassLoader().getResource("");
        if (Validator.isEmpty(classLoaderUrl)) {
            path = Thread.currentThread().getContextClassLoader().getResource("/").getPath();//tomcat/weblogic
        } else {
            path = classLoaderUrl.getPath();
        }
        if (Validator.isNotNull(path)) {
            if (path.startsWith(File.separator) || path.startsWith("/")) {
                String systemType = getSystemType();
                if ("windows".equals(systemType))
                    path = path.substring(1);
            }
            if (!path.endsWith(File.separator) && !path.endsWith("/"))
                path += "/";
        }
        return path;
    }

    /**
     * description:获取操作系统类型
     *
     * @return 返回操作系统类型
     * @author heppy1.com
     * @update Jun 29, 2015
     */
    public static String getSystemType() {
        String result = "windows";
        if (System.getProperty("os.name").equals("Linux")) {
            result = "linux";
        } else if ("Mac OS X".equals(System.getProperty("os.name"))) {
            result = "Mac OS X";
        }
        return result;
    }

    /**
     * description:  对象深度克隆
     *
     * @param o 待克隆对象
     * @return 返克隆后的新对象--由于common包升级,造成此方法暂时无法使用
     * @author heppy1.com
     * @update Jun 29, 2015
     */
    //@Deprecated
    public static Object clone(Object o) {
        Object ret = null;
        try {
            // save the object to a byte array
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(o);
            out.close();
            // read a clone of the object from byte array
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bin);
            ret = in.readObject();
            in.close();
        } catch (Exception e) {
            try {
                ret = BeanUtils.cloneBean(o);
            } catch (Exception e1) {
                log.error("ERROR:",e);
                ret = null;
            }
        }
        return ret;
    }

    /**
     * description:  对string进行md5加密
     *
     * @param s md5加密后的 数据值
     * @return
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public final static String makeMd5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error("ERROR:",e);
            return null;
        }
    }

    /**
     * Assert that an object is not <code>null</code> .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     *
     * @param object  the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that an object is not <code>null</code> .
     * <pre class="code">Assert.notNull(clazz);</pre>
     *
     * @param object the object to check
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }
}
