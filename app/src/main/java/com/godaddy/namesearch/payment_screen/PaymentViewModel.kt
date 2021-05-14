package com.godaddy.namesearch.payment_screen

import android.app.Application
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.*
import com.godaddy.namesearch.utils.DaggerRepositoryPaymentDependencyInjector
import com.godaddy.namesearch.utils.PaymentCallbacks
import com.godaddy.namesearch.utils.RepositoryPaymentInjectorModule
import kotlinx.android.synthetic.main.activity_payment_method_new.view.*
import java.text.NumberFormat
import javax.inject.Inject


@Suppress("UNCHECKED_CAST")
class PaymentViewModelFactory(private val paymentCallbacks: PaymentCallbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentViewModel(paymentCallbacks) as T
    }
}

@Suppress("UNCHECKED_CAST")
class PaymentViewModel(private val paymentCallbacks: PaymentCallbacks) : RecyclerViewViewModel(paymentCallbacks.fetchPaymentActivity().application) {

    @Inject
    lateinit var repository: Repository

    override var adapter: PaymentAdapter = PaymentAdapter(paymentCallbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = DividerItemDecoration(paymentCallbacks.fetchPaymentActivity(), LinearLayout.VERTICAL)
    private lateinit var paymentMethodList: List<PaymentMethod>
    val totalPrice: ObservableField<String> = ObservableField("")

    init {
        DaggerRepositoryPaymentDependencyInjector.builder()
            .repositoryPaymentInjectorModule(RepositoryPaymentInjectorModule(paymentCallbacks.fetchPaymentActivity()))
            .build()
            .inject(this)
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
        totalPrice.set(updatePayButton())
        paymentCallbacks.fetchPaymentRootView().payment_progress_bar.visibility = View.VISIBLE
        repository.getPaymentMethods(this::showPaymentsList)
        paymentCallbacks.fetchPaymentRootView().pay_now_button.isEnabled = false
    }

    private fun showPaymentsList(paymentMethodList: List<PaymentMethod>) {
        paymentCallbacks.fetchPaymentRootView().payment_progress_bar.visibility = View.GONE
        this.paymentMethodList = paymentMethodList
        adapter.addAll(paymentMethodList)
    }

    fun onItemClicked(view: View) {
        paymentCallbacks.fetchPaymentRootView().pay_now_button.isEnabled = true
        PaymentsManagerNew.selectedPaymentMethod = paymentMethodList[view.tag as Int]
        paymentCallbacks.fetchPaymentActivity().selectedPaymentMethodView?.setBackgroundColor(Color.TRANSPARENT)
        paymentCallbacks.fetchPaymentActivity().selectedPaymentMethodView = view
        view.setBackgroundColor(Color.LTGRAY)
        val buttonView: Button = paymentCallbacks.fetchPaymentRootView().pay_now_button
        buttonView.setBackgroundColor(Color.BLACK)
    }

    fun onSelectPaymentClicked() {
        repository.postPaymentProcessing(PaymentRequest(auth = AuthManagerNew.token, token = PaymentsManagerNew.selectedPaymentMethod.token), this::processPayment)
    }

    private fun processPayment() {
        AlertDialog.Builder(paymentCallbacks.fetchPaymentActivity())
            .setTitle("All done!")
            .setMessage("Your purchase is complete!")
            .show()
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