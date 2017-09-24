package com.ijson.platform.generator;

import com.ijson.config.ConfigFactory;
import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.generator.dao.LoadDaoFactory;
import com.ijson.platform.generator.manager.CodeGeneratorManager;
import com.ijson.platform.generator.manager.LoadManagerFactory;
import com.ijson.platform.generator.model.TableEntity;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class CoderTest {

    @Test
    public void generatorCode() {
        ConfigFactory.getConfig("generator", (config) -> {
            Map<String, String> configMap = config.getAll();
            CodeGeneratorManager codeGeneratorManager = LoadManagerFactory.getInstance().getCodeGeneratorManager();
            String tableNames[] = config.get("builder_tables").split(",");
            List<TableEntity> result = LoadDaoFactory.getInstance().getDao("Mysql").getTables(tableNames,configMap);
            ParamsVo<TableEntity> vo = new ParamsVo<>();
            vo.setObjs(result);
            codeGeneratorManager.execute(vo, configMap);
        });
    }

}
