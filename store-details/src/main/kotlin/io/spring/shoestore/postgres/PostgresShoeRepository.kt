package io.spring.shoestore.postgres

import io.spring.shoestore.core.products.Shoe
import io.spring.shoestore.core.products.ShoeId
import io.spring.shoestore.core.products.ShoeRepository
import io.spring.shoestore.postgres.mappers.ShoeMapper
import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal


class PostgresShoeRepository(private val jdbcTemplate: JdbcTemplate): ShoeRepository {

    private val shoeMapper = ShoeMapper()

    override fun findById(id: ShoeId): Shoe? {
        return jdbcTemplate.queryForObject("select * from shoes where id = ? limit 1;",
            shoeMapper,
            id.value
        )
    }

    override fun list(): List<Shoe> {
        return jdbcTemplate.query("select * from shoes;", shoeMapper)
    }

    override fun findByName(namePartial: String): List<Shoe> {
        return jdbcTemplate.query("select * from shoes where name ilike ?", shoeMapper, "%$namePartial%")
    }

    override fun findByPriceUnder(upperPrice: BigDecimal): List<Shoe> {
        return jdbcTemplate.query("select * from shoes where price_in_cents <= ?", shoeMapper, upperPrice.toInt())
    }
}