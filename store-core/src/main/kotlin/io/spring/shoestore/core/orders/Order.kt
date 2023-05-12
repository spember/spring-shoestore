package io.spring.shoestore.core.orders

import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.core.variants.ProductVariant
import io.spring.shoestore.core.variants.Sku
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

    private val items: MutableList<OrderLineItem> = mutableListOf()

    var price = 0
        private set

    fun getItems() = items

    fun addItem(item: ProductVariant, pricePer: Int, inventoryItems: List<InventoryItem>) {
        addItem(item.sku, pricePer, inventoryItems)
    }

    fun addItem(sku: Sku, pricePer: Int, inventoryItems: List<InventoryItem>) {
        items.add(OrderLineItem(sku, pricePer, inventoryItems))
        price += pricePer*inventoryItems.size
    }

    override fun toString(): String = "Order: ${id} ${time} : $price : $user, ${items.count()}"

    // add
    data class OrderLineItem(
        val sku: Sku,
        val pricePer: Int,
        val inventoryItems: List<InventoryItem>
    )
}