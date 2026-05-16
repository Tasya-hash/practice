package com.example.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.domain.AuthNavigator
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

    override fun openAuthFlow(requestCode: Int) {
        if (context is Activity) {
            val intent = Intent(context, AuthActivity::class.java)
            context.startActivityForResult(intent, requestCode)
        }
    }

    override fun navigateToMainAfterAuth() {
        val intent = Intent("AUTH_SUCCESS")
        context.sendBroadcast(intent)
    }
}