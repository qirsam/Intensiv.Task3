package com.qirsam;

import com.qirsam.utils.ConnectionPool;
import com.qirsam.utils.PropertiesUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Properties;


public abstract class IntegrationTestBase {

    private final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.5")
            .withInitScript("initialScript.sql");

    @BeforeEach
    void startContainer() {

        container.start();
        Properties properties = new Properties();
        properties.put("db.url", container.getJdbcUrl());
        properties.put("db.username", container.getUsername());
        properties.put("db.password", container.getPassword());
        PropertiesUtils.setProperties(properties);
        if (!ConnectionPool.getConnectionFlag()) {
            ConnectionPool.initConnectionPool();
        }
    }

    @AfterEach
    void close() {
        ConnectionPool.closePool();
        container.stop();
    }


}
