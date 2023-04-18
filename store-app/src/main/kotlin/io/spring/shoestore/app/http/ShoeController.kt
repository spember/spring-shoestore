package io.spring.shoestore.app.http

import io.spring.shoestore.app.http.api.ShoeData
import io.spring.shoestore.app.http.api.ShoeResults
import io.spring.shoestore.core.products.Shoe
import io.spring.shoestore.core.products.ShoeLookupQuery
import io.spring.shoestore.core.products.ShoeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoeController(private val shoeService: ShoeService) {

    @GetMapping("/shoes")
    fun listShoes(): ShoeResults {
        val query = ShoeLookupQuery(null, null)
        return ShoeResults(shoeService.search(query).map { convert(it) })
    }

    private fun convert(domain: Shoe): ShoeData = ShoeData(
        domain.id.value.toString(),
        domain.name,
        domain.description?:"",
        "${domain.price} ${domain.currency.code}"
    )
}