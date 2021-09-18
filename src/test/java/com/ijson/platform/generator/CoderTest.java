package com.ijson.platform.generator;

import com.google.common.collect.Maps;
import com.ijson.config.ConfigFactory;
import com.ijson.platform.api.model.ExtPv;
import com.ijson.platform.generator.dao.LoadDaoFactory;
import com.ijson.platform.generator.manager.CodeGeneratorManager;
import com.ijson.platform.generator.manager.LoadManagerFactory;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.TemplateUtil;
import org.apache.commons.io.IOUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CoderTest {

    @Test
    public void generatorCode() {
        ConfigFactory.getConfig("generator", (config) -> {
            Map<String, String> configMap = config.getAll();
            CodeGeneratorManager codeGeneratorManager = LoadManagerFactory.getInstance().getCodeGeneratorManager();
            String tableNames[] = config.get("builder_tables").split(",");
            List<TableEntity> result = LoadDaoFactory.getInstance().getDao("Mysql").getTables(tableNames, configMap);
            ExtPv<TableEntity> vo = new ExtPv<>();
            vo.setObjs(result);
            codeGeneratorManager.execute(vo, configMap);
        });
    }

    @Test
    public void beetlGen() throws IOException {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        String io = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("template/pom.ijson"));
        Template t = gt.getTemplate(io);
        t.binding("project_name", "in-aaaa");
        String str = t.render();
        System.out.println(str);
    }


    @Test
    public void beetlGen3()  {
        Map aa = Maps.newHashMap();
        aa.put("project_name", "in-aaaa");
        System.out.println(TemplateUtil.getTemplate("pom.ijson",aa));
    }

}
