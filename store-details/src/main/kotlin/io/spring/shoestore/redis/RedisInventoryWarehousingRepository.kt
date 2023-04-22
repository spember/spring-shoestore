package io.spring.shoestore.redis

import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.core.variants.InventoryWarehousingRepository
import io.spring.shoestore.core.variants.Sku
import redis.clients.jedis.Jedis

class RedisInventoryWarehousingRepository(private val redisClient: Jedis): InventoryWarehousingRepository {

    override fun stock(sku: Sku, items: List<InventoryItem>) {
        redisClient.sadd(sku.value, *items.map { it.serialNumber }.toTypedArray())
    }

    override fun checkInventoryCount(sku: Sku) = redisClient.scard(sku.value)

    override fun reserveItem(sku: Sku): InventoryItem? {
        val serial = redisClient.spop(sku.value)
        return if (serial.isNullOrEmpty()) {
            null
        } else {
            InventoryItem(serial)
        }
    }

    override fun replaceItem(sku: Sku, item: InventoryItem) {
        redisClient.sadd(sku.value, item.serialNumber)
    }
}