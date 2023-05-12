package io.spring.shoestore.app.http.api

import java.time.Instant

data class OrderRequest(val items: Map<String, Int>)

data class OrderAPIResponse(val result: Boolean, val orderNumber: String, val date: Instant)

data class PreviousCustomerOrdersResponse(val orders: List<PreviousOrder>)

data class PreviousOrder(
    val id: String,
    val time: String,
    val price: Int,
    val items: Map<String, Int>
)

