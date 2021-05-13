package com.godaddy.namesearch.payment_screen

import android.app.Application
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.R
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.*
import com.godaddy.namesearch.utils.DaggerRepositoryPaymentDependencyInjector
import com.godaddy.namesearch.utils.PaymentCallbacks
import com.godaddy.namesearch.utils.RepositoryPaymentInjectorModule
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
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    private lateinit var paymentMethodList: List<PaymentMethod>
    val totalPrice: ObservableField<String> = ObservableField("")

    init {
        paymentCallbacks.fetchPaymentActivity()?.let { activity ->
            DaggerRepositoryPaymentDependencyInjector.builder()
                .repositoryPaymentInjectorModule(RepositoryPaymentInjectorModule(activity))
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
        totalPrice.set(updatePayButton())
        val progressBar: ProgressBar = paymentCallbacks.fetchPaymentActivity().findViewById(R.id.payment_progress_bar)
        progressBar.visibility = View.VISIBLE
        repository.getPaymentMethods(progressBar, this::showPaymentsList)
    }

    private fun showPaymentsList(paymentMethodList: List<PaymentMethod>) {
        this.paymentMethodList = paymentMethodList
        adapter.addAll(paymentMethodList)
    }

    fun onItemClicked(view: View) {
        PaymentsManagerNew.selectedPaymentMethod = paymentMethodList[view.tag as Int]
        paymentCallbacks.fetchPaymentActivity().selectedPaymentMethodView?.let {
            it.setBackgroundColor(Color.TRANSPARENT)
        }
        paymentCallbacks.fetchPaymentActivity().selectedPaymentMethodView = view
        view.setBackgroundColor(Color.LTGRAY)
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