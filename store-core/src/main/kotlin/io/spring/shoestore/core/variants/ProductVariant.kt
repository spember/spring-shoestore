package io.spring.shoestore.core.variants

import io.spring.shoestore.core.products.ShoeId

/**
 * Represents a combination of properties that a Product is available in. Sometimes called a 'SKU' or a 'Colorway'.
 * For example a 'Small, Blue' Shirt. "Green, size 11" Sneaker.
 */
class ProductVariant(val sku: Sku, val shoeId: ShoeId, val label: String, val size: VariantSize, val color: VariantColor) {
    override fun toString(): String {
        return "Variant: '$label' ($sku) $size, $color"
    }
}

data class Sku(val value: String) {
    init {
        assert(value.isNotEmpty())
        assert(value.length in 6..127)
    }
}