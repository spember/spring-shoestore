package io.spring.shoestore.core.products

/**
 * Yes we absolutely could have used java.util.Currency.
 *
 */
enum class Currency(val code: String) {
    US_DOLLAR("USD"),
    EURO("EUR"),
    BRITISH_POUND("GBP");

    companion object {
        fun lookup(code: String): Currency? {
            return values().find { it.code == code }
        }
    }
}