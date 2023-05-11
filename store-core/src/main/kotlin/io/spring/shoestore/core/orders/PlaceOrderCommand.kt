package io.spring.shoestore.core.orders

import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.variants.Sku

data class PlaceOrderCommand(val user: PrincipalUser, val items: List<Pair<Sku, Int>>)