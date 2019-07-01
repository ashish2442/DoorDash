package com.doordash.android.network.service

import com.doordash.android.BuildConfig
import com.doordash.android.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Ashish Thirunavalli on 30 June 2019.
 */
open class NetworkServiceProvider {

    /**
     * provides gate way for making a network call using retrofit service
     *
     */
    open fun gatewayApi(): GatewayApi {

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            //adding gson converter to convert json data
            .addConverterFactory(GsonConverterFactory.create())
            // RxJava2 support for Retrofit2
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        enableNetworkLogs(retrofitBuilder)

        return retrofitBuilder
            .build()
            .create(GatewayApi::class.java)
    }

    /**
     *  Needed for prints logs for okHttp calls.
     *  NOTE: implement this in code only for debug builds but not for production.
     *
     *  @param retrofitBuilder : retrofit builder class for which you want to enable logs
     */
    private fun enableNetworkLogs(retrofitBuilder: Retrofit.Builder) {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.addInterceptor(logging)

            retrofitBuilder.client(okHttpClient.build())
        }
    }

}