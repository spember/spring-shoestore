package io.spring.shoestore.core.variants

import io.spring.shoestore.core.products.ShoeId

class ProductVariantService(
    private val productVariantRepository: ProductVariantRepository
) {
    fun listForId(shoeId: ShoeId): List<ProductVariant> {
        return productVariantRepository.findAllVariantsForShoe(shoeId)
    }
}