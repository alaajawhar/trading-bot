package com.amdose.database.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Alaa Jawhar
 */
@Configuration
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase springLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setDatabaseChangeLogTable("LIQUIBASE_DATABASE_CHANGE_LOG");
        liquibase.setDatabaseChangeLogLockTable("LIQUIBASE_DATABASE_CHANGE_LOG_LOCK");
        return liquibase;
    }
}