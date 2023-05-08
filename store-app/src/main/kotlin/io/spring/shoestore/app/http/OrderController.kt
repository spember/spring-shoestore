package io.spring.shoestore.app.http

import io.spring.shoestore.app.http.api.OrderRequest
import io.spring.shoestore.app.http.api.OrderResponse
import io.spring.shoestore.core.security.StoreAuthProvider
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


@RestController
class OrderController(private val storeAuthProvider: StoreAuthProvider) {


    @PostMapping("/orders")
    fun processOrder(@RequestBody orderRequest: OrderRequest): OrderResponse {
        val currentUser = storeAuthProvider.getCurrentUser()
        log.info("User ${currentUser.email} is attempting to make a purchase")
        return OrderResponse(true, "Foo", Instant.now())
    }

    @GetMapping("/orders")
    fun listOrdersForUser() {
        log.info("Fetching orders for user ${storeAuthProvider.getCurrentUser()}")
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderController::class.java)
    }
}