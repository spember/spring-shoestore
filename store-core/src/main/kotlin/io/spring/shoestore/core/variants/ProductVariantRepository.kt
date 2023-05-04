package io.spring.shoestore.core.variants

import io.spring.shoestore.core.products.ShoeId

/**
 * Provides basic lookup of Variants that we know about. Should provide fast lookup on which 'SKU's belong to a Shoe
 */
interface ProductVariantRepository  {

    fun findAllVariantsForShoe(shoeId: ShoeId): List<ProductVariant>

    fun findById(sku: Sku): ProductVariant?

    fun registerNewVariants(variants: List<ProductVariant>)
}