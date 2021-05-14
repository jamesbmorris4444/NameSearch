package com.godaddy.namesearch.repository.network


import com.godaddy.namesearch.logger.LogUtils
import com.godaddy.namesearch.utils.Constants.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    val loginPostClient: APIInterfaceLoginPOST
        get() {
            return builder().build().create(APIInterfaceLoginPOST::class.java)
        }

    val getExactList: APIInterfaceExactGET
        get() {
            return builder().build().create(APIInterfaceExactGET::class.java)
        }

    val getSpinsList: APIInterfaceSpinsGET
        get() {
            return builder().build().create(APIInterfaceSpinsGET::class.java)
        }

    val getPaymentsList: APIInterfacePaymentsGET
        get() {
            return builder().build().create(APIInterfacePaymentsGET::class.java)
        }

    val PaymentProcessingPostClient: APIInterfaceProcessPOST
        get() {
            return builder().build().create(APIInterfaceProcessPOST::class.java)
        }

    private fun builder(): Retrofit.Builder {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                LogUtils.D(APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(
                    LogUtils.TagFilter.API
                ), String.format("okHttp logging interceptor=%s", message))
            }
        })
        interceptor.level = HttpLoggingInterceptor.Level.BASIC  // BASIC or BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val gson = GsonBuilder()
            .create()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
    }
}
