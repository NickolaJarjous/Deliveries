package com.test.deliveries.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deliveries_table")
class Delivery (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id:String,

    @ColumnInfo(name = "remarks")
    var remarks:String,

    @ColumnInfo(name = "pickup_time")
    var pickupTime:String?,

    @ColumnInfo(name = "goods_picture")
    var goodsPicture:String,

    @ColumnInfo(name = "delivery_fee")
    var deliveryFee:String,

    @ColumnInfo(name = "surcharge")
    var surcharge:String,

    @ColumnInfo(name = "route_start")
    var routeStart:String,

    @ColumnInfo(name = "route_end")
    var routeEnd:String,

    @ColumnInfo(name = "sender_phone")
    var senderPhone:String,

    @ColumnInfo(name = "sender_name")
    var senderName:String,

    @ColumnInfo(name = "sender_email")
    var senderEmail:String,

    @ColumnInfo(name = "is_bookmarked")
    var isBookmarked:Boolean

)