package com.godaddy.namesearch.repository.network

import com.godaddy.namesearch.repository.storage.*
import com.godaddy.namesearch.utils.Constants.EXACT_PARAM
import com.godaddy.namesearch.utils.Constants.LOGIN_PARAM
import com.godaddy.namesearch.utils.Constants.PAYMENTS_PARAM
import com.godaddy.namesearch.utils.Constants.QUERY_PARAM
import com.godaddy.namesearch.utils.Constants.SPINS_PARAM
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIInterfacePOST {
    @POST(LOGIN_PARAM)
    fun login(@Body request: LoginRequest): Single<LoginResponse>
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


