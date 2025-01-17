package com.example.support.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.support.BootApplication;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
    classes = BootApplication.class,
    webEnvironment = WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@Testcontainers
class TestcontainerTest {

    private static final Logger logger = LoggerFactory.getLogger(TestcontainerTest.class);

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:9.1-oraclelinux9"))
                                    .withUsername("test")
                                    .withPassword("test").withDatabaseName("app").withReuse(true);

    @DynamicPropertySource
    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @BeforeAll
    static void beforeAll() {
        mysql.followOutput(new Slf4jLogConsumer(logger));
        mysql.start();
    }

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() throws SQLException {
        // Check if the connection works
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            logger.info("Connected to MySQL container successfully.");
        }
    }
}
