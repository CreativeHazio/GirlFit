package com.creativehazio.auth.data

import com.creativehazio.auth.error.LoginError
import com.creativehazio.auth.error.SignUpError
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.OAuthProvider
import dev.gitlive.firebase.auth.auth
import com.creativehazio.common.resulthandler.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.IOException


class FirebaseAuthService : AuthService {
    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<Unit, LoginError> = withContext(Dispatchers.IO){
        return@withContext try {
            val result = Firebase.auth.signInWithEmailAndPassword(email, password)

            if (result.user?.isEmailVerified == true) {
                Result.Success(Unit)
            } else {
                Result.Error(LoginError.USER_NOT_VERIFIED)
            }
        } catch (e: FirebaseAuthException) {
            println("Error is ${e.message}")
            Result.Error(LoginError.fromMessage(e.message))
        } catch (e: Exception) {
            Result.Error(LoginError.NETWORK_ERROR)
        }
    }

    override suspend fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): Result<Unit, SignUpError> = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = Firebase.auth.createUserWithEmailAndPassword(email, password)

            result.user?.updateProfile(displayName = name)

            result.user?.sendEmailVerification()

            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.Error(SignUpError.fromMessage(e.message))
        } catch (e: Exception) {
            Result.Error(SignUpError.NETWORK_ERROR)
        }
    }

    override suspend fun signInWithGoogle(
        idToken: String,
        accessToken: String?
    ): Result<Unit, LoginError> {
        return try {
            val credential = GoogleAuthProvider.credential(idToken, accessToken)

            Firebase.auth.signInWithCredential(credential)

            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.Error(LoginError.fromMessage(e.message))
        } catch (e: Exception) {
            Result.Error(LoginError.NETWORK_ERROR)
        }
    }

    override suspend fun signInWithApple(
        idToken: String,
        rawNonce: String
    ): Result<Unit, LoginError> {
        return try {
//            val provider = OAuthProvider(
//                provider = "apple.com"
//            )
//
//            val credential = provider.credential(
//                idToken = idToken,
//                accessToken = null,
//                rawNonce = rawNonce
//            )
//
//            Firebase.auth.signInWithCredential(credential)

            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.Error(LoginError.fromMessage(e.message))
        } catch (e: Exception) {
            Result.Error(LoginError.NETWORK_ERROR)
        }
    }
}