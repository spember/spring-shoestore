package io.spring.shoestore.core

import io.spring.shoestore.core.products.ShoeId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class CoreTest {

    @Test
    fun shoeIdComparison() {
        val common = UUID.randomUUID()
        assertEquals(ShoeId(common), ShoeId(common))
        assertNotEquals(ShoeId(common), ShoeId(UUID.randomUUID()))
    }

    @Test
    fun `creating shoeIds from raw Values`() {
        val base = UUID.randomUUID()
        assertEquals(ShoeId(base), ShoeId.from(base.toString()))
    }
}