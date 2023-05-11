package io.spring.shoestore.core.orders

import java.time.Instant

sealed interface OrderResult

data class OrderFailure(val reason: String): OrderResult
data class OrderSuccess(val orderId: String, val totalCost: String, val time: Instant): OrderResult