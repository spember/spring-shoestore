package io.spring.shoestore.core.shipments

import io.spring.shoestore.core.variants.Sku

data class ReceiveShipmentCommand(
    val shipmentId: String,
    val items: List<Pair<Sku, List<String>>>
)