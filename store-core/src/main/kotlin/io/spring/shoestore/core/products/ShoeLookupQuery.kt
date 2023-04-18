package io.spring.shoestore.core.products

import java.math.BigDecimal

data class ShoeLookupQuery(val byName: String?, val byPrice: BigDecimal? ) {
    fun isEmpty() = byName == null && byPrice == null
}