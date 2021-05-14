package com.godaddy.namesearch.repository

import com.godaddy.namesearch.repository.network.*
import com.godaddy.namesearch.repository.storage.*
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Repository {

    private val apiInterfaceLoginPost: APIInterfaceLoginPOST = APIClient.loginPostClient
    private val apiInterfaceExactGET: APIInterfaceExactGET = APIClient.getExactList
    private val apiInterfaceSpinsGET: APIInterfaceSpinsGET = APIClient.getSpinsList
    private val apiInterfacePaymentsGET: APIInterfacePaymentsGET = APIClient.getPaymentsList
    private val apiInterfaceProcessPost: APIInterfaceProcessPOST = APIClient.PaymentProcessingPostClient
    private lateinit var disposable: Disposable

    fun postLogin(request: LoginRequest, processLogin: (loginResponse: LoginResponse) -> Unit) {
        disposable = apiInterfaceLoginPost.login(request)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe({ loginResponse ->
                disposable.dispose()
                processLogin(loginResponse)
            },
            { throwable ->
                disposable.dispose()
                throwable.message?.let {
                    processLogin(LoginResponse(Auth(it), User("", "")))
                } ?: run {
                    processLogin(LoginResponse(Auth(""), User("", "")))
                }
            })
    }

    fun getExactListDomains(query: String, showExactList: (exactResponse: DomainSearchExactMatchResponse) -> Unit) {
        disposable = apiInterfaceExactGET.getExactDomains(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{ exactResponse ->
                disposable.dispose()
                showExactList(exactResponse)
            }
    }

    fun getSpinsListDomains(query: String, showSpinsList: (spinsResponse: DomainSearchRecommendedResponse) -> Unit) {
        disposable = apiInterfaceSpinsGET.getSpinsDomains(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{ spinsResponse ->
                disposable.dispose()
                showSpinsList(spinsResponse)
            }
    }

    fun getPaymentMethods(showPaymentsList: (paymentsResponse: List<PaymentMethod>) -> Unit) {
        disposable = apiInterfacePaymentsGET.getPaymentsList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{ paymentsResponse ->
                disposable.dispose()
                showPaymentsList(paymentsResponse)
            }
    }

    fun postPaymentProcessing(request: PaymentRequest, processPayment: () -> Unit) {
        disposable = Completable.fromAction { apiInterfaceProcessPost.processPayment(request) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe{
                disposable.dispose()
                processPayment()
            }
    }

}