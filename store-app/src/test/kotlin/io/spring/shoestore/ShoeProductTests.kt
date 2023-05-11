package io.spring.shoestore

import io.spring.shoestore.app.http.api.ShoeResults
import io.spring.shoestore.support.BaseIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class ShoeProductTests: BaseIntegrationTest() {

	@Test
	fun contextLoads() {
	}

	@Test
	fun `no query params should return all`() {
		val results = restTemplate.getForObject("http://localhost:${serverPort}/shoes", ShoeResults::class.java)
		assertEquals(2, results.shoes.size)
		assertTrue(results.shoes.map { it.name }.contains("Spring Sneaker"))
	}

	@Test
	fun `filter by name`() {
		val results = restTemplate.getForObject("http://localhost:${serverPort}/shoes?name=neak", ShoeResults::class.java)
		assertEquals(1, results.shoes.size)
		assertEquals("Spring Sneaker", results.shoes.first().name)
	}

}
