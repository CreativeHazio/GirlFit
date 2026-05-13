package com.creativehazio.auth.data

import com.creativehazio.auth.error.LoginError
import com.creativehazio.auth.error.SignUpError
import com.creativehazio.common.resulthandler.Result

interface AuthService {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<Unit, LoginError>
    suspend fun signUpWithEmailAndPassword(name: String, email: String, password: String): Result<Unit, SignUpError>

    suspend fun signInWithGoogle(idToken: String, accessToken: String? = null): Result<Unit, LoginError>
    suspend fun signInWithApple(idToken: String, rawNonce: String): Result<Unit, LoginError>
}