package io.spring.shoestore.core.variants

import org.slf4j.LoggerFactory


class InventoryManagementService(
    private val productVariantRepository: ProductVariantRepository,
    private val inventoryWarehousingRepository: InventoryWarehousingRepository
) {

    fun receiveNewItems(variant: ProductVariant, items: List<InventoryItem>) {
        productVariantRepository.registerNewVariants(listOf(variant))
        inventoryWarehousingRepository.stock(variant.sku, items)
        log.info("Stocked ${items.count()} items for variant $variant")
    }

    fun getInventoryCount(variant: ProductVariant):Long {
        return inventoryWarehousingRepository.checkInventoryCount(variant.sku)
    }

    fun holdForOrder(variant: ProductVariant): InventoryItem? = holdForOrder(variant.sku)

    fun holdForOrder(sku: Sku): InventoryItem? {
        return inventoryWarehousingRepository.reserveItem(sku)
    }

    fun restockItem(variant: ProductVariant, item: InventoryItem) {
        inventoryWarehousingRepository.replaceItem(variant.sku, item)
    }

    fun retrieveVariantsAndCount(sku: Sku): Pair<ProductVariant, Long>? {
        val productVariant = productVariantRepository.findById(sku) ?: return null
        return productVariant to inventoryWarehousingRepository.checkInventoryCount(sku)
    }

    companion object {
        private val log = LoggerFactory.getLogger(InventoryManagementService::class.java)
    }
}