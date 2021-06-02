package com.godaddy.namesearch.payment_screen

import android.app.Application
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.R
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.storage.*
import com.godaddy.namesearch.utils.GetFragment
import java.text.NumberFormat


@Suppress("UNCHECKED_CAST")
class PaymentViewModelFactory(private val getFragment: GetFragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentViewModel(getFragment) as T
    }
}

@Suppress("UNCHECKED_CAST")
class PaymentViewModel(private val getFragment: GetFragment) : RecyclerViewViewModel(getFragment.getNonNullActivity().application) {

    override var adapter: PaymentAdapter = PaymentAdapter(getFragment)
    override val itemDecorator: RecyclerView.ItemDecoration? = DividerItemDecoration(getApplication<Application>().applicationContext, LinearLayout.VERTICAL)
    private lateinit var paymentMethodList: List<PaymentMethod>
    val totalPrice: ObservableField<String> = ObservableField("")
    val isPayNowEnabled: ObservableField<Boolean> = ObservableField(false)
    val backgroundColor: ObservableField<String> = ObservableField("#D3D3D3")

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
        getFragment.getNonNullActivity().progressBarVisibility.set(View.GONE)
        Repository.getPaymentMethods(this::showPaymentsList)
        isPayNowEnabled.set(false)
    }

    private fun showPaymentsList(paymentMethodList: List<PaymentMethod>) {
        getFragment.getNonNullActivity().progressBarVisibility.set(View.GONE)
        this.paymentMethodList = paymentMethodList
        adapter.addAll(paymentMethodList)
    }

    fun onItemClicked(view: View) {
        isPayNowEnabled.set(true)
        PaymentsManagerNew.selectedPaymentMethod = paymentMethodList[view.tag as Int]
        (getFragment.getFragment() as PaymentFragment).selectedPaymentMethodView = view
        view.setBackgroundColor(Color.LTGRAY)
        backgroundColor.set("#${Integer.toHexString(ContextCompat.getColor(getApplication<Application>().applicationContext, R.color.black))}")
    }

    fun onSelectPaymentClicked() {
        Repository.postPaymentProcessing(PaymentRequest(auth = AuthManagerNew.token, token = PaymentsManagerNew.selectedPaymentMethod.token), this::processPayment)
    }

    private fun processPayment() {
        AlertDialog.Builder(getFragment.getNonNullActivity())
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