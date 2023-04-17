package io.spring.shoestore.core

import io.spring.shoestore.core.products.Currency
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CurrencyTest {

    @Test
    fun `Currency codes are all 3 characters`() {
        assertEquals(3, Currency.BRITISH_POUND.code.length)
        Currency.values().forEach { c ->
            assertEquals(3, c.code.length)
        }
    }

    @Test
    fun `Currencies can be looked up by code`() {
        assertNull(Currency.lookup("GCP"))
        assertEquals(Currency.BRITISH_POUND, Currency.lookup("GBP"))
    }
}