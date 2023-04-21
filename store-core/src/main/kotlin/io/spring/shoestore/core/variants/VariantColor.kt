package io.spring.shoestore.core.variants

enum class VariantColor(val code: String) {

    WHITE("white"),
    GREEN("green"),
    BLACK("black"),
    BLUE("blue"),
    RED("red");


    companion object {
        fun lookup(code: String): VariantColor? {
            return values().find { it.code == code }
        }
    }

}