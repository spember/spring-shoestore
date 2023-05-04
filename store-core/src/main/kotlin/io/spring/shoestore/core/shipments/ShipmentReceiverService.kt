package io.spring.shoestore.core.shipments

import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.core.variants.InventoryManagementService
import io.spring.shoestore.core.variants.ProductVariant
import io.spring.shoestore.core.variants.Sku
import org.slf4j.LoggerFactory
import java.time.Instant


class ShipmentReceiverService(
    private val shipmentDetailsRepository: ShipmentDetailsRepository,
    private val inventoryManagementService: InventoryManagementService,
) {
    // only handles the receiving and processing of shipments. If an error occurred and we needed to rebuild
    // the current state of inventory, we would use the set of items from shipments minus the items in orders

    fun handle(command: ReceiveShipmentCommand): Boolean {
        log.info("Receiving and opening a shipment for id ${command.shipmentId}")
        // convert to instructions to persist

        // yes this is horribly inefficient. Look at that n+1 loop + sql. oof. who would write such a thing?
        // first make sure we know about all of these skus
        val skuLookup = mutableMapOf<Sku, ProductVariant>()
        command.items.forEach {(sku, _) ->
            val variantAndCount = inventoryManagementService.retrieveVariantsAndCount(sku)
            if (variantAndCount == null) {
                log.error("Unknown sku: ${sku}")
                return false
            }
            skuLookup[sku] = variantAndCount.first
        }

        val populatedItems: List<Pair<ProductVariant, List<InventoryItem>>> = command.items.map { pair ->
            skuLookup[pair.first]!! to pair.second.map { InventoryItem(it) }
        }

        shipmentDetailsRepository.store(command.shipmentId, Instant.now(), populatedItems)
        // now that the line items have been persisted, receive new shipments
        populatedItems.forEach {(variant, items) ->
            // THE HORROR. LOOK AT THIS LOOP ^
            inventoryManagementService.receiveNewItems(variant, items)
        }
        log.info("Recorded details for shipment ${command.shipmentId}. ")
        return true
    }



    companion object {
        private val log = LoggerFactory.getLogger(ShipmentReceiverService::class.java)
    }
}