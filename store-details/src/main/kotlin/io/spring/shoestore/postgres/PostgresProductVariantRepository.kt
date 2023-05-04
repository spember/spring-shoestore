package io.spring.shoestore.postgres

import io.spring.shoestore.core.products.ShoeId
import io.spring.shoestore.core.variants.ProductVariant
import io.spring.shoestore.core.variants.ProductVariantRepository
import io.spring.shoestore.core.variants.Sku
import io.spring.shoestore.postgres.mappers.ProductVariantMapper
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement

class PostgresProductVariantRepository(private val jdbcTemplate: JdbcTemplate): ProductVariantRepository {

    override fun findAllVariantsForShoe(shoeId: ShoeId): List<ProductVariant> {
        return jdbcTemplate.query("select * from variants where shoe_id = ?;",
            PV_MAPPER,
            shoeId.value
        )
    }

    override fun findById(sku: Sku): ProductVariant? {
        return jdbcTemplate.queryForObject("select * from variants where sku = ? limit 1;", PV_MAPPER, sku.value)
    }

    override fun registerNewVariants(variants: List<ProductVariant>) {
        jdbcTemplate.batchUpdate("insert into variants (sku, shoe_id, label, size, color) values (?, ?, ?, ?, ?) on conflict do nothing; ", object: BatchPreparedStatementSetter {
            override fun setValues(ps: PreparedStatement, i: Int) {
                val variant = variants[i]
                ps.setString(1, variant.sku.value)
                ps.setObject(2, variant.shoeId.value)
                ps.setString(3, variant.label)
                ps.setString(4, variant.size.code)
                ps.setString(5, variant.color.code)
            }

            override fun getBatchSize(): Int = variants.size
        })
    }

    companion object {
        private val PV_MAPPER = ProductVariantMapper()
    }

}