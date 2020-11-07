package com.test.deliveries.network.appApi

import com.test.deliveries.app.AppConsts
import com.test.deliveries.views.main_activity.responses.GetDeliveriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDeliveriesApi {
    // get deliveries
    @GET(AppConsts.GET_LISTS_URL)
    fun getDeliveries(
        @Query("offset") offset:Int,
        @Query("limit") limit:Int,
    ): Call<ArrayList<GetDeliveriesResponse>>
}