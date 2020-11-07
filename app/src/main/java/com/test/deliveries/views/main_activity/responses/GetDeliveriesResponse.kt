package com.test.deliveries.views.main_activity.responses

class GetDeliveriesResponse (
    val id : String,
    val remarks : String,
    val pickupTime : String,
    val goodsPicture : String,
    val deliveryFee : String,
    val surcharge : String,
    val route : Route,
    val sender : Sender
)
