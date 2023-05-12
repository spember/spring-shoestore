package io.spring.shoestore.core.orders

import io.spring.shoestore.core.products.ShoeService
import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.variants.InventoryManagementService
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.UUID


class OrderProcessingService(
    private val inventoryManagementService: InventoryManagementService,
    private val shoeService: ShoeService,
    private val orderRepository: OrderRepository
    ) {


    fun placeOrder(command: PlaceOrderCommand): OrderResult {
        val order = Order(UUID.randomUUID(), command.user, Instant.now())
        // 'hold' inventory from the warehouse for the skus
        // on an error, restore the inventory

        command.items.forEach {(sku, quantity) ->
            // ugh more loops while fetching. Who even wrote this?
            val variantData = inventoryManagementService.retrieveVariantsAndCount(sku) ?: return OrderFailure("Unknown sku $sku")
            val actualShoe = shoeService.get(variantData.first.shoeId) ?: return OrderFailure("Unknown shoe ${variantData.first.shoeId}")
            val physicalInventoryItem = inventoryManagementService.holdForOrder(variantData.first) ?: return OrderFailure("No Items remaining!")
            // no... we want the sku, the price, but the actual inventory item
            order.addItem(variantData.first, actualShoe.price.toInt(), listOf(physicalInventoryItem))
        }

        log.info("Total cost of ${command.items.size} is ${order.price}")
        // assume that other operations - likely actually handing payments - are ... taken care of. Hand wave away!
        orderRepository.submitOrder(order)
        return OrderSuccess(order.id, order.price.toString(), order.time)
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderProcessingService::class.java)
    }
}