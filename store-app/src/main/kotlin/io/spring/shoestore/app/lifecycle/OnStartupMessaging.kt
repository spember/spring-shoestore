package io.spring.shoestore.app.lifecycle

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
class OnStartupMessaging: ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        log.info("Configuring Messaging")
    }

    companion object {
        private val log = LoggerFactory.getLogger(OnStartupMessaging::class.java)
    }
}