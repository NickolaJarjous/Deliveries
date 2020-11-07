package com.test.deliveries.views.main_activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.github.yasevich.endlessrecyclerview.EndlessRecyclerView
import com.test.deliveries.R
import com.test.deliveries.app.AppConsts
import com.test.deliveries.database.tables.Delivery
import com.test.deliveries.databinding.ActivityMainBinding
import com.test.deliveries.utils.DisplayUtils
import com.test.deliveries.utils.PreferenceManager
import com.test.deliveries.views.delivery_detail_dialog.DeliveryDetailDialog
import com.test.deliveries.views.main_activity.responses.GetDeliveriesResponse


private const val ITEMS_ON_PAGE = 20

class MainActivity : AppCompatActivity(), EndlessRecyclerView.Pager,
    DeliveriesAdapter.OnDeliveriesRecyclerViewClickListener {

    lateinit var mViewModel: MainActivityViewModel
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAdapter: DeliveriesAdapter
    private var loading = false
    private var offset = 0


    @BindView(R.id.main_recyclerView)
    lateinit var recyclerView: EndlessRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        offset = 0

        ButterKnife.bind(this)
        initializeObservers()
        setStatusBarColor()
        setUpRecyclerView()

        // get the first 20 deliveries
        mViewModel.callGetDeliveries(offset, ITEMS_ON_PAGE)
    }


    private fun initializeObservers() {
        // deliveries received
        mViewModel.getDeliveriesResponse.observe(
            this,
            object : Observer<ArrayList<GetDeliveriesResponse>> {
                override fun onChanged(t: ArrayList<GetDeliveriesResponse>?) {
                    if (t != null) {
                        updateRecyclerView(t)
                    }
                }
            })

        // server error occurred
        mViewModel.showServerError.observe(this, object: Observer<Boolean> {
            override fun onChanged(t: Boolean) {
                if (t) {
                    if (loading) {
                        recyclerView.isRefreshing = false
                        loading = false
                    }
                }
            }
        })

        // network error occurred
        mViewModel.showNetworkError.observe(this, object: Observer<Boolean> {
            override fun onChanged(t: Boolean) {
                if (t) {
                    DisplayUtils.showChocoBarLong(this@MainActivity, getString(R.string.internet_error))
                    if (loading) {
                        recyclerView.isRefreshing = false
                        loading = false
                    }
                    // get cached data if any
                    if (PreferenceManager.getInstance().getBoolean(AppConsts.CACHE_EXISTS, false))
                    {
                        // get cached data from DB
                        val res = mViewModel.getAllDeliveries()
                        offset += res.size
                        mAdapter.addItems(res)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }


    // function to set up the recyclerview
    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setProgressView(R.layout.item_loading)
        mAdapter = DeliveriesAdapter(ArrayList(emptyList<Delivery>()), this)
        recyclerView.adapter = mAdapter
        recyclerView.setPager(this)
    }

    private fun updateRecyclerView(items: ArrayList<GetDeliveriesResponse>) {
        if (loading) {
            recyclerView.isRefreshing = false
            loading = false
        }
        val newItems = ArrayList(emptyList<Delivery>())
        items.forEach {
            val delivery = Delivery(
                it.id,
                it.remarks,
                it.pickupTime,
                it.goodsPicture,
                it.deliveryFee,
                it.surcharge,
                it.route.start,
                it.route.end,
                it.sender.phone,
                it.sender.name,
                it.sender.email,
                false
            )
            newItems.add(delivery)
            // check if delivery is new to cache it or update it
            val oldDelivery = mViewModel.getDeliveryById(it.id)
            if (oldDelivery == null) {
                // new add it to database
                mViewModel.insertDelivery(delivery)
                Log.d("MAIN_DELIVERY_NEW",delivery.id)
            } else {
                // old update it and check if bookmarked
                delivery.isBookmarked = oldDelivery.isBookmarked
                mViewModel.updateDelivery(delivery)
                Log.d("MAIN_DELIVERY_UPDATE",delivery.id)
            }
            // set cache exists to true
            PreferenceManager.getInstance().putBoolean(AppConsts.CACHE_EXISTS, true)
        }
        Log.d("MAIN_OFFSET","$offset")
        offset += items.size
        mAdapter.addItems(newItems)
        mAdapter.notifyDataSetChanged()
    }

    override fun shouldLoad(): Boolean {
        Log.d("MAIN_SHOULD","$loading")
        //return !loading && mAdapter.itemCount / ITEMS_ON_PAGE < TOTAL_PAGES
        if (mAdapter.itemCount < 100 && !loading) return true
            else return false
    }

    override fun loadNextPage() {
        Log.d("MAIN_LOADING","true")
        loading = true
        // load the next 20 items
        mViewModel.callGetDeliveries(offset, ITEMS_ON_PAGE)

    }


    override fun onDeliveriesItemClicked(delivery: Delivery) {
        showDetailDialog(delivery)
    }

    override fun onDeliveriesItemEdited(delivery: Delivery) {
        // check if bookmarked
        delivery.isBookmarked = !delivery.isBookmarked
        mViewModel.updateDelivery(delivery)
        mAdapter.notifyDataSetChanged()
        mAdapter.closeOpenItem(delivery)
    }

    // function to change status bar color
    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = getWindow()
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }
    }

    // function to show detail dialog
    private fun showDetailDialog(delivery: Delivery) {
        val dialog = DeliveryDetailDialog(this, delivery, mViewModel)
        dialog.window!!.setBackgroundDrawable(object:ColorDrawable(Color.TRANSPARENT){})
        dialog.show()
        dialog.setOnDismissListener {
            mAdapter.notifyDataSetChanged()
        }
    }
}