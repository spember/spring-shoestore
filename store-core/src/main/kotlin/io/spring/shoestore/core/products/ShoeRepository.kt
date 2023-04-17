package io.spring.shoestore.core.products

import java.math.BigDecimal

interface ShoeRepository {

    fun findById(id: ShoeId): Shoe?

    fun list(): List<Shoe>

    fun findByName(namePartial: String): List<Shoe>

    fun findByPriceUnder(upperPrice: BigDecimal): List<Shoe>
}