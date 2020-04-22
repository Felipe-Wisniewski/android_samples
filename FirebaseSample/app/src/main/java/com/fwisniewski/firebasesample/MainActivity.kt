package com.fwisniewski.firebasesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.fwisniewski.firebasesample.auth.AuthManager
import com.fwisniewski.firebasesample.ui.home.HomeFragment
import com.fwisniewski.firebasesample.ui.login.LoginFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val authManager: AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        verifyUserLoggedIn()
    }

    fun verifyUserLoggedIn() {
        val account = authManager.getUserAccount()
        val fragment : Fragment

        fragment = if (account == null) LoginFragment(this::isLoggedCallback) else HomeFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commitNow()
    }

    private fun isLoggedCallback() {
        verifyUserLoggedIn()
    }
}
