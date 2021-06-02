package com.godaddy.namesearch.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.godaddy.namesearch.R
import com.godaddy.namesearch.cart_screen.CartFragment
import com.godaddy.namesearch.databinding.ActivityMainBinding
import com.godaddy.namesearch.login_screen.LoginFragment
import com.godaddy.namesearch.payment_screen.PaymentFragment
import com.godaddy.namesearch.search_screen.SearchFragment
import com.godaddy.namesearch.utils.ViewPagerFragment
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val progressBarVisibility: ObservableField<Int> = ObservableField(View.GONE)
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.lifecycleOwner = this
        activityMainBinding.mainActivity = this
        loadLoginFragment()
    }

    private fun loadLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_container, LoginFragment.newInstance())
            .commit()
    }

    fun loadSearchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_container, SearchFragment.newInstance(0))
            .commit()
    }

    fun loadCartFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_container, CartFragment.newInstance(1))
            .commit()
    }

    fun loadPaymentFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_container, PaymentFragment.newInstance(2))
            .commit()
    }

    fun loadViewPagerFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_container, ViewPagerFragment.newInstance())
            .commit()
    }
}