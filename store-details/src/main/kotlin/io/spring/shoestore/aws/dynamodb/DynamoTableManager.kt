package io.spring.shoestore.aws.dynamodb

import org.slf4j.LoggerFactory
import software.amazon.awssdk.core.waiters.WaiterResponse
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter

/**
 * A little layer to help us configure our Dynamo Table(s)
 */
class DynamoTableManager(private val dynamoDbClient: DynamoDbClient) {

    fun establishOrderTable() {
        /*
            The Order table will be constructed with the userId as the hash and a combination of OrderId and position
            as the range.

            This is not sufficient for production, but for our silly sample code, well...
         */
        println("ahhhh")
        log.info("Establishing the ${OrderTableDetails.NAME} table")
        val dbWaiter: DynamoDbWaiter = dynamoDbClient.waiter()
        val request: CreateTableRequest = CreateTableRequest.builder()
            .attributeDefinitions(
                *OrderTableDetails.attributes.toTypedArray()
            )
            .keySchema(
                *OrderTableDetails.keys.toTypedArray()
            )
            .provisionedThroughput(
                ProvisionedThroughput.builder()
                    .readCapacityUnits(1)
                    .writeCapacityUnits(1)
                    .build()
            )
            .tableName(OrderTableDetails.NAME)
            .build()
        println("hmmm")
        try {
            val response: CreateTableResponse = dynamoDbClient.createTable(request)
            val tableRequest = DescribeTableRequest.builder()
                .tableName(OrderTableDetails.NAME)
                .build()

            // Wait until the Amazon DynamoDB table is created.
            val waiterResponse: WaiterResponse<DescribeTableResponse> = dbWaiter.waitUntilTableExists(tableRequest)
            waiterResponse.matched().response().ifPresent { x: DescribeTableResponse? ->
                log.info("response from system is ${x}")
            }
            log.info("DDB Table is ${response.tableDescription().tableName()}")


        } catch (e: DynamoDbException) {
            log.error("Could not establish table: ${e}")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(DynamoTableManager::class.java)
    }

}