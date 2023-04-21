package io.spring.shoestore.postgres.mappers

import io.spring.shoestore.core.products.ShoeId
import io.spring.shoestore.core.variants.ProductVariant
import io.spring.shoestore.core.variants.Sku
import io.spring.shoestore.core.variants.VariantColor
import io.spring.shoestore.core.variants.VariantSize
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

internal class ProductVariantMapper: RowMapper<ProductVariant> {

    override fun mapRow(rs: ResultSet, rowNum: Int): ProductVariant {
        return ProductVariant(
            sku=Sku(rs.getString("sku")),
            shoeId = ShoeId(UUID.fromString(rs.getString("shoe_id"))),
            label = rs.getString("label"),
            size= VariantSize.lookup(rs.getString("size"))!!,
            color= VariantColor.lookup(rs.getString("color"))!!
        )
    }
}