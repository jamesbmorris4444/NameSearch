package com.godaddy.namesearch.payment_screen

import android.app.Application
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godaddy.namesearch.R
import com.godaddy.namesearch.logger.LogUtils
import com.godaddy.namesearch.recyclerview.RecyclerViewViewModel
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.repository.network.APIClient
import com.godaddy.namesearch.repository.storage.PaymentMethod
import com.godaddy.namesearch.utils.DaggerRepositoryPaymentDependencyInjector
import com.godaddy.namesearch.utils.PaymentCallbacks
import com.godaddy.namesearch.utils.RepositoryPaymentInjectorModule
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

    /**
     * Initialize the ViewModel using the primary constructor
     */
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
        val progressBar: ProgressBar = paymentCallbacks.fetchPaymentActivity().findViewById(R.id.payment_progress_bar)
        progressBar.visibility = View.VISIBLE
        repository.getPaymentMethods(progressBar, this::showPaymentsList)
    }

    private fun showPaymentsList(paymentMethodList: List<PaymentMethod>) {
        LogUtils.D(
            APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(
                LogUtils.TagFilter.API
            ), String.format("exact response1************=%s", paymentMethodList))
        adapter.addAll(paymentMethodList)
    }

    fun onItemClicked(view: View) {

    }

}