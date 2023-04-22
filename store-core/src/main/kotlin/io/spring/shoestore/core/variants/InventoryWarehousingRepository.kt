package io.spring.shoestore.core.variants

/**
 * A digital 'warehouse'. Should provide some method for locking, preventing inventory from going negative.
 */
interface InventoryWarehousingRepository {

    fun stock(sku: Sku, items: List<InventoryItem>)

    fun checkInventoryCount(sku: Sku): Long

    fun reserveItem(sku: Sku): InventoryItem?

    fun replaceItem(sku: Sku, item:InventoryItem)
}