package com.godaddy.namesearch.cart_screen

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.payment_screen.PaymentNewActivity
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.ShoppingCartNew
import com.godaddy.namesearch.utils.CartCallbacks
import com.godaddy.namesearch.utils.DaggerRepositoryCartDependencyInjector
import com.godaddy.namesearch.utils.RepositoryCartInjectorModule
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class CartViewModelFactory(private val cartCallbacks: CartCallbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(cartCallbacks) as T
    }
}

@Suppress("UNCHECKED_CAST")
class CartViewModel(private val cartCallbacks: CartCallbacks) : RecyclerViewViewModel(cartCallbacks.fetchCartActivity().application) {

    @Inject
    lateinit var repository: Repository

    override var adapter: CartAdapter = CartAdapter(cartCallbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null

    /**
     * Initialize the ViewModel using the primary constructor
     */
    init {
        cartCallbacks.fetchCartActivity()?.let { activity ->
            DaggerRepositoryCartDependencyInjector.builder()
                .repositoryCartInjectorModule(RepositoryCartInjectorModule(activity))
                .build()
                .inject(this)
        }
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(getApplication<Application>().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    fun initialize() {
        adapter.addAll(ShoppingCartNew.domains)
    }

    fun onPayNowClicked() {
        cartCallbacks.fetchCartActivity().startActivity(Intent(cartCallbacks.fetchCartActivity(), PaymentNewActivity::class.java))
    }

    fun onRemoveClicked(view: View) {
        adapter.remove(view.tag as Int)
        adapter.notifyDataSetChanged()
        ShoppingCartNew.domains.removeAt(view.tag as Int)
    }

}