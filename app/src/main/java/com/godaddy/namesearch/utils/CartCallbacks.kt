package com.godaddy.namesearch.utils

import android.view.View
import com.godaddy.namesearch.cart_screen.CartNewActivity

interface CartCallbacks {
    fun fetchCartActivity(): CartNewActivity
    fun fetchCartRootView(): View
}