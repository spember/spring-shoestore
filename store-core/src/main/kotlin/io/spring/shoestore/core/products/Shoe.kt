package io.spring.shoestore.core.products

import java.math.BigDecimal
import java.util.UUID

class Shoe(
    val id: ShoeId,
    val name: String,
    val description: String? = "",
    val price: BigDecimal,
    val currency: Currency
) {
}

data class ShoeId(val value: UUID) {

    companion object {
        @JvmStatic
        fun from(rawValue: String): ShoeId {
            return ShoeId(UUID.fromString(rawValue))
        }
    }
}