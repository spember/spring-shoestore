package io.spring.shoestore.postgres.mappers

import io.spring.shoestore.core.orders.Order
import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.variants.Sku
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

internal class OrderMapper: RowMapper<Order> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Order? {
        return Order(
            UUID.fromString(rs.getString("id")),
            PrincipalUser(rs.getString(5), rs.getString(6)),
            rs.getTimestamp("time_placed").toInstant()
        )
    }
}

/**
 * Represents the row in the db which we'll eventually collapse into a line item on the Order
 */
internal data class LineItemRow(
    val orderId: UUID,
    val position: Int,
    val sku: Sku,
    val pricePer: Int,
    val serialNumbers: List<String>
)

internal class LineItemMapper: RowMapper<LineItemRow> {
    override fun mapRow(rs: ResultSet, rowNum: Int): LineItemRow {

        val serialArray: Array<String> = rs.getArray("serial_numbers").array as Array<String>
        return LineItemRow(
            UUID.fromString(rs.getString("order_id")),
            rs.getInt("position"),
            Sku(rs.getString("sku")),
            rs.getInt("price_per"),
            serialArray.toList()
        )
    }

}