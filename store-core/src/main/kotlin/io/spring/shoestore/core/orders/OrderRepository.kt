package io.spring.shoestore.core.orders

import io.spring.shoestore.core.security.PrincipalUser

interface OrderRepository {

    fun submitOrder(order: Order)

    fun listOrdersForUser(user: PrincipalUser): List<Order>

    fun removeAllOrders()
}