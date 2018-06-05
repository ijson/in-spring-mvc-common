package com.ijson.platform.database.db.mybatis;

import com.google.common.collect.Maps;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * description: 重写ibatis的SqlSessionFactory
 */
@Slf4j
public class SqlSessionFactoryFactoryBean implements FactoryBean, InitializingBean {

    private Resource configLocation;
    private Resource[] mapperLocations;
    private DataSource dataSource;
    private boolean useTransactionAwareDataSource = true;
    SqlSessionFactory sqlSessionFactory;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(configLocation, "configLocation must be not null");
        sqlSessionFactory = createSqlSessionFactory();
    }

    /**
     * description: 创建SqlSessionFactory
     *
     * @return 返回SqlSessionFactory实例
     */
    private SqlSessionFactory createSqlSessionFactory() throws IOException {
        try (Reader reader = new InputStreamReader(getConfigLocation().getInputStream())) {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            Configuration conf = sqlSessionFactory.getConfiguration();
            if (dataSource != null) {
                DataSource dataSourceToUse = this.dataSource;
                if (this.useTransactionAwareDataSource && !(this.dataSource instanceof TransactionAwareDataSourceProxy)) {
                    dataSourceToUse = new TransactionAwareDataSourceProxy(this.dataSource);
                }
                conf.setEnvironment(new Environment("development", new ManagedTransactionFactory(), dataSourceToUse));
            }

            if (mapperLocations != null) {
                Map<String, XNode> sqlFragments = Maps.newHashMap();
                for (Resource r : mapperLocations) {
                    log.info("Loading iBatis3 mapper xml from file[" + r.getFile().getAbsolutePath() + "]");

                    //Reader mapperReader = new InputStreamReader(r.getInputStream());
                    XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(r.getInputStream(), conf, r.getFile()
                            .getAbsolutePath(), sqlFragments);
                    mapperBuilder.parse();
                }
            }
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(conf);
            return sqlSessionFactory;
        }
    }

    public Object getObject() throws Exception {
        return sqlSessionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Class getObjectType() {
        return SqlSessionFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Resource getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(Resource configurationFile) {
        this.configLocation = configurationFile;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

}
