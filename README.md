### in-spring-mvc 业务代码生成器

框架地址:[in-spring-mvc框架](https://github.com/ijson/in-spring-mvc/)

### 系统指南

`本工程为了支持自由框架in-spring-mvc而编写,方便业务代码的生成`

#### 前提条件
- JAVA环境:java8+
- MAVEN:3.0.2+

#### 如何使用本系统生成的代码
1. 首先将in-spring-mvc框架下载到本地
2. 配置eclipse或idea的maven运行环境
3. 将in-spring-mvc导入到项目中,然后将生成的项目一并导入项目中,如果是idea导入,需要从右侧的maven Projects中导入,eclipse可以直接导入到项目中
4. 在in-spring-mvc的pom中,添加刚刚生成项目的dependency
5. 在in-spring-mvc的WEB-INF下,找到applicationContext.xml,在其中添加,刚刚生成项目中生成的spring文件,文件都在resources/spring/目录下,命名规则为in-spring-biz-业务名称.xml
6.修改in-spring-mvc下authconf中的in-db文件,目前支持单机
7. 启动tomcat,访问controller下的ping地址即可
