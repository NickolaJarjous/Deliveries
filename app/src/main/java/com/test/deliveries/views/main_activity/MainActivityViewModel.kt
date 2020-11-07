package com.test.deliveries.views.main_activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.deliveries.app.AppDatabase
import com.test.deliveries.database.tables.Delivery
import com.test.deliveries.network.ApiStateListener
import com.test.deliveries.views.main_activity.responses.GetDeliveriesResponse
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    var showServerError: MutableLiveData<Boolean> = MutableLiveData()
    var showNetworkError: MutableLiveData<Boolean> = MutableLiveData()
    var getDeliveriesResponse: MutableLiveData<ArrayList<GetDeliveriesResponse>> = MutableLiveData()

    private val mRepository:MainActivityRepository

    init {
        val deliveriesDao = AppDatabase.getInstance(application).deliveriesDao()
        mRepository = MainActivityRepository(deliveriesDao)
    }


    //<------------------------------------------------ DB QUERIES ---------------------------------------------------->

    // function to add delivery to db using repo
    fun insertDelivery(delivery: Delivery) {
        viewModelScope.launch {
            mRepository.insertDeliveryToDb(delivery)
        }
    }

    // function to add delivery to db using repo
    fun updateDelivery(delivery: Delivery) {
        viewModelScope.launch {
            mRepository.updateDeliveryInDb(delivery)
        }
    }

    // function to delete delivery from db using repo
    fun deleteDelivery(delivery: Delivery) {
        viewModelScope.launch {
            mRepository.updateDeliveryInDb(delivery)
        }
    }


    // function to get delivery by its id
    fun getDeliveryById(id: String): Delivery? {
        return mRepository.getDeliveryById(id)
    }


    // function to get all deliveries from db using repo
    fun getAllDeliveries():ArrayList<Delivery> {
        return  ArrayList(mRepository.getAllDeliveries())
    }


    //<----------------------------------------------- NETWORK CALLS --------------------------------------------------->

    // function to call the get deliveries call from repo
    fun callGetDeliveries(offset:Int, limit:Int)
    {
        mRepository.connectToGetDeliveriesApi(offset,limit, object : ApiStateListener {
            override fun onSuccess(vararg params: Any?) {
                // success
                getDeliveriesResponse.value = params[0] as ArrayList<GetDeliveriesResponse>
            }

            override fun onFailure(vararg params: Any?) {
                if (params[1] == false) {
                    // server error
                    showServerError.value = true
                } else {
                    // network issues
                    showNetworkError.value = true
                }
            }
        })
    }

}