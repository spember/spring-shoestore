package io.spring.shoestore.postgres.mappers

import io.spring.shoestore.core.products.Currency
import io.spring.shoestore.core.products.Shoe
import io.spring.shoestore.core.products.ShoeId
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

internal class ShoeMapper: RowMapper<Shoe> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Shoe {
        return Shoe(
            ShoeId(UUID.fromString(rs.getString("id"))),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price_in_cents"),
            Currency.lookup(rs.getString("price_currency"))!!
        )
    }
}