package com.ijson.platform.generator.manager.impl;

import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.manager.CodeGeneratorManager;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.template.TemplateHanlder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class CodeGeneratorManagerImpl implements CodeGeneratorManager {

    private List<TemplateHanlder> hanlders;

    /**
     * @param vo     方法参数
     * @param config 配置文件
     */
    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        String prefix = "src/main/";
        vo.setParams("prefix", prefix);
        if (!Validator.isEmpty(hanlders)) {
            for (TemplateHanlder hanlder : hanlders) {
                hanlder.execute(vo, config);
            }
        } else {
            log.error("生成器模板接口没有被注入成功");
        }
    }

    public void setHanlders(List<TemplateHanlder> hanlders) {
        this.hanlders = hanlders;
    }

}
