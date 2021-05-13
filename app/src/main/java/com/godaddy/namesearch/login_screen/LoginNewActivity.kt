package com.godaddy.namesearch.login_screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ActivityLoginNewBinding
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.utils.LoginCallbacks
import timber.log.Timber

class LoginNewActivity : AppCompatActivity(), LoginCallbacks {

    lateinit var repository: Repository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var activityMainBinding: ActivityLoginNewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository()
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_new)
        activityMainBinding.lifecycleOwner = this
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)
        activityMainBinding.loginViewModel = loginViewModel
    }

    override fun fetchLoginActivity(): LoginNewActivity {
        return this
    }

    override fun fetchLoginRootView(): View {
        return activityMainBinding.root
    }
}