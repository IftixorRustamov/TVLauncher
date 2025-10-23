package com.example.tvlauncher.data.preferences

import android.content.Context
import android.content.SharedPreferences

class LauncherPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "tv_launcher_preferences"
        private const val KEY_LAST_SELECTED_APP = "last_selected_app"
        private const val KEY_LAST_SCROLL_POSITION = "last_scroll_position"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_GRID_COLUMNS = "grid_columns"
    }


    fun saveLastSelectedApp(packageName: String) {
        sharedPreferences.edit()
            .putString(KEY_LAST_SELECTED_APP, packageName)
            .apply()
    }


    fun getLastSelectedApp(): String? {
        return sharedPreferences.getString(KEY_LAST_SELECTED_APP, null)
    }

    fun saveScrollPosition(position: Int) {
        sharedPreferences.edit()
            .putInt(KEY_LAST_SCROLL_POSITION, position)
            .apply()
    }


    fun getScrollPosition(): Int {
        return sharedPreferences.getInt(KEY_LAST_SCROLL_POSITION, 0)
    }


    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true)
    }


    fun setFirstLaunchCompleted() {
        sharedPreferences.edit()
            .putBoolean(KEY_FIRST_LAUNCH, false)
            .apply()
    }


    fun saveGridColumns(columns: Int) {
        sharedPreferences.edit()
            .putInt(KEY_GRID_COLUMNS, columns)
            .apply()
    }


    fun getGridColumns(default: Int = 4): Int {
        return sharedPreferences.getInt(KEY_GRID_COLUMNS, default)
    }


    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}