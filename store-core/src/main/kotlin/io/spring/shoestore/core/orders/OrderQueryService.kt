package io.spring.shoestore.core.orders

import io.spring.shoestore.core.security.PrincipalUser


class OrderQueryService(private val orderRepository: OrderRepository) {

    fun retrieveOrdersForUser(user: PrincipalUser): List<Order> {
        return orderRepository.listOrdersForUser(user)
    }
}