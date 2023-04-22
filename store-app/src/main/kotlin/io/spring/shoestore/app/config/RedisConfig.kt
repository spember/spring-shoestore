package io.spring.shoestore.app.config

import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import redis.clients.jedis.Jedis


@Configuration
class RedisConfig {

    @Bean
    fun jedisConnectionFactory(
        @Value("\${spring.redis.host}") redisHost: String,
        @Value("\${spring.redis.port}") redisPort: String
    ): JedisConnectionFactory {
        val config = RedisStandaloneConfiguration()
        config.hostName = redisHost
        config.port = Integer.parseInt(redisPort)
        return JedisConnectionFactory(config)
    }

    @Bean
    fun redisTemplate(connectionFactory: JedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(connectionFactory)
        return template
    }

    @Bean(destroyMethod = "close")
    fun getJedisClient(connectionFactory: JedisConnectionFactory): Jedis {
        return connectionFactory.connection.nativeConnection as Jedis
    }

}