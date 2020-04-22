package com.fwisniewski.firebasesample.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult

import com.fwisniewski.firebasesample.R
import com.fwisniewski.firebasesample.auth.AuthManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class LoginFragment(private val isLogged: () -> Unit) : Fragment() {

    private val authManager: AuthManager by inject()
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCreateGoogleLogin()
        onCreateFacebookLogin()
    }

    private fun onCreateGoogleLogin() {
        btnGoogleSignIn.setOnClickListener {
            val signInIntent = authManager.getSignInIntent()
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }
    }

    private fun onCreateFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()

        btnFbookSignIn.setPermissions("email", "public_profile")
        btnFbookSignIn.fragment = this
        btnFbookSignIn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Log.d("FLMWG", "facebook:onSuccess: $result")
                authManager.firebaseSignInWithFacebook(result.accessToken)
            }

            override fun onCancel() {
                Log.d("FLMWG", "facebook:onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("FLMWG", "facebook:onError:", error)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("FLMWG", "requestCode: $requestCode")
        Log.d("FLMWG", "resultCode: $resultCode")

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                authManager.firebaseSignInWithGoogle(account!!) { isSuccessful ->
                    if (isSuccessful) isLogged() else alert("Ocorreu um erro ao realizar o login!")
                }

            } catch (e: ApiException) {
                Log.d("FLMWG", "LF Google sign in failed", e)
                alert("Ocorreu um erro ao realizar o login !")
            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun alert(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 9001
    }
}
