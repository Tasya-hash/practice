package com.example.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.domain.AuthNavigator

class AuthNavigatorImpl : AuthNavigator {

    override fun navigateToLogin(context: Context) {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra("mode", "login")
        context.startActivity(intent)
    }

    override fun navigateToRegister(context: Context) {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra("mode", "register")
        context.startActivity(intent)
    }

    override fun openAuthFlow(activity: Activity, requestCode: Int) {
        val intent = Intent(activity, AuthActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }

    override fun navigateToMainAfterAuth(context: Context) {
        // Этот метод будет реализован в главном приложении
        // Через broadcast или callback
        val intent = Intent("AUTH_SUCCESS")
        context.sendBroadcast(intent)
    }
}