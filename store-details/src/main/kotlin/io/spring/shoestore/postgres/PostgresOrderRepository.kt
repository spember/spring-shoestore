package io.spring.shoestore.postgres

import io.spring.shoestore.core.orders.Order
import io.spring.shoestore.core.orders.OrderRepository
import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.postgres.mappers.LineItemMapper
import io.spring.shoestore.postgres.mappers.OrderMapper
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement
import java.sql.Timestamp

class PostgresOrderRepository(private val jdbcTemplate: JdbcTemplate): OrderRepository {

    private val orderMapper = OrderMapper()
    private val lineItemMapper = LineItemMapper()

    override fun submitOrder(order: Order) {
        log.info("Persisting order ${order} -> ${order.price}")
        order.getItems().forEach {
            log.info("${it.sku} at ${it.pricePer} each. Serials are: ${it.inventoryItems.map{s -> s.serialNumber}}")
        }
        // insert into orders, then insert each item into order_line_items
        jdbcTemplate.update("insert into orders (id, user_email, time_placed, total_price) values (?, ?, ?, ?)",
            order.id,
            order.user.email,
            Timestamp.from(order.time),
            order.price
            )
        jdbcTemplate.batchUpdate("insert into order_line_items (order_id, position, sku, price_per, serial_numbers) " +
                "values (?, ?, ?, ?, ?);",
            object: BatchPreparedStatementSetter {

                override fun setValues(ps: PreparedStatement, i: Int) {
                    val lineItem = order.getItems()[i]
                    val serials = lineItem.inventoryItems.map {it.serialNumber}.toTypedArray()
                    ps.setObject(1, order.id)
                    ps.setInt(2, i)
                    ps.setString(3, lineItem.sku.value)
                    ps.setInt(4, lineItem.pricePer)
                    ps.setArray(5, jdbcTemplate.dataSource!!.connection.createArrayOf("text", serials))
                }

                override fun getBatchSize(): Int = order.getItems().size
        })
    }

    override fun listOrdersForUser(user: PrincipalUser): List<Order> {
        // first grab the orders
        val orders = jdbcTemplate.query("select o.*, ?, ? from orders o  where o.user_email = ?", orderMapper, user.name, user.email, user.email)
        val lookup = orders.associateBy { it.id }

        val items = jdbcTemplate.query("select li.* from order_line_items li, orders o  where li.order_id = o.id and o.user_email = ? order by o.id, li.position asc",
            lineItemMapper,
            user.email
        )
        items.forEach {li ->
            lookup[li.orderId]?.let { order ->
                order.addItem(li.sku, li.pricePer, li.serialNumbers.map { InventoryItem(it) })
            }
        }
        return orders
    }

    override fun removeAllOrders() {
        jdbcTemplate.update("delete from order_line_items;")
        jdbcTemplate.update("delete from orders;")
    }

    companion object {
        private val log = LoggerFactory.getLogger(PostgresOrderRepository::class.java)
    }
}