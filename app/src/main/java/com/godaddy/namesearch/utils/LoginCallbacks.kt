package com.godaddy.namesearch.utils

import android.view.View
import com.godaddy.namesearch.login_screen.LoginNewActivity

interface LoginCallbacks {
    fun fetchLoginActivity(): LoginNewActivity
    fun fetchLoginRootView(): View
}