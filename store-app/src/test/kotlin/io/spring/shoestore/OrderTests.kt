package io.spring.shoestore

import io.spring.shoestore.app.http.api.OrderRequest
import io.spring.shoestore.app.http.api.OrderAPIResponse
import io.spring.shoestore.app.http.api.PreviousCustomerOrdersResponse
import io.spring.shoestore.core.orders.OrderAdminService
import io.spring.shoestore.core.products.Shoe
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus

class OrderTests: BaseIntegrationTest() {

    @Autowired
    private lateinit var inventoryManagementService: InventoryManagementService

    @Autowired
    private lateinit var adminService: OrderAdminService

    @BeforeEach
    fun beforeEach() {
        adminService.purgeOrders()
    }

    private fun blackVariant(shoeId: String) = ProductVariant(
        Sku("OT-001"),
        ShoeId.from(shoeId),
        "Black Sneaker: Medium", // I have no idea, just random text really
        VariantSize.US_10,
        VariantColor.BLACK
    )

    private fun greenVariant(shoeId: String) = ProductVariant(
        Sku("OT-456"),
        ShoeId.from(shoeId),
        "Green Sneaker: Medium",
        VariantSize.US_10,
        VariantColor.GREEN
    )

    @Test
    fun `basic order processing` () {

        // insert some inventory
        val shoe = getShoeByName("Sneak")

        inventoryManagementService.receiveNewItems(blackVariant(shoe.id), listOf(
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
        val firstOrder = results.body!!.orderNumber


        val orders = restTemplate.getForEntity("http://localhost:${serverPort}/orders", PreviousCustomerOrdersResponse::class.java)
        assertEquals(HttpStatus.OK, orders.statusCode)
        assertEquals(1, orders.body!!.orders.size)
        assertEquals(firstOrder, orders.body!!.orders.first().id)
    }

    @Test
    fun `more complex orders`() {
        val shoe = getShoeByName("Sneak")

        inventoryManagementService.receiveNewItems(blackVariant(shoe.id), listOf(
            InventoryItem("B-1001"),
            InventoryItem("B-1002"),
        ))

        inventoryManagementService.receiveNewItems(greenVariant(shoe.id), listOf(
            InventoryItem("G-001"),
            InventoryItem("G-025"),
            InventoryItem("G-1003")
        ))

        val order1Results = restTemplate.postForEntity(
            "http://localhost:${serverPort}/orders",
            OrderRequest(mapOf("OT-001" to 2, "OT-456" to 1)),
            OrderAPIResponse::class.java
        )

        println(order1Results.body)

        val order2Results = restTemplate.postForEntity(
            "http://localhost:${serverPort}/orders",
            OrderRequest(mapOf("OT-456" to 2)),
            OrderAPIResponse::class.java
        )

        val orders = restTemplate.getForEntity("http://localhost:${serverPort}/orders", PreviousCustomerOrdersResponse::class.java)
        assertEquals(HttpStatus.OK, orders.statusCode)
        assertEquals(2, orders.body!!.orders.size)

        orders.body!!.orders.forEach {
            println("${it.id} -> ${it.price}")
        }

        val o1 = orders.body!!.orders.find { it.id == order1Results.body!!.orderNumber }
        assertEquals(19800, o1!!.price)
        val o2 = orders.body!!.orders.find { it.id == order2Results.body!!.orderNumber }
        assertEquals(9900, o2!!.price)
    }
}