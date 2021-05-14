package com.godaddy.namesearch.cart_screen

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.payment_screen.PaymentNewActivity
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.ShoppingCartNew
import com.godaddy.namesearch.utils.CartCallbacks
import com.godaddy.namesearch.utils.DaggerRepositoryCartDependencyInjector
import com.godaddy.namesearch.utils.RepositoryCartInjectorModule
import kotlinx.android.synthetic.main.activity_cart_new.view.*
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
    override val itemDecorator: RecyclerView.ItemDecoration? = DividerItemDecoration(cartCallbacks.fetchCartActivity(), LinearLayout.VERTICAL)
    val clickable: ObservableField<Boolean> = ObservableField(false)

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
        updateButton()
    }

    fun onPayNowClicked() {
        cartCallbacks.fetchCartActivity().startActivity(Intent(cartCallbacks.fetchCartActivity(), PaymentNewActivity::class.java))
    }

    fun onRemoveClicked(view: View) {
        adapter.remove(view.tag as Int)
        adapter.notifyDataSetChanged()
        ShoppingCartNew.domains.removeAt(view.tag as Int)
        updateButton()
    }

    private fun updateButton() {
        val buttonView = cartCallbacks.fetchCartRootView().select_button
        if (ShoppingCartNew.domains.size == 0) {
            buttonView.setBackgroundColor(Color.LTGRAY)
            clickable.set(false)
        } else {
            buttonView.setBackgroundColor(Color.BLACK)
            clickable.set(true)
        }
    }

}