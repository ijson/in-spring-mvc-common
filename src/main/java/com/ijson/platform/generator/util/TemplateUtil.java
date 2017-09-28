package com.ijson.platform.generator.util;

import org.apache.commons.io.IOUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by cuiyongxu on 17/9/25.
 */
public class TemplateUtil {


    public static String getTemplate(String tmpName, Map params) {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg;
        String value = null;
        try {
            cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            String io = IOUtils.toString(TemplateUtil.class.getClassLoader().getResourceAsStream("template/" + tmpName));
            Template tmp = gt.getTemplate(io);
            tmp.binding(params);
            value = tmp.render();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    /**
     * 首字母转小写
     *
     * @param s string
     * @return 首字母转小写
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     *
     * @param s string
     * @return 首字母转大写
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
