package io.spring.shoestore.core.variants

/**
 * Represents an 'instance' of a given Variant
 */
data class InventoryItem(val serialNumber: String) {
    init {
        assert(serialNumber.isNotEmpty() && serialNumber.length > 3)
    }
}