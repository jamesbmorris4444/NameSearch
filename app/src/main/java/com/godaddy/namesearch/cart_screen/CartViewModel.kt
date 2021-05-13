package com.godaddy.namesearch.cart_screen

import android.app.Application
import android.content.Intent
import androidx.databinding.ObservableField
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
import java.text.NumberFormat
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
    val totalPrice: ObservableField<String> = ObservableField("")

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
        totalPrice.set(updatePayButton())
    }

    fun onPayNowClicked() {
        cartCallbacks.fetchCartActivity().startActivity(Intent(cartCallbacks.fetchCartActivity(), PaymentNewActivity::class.java))
    }

    private fun updatePayButton(): String {
        var totalPayment = 0.00
        ShoppingCartNew.domains.forEach {
            val priceDouble = it.price.replace("$","").replace(",","").toDouble()
            totalPayment += priceDouble
        }
        val currencyFormatter = NumberFormat.getCurrencyInstance()
        return "Pay ${currencyFormatter.format(totalPayment)} Now"
    }

}