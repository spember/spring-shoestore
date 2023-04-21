package io.spring.shoestore.app.config

import io.spring.shoestore.core.products.ShoeRepository
import io.spring.shoestore.core.products.ShoeService
import io.spring.shoestore.core.variants.ProductVariantService
import io.spring.shoestore.postgres.PostgresProductVariantRepository
import io.spring.shoestore.postgres.PostgresShoeRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class CoreConfig {

    @Bean
    fun getShoeService(jdbcTemplate: JdbcTemplate): ShoeService {
        // an example of 'hiding' the details implementation, only the shoeservice can be grabbed via DI
        return ShoeService(PostgresShoeRepository(jdbcTemplate))
    }

    @Bean
    fun getProductVariantService(jdbcTemplate: JdbcTemplate): ProductVariantService {
        return ProductVariantService(PostgresProductVariantRepository(jdbcTemplate))
    }
}