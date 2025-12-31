package com.mootai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * 数据库配置类
 * 用于验证数据库连接和显示数据库信息
 */
@Configuration
@Slf4j
public class DatabaseConfig {

    /**
     * 应用启动时验证数据库连接
     */
    @Bean
    @Order(1)
    public CommandLineRunner databaseConnectionChecker(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                log.info("=========================================");
                log.info("[SUCCESS] Database Connection Established!");
                log.info("Database Product:  {}", metaData.getDatabaseProductName());
                log.info("Database Version:  {}", metaData.getDatabaseProductVersion());
                log.info("Driver Name:       {}", metaData.getDriverName());
                log.info("Driver Version:    {}", metaData.getDriverVersion());
                log.info("Database URL:      {}", metaData.getURL());
                log.info("Username:          {}", metaData.getUserName());
                log.info("=========================================");
            } catch (Exception e) {
                log.error("");
                log.error("=========================================");
                log.error("[ERROR] Database Connection Failed!");
                log.error("=========================================");
                log.error("Error Message: {}", e.getMessage());
                log.error("");
                log.error("Please check the following:");
                log.error("1. Is PostgreSQL service running?");
                log.error("2. Has database 'MootAI' been created?");
                log.error("   - Use script: backend/scripts/create-database.ps1 (Windows)");
                log.error("   - Or manually: CREATE DATABASE \"MootAI\";");
                log.error("3. Is database connection configuration correct?");
                log.error("   - Host:     127.0.0.1");
                log.error("   - Port:     5432");
                log.error("   - Database: MootAI");
                log.error("   - Username and password");
                log.error("4. See detailed config: backend/DATABASE_SETUP.md");
                log.error("=========================================");
                log.error("");
                throw new RuntimeException("Failed to connect to database. Please check configuration. Error: " + e.getMessage(), e);
            }
        };
    }
}

