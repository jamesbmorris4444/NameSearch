package com.godaddy.namesearch.payment_screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.godaddy.namesearch.R
import com.godaddy.namesearch.databinding.ActivityPaymentMethodNewBinding
import com.godaddy.namesearch.repository.Repository
import com.godaddy.namesearch.utils.PaymentCallbacks
import timber.log.Timber

class PaymentNewActivity : AppCompatActivity(), PaymentCallbacks {

    lateinit var repository: Repository
    lateinit var paymentViewModel: PaymentViewModel
    private lateinit var activityMainBinding: ActivityPaymentMethodNewBinding
    var selectedPaymentMethodView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository()
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment_method_new)
        activityMainBinding.lifecycleOwner = this
        paymentViewModel = ViewModelProvider(this, PaymentViewModelFactory(this)).get(PaymentViewModel::class.java)
        activityMainBinding.paymentViewModel = paymentViewModel
        paymentViewModel.initialize()
    }

    override fun fetchPaymentActivity(): PaymentNewActivity {
        return this
    }

    override fun fetchPaymentRootView(): View {
        return activityMainBinding.root
    }
}