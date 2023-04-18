package io.spring.shoestore.postgres

import io.spring.shoestore.core.products.Shoe
import io.spring.shoestore.core.products.ShoeId
import io.spring.shoestore.core.products.ShoeRepository
import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal


class PostgresShoeRepository(private val jdbcTemplate: JdbcTemplate): ShoeRepository {
    override fun findById(id: ShoeId): Shoe? {
        return jdbcTemplate.queryForObject("select * from shoes where id = ?;",
            ShoeMapper(),
            id.value
        )
    }

    override fun list(): List<Shoe> {
        return jdbcTemplate.query("select * from shoes;", ShoeMapper())
    }

    override fun findByName(namePartial: String): List<Shoe> {
        TODO("Not yet implemented")
    }

    override fun findByPriceUnder(upperPrice: BigDecimal): List<Shoe> {
        TODO("Not yet implemented")
    }
}