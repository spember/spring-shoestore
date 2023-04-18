package io.spring.shoestore

import io.spring.shoestore.app.http.api.ShoeResults
import io.spring.shoestore.support.BaseIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class ShoeControllerTests: BaseIntegrationTest() {

	@Test
	fun contextLoads() {
	}

	@Test
	fun listShoes() {
		val results = restTemplate.getForObject("http://localhost:${serverPort}/shoes", ShoeResults::class.java)
		assertEquals(2, results.shoes.size)
		assertTrue(results.shoes.map { it.name }.contains("Spring Sneaker"))
	}

}
