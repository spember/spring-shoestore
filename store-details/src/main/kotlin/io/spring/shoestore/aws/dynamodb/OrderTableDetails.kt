package io.spring.shoestore.aws.dynamodb

import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType

internal object OrderTableDetails {

    const val NAME = "Orders"

    private const val PRIMARY_KEY = "UserId"
    private const val RANGE_KEY = "OrderAndPosition"

    val attributes: List<AttributeDefinition> = listOf(
        AttributeDefinition.builder()
            .attributeName(PRIMARY_KEY)
            .attributeType(ScalarAttributeType.S)
            .build(),

        AttributeDefinition.builder()
            .attributeName(RANGE_KEY)
            .attributeType(ScalarAttributeType.S)
            .build()
    )

    val keys: List<KeySchemaElement> = listOf(
        KeySchemaElement.builder()
            .attributeName(PRIMARY_KEY)
            .keyType(KeyType.HASH)
            .build(),

        KeySchemaElement.builder()
            .attributeName(RANGE_KEY)
            .keyType(KeyType.RANGE)
            .build(),
    )
}