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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShoeId

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}