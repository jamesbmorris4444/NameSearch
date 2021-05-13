package com.godaddy.namesearch.cart_screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ActivityCartNewBinding
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.utils.CartCallbacks
import timber.log.Timber

class CartNewActivity : AppCompatActivity(), CartCallbacks {

    lateinit var repository: Repository
    lateinit var cartViewModel: CartViewModel
    private lateinit var activityMainBinding: ActivityCartNewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository()
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_cart_new)
        activityMainBinding.lifecycleOwner = this
        cartViewModel = ViewModelProvider(this, CartViewModelFactory(this)).get(CartViewModel::class.java)
        activityMainBinding.cartViewModel = cartViewModel
        cartViewModel.initialize()
    }

    override fun fetchCartActivity(): CartNewActivity {
        return this
    }

    override fun fetchCartRootView(): View {
        return activityMainBinding.root
    }
}