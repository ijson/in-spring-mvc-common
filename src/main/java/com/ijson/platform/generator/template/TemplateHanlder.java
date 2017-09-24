package com.ijson.platform.generator.template;


import com.ijson.platform.generator.model.ParamsVo;
import com.ijson.platform.generator.model.TableEntity;

import java.util.Map;

public interface TemplateHanlder {

    /**
     * 代码生成器模板接口
     *
     * @param vo 方法参数
     */
     void execute(ParamsVo<TableEntity> vo, Map<String, String> config);
}
