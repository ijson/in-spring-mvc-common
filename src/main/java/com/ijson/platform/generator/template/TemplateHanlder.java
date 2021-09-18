package com.ijson.platform.generator.template;


import com.ijson.platform.api.model.ExtPv;
import com.ijson.platform.generator.model.TableEntity;

import java.util.Map;

public interface TemplateHanlder {

    /**
     * 代码生成器模板接口
     *
     * @param config config
     * @param vo 方法参数
     */
     void execute(ExtPv<TableEntity> vo, Map<String, String> config);
}
