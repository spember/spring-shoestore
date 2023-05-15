package io.spring.shoestore.app.lifecycle

import io.spring.shoestore.aws.dynamodb.DynamoTableManager
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.ContextStartedEvent
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.dynamodb.DynamoDbClient


@Component
class OnStartupMessaging(private val dynamoDbClient: DynamoDbClient): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        println("App started!")
        DynamoTableManager(dynamoDbClient).establishOrderTable()
    }
}