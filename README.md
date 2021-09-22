
## 因github国内无法正常使用 本项目废弃,该项目被迁移至:https://gitee.com/ijson/in-spring-mvc-common, 感谢各位支持

### in-spring-mvc 业务代码生成器

框架地址:[in-spring-mvc框架](https://github.com/ijson/in-spring-mvc/)

### 系统指南

`本工程为了支持自由框架in-spring-mvc而编写,方便业务代码的生成,此项目暂只支持mysql,存在关联关系,需要自行实现关联`

#### 前提条件
- JAVA环境:java8+
- MAVEN:3.0.2+
- DB:MySQL
- CACHE:ehcache

### 代码如何生成,修改test/resources/autoconf/generator
- fs_path: 保存到哪个目录下,例如:/Users/cuiyongxu/Desktop 最后不要加 `/`
- project_name 项目名称,会在/Users/cuiyongxu/Desktop下自动生成该目录
- package_name 项目的包名称,必填项,会在此包下生成,dao,manager,entity等目录
- builder_tables 表结构,多张表以`，`隔开,会根据这些表生成对应的entity,dao,manager等
- jdbc.driver  驱动
- jdbc.url 地址
- jdbc.user mysql用户名
- jdbc.password 密码

``以上配置完成后,运行com.ijson.platform.generator.CoderTest即可``


#### 如何使用本系统生成的代码
1. 首先将in-spring-mvc框架下载到本地
2. 配置eclipse或idea的maven运行环境
3. 将in-spring-mvc导入到项目中,然后将生成的项目一并导入项目中,如果是idea导入,需要从右侧的maven Projects中导入,eclipse可以直接导入到项目中
4. 在in-spring-mvc的pom中,添加刚刚生成项目的dependency
5. 在in-spring-mvc的WEB-INF下,找到applicationContext.xml,在其中添加,刚刚生成项目中生成的spring文件,文件都在resources/spring/目录下,命名规则为in-spring-biz-业务名称.xml

6. 修改in-spring-mvc下authconf中的in-db文件,目前支持单机

7. 启动tomcat,访问controller下的ping地址即可

#### 本项目有点

1. 自动生成相关pom.xml
2. 自动生成mybatis.xml配置文件
3. 自动生成hibernate.xml配置文件
4. 自动生成controller
5. 自动生成entity实体类
6. 自动生成dao层代码结构
7. 自动生成service层代码逻辑
8. 自动生成测试代码及测试配置文件


