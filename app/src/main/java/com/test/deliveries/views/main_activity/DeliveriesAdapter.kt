package com.test.deliveries.views.main_activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.squareup.picasso.Picasso
import com.test.deliveries.R
import com.test.deliveries.database.tables.Delivery
import kotlinx.android.synthetic.main.item_delivery.view.*
import java.math.RoundingMode
import java.text.DecimalFormat


class DeliveriesAdapter(
    var deliveriesList: ArrayList<Delivery>,
    private var clickListener: OnDeliveriesRecyclerViewClickListener,
) : RecyclerView.Adapter<DeliveriesAdapter.DeliveriesViewHolder>() {

    private var context: Context? = null
    private var viewBinderHelper: ViewBinderHelper
    private var count = 0;

    init {
        viewBinderHelper = ViewBinderHelper()
        viewBinderHelper.setOpenOnlyOne(true)
    }


    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, i: Int): DeliveriesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_delivery, parent, false
            )
        context = parent.context
        return DeliveriesViewHolder(itemView)

    }


    override fun onBindViewHolder(@NonNull holder: DeliveriesViewHolder, position: Int) {

        viewBinderHelper.bind(
            holder.itemView.delivery_adapter_swipe_reveal_layout,
            deliveriesList[position].id
        )

        holder.itemView.delivery_adapter_from_tv.text =
            deliveriesList[position].senderName
        holder.itemView.delivery_adapter_to_tv.text = deliveriesList[position].routeEnd
        holder.itemView.delivery_adapter_date_tv.text = deliveriesList[position].pickupTime

        // load image
        Picasso.get().load(deliveriesList[position].goodsPicture)
            .into(holder.itemView.delivery_adapter_main_iv)

        // calculate price
        try {
            val a = deliveriesList[position].deliveryFee.substring(1)
            val b = deliveriesList[position].surcharge.substring(1)
            val fee = a.toFloat()
            val surcharge = b.toFloat()
            val sum = fee + surcharge
            // format to 2 decimal places
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            val sumStringFormated = df.format(sum)
            Log.d("ADAPTER_FEE", "$fee")
            Log.d("ADAPTER_SUR", "$surcharge")
            holder.itemView.delivery_adapter_price_tv.text = "$sumStringFormated"
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            holder.itemView.delivery_adapter_price_tv.text = context!!.getString(R.string.nan)
        }


        if (deliveriesList[position].isBookmarked) {
            holder.itemView.delivery_adapter_bookmark_iv.isVisible = true
            holder.itemView.delivery_adapter_bookmark_btn_iv.setImageResource(R.drawable.ic_unbookmark)
        } else {
            holder.itemView.delivery_adapter_bookmark_iv.isVisible = false
            holder.itemView.delivery_adapter_bookmark_btn_iv.setImageResource(R.drawable.ic_bookmark)
        }

    }


    override fun getItemCount(): Int {
        return deliveriesList.size
    }

    fun setCount(count:Int) {
        this.count = count
        notifyDataSetChanged()
    }

    fun addItems(items:ArrayList<Delivery>) {
        deliveriesList.addAll(items)
        notifyDataSetChanged()
    }

    fun saveStates(outState: Bundle) {
        viewBinderHelper.saveStates(outState)
    }


    fun restoreStates(inState: Bundle?) {
        viewBinderHelper.restoreStates(inState)
    }

    fun closeOpenItem(delivery: Delivery) {
        viewBinderHelper.closeLayout(delivery.id)
    }


    interface OnDeliveriesRecyclerViewClickListener {
        fun onDeliveriesItemClicked(delivery: Delivery)

        fun onDeliveriesItemEdited(delivery: Delivery)
    }


    inner class DeliveriesViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        init {
            // item click listener
            itemView.delivery_adapter_main_ll.setOnClickListener {
                clickListener.onDeliveriesItemClicked(deliveriesList[adapterPosition])
                Log.d("DELIVERIES_ADAPTER", deliveriesList[adapterPosition].id)
            }

            // edit click listener
            itemView.delivery_adapter_edit_ll.setOnClickListener {
                clickListener.onDeliveriesItemEdited(deliveriesList[adapterPosition])
            }
        }
    }


}