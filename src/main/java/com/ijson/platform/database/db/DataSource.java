package com.ijson.platform.database.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.ijson.config.ConfigFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by cuiyongxu on 17/9/22.
 */
@Slf4j
@Data
public class DataSource extends DruidDataSource {

    public String configName = "in-spring-db";

    @PostConstruct
    public void initDataSource() {
        ConfigFactory.getConfig(configName, (c -> {
            Map<String, String> config = c.getAll();
            if (MapUtils.isEmpty(config)) {
                log.error("init datasource error,config is null");
                System.exit(0);
            }
            try {
                setUrl(config.get("db.url"));
                setUsername(config.get("db.username"));
                setPassword(config.get("db.password"));
                setInitialSize(Integer.parseInt(config.get("db.initialSize")));
                setMinIdle(Integer.parseInt(config.getOrDefault("db.minIdle", "10")));
                setMaxActive(Integer.parseInt(config.get("db.maxActive")));
                //配置获取连接等待超时的时间
                setMaxWait(60000);
                //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
                setTimeBetweenEvictionRunsMillis(60000);
                //配置一个连接在池中最小生存的时间，单位是毫秒
                setMinEvictableIdleTimeMillis(300000);
                setValidationQuery("SELECT 'x'");
                setTestWhileIdle(true);
                setTestOnBorrow(false);
                setTestOnReturn(false);
                setPoolPreparedStatements(true);
                setMaxPoolPreparedStatementPerConnectionSize(20);
                setFilters("stat,wall");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
//        init();
        //close();
    }


}
