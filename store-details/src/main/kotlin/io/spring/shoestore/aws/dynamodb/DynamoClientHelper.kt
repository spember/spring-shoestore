package io.spring.shoestore.aws.dynamodb

import software.amazon.awssdk.services.dynamodb.model.AttributeValue

/**
 * Some of the repetition when working with all of the Dynamo client builders was making things hard to read.
 *
 * Moving some here to clean up
 */
internal object DynamoClientHelper {

    fun createStringAttribute(s: String): AttributeValue = AttributeValue.builder().s(s).build()

    fun createNumAttribute(number: Int): AttributeValue = AttributeValue.builder().n(number.toString()).build()
}