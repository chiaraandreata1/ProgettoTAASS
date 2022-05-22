package com.example.facilityservice;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.session.jdbc.config.annotation.SpringSessionDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        return dataSourceProperties;
    }

    @Bean
    @Primary
    public DataSource primaryDataSource() {
        DataSourceProperties properties = primaryDataSourceProperties();
        DataSourceBuilder<?> builder = properties.initializeDataSourceBuilder();
        DataSourceBuilder<HikariDataSource> type = builder.type(HikariDataSource.class);
        HikariDataSource build = type.build();
        return build;
    }

    @Bean
    @ConfigurationProperties("session.datasource")
    public DataSourceProperties sessionDataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();

        return properties;
    }

    @Bean
    @SpringSessionDataSource
    public DataSource springSessionDataSource() {
        DataSourceProperties properties = sessionDataSourceProperties();
        DataSourceBuilder<?> builder = properties.initializeDataSourceBuilder();
        DataSourceBuilder<HikariDataSource> type = builder.type(HikariDataSource.class);
        HikariDataSource build = type.build();
        return build;
    }
}
