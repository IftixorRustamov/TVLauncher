package com.example.tvlauncher.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tvlauncher.data.model.AppInfo
import com.example.tvlauncher.data.preferences.LauncherPreferences
import com.example.tvlauncher.data.repository.AppRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the main launcher screen
 * Manages app data and UI state
 */
class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val preferences = LauncherPreferences(application)

    private val _apps = MutableLiveData<List<AppInfo>>()
    val apps: LiveData<List<AppInfo>> = _apps

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadApps()
    }


    fun loadApps() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val installedApps = repository.getInstalledApps()
                _apps.value = installedApps

            } catch (e: Exception) {
                _error.value = "Failed to load apps: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getLaunchIntent(packageName: String) = repository.getLaunchIntent(packageName)

    fun saveLastSelectedApp(packageName: String) {
        preferences.saveLastSelectedApp(packageName)
    }

    fun saveScrollPosition(position: Int) {
        preferences.saveScrollPosition(position)
    }

    fun getScrollPosition(): Int = preferences.getScrollPosition()

    fun isFirstLaunch(): Boolean = preferences.isFirstLaunch()

    fun setFirstLaunchCompleted() {
        preferences.setFirstLaunchCompleted()
    }

    fun getGridColumns(default: Int): Int = preferences.getGridColumns(default)

    fun saveGridColumns(columns: Int) {
        preferences.saveGridColumns(columns)
    }
}