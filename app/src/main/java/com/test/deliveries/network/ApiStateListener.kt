package com.test.deliveries.network

interface ApiStateListener {
    //region onSuccess
    fun onSuccess(vararg params: Any?)

    //endregion
    //region onFailure
    fun onFailure(vararg params: Any?) //endregion
}