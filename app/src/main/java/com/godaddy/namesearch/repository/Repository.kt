package com.godaddy.namesearch.repository

import android.view.View
import android.widget.ProgressBar
import com.godaddy.namesearch.repository.network.*
import com.godaddy.namesearch.repository.storage.*
import com.godaddy.namesearch.utils.Constants
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Repository {

    private val apiInterfaceLoginPost: APIInterfaceLoginPOST = APIClient.loginPostClient
    private val apiInterfaceNewsGet: APIInterfaceNewsGET = APIClient.newsGetClient
    private val apiInterfaceExactGET: APIInterfaceExactGET = APIClient.getExactList
    private val apiInterfaceSpinsGET: APIInterfaceSpinsGET = APIClient.getSpinsList
    private val apiInterfacePaymentsGET: APIInterfacePaymentsGET = APIClient.getPaymentsList
    private val apiInterfaceProcessPost: APIInterfaceProcessPOST = APIClient.PaymentProcessingPostClient
    private lateinit var disposable: Disposable

    fun postLogin(request: LoginRequest, progressBar: ProgressBar, processLogin: (loginResponse: LoginResponse) -> Unit, showError: (error: String?) -> Unit) {
        disposable = apiInterfaceLoginPost.login(request)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe({ loginResponse ->
                disposable.dispose()
                progressBar.visibility = View.GONE
                processLogin(loginResponse)
            },
            { throwable ->
                progressBar.visibility = View.GONE
                disposable?.dispose()
                showError(throwable.message)})
    }

    fun getNewsFeed(key: String, progressBar: ProgressBar, showNews: (newsResponse: NewsFeedItem) -> Unit, showNewsError: (error: String?) -> Unit) {
        disposable = apiInterfaceNewsGet.getNewsFeed(Constants.Q, Constants.FROM, Constants.SORT, key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe({ newsResponse ->
                disposable.dispose()
                progressBar.visibility = View.GONE
                showNews(newsResponse)
            },
            { throwable ->
                progressBar.visibility = View.GONE
                disposable?.dispose()
                showNewsError(throwable.message)
            })
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
            }
    }

    fun postPaymentProcessing(request: PaymentRequest, processPayment: () -> Unit) {
        disposable = Completable.fromAction { apiInterfaceProcessPost.processPayment(request) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{
                processPayment()
            }
    }

}