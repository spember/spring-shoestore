package io.spring.shoestore.core.variants

enum class VariantSize(val code: String) {
    // an enum is probably not the best for this ;)
    US_10("US 10"),
    US_10_5("US 10.5"),
    US_11("US 11"),
    US_11_5("US 11.5");

    companion object {
        fun lookup(code: String): VariantSize? {
            return values().find { it.code == code }
        }
    }
}