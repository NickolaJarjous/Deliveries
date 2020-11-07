package com.test.deliveries.app

import android.app.Application

class Deliveries: Application() {

    private var instance: Deliveries? = null

    init {
        instance = this
    }

    companion object {
        lateinit var instance: Deliveries
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Deliveries.instance = this
    }
}
