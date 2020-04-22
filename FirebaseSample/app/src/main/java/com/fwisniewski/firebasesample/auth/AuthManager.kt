package com.fwisniewski.firebasesample.auth

import android.content.Context
import android.util.Log
import com.facebook.AccessToken
import com.fwisniewski.firebasesample.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthManager(context: Context) {

    private val app = context.applicationContext

    private val auth = FirebaseAuth.getInstance()

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(app, gso)
    }

    fun getUserId() = getUserAccount()?.providerId

    fun getUserAccount() = auth.currentUser

    fun getSignInIntent() = googleSignInClient.signInIntent

    fun firebaseSignInWithGoogle(acct: GoogleSignInAccount,
                               callback: (isSuccessful: Boolean) -> Unit) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener {
            callback(it.isSuccessful)
        }
    }

    fun firebaseSignOut(callback: () -> Unit) {
        googleSignInClient.signOut().addOnCompleteListener {
            if (it.isSuccessful) {
                auth.signOut()
                callback()
            }
        }
    }

    fun firebaseSignInWithFacebook(token: AccessToken) {
        Log.d("FLMWG", "facebook token -> $token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        auth.signInWithCredential(credential).addOnCompleteListener {
            Log.d("FLMWG", "login face -> ${it.isSuccessful}")
        }
    }
}