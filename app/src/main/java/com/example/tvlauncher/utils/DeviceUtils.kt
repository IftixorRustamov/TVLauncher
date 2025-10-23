package com.example.tvlauncher.utils

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration


object DeviceUtils {

    fun isTelevision(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    }

    fun isTablet(context: Context): Boolean {
        val configuration = context.resources.configuration
        val screenLayout = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun getGridColumnCount(context: Context): Int {
        return when {
            isTelevision(context) -> Constants.GRID_COLUMNS_TV
            isTablet(context) -> Constants.GRID_COLUMNS_TABLET
            else -> Constants.GRID_COLUMNS_PHONE
        }
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}