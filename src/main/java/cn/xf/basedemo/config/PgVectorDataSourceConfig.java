package cn.xf.basedemo.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * PgVectorDataSourceConfig
 *
 * @author 海言
 * @date 2025/10/24
 * @time 17:03
 * @Description PgVector数据源配置类
 */
@Configuration
public class PgVectorDataSourceConfig {

    @Bean(name = "pgvectorDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.pgvector")
    public DataSource pgvectorDataSource() {
        return new DriverManagerDataSource();
    }

    @Bean
    public JdbcTemplate pgvectorJdbcTemplate(@Qualifier("pgvectorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
