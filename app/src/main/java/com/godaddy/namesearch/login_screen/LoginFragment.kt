package com.godaddy.namesearch.login_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.FragmentLoginBinding
import com.godaddy.namesearch.utils.BaseFragment

class LoginFragment : BaseFragment() {

    private lateinit var loginViewModel: LoginViewModel

    companion object {
        fun newInstance(): LoginFragment { return LoginFragment() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_login, container, false) as FragmentLoginBinding
        binding.lifecycleOwner = this
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)
        (binding as FragmentLoginBinding).loginViewModel = loginViewModel
        return binding.root
    }
}