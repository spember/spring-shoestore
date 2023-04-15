package io.spring.shoestore.support

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseIntegrationTest {

    @LocalServerPort
    protected var serverPort: Int = 0

    companion object {

        @JvmStatic
        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15.2-alpine")
            .withDatabaseName("shoestore")
            .withUsername("stavvy")
            .withPassword("stavvy")


        @JvmStatic
        @BeforeAll
        internal fun setUp() {
            if (!postgresContainer.isRunning) {
                postgresContainer.start()
            }
//            if (!localStackContainer.isRunning) {
//                localStackContainer.start()
//            }
        }

        @JvmStatic
        @AfterAll
        internal fun teardown() {

        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
//            registry.add("cloud.aws.end-point.uri",
//                { localStackContainer.getEndpointOverride(LocalStackContainer.Service.SNS)}
//            )
        }

    }
}