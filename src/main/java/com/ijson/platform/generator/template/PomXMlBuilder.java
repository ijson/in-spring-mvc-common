package com.ijson.platform.generator.template;


import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.generator.model.TableEntity;

import java.util.Map;

public class PomXMlBuilder implements TemplateHanlder {

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        //List<TableEntity> tables = vo.getObjs();
        //String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
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
        //FileOperate.getInstance().newCreateFolder(xmlPath + "src/test/java/");
        //FileOperate.getInstance().newCreateFolder(xmlPath + "src/test/resources/");


    }


    private String getPomStr(Map<String, String> config) {
        StringBuilder result = new StringBuilder("");
        String jarPath = config.get("package_name");
        result.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" + "    <parent>\n" + "        <artifactId>in-spring-mvc</artifactId>\n" + "        <groupId>com.ijson</groupId>\n" + "        <version>1.0.0-SNAPSHOT</version>\n" + "    </parent>\n" + "    <modelVersion>4.0.0</modelVersion>\n" + "\n" + "    <artifactId>").append(config.get("project_name")).append("</artifactId>\n").append("    <packaging>jar</packaging>\n").append("\n").append("    <name>").append(config.get("project_name")).append("</name>\n").append("    <url>http://www.ijson.com</url>\n").append("\n").append("\n").append("    <dependencies>\n").append("        <dependency>\n").append("            <groupId>com.google.guava</groupId>\n").append("            <artifactId>guava</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.slf4j</groupId>\n").append("            <artifactId>slf4j-api</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.projectlombok</groupId>\n").append("            <artifactId>lombok</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>ch.qos.logback</groupId>\n").append("            <artifactId>logback-classic</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>aopalliance</groupId>\n").append("            <artifactId>aopalliance</artifactId>\n").append("            <version>1.0</version>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.springframework</groupId>\n").append("            <artifactId>spring-core</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.springframework</groupId>\n").append("            <artifactId>spring-beans</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.springframework</groupId>\n").append("            <artifactId>spring-webmvc</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.springframework</groupId>\n").append("            <artifactId>spring-web</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.springframework</groupId>\n").append("            <artifactId>spring-context</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.springframework</groupId>\n").append("            <artifactId>spring-orm</artifactId>\n").append("            <version>4.2.6.RELEASE</version>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.springframework</groupId>\n").append("            <artifactId>spring-context-support</artifactId>\n").append("            <version>4.2.6.RELEASE</version>\n").append("        </dependency>\n").append("        <dependency>\n" +
                "            <groupId>javax.servlet</groupId>\n" +
                "            <artifactId>javax.servlet-api</artifactId>\n" +
                "            <version>3.1.0</version>\n" +
                "            <scope>compile</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>javax.servlet</groupId>\n" +
                "            <artifactId>jstl</artifactId>\n" +
                "            <version>1.2</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.glassfish.web</groupId>\n" +
                "            <artifactId>jstl-impl</artifactId>\n" +
                "            <version>1.2</version>\n" +
                "            <exclusions>\n" +
                "                <exclusion>\n" +
                "                    <artifactId>servlet-api</artifactId>\n" +
                "                    <groupId>javax.servlet</groupId>\n" +
                "                </exclusion>\n" +
                "                <exclusion>\n" +
                "                    <artifactId>jsp-api</artifactId>\n" +
                "                    <groupId>javax.servlet.jsp</groupId>\n" +
                "                </exclusion>\n" +
                "                <exclusion>\n" +
                "                    <artifactId>jstl-api</artifactId>\n" +
                "                    <groupId>javax.servlet.jsp.jstl</groupId>\n" +
                "                </exclusion>\n" +
                "            </exclusions>\n" +
                "        </dependency>\n").append("        <dependency>\n" +
                "        \t<groupId>com.ijson</groupId>\n" +
                "        \t<artifactId>in-spring-mvc-common</artifactId>\n" +
                "        \t<version>1.0.0</version>\n" +
                "        </dependency>\n").append("        <dependency>\n").append("            <groupId>commons-logging</groupId>\n").append("            <artifactId>commons-logging</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>commons-fileupload</groupId>\n").append("            <artifactId>commons-fileupload</artifactId>\n").append("            <version>1.2</version>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>commons-beanutils</groupId>\n").append("            <artifactId>commons-beanutils</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>com.fasterxml.jackson.core</groupId>\n").append("            <artifactId>jackson-core</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>com.fasterxml.jackson.core</groupId>\n").append("            <artifactId>jackson-databind</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>com.alibaba</groupId>\n").append("            <artifactId>druid</artifactId>\n").append("            <version>1.0.11</version>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>net.sf.ehcache</groupId>\n").append("            <artifactId>ehcache-core</artifactId>\n").append("            <version>2.6.8</version>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.hibernate</groupId>\n").append("            <artifactId>hibernate-core</artifactId>\n").append("            <version>3.6.10.Final</version>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.aspectj</groupId>\n").append("            <artifactId>aspectjweaver</artifactId>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>mysql</groupId>\n").append("            <artifactId>mysql-connector-java</artifactId>\n").append("            <version>5.1.43</version>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>junit</groupId>\n").append("            <artifactId>junit</artifactId>\n").append("            <scope>test</scope>\n").append("        </dependency>\n").append("        <dependency>\n").append("            <groupId>org.springframework</groupId>\n").append("            <artifactId>spring-test</artifactId>\n").append("            <scope>test</scope>\n").append("        </dependency>\n").append("\n").append("    </dependencies>\n").append("\n").append("\n").append("</project>\n");
        return result.toString();
    }


    private String getReadmeStr(Map<String, String> config) {
        return "### " + config.get("project_name") + "\n\n `本项目通过ijson.com代码生成器生成`";
    }

    private String getGitlabCi(Map<String, String> config) {
        return "build:\n" +
                "    script:\n" +
                "       - pwd\n" +
                "       - mvn cobertura:cobertura sonar:sonar -Dmaven.test.skip=true\n" +
                "       - mvn clean\n";
    }


    private String getGitignoreStr(Map<String, String> config) {
        return "target/\n" +
                ".DS_Store\n" +
                "*.un~\n" +
                "*.iml\n" +
                "*/*.iml\n" +
                ".idea/\n" +
                "*.swp\n" +
                "rebel.xml\n" +
                "logs/\n" +
                "build/\n";
    }

}
