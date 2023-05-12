package io.spring.shoestore.core.security

/**
 * Represents the security principal, or logged-in user
 */
class PrincipalUser(val name: String, val email: String) {
    override fun toString(): String = "$name ($email)"
}