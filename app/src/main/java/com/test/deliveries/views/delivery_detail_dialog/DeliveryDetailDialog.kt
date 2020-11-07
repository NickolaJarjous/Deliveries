package com.test.deliveries.views.delivery_detail_dialog


import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window

import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData

import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.test.deliveries.R
import com.test.deliveries.database.tables.Delivery
import com.test.deliveries.views.main_activity.MainActivityViewModel
import kotlinx.android.synthetic.main.item_delivery.view.*
import net.colindodd.toggleimagebutton.ToggleImageButton
import java.math.RoundingMode
import java.text.DecimalFormat

class DeliveryDetailDialog(val activity: Activity, val delivery: Delivery, val mainActivityViewModel: MainActivityViewModel) :
    Dialog(activity, R.style.MyDialog) {


    @BindView(R.id.delivery_dialog_btn)
    lateinit var bookmarkBtn: ToggleImageButton

    @BindView(R.id.delivery_dialog_close_iv)
    lateinit var closeBtn: ImageView

    @BindView(R.id.delivery_dialog_main_iv)
    lateinit var mainImg: ImageView

    @BindView(R.id.delivery_dialog_date_tv)
    lateinit var dateTv: TextView

    @BindView(R.id.delivery_dialog_price_tv)
    lateinit var priceTv: TextView

    @BindView(R.id.delivery_dialog_remark_tv)
    lateinit var remarkTv: TextView

    @BindView(R.id.delivery_dialog_start_tv)
    lateinit var startTv: TextView

    @BindView(R.id.delivery_dialog_end_tv)
    lateinit var endTv: TextView

    @BindView(R.id.delivery_dialog_name_tv)
    lateinit var nameTv: TextView

    @BindView(R.id.delivery_dialog_phone_tv)
    lateinit var phoneTv: TextView

    @BindView(R.id.delivery_dialog_email_tv)
    lateinit var emailTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_delivery_detail)
        ButterKnife.bind(this)

        // fill data
        // load image
        Picasso.get().load(delivery.goodsPicture)
            .into(mainImg)

        // date
        dateTv.text = delivery.pickupTime
        // remarks
        remarkTv.text = delivery.remarks
        // start
        startTv.text = delivery.routeStart
        // end
        endTv.text = delivery.routeEnd
        // name
        nameTv.text = delivery.senderName
        // phone
        phoneTv.text = delivery.senderPhone
        // email
        emailTv.text = delivery.senderEmail

        // btn
        bookmarkBtn.isChecked = delivery.isBookmarked

        // price
        try {
            val a = delivery.deliveryFee.substring(1)
            val b = delivery.surcharge.substring(1)
            val fee = a.toFloat()
            val surcharge = b.toFloat()
            val sum = fee + surcharge
            // format to 2 decimal places
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            val sumStringFormated = df.format(sum)
            priceTv.text = "$sumStringFormated"
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            priceTv.text = context!!.getString(R.string.nan)
        }

        // click listeners
        bookmarkBtn.setOnClickListener {
            delivery.isBookmarked = !delivery.isBookmarked
            mainActivityViewModel.updateDelivery(delivery)
            dismiss()
        }

        closeBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        closeBtn.performClick()
    }



}