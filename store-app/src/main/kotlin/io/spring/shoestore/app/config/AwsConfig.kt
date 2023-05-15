package io.spring.shoestore.app.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Configuration
class AwsConfig {

    @Bean
    fun getDynamoClient(
        @Value("\${cloud.aws.region.main}") region: String,
        @Value("\${cloud.aws.credentials.access-key}") accessKey: String,
        @Value("\${cloud.aws.credentials.secret-key}") secretKey: String,
        @Value("\${cloud.aws.end-point.uri}") awsUri: String
    ): DynamoDbClient {
        return DynamoDbClient.builder()
            .endpointOverride(URI(awsUri))
            .region(Region.of(region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        accessKey,
                        secretKey
                    )
                )
            )
            .build()
    }
}