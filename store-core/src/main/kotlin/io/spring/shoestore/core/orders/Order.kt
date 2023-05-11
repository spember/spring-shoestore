package io.spring.shoestore.core.orders

import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.variants.ProductVariant
import java.time.Instant
import java.util.UUID

class Order(
    val id: UUID,
    val user: PrincipalUser,
    val time: Instant,
    ) {
    // user id, sku, quantity, price at the time
    // methods for calculating total cost
    // Order line item?

    private val items: MutableList<Pair<ProductVariant, Int>> = mutableListOf()

    var price = 0
        private set

    fun getItems() = items

    fun addItem(item: ProductVariant, quantity: Int, pricePer: Int) {
        items.add(item to quantity)
        price += pricePer*quantity
    }
}