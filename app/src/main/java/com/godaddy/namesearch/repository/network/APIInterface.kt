package com.godaddy.namesearch.repository.network

import com.godaddy.namesearch.repository.storage.*
import com.godaddy.namesearch.utils.Constants.EVERYTHING_PARAM
import com.godaddy.namesearch.utils.Constants.EXACT_PARAM
import com.godaddy.namesearch.utils.Constants.FROM_PARAM
import com.godaddy.namesearch.utils.Constants.KEY_PARAM
import com.godaddy.namesearch.utils.Constants.LOGIN_PARAM
import com.godaddy.namesearch.utils.Constants.PAYMENTS_PARAM
import com.godaddy.namesearch.utils.Constants.PROCESS_PARAM
import com.godaddy.namesearch.utils.Constants.QUERY_PARAM
import com.godaddy.namesearch.utils.Constants.Q_PARAM
import com.godaddy.namesearch.utils.Constants.SORT_PARAM
import com.godaddy.namesearch.utils.Constants.SPINS_PARAM
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIInterfaceLoginPOST {
    @POST(LOGIN_PARAM)
    fun login(@Body request: LoginRequest): Single<LoginResponse>
}

interface APIInterfaceNewsGET {
    @GET(EVERYTHING_PARAM)
    fun getNewsFeed(
        @Query(Q_PARAM) query: String,
        @Query(FROM_PARAM) from: String,
        @Query(SORT_PARAM) sortBy: String,
        @Query(KEY_PARAM) key: String
    ): Single<NewsFeedItem>
}

interface APIInterfaceExactGET {
    @GET(EXACT_PARAM)
    fun getExactDomains(
        @Query(QUERY_PARAM) query: String
    ): Single<DomainSearchExactMatchResponse>
}

interface APIInterfaceSpinsGET {
    @GET(SPINS_PARAM)
    fun getSpinsDomains(
        @Query(QUERY_PARAM) query: String
    ): Single<DomainSearchRecommendedResponse>
}

interface APIInterfacePaymentsGET {
    @GET(PAYMENTS_PARAM)
    fun getPaymentsList(): Single<List<PaymentMethod>>
}

interface APIInterfaceProcessPOST {
    @POST(PROCESS_PARAM)
    fun processPayment(@Body paymentRequest: PaymentRequest): Completable
}


