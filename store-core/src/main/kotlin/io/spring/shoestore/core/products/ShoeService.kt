package io.spring.shoestore.core.products

class ShoeService(private val repository: ShoeRepository) {

    // parse query

    fun get(id: ShoeId): Shoe? = repository.findById(id)

    fun search(query: ShoeLookupQuery): List<Shoe> {
        return listOf()
    }

}