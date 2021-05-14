package com.godaddy.namesearch.login_screen

import android.content.Intent
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.AuthManagerNew
import com.godaddy.namesearch.repository.storage.LoginRequest
import com.godaddy.namesearch.repository.storage.LoginResponse
import com.godaddy.namesearch.search_screen.SearchNewActivity
import com.godaddy.namesearch.utils.DaggerRepositoryLoginDependencyInjector
import com.godaddy.namesearch.utils.LoginCallbacks
import com.godaddy.namesearch.utils.NonNullObservableField
import com.godaddy.namesearch.utils.RepositoryLoginInjectorModule
import kotlinx.android.synthetic.main.activity_login_new.view.*
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val loginCallbacks: LoginCallbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginCallbacks) as T
    }
}

@Suppress("UNCHECKED_CAST")
class LoginViewModel(private val loginCallbacks: LoginCallbacks) : AndroidViewModel(loginCallbacks.fetchLoginActivity().application) {

    @Inject
    lateinit var repository: Repository

    var userNameTextInputEditText: NonNullObservableField<String> = NonNullObservableField("")
    fun userNameTextInputEditTextChanged(string: CharSequence, start: Int, before: Int, count: Int) { }

    var passwordTextInputEditText: NonNullObservableField<String> = NonNullObservableField("")
    fun passwordTextInputEditTextChanged(string: CharSequence, start: Int, before: Int, count: Int) { }
    /**
     * Initialize the ViewModel using the primary constructor
     */
    init {
        loginCallbacks.fetchLoginActivity()?.let { activity ->
            DaggerRepositoryLoginDependencyInjector.builder()
                .repositoryLoginInjectorModule(RepositoryLoginInjectorModule(activity))
                .build()
                .inject(this)
        }
    }

    fun onLoginClicked() {
        loginCallbacks.fetchLoginRootView().main_progress_bar.visibility = View.VISIBLE
        repository.postLogin(LoginRequest(user = userNameTextInputEditText.get(), pwd = passwordTextInputEditText.get()), this::processLogin)
    }

    private fun processLogin(loginResponse: LoginResponse) {
        loginCallbacks.fetchLoginRootView().main_progress_bar.visibility = View.GONE
        AuthManagerNew.user = loginResponse.user
        AuthManagerNew.token = loginResponse.auth.token
        loginCallbacks.fetchLoginActivity().startActivity(Intent(loginCallbacks.fetchLoginActivity(), SearchNewActivity::class.java))
    }

}