package io.spring.shoestore

import io.spring.shoestore.app.http.api.ShoeData
import io.spring.shoestore.app.http.api.ShoeResults
import io.spring.shoestore.core.products.Shoe
import io.spring.shoestore.core.products.ShoeId
import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.core.variants.InventoryManagementService
import io.spring.shoestore.core.variants.ProductVariant
import io.spring.shoestore.core.variants.ProductVariantService
import io.spring.shoestore.core.variants.Sku
import io.spring.shoestore.core.variants.VariantColor
import io.spring.shoestore.core.variants.VariantSize
import io.spring.shoestore.support.BaseIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class InventoryTests: BaseIntegrationTest() {

    @Autowired
    lateinit var productVariantService: ProductVariantService

    @Autowired
    lateinit var inventoryManagementService: InventoryManagementService

    @Test
    fun `Test Basic insertion`() {
        val shoe = getShoeByName("neak")

        val previousProductSize = productVariantService.listForId(ShoeId.from(shoe.id)).size

        inventoryManagementService.receiveNewItems(
            ProductVariant(Sku("SN-001"), ShoeId.from(shoe.id), "Green Small Sneaker", VariantSize.US_10, VariantColor.GREEN),
            listOf(InventoryItem("0001111"))
        )

        val products = productVariantService.listForId(ShoeId.from(shoe.id))
        assertEquals(previousProductSize+1, products.size)
        val foundVariant = products.first { it.sku == Sku("SN-001") }
        assertEquals(Sku("SN-001"), foundVariant.sku)
        assertEquals(ShoeId.from(shoe.id), foundVariant.shoeId)
        assertEquals("Green Small Sneaker", foundVariant.label)
    }

    @Test
    fun `Registering a variant more than once does not cause a problem`() {
        val shoe = getShoeByName("neak")


        val variant = ProductVariant(Sku("SN-011"), ShoeId.from(shoe.id), "Green Small Sneaker 2", VariantSize.US_10, VariantColor.GREEN)

        val previousProductSize = productVariantService.listForId(ShoeId.from(shoe.id)).size
        inventoryManagementService.receiveNewItems(variant, listOf(InventoryItem("00111")))
        inventoryManagementService.receiveNewItems(variant, listOf(InventoryItem("00112"), InventoryItem("00113")))

        val products = productVariantService.listForId(ShoeId.from(shoe.id))
        assertEquals(previousProductSize+1, products.size)
    }

    @Test
    fun `Receiving new shipments add inventory`() {
        val shoe = getShoeByName("neak")
        val variant = ProductVariant(Sku("SN-002"),
            ShoeId.from(shoe.id),
            "Green Medium Sneaker",
            VariantSize.US_10_5,
            VariantColor.GREEN
        )

        val variant2 = ProductVariant(Sku("SN-003"),
            ShoeId.from(shoe.id),
            "Black Medium Sneaker",
            VariantSize.US_10_5,
            VariantColor.BLACK
        )
        inventoryManagementService.receiveNewItems(variant2, listOf(
            InventoryItem("0001")
        ))

        inventoryManagementService.receiveNewItems(variant, listOf(
            InventoryItem("0011"),
            InventoryItem("0012"),
        ))

        assertEquals(2, inventoryManagementService.getInventoryCount(variant))
        assertEquals(1, inventoryManagementService.getInventoryCount(variant2))

        inventoryManagementService.receiveNewItems(variant, listOf(
            InventoryItem("0013"),
            InventoryItem("0014"),
        ))

        assertEquals(4, inventoryManagementService.getInventoryCount(variant))
    }

    @Test
    fun `holding and restoring an Inventory Item`() {
        val shoe = getShoeByName("neak")
        val variant = ProductVariant(Sku("AK-001"),
            ShoeId.from(shoe.id),
            "Blue Sneaker",
            VariantSize.US_10_5,
            VariantColor.BLUE
        )

        inventoryManagementService.receiveNewItems(variant, listOf(
            InventoryItem("0001")
        ))

        val item = inventoryManagementService.holdForOrder(variant)
        assertNotNull(item)

        assertNull(inventoryManagementService.holdForOrder(variant))

        inventoryManagementService.restockItem(variant, item!!)
        assertEquals(1, inventoryManagementService.getInventoryCount(variant))
    }
}