package com.brillio.newsfeed.repository.network


import com.brillio.newsfeed.logger.LogUtils
import com.brillio.newsfeed.utils.Constants.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    val newsGetClient: APIInterfaceNewsGET
        get() {
            val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    LogUtils.D(
                        APIClient::class.java.simpleName, LogUtils.FilterTags.withTags(
                            LogUtils.TagFilter.API
                        ), String.format("okHttp logging interceptor=%s", message)
                    )
                }
            })
            interceptor.level = HttpLoggingInterceptor.Level.BODY  // BASIC or BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val gson = GsonBuilder()
                .create()
            val builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
            return builder.build().create(APIInterfaceNewsGET::class.java)
        }
}