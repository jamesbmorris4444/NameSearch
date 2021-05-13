package com.godaddy.namesearch.repository

import android.view.View
import android.widget.ProgressBar
import com.godaddy.namesearch.logger.LogUtils
import com.godaddy.namesearch.repository.network.*
import com.godaddy.namesearch.repository.storage.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Repository {

    private val apiInterfacePost: APIInterfacePOST = APIClient.postClient
    private val apiInterfaceExactGET: APIInterfaceExactGET = APIClient.getExactList
    private val apiInterfaceSpinsGET: APIInterfaceSpinsGET = APIClient.getSpinsList
    private val apiInterfacePaymentsGET: APIInterfacePaymentsGET = APIClient.getPaymentsList
    private lateinit var disposable: Disposable

    fun postLogin(request: LoginRequest, progressBar: ProgressBar, processLogin: (loginResponse: LoginResponse) -> Unit) {
        disposable = apiInterfacePost.login(request)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{ loginResponse ->
                disposable.dispose()
                progressBar.visibility = View.GONE
                processLogin(loginResponse)
            }
    }

    fun getExactListDomains(query: String, progressBar: ProgressBar, showExactList: (exactResponse: DomainSearchExactMatchResponse) -> Unit) {
        disposable = apiInterfaceExactGET.getExactDomains(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{ exactResponse ->
                disposable.dispose()
                progressBar.visibility = View.GONE
                showExactList(exactResponse)
                LogUtils.D(
                    APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(
                        LogUtils.TagFilter.API
                    ), String.format("5555555************=%s", exactResponse))
            }
    }

    fun getSpinsListDomains(query: String, progressBar: ProgressBar, showSpinsList: (spinsResponse: DomainSearchRecommendedResponse) -> Unit) {
        disposable = apiInterfaceSpinsGET.getSpinsDomains(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{ spinsResponse ->
                disposable.dispose()
                progressBar.visibility = View.GONE
                showSpinsList(spinsResponse)
                LogUtils.D(
                    APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(
                        LogUtils.TagFilter.API
                    ), String.format("5555555************=%s", spinsResponse))
            }
    }

    fun getPaymentMethods(progressBar: ProgressBar, showPaymentsList: (paymentsResponse: List<PaymentMethod>) -> Unit) {
        disposable = apiInterfacePaymentsGET.getPaymentsList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{ paymentsResponse ->
                disposable.dispose()
                progressBar.visibility = View.GONE
                showPaymentsList(paymentsResponse)
                LogUtils.D(
                    APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(
                        LogUtils.TagFilter.API
                    ), String.format("9999999************=%s", paymentsResponse))
            }
    }

}