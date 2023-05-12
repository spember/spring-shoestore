package io.spring.shoestore

import io.spring.shoestore.app.http.api.OrderRequest
import io.spring.shoestore.app.http.api.OrderAPIResponse
import io.spring.shoestore.core.products.ShoeId
import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.core.variants.InventoryManagementService
import io.spring.shoestore.core.variants.ProductVariant
import io.spring.shoestore.core.variants.Sku
import io.spring.shoestore.core.variants.VariantColor
import io.spring.shoestore.core.variants.VariantSize
import io.spring.shoestore.support.BaseIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class OrderTests: BaseIntegrationTest() {

    @Autowired
    private lateinit var inventoryManagementService: InventoryManagementService


    @Test
    fun `basic order processing` () {

        // insert some inventory
        val shoe = getShoeByName("Sneak")
        val variant = ProductVariant(
            Sku("OT-001"),
            ShoeId.from(shoe.id),
            "Black Sneaker: Medium", // I have no idea, just random text really
            VariantSize.US_10,
            VariantColor.BLACK
        )

        inventoryManagementService.receiveNewItems(variant, listOf(
            InventoryItem("0001"),
            InventoryItem("0002"),
            InventoryItem("0003")
        ))


        val results = restTemplate.postForEntity(
            "http://localhost:${serverPort}/orders",
            OrderRequest(mapOf("OT-001" to 1)),
            OrderAPIResponse::class.java
        )

        assertNotNull(results)
        assertEquals(HttpStatus.OK, results.statusCode)
        assertTrue(results.body!!.result)
        assertNotNull(results.body!!.orderNumber)

        val orders = restTemplate.getForEntity("http://localhost:${serverPort}/orders", String::class.java)
        assertEquals(HttpStatus.OK, orders.statusCode)
    }
}