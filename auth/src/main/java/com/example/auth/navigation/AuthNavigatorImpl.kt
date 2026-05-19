package com.example.auth.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.auth.ui.AuthActivity
import com.example.domain.interfaces.AuthNavigator

class AuthNavigatorImpl(private val context: Context) : AuthNavigator {

    override fun navigateToLogin() {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra("mode", "login")
        context.startActivity(intent)
    }

    override fun navigateToRegister() {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra("mode", "register")
        context.startActivity(intent)
    }

    override fun openAuthFlow() {
        val intent = Intent(context, AuthActivity::class.java)
        context.startActivity(intent)
    }

    override fun navigateToMainAfterAuth() {
        (context as? Activity)?.finish()
    }
}