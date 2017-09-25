package com.ijson.platform.generator.template;


import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.TemplateUtil;

import java.util.Map;

public class PomXMlBuilder implements TemplateHanlder {

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        createdPomXml(config);
    }

    public void createdPomXml(Map<String, String> config) {
        String projectName = config.get("project_name");
        String xmlPath = config.get("fs_path") + "/" + projectName + "/";
        FileOperate.getInstance().newCreateFolder(xmlPath);
        FileOperate.getInstance().newCreateFile(xmlPath + "pom.xml", getPomStr(config));
        //生成.gitignore 文件,内部包含以下
        FileOperate.getInstance().newCreateFile(xmlPath + ".gitignore", getGitignoreStr(config));
        FileOperate.getInstance().newCreateFile(xmlPath + "..gitlab-ci.yml", getGitlabCi(config));
        //生成README.md 文件,内部包含以下
        FileOperate.getInstance().newCreateFile(xmlPath + "README.md", getReadmeStr(config));
        FileOperate.getInstance().newCreateFolder(xmlPath + "src/main/java/");
        FileOperate.getInstance().newCreateFolder(xmlPath + "src/main/resources/");
    }


    private String getPomStr(Map<String, String> config) {
        return TemplateUtil.getTemplate("pom.ijson",config);
    }


    private String getReadmeStr(Map<String, String> config) {
        return TemplateUtil.getTemplate("readme.ijson",config);
    }

    private String getGitlabCi(Map<String, String> config) {
        return TemplateUtil.getTemplate("gitlab-ci.ijson",config);
    }


    private String getGitignoreStr(Map<String, String> config) {
        return TemplateUtil.getTemplate("gitignore.ijson",config);
    }

}
