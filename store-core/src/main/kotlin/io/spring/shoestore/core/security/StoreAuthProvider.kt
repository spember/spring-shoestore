package io.spring.shoestore.core.security

interface StoreAuthProvider {

    fun getCurrentUser(): PrincipalUser
}