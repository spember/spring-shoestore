package io.spring.shoestore.postgres

import io.spring.shoestore.core.orders.Order
import io.spring.shoestore.core.orders.OrderRepository
import io.spring.shoestore.core.security.PrincipalUser
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate

class PostgresOrderRepository(private val jdbcTemplate: JdbcTemplate): OrderRepository {
    override fun submitOrder(order: Order) {
        log.info("Need to save order ${order} -> ${order.price}")
        order.getItems().forEach {
            log.info("${it.first} -> ${it.second}")
        }
    }

    override fun listOrdersForUser(user: PrincipalUser) {
        TODO("Not yet implemented")
    }

    companion object {
        private val log = LoggerFactory.getLogger(PostgresOrderRepository::class.java)
    }
}