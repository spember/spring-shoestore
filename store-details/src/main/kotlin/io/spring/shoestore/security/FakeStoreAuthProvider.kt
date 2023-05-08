package io.spring.shoestore.security

import io.spring.shoestore.core.security.PrincipalUser
import io.spring.shoestore.core.security.StoreAuthProvider

/**
 * Simulate real security and just spit out some user. In reality this class would be something like
 * "AuthZeroStoreAuthProvider" or something similar.
 *
 */
class FakeStoreAuthProvider: StoreAuthProvider {

    private var currentUser: PrincipalUser? = null


    override fun getCurrentUser(): PrincipalUser {
        if (currentUser == null) {
            throw RuntimeException("No logged in user")

        }
        return currentUser!!
    }

    /**
     * For testing purposes, login some user for testing
     */
    fun login(user: PrincipalUser) {
        this.currentUser = user
    }
}