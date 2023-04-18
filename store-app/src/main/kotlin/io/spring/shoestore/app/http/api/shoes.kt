package io.spring.shoestore.app.http.api

data class ShoeResults(val shoes: List<ShoeData>)

// naming is hard
data class ShoeData(
    val id: String,
    val name: String,
    val description: String = "", // note this is not nullable
    val displayPrice: String
)