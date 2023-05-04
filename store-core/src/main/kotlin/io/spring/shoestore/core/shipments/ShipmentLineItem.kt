package io.spring.shoestore.core.shipments

import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.core.variants.Sku
import java.time.Instant


data class ShipmentLineItem(
    val shipmentId: String,
    val timeReceived: Instant,
    val orderPart: Int,
    val sku: Sku,
    val list: InventoryItem
    )
{
}

