package io.spring.shoestore.app.config

import io.spring.shoestore.core.products.ShoeRepository
import io.spring.shoestore.core.products.ShoeService
import io.spring.shoestore.core.variants.InventoryManagementService
import io.spring.shoestore.core.variants.ProductVariantService
import io.spring.shoestore.postgres.PostgresProductVariantRepository
import io.spring.shoestore.postgres.PostgresShoeRepository
import io.spring.shoestore.redis.RedisInventoryWarehousingRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
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
}