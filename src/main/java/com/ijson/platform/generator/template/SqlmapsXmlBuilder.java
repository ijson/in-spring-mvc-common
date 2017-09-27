
package com.ijson.platform.generator.template;

import com.google.common.collect.Maps;
import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.TemplateUtil;

import java.util.List;
import java.util.Map;

/**
 * 生成myibatis的sqlmaps的xml模板
 */
public class SqlmapsXmlBuilder implements TemplateHanlder {

    private String pkId = "";

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createdSqlmapsXml(prefix, tables, config);
    }

    /**
     * description: 生成sqlmaps.xml文件
     *
     * @param tables tables
     * @param prefix prefix
     * @param config configs
     */
    public void createdSqlmapsXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String projectName = config.get("project_name");
        String xmlPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "resources/ibatis/sqlmaps/";
        FileOperate.getInstance().newCreateFolder(xmlPath);
        if (!Validator.isEmpty(tables)) {
            for (TableEntity table : tables) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("package_name", jarPath);
                map.put("tableName", table.getTableAttName());
                map.put("table_name", table.getTableName());
                map.put("table", table);

                String context = TemplateUtil.getTemplate("mapping.ijson", map);
                FileOperate.getInstance().newCreateFile(xmlPath + table.getTableAttName() + "Mapper.xml", context);
            }
        }
    }
}
