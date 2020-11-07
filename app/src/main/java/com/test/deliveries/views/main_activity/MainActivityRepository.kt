package com.test.deliveries.views.main_activity

import android.util.Log
import com.google.gson.GsonBuilder
import com.test.deliveries.app.AppConsts
import com.test.deliveries.database.dao.DeliveriesDao
import com.test.deliveries.database.tables.Delivery
import com.test.deliveries.views.main_activity.responses.GetDeliveriesResponse
import com.test.deliveries.network.ApiClient
import com.test.deliveries.network.ApiStateListener
import com.test.deliveries.network.appApi.GetDeliveriesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityRepository(private val deliveriesDao: DeliveriesDao){


    //<------------------------------------------------ DB QUERIES ---------------------------------------------------->

    // function to insert a delivery into DB
    suspend fun insertDeliveryToDb(delivery: Delivery) {
        deliveriesDao.insertAll(delivery)
    }

    // function to delete a delivery from DB
    suspend fun deleteDeliveryFromDb(delivery: Delivery) {
        deliveriesDao.delete(delivery)
    }

    // function to update a delivery from DB
    suspend fun updateDeliveryInDb(delivery: Delivery) {
        deliveriesDao.update(delivery)
    }

    // function that returns delivery by its ID
    fun getDeliveryById(id:String):Delivery? {
        return deliveriesDao.getDeliveryById(id)
    }

    // function to get all deliveries from DB
    fun getAllDeliveries():List<Delivery> {
        return deliveriesDao.getAll()
    }


    //<----------------------------------------------- NETWORK CALLS --------------------------------------------------->

    // function to get deliveries from API
    fun connectToGetDeliveriesApi(offset:Int, limit:Int, apiStateListener: ApiStateListener) {

        val apiInterfaces = ApiClient.getAPIClient()!!.create(GetDeliveriesApi::class.java)

        val call: Call<ArrayList<GetDeliveriesResponse>> = apiInterfaces.getDeliveries(offset, limit)

        call.enqueue(object : Callback<ArrayList<GetDeliveriesResponse>?> {
            override fun onResponse(
                call: Call<ArrayList<GetDeliveriesResponse>?>,
                response: Response<ArrayList<GetDeliveriesResponse>?>
            ) {
                if (response.code() == 200) {
                    // call success
                    apiStateListener.onSuccess(response.body())
                    Log.d("GET_DELI_REPO_SUCCESS", response.body()!!.size.toString())
                    Log.d("GET_DELI_REPO_SUCCESS", GsonBuilder().setPrettyPrinting().create().toJson(response))
                } else {
                    // call failed
                    Log.d("GET_DELI_REPO_FAIL", "Server Error")
                    apiStateListener.onFailure(AppConsts.SERVER_ERROR, false)
                }
            }

            override fun onFailure(call: Call<ArrayList<GetDeliveriesResponse>?>, t: Throwable) {
                // call failed due to internet issues
                Log.d("GET_DELI_REPO_FAIL", "Network Error")
                apiStateListener.onFailure(null, true)
            }
        })

    }
}