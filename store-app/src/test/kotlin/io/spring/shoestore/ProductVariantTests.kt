package io.spring.shoestore

import io.spring.shoestore.app.http.api.ShoeResults
import io.spring.shoestore.core.products.ShoeId
import io.spring.shoestore.core.variants.ProductVariant
import io.spring.shoestore.core.variants.ProductVariantService
import io.spring.shoestore.core.variants.Sku
import io.spring.shoestore.core.variants.VariantColor
import io.spring.shoestore.core.variants.VariantSize
import io.spring.shoestore.support.BaseIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class ProductVariantTests: BaseIntegrationTest() {

    @Autowired
    lateinit var productVariantService: ProductVariantService

    @Test
    fun `insertion of duplicate variants shouldn't cause problems`() {
        val results = restTemplate.getForObject("http://localhost:${serverPort}/shoes?name=neak", ShoeResults::class.java)
        val shoe = results.shoes.first()
        productVariantService.registerVariants(
            listOf(ProductVariant(Sku("SN-001"), ShoeId(UUID.fromString(shoe.id)), "Green Small Sneaker", VariantSize.US_10, VariantColor.GREEN))
        )

        val products = productVariantService.listForId(ShoeId(UUID.fromString(shoe.id)))
        assertEquals(1, products.size)
        assertEquals(Sku("SN-001"), products.first().sku)
        assertEquals(ShoeId(UUID.fromString(shoe.id)), products.first().shoeId)
        assertEquals("Green Small Sneaker", products.first().label)
    }
}