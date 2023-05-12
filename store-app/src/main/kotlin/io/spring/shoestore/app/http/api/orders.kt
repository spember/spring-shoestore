package io.spring.shoestore.app.http.api

import java.time.Instant

data class OrderRequest(val items: Map<String, Int>)

data class OrderAPIResponse(val result: Boolean, val orderNumber: String, val date: Instant)

