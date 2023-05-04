package io.spring.shoestore.core.shipments

import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.core.variants.ProductVariant
import java.time.Instant

interface ShipmentDetailsRepository {

    fun store(shipmentId: String, time: Instant, items: List<Pair<ProductVariant, List<InventoryItem>>>): List<ShipmentLineItem>

    fun countByShipment(shipmentId: String): Int
}