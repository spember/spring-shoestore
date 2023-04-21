package io.spring.shoestore.core.products

class ShoeService(private val repository: ShoeRepository) {

    // parse query

    fun get(id: ShoeId): Shoe? = repository.findById(id)

    fun search(query: ShoeLookupQuery): List<Shoe> {
        if (query.isEmpty()) {
            return repository.list()
        } else {
            val nameResults = repository.findByName(query.byName?: "")
            // todo: price
            return nameResults
        }
    }
}