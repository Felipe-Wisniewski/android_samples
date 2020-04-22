package com.fwisniewski.firebasesample.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fwisniewski.firebasesample.MainActivity

import com.fwisniewski.firebasesample.R
import com.fwisniewski.firebasesample.auth.AuthManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val authManager: AuthManager by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        val user = authManager.getUserAccount()

        user.let {
            txt_user_name.text = user?.displayName
            txt_user_email.text = user?.email
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                authManager.firebaseSignOut {
                    logout()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        if (activity is MainActivity) {
            (activity as MainActivity).verifyUserLoggedIn()
        }
    }
}
