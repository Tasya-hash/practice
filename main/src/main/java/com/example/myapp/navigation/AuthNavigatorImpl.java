package com.example.myapp.navigation;

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.auth.ui.LoginActivity
import com.example.auth.ui.RegisterActivity
import com.example.domain.navigation.AuthNavigator
import com.example.myapp.practice.MainActivity

class AuthNavigatorImpl : AuthNavigator {
    override fun navigateToLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    override fun navigateToRegister(context: Context) {
        context.startActivity(Intent(context, RegisterActivity::class.java))
    }

    override fun openAuthFlow(activity: Activity, requestCode: Int) {
        activity.startActivityForResult(
                Intent(activity, LoginActivity::class.java),
        requestCode
        )
    }

    override fun navigateToMain(context: Context) {
        context.startActivity(Intent(context, MainActivity::class.java))
    }
}