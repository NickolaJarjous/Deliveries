package com.test.deliveries.network

import com.test.deliveries.app.AppConsts
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    //region Get retrofit api client
    companion object {
        var retrofit: Retrofit? = null

        fun getAPIClient(): Retrofit? {
            if (retrofit == null) {
                val url = AppConsts.BASE_URL
                retrofit =
                    Retrofit.Builder().baseUrl(url)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(getHttpOkClient())
                        .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }
        //endregion

        //region Handle headers for api calls
        private fun getHttpOkClient(): OkHttpClient {
            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
            httpClientBuilder.addInterceptor { chain ->


                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method(), original.body())
                    .addHeader("Accept", "application/json")
                    .build()

                chain.proceed(request)
            }
            return httpClientBuilder.build()
        }
        //endregion
    }


}