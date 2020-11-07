package com.test.deliveries.utils

import android.app.Activity
import android.app.AlertDialog
import androidx.core.content.ContextCompat
import com.pd.chocobar.ChocoBar
import com.test.deliveries.R


class DisplayUtils {
    companion object {

        fun showChocoBarLong(activity: Activity, message: String) {
            ChocoBar.builder().setActivity(activity)
                .setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                .setText(message)
                .centerText()
                .setDuration(ChocoBar.LENGTH_LONG)
                .setBackgroundColor(ContextCompat.getColor(activity, R.color.colorAccent))
                .build()
                .show()
        }

        fun showChocoBarShort(activity: Activity, message: String) {
            ChocoBar.builder().setActivity(activity)
                .setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                .setText(message)
                .centerText()
                .setDuration(ChocoBar.LENGTH_SHORT)
                .setBackgroundColor(ContextCompat.getColor(activity, R.color.colorAccent))
                .build()
                .show()
        }
    }
}