package com.godaddy.namesearch.login_screen

import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.activity.MainActivity
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.AuthManagerNew
import com.godaddy.namesearch.repository.storage.LoginRequest
import com.godaddy.namesearch.repository.storage.LoginResponse
import com.godaddy.namesearch.utils.GetFragment
import com.godaddy.namesearch.utils.NonNullObservableField


@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val getFragment: GetFragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(getFragment) as T
    }
}

@Suppress("UNCHECKED_CAST")
class LoginViewModel(private val getFragment: GetFragment) : AndroidViewModel(getFragment.getFragment().requireActivity().application) {

    var userNameTextInputEditText: NonNullObservableField<String> = NonNullObservableField("")
    fun userNameTextInputEditTextChanged(string: CharSequence, start: Int, before: Int, count: Int) { }

    var passwordTextInputEditText: NonNullObservableField<String> = NonNullObservableField("")
    fun passwordTextInputEditTextChanged(string: CharSequence, start: Int, before: Int, count: Int) { }

    fun onLoginClicked() {
        (getFragment.getFragment().requireActivity() as MainActivity).progressBarVisibility.set(View.VISIBLE)
        Repository.postLogin(LoginRequest(user = userNameTextInputEditText.get(), pwd = passwordTextInputEditText.get()), this::processLogin)
    }

    private fun processLogin(loginResponse: LoginResponse) {
        (getFragment.getFragment().requireActivity() as MainActivity).progressBarVisibility.set(View.GONE)
        AuthManagerNew.user = loginResponse.user
        AuthManagerNew.token = loginResponse.auth.token
        (getFragment.getFragment().requireActivity() as MainActivity).loadSearchFragment()
    }

}