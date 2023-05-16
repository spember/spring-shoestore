package io.spring.shoestore.aws.dynamodb

import io.spring.shoestore.aws.dynamodb.DynamoClientHelper.createNumAttribute
import io.spring.shoestore.aws.dynamodb.DynamoClientHelper.createStringAttribute
import io.spring.shoestore.core.orders.Order
import io.spring.shoestore.core.orders.OrderRepository
import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.variants.InventoryItem
import io.spring.shoestore.core.variants.Sku
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.BatchWriteItemRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest
import software.amazon.awssdk.services.dynamodb.model.PutRequest
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse
import software.amazon.awssdk.services.dynamodb.model.WriteRequest
import java.time.Instant
import java.util.UUID


/**
 * Intended as a drop-in replacement for the Postgres Repo
 */
class DynamoDbOrderRepository(private val dynamoDbClient: DynamoDbClient): OrderRepository {
    override fun submitOrder(order: Order) {
        // persisting the structure such that each users' range is just ... all of their order line items
        val writes = mutableListOf<WriteRequest>()
        val headerData = mutableMapOf<String, AttributeValue>()
        headerData[OrderTableDetails.PRIMARY_KEY] = createStringAttribute("user:${order.user.email}")
        headerData[OrderTableDetails.RANGE_KEY] = createStringAttribute("order:${order.id}:p:0")
        headerData[OrderTableDetails.DUPE_ORDER_ID] = createStringAttribute(order.id.toString())
        headerData[OrderTableDetails.TIME] = createStringAttribute(order.time.toString())
        headerData[OrderTableDetails.PRICE] = createNumAttribute(order.price)

        writes.add(WriteRequest.builder().putRequest(PutRequest.builder().item(headerData).build()).build())
        order.getItems().forEachIndexed{position, lineItem ->
            val data = mutableMapOf<String, AttributeValue>()
            data[OrderTableDetails.PRIMARY_KEY] = createStringAttribute("user:${order.user.email}")
            data[OrderTableDetails.RANGE_KEY] = createStringAttribute("order:${order.id}:p:${position+1}")
            // too lazy to regex the order out of the range key. oops.
            data[OrderTableDetails.DUPE_ORDER_ID] = createStringAttribute(order.id.toString())
            data[OrderTableDetails.SKU] = createStringAttribute(lineItem.sku.value)
            data[OrderTableDetails.PRICE_PER] = createNumAttribute(lineItem.pricePer)
            data[OrderTableDetails.SERIALS] = createStringAttribute(lineItem.inventoryItems.map { it.serialNumber }.joinToString(""))
            writes.add(WriteRequest.builder().putRequest(PutRequest.builder().item(data).build()).build())
        }


        dynamoDbClient.batchWriteItem(BatchWriteItemRequest.builder().requestItems(
            mapOf(OrderTableDetails.NAME to writes)
        ).build())

        log.info("wrote ${writes.size} items to table ${OrderTableDetails.NAME}")
    }

    override fun listOrdersForUser(user: PrincipalUser): List<Order> {
        val queryResult: QueryResponse = dynamoDbClient.query(QueryRequest.builder()
            .tableName(OrderTableDetails.NAME)
            .keyConditionExpression("${OrderTableDetails.PRIMARY_KEY} = :userEmail")
            .expressionAttributeValues(mapOf(":userEmail" to createStringAttribute("user:" + user.email)))
            .build()
        )

        val items: List<Map<String, AttributeValue>> = queryResult.items()
        log.info("How many items did we get? ${items.size}")
        // first, build the orders.
        val orderStore = mutableMapOf<String, Order>()
        items.forEach {item ->
            if (item[OrderTableDetails.RANGE_KEY]!!.s().endsWith("p:0")) {
                val orderIdRaw = item[OrderTableDetails.DUPE_ORDER_ID]!!.s()
                orderStore[orderIdRaw] = Order(UUID.fromString(orderIdRaw), user, Instant.parse(item["Time"]!!.s()))
            }
        }

        // loop again and ignore the header

        items.forEach {item ->
            if (!item[OrderTableDetails.RANGE_KEY]!!.s().endsWith("p:0")) {
                val orderIdRaw = item[OrderTableDetails.DUPE_ORDER_ID]!!.s()
                orderStore[orderIdRaw]!!.addItem(
                    Sku(item[OrderTableDetails.SKU]!!.s()),
                    Integer.parseInt(item[OrderTableDetails.PRICE_PER]!!.n()),
                    item[OrderTableDetails.SERIALS]!!.s().split(",").map { InventoryItem(it) }
                )
            }
        }
        // kotlin was giving me a hard time with `.values`, so... take that.
        return orderStore.toList().map { it.second }
    }

    override fun removeAllOrders() {
        // it's better to just drop the table and re-create it
        log.warn("About to do something very silly")

        dynamoDbClient.deleteTable(
            DeleteTableRequest
                .builder()
                .tableName(OrderTableDetails.NAME)
                .build()
        )

        DynamoTableManager(dynamoDbClient).establishOrderTable()

        log.info("reset orders table")

    }

    companion object {
        private val log = LoggerFactory.getLogger(DynamoDbOrderRepository::class.java)

    }
}