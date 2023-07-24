package io.spring.shoestore.app.http

import io.spring.shoestore.app.http.api.OrderAPIResponse
import io.spring.shoestore.app.http.api.OrderRequest
import io.spring.shoestore.app.http.api.PreviousCustomerOrdersResponse
import io.spring.shoestore.app.http.api.PreviousOrder
import io.spring.shoestore.core.orders.OrderFailure
import io.spring.shoestore.core.orders.OrderProcessingService
import io.spring.shoestore.core.orders.OrderQueryService
import io.spring.shoestore.core.orders.OrderSuccess
import io.spring.shoestore.core.orders.PlaceOrderCommand
import io.spring.shoestore.core.security.StoreAuthProvider
import io.spring.shoestore.core.variants.Sku
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


@RestController
class OrderController(
    private val storeAuthProvider: StoreAuthProvider,
    private val orderProcessingService: OrderProcessingService,
    private val orderQueryService: OrderQueryService
    ) {


    @PostMapping("/orders")
    fun processOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderAPIResponse> {
        val response = orderProcessingService.placeOrder(
            PlaceOrderCommand(storeAuthProvider.getCurrentUser(), orderRequest.items.map { Sku(it.key) to it.value })
        )
        return when (response) {
            is OrderFailure -> {
                log.info("order resulted in a failure")
                ResponseEntity<OrderAPIResponse>(
                    OrderAPIResponse(false, "", Instant.now()),
                    HttpStatus.BAD_REQUEST
                )
            }
            is OrderSuccess -> {
                log.info ("Order was successful")
                ResponseEntity<OrderAPIResponse>(
                    OrderAPIResponse(true, response.orderId.toString(), response.time),
                    HttpStatus.OK
                )
            }
        }
    }

    @GetMapping("/orders")
    fun listOrdersForUser(): ResponseEntity<PreviousCustomerOrdersResponse> {
        log.info("Fetching orders for user ${storeAuthProvider.getCurrentUser()}")
        val foundOrders = orderQueryService.retrieveOrdersForUser(storeAuthProvider.getCurrentUser())

        return ResponseEntity<PreviousCustomerOrdersResponse>(
            PreviousCustomerOrdersResponse(foundOrders.map {order ->
                PreviousOrder(order.id.toString(), order.time.toString(), order.price,
                    order.getItems().associate { it.sku.toString() to it.inventoryItems.size }
                )
            }),
            HttpStatus.OK)
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderController::class.java)
    }
}