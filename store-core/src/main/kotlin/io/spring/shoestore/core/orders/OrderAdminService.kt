package io.spring.shoestore.core.orders

import org.slf4j.LoggerFactory

class OrderAdminService(private val orderRepository: OrderRepository) {

    fun purgeOrders() {
        log.warn("Performing a very dangerous operation")
        // how dangerous and silly of me
        orderRepository.removeAllOrders()
        log.info("Orders purged")
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderAdminService::class.java)
    }
}