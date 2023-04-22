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

    fun holdForOrder(variant: ProductVariant): InventoryItem? {
        return inventoryWarehousingRepository.reserveItem(variant.sku)
    }

    fun restockItem(variant: ProductVariant, item: InventoryItem) {
        inventoryWarehousingRepository.replaceItem(variant.sku, item)
    }

    companion object {
        private val log = LoggerFactory.getLogger(InventoryManagementService::class.java)
    }
}