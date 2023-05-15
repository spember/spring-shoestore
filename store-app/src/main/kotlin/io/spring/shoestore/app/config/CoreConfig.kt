package io.spring.shoestore.app.config


import io.spring.shoestore.core.orders.OrderAdminService
import io.spring.shoestore.core.orders.OrderProcessingService
import io.spring.shoestore.core.orders.OrderQueryService
import io.spring.shoestore.core.orders.OrderRepository
import io.spring.shoestore.core.products.ShoeService
import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.security.StoreAuthProvider
import io.spring.shoestore.core.variants.InventoryManagementService
import io.spring.shoestore.core.variants.ProductVariantService
import io.spring.shoestore.postgres.PostgresOrderRepository
import io.spring.shoestore.postgres.PostgresProductVariantRepository
import io.spring.shoestore.postgres.PostgresShoeRepository
import io.spring.shoestore.redis.RedisInventoryWarehousingRepository
import io.spring.shoestore.security.FakeStoreAuthProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.jdbc.core.JdbcTemplate
import redis.clients.jedis.Jedis

@Configuration
class CoreConfig {

    @Bean
    fun getShoeService(jdbcTemplate: JdbcTemplate): ShoeService {
        // an example of 'hiding' the details implementation, only the shoeservice can be grabbed via DI
        return ShoeService(PostgresShoeRepository(jdbcTemplate))
        // todo: swap in
//        return ShoeService(RedisShoeRepository(jedis))
    }

    @Bean
    fun getProductVariantService(jdbcTemplate: JdbcTemplate, redisTemplate: RedisTemplate<String, Any>, jedis: Jedis): ProductVariantService {
        return ProductVariantService(PostgresProductVariantRepository(jdbcTemplate))
    }

    @Bean
    fun getInventoryManagementService(jdbcTemplate: JdbcTemplate, jedis: Jedis): InventoryManagementService {
        return InventoryManagementService(
            PostgresProductVariantRepository(jdbcTemplate),
            RedisInventoryWarehousingRepository(jedis)
        )
    }

    @Bean
    fun getOrderRepository(jdbcTemplate: JdbcTemplate): OrderRepository {
        return PostgresOrderRepository(jdbcTemplate)
    }

    @Bean
    fun getOrderProcessingService(
        inventoryManagementService: InventoryManagementService,
        shoeService: ShoeService,
        orderRepository: OrderRepository
    ) = OrderProcessingService(inventoryManagementService, shoeService, orderRepository)

    @Bean
    fun getOrderAdminService(orderRepository: OrderRepository) = OrderAdminService(orderRepository)

    @Bean
    fun getOrderQueryService(orderRepository: OrderRepository): OrderQueryService = OrderQueryService(orderRepository)

    @Bean
    fun getStoreAuthProvider(): StoreAuthProvider {
        val provider = FakeStoreAuthProvider()
        provider.login(PrincipalUser("Sam Testington", "stestington@test.com"))
        return provider
    }
}