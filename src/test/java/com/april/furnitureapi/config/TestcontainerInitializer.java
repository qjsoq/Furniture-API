package com.april.furnitureapi.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public class TestcontainerInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static MySQLContainer<?> mySQLContainer =
            new MySQLContainer<>(DockerImageName.parse("mysql:oraclelinux9"));

    static {
        Startables.deepStart(mySQLContainer).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        TestPropertyValues.of(
                "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + mySQLContainer.getUsername(),
                "spring.datasource.password=" + mySQLContainer.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
