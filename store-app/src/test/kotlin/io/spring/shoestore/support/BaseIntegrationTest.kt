package io.spring.shoestore.support

import io.spring.shoestore.app.http.api.ShoeData
import io.spring.shoestore.app.http.api.ShoeResults
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB
import org.testcontainers.containers.wait.strategy.WaitStrategy
import org.testcontainers.utility.DockerImageName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseIntegrationTest {

    @LocalServerPort
    protected var serverPort: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    protected fun getShoeByName(namePart: String): ShoeData {
        val results = restTemplate.getForObject("http://localhost:${serverPort}/shoes?name=${namePart}", ShoeResults::class.java)
        return results.shoes.first()
    }

    companion object {

        @JvmStatic
        val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15.2-alpine")
            .withDatabaseName("shoestore")
            .withUsername("stavvy")
            .withPassword("stavvy")

        @JvmStatic
        val redisContainer: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7.0.11-alpine"))
            .withExposedPorts(6379)

        @JvmStatic
        val localStackContainer: LocalStackContainer = LocalStackContainer(DockerImageName.parse("localstack/localstack:1.3.1"))
            .withServices(DYNAMODB)



        @JvmStatic
        @BeforeAll
        internal fun setUp() {
            if (!postgresContainer.isRunning) {
                postgresContainer.start()
            }
            if (!redisContainer.isRunning) {
                redisContainer.start()
            }
            if (!localStackContainer.isRunning) {
                localStackContainer.start()
            }
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
            registry.add("cloud.aws.end-point.uri",
                { localStackContainer.getEndpointOverride(LocalStackContainer.Service.DYNAMODB)}
            )
            println("Configuring Redis: ${redisContainer.getHost()}, ${redisContainer.getMappedPort(6379).toString()}")
            registry.add("spring.redis.host", redisContainer::getHost)
            registry.add("spring.redis.port", { redisContainer.getMappedPort(6379).toString()} )
        }

    }
}