package com.qirsam;

import com.qirsam.utils.PropertiesUtils;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Properties;


@Testcontainers
public abstract class IntegrationTestBase {

    @Container
    static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.5")
            .withInitScript("initialScript.sql");

    @BeforeAll
    static void startContainer() {
        Properties properties = new Properties();
        properties.put("db.url", container.getJdbcUrl());
        properties.put("db.username", container.getUsername());
        properties.put("db.password", container.getPassword());
        PropertiesUtils.setProperties(properties);
        container.start();
    }
}
