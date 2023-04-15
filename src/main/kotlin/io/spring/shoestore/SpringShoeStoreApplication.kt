package io.spring.shoestore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringShoeStoreApplication

fun main(args: Array<String>) {
	runApplication<SpringShoeStoreApplication>(*args)
}
