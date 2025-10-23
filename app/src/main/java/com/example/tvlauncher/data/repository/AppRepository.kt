package com.example.tvlauncher.data.repository


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.example.tvlauncher.data.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val apps = mutableListOf<AppInfo>()

        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        try {
            val resolveInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                packageManager.queryIntentActivities(
                    mainIntent,
                    PackageManager.MATCH_ALL
                )
            } else {
                packageManager.queryIntentActivities(mainIntent, 0)
            }

            Log.d("AppRepository", "Found ${resolveInfoList.size} apps")

            for (resolveInfo in resolveInfoList) {
                val packageName = resolveInfo.activityInfo.packageName

                // Skip our launcher app
                if (packageName == context.packageName) continue

                // Skip system UI and other system launchers
                if (isSystemLauncher(packageName)) continue

                try {
                    val appInfo = AppInfo(
                        appName = resolveInfo.loadLabel(packageManager).toString(),
                        packageName = packageName,
                        icon = resolveInfo.loadIcon(packageManager),
                        activityName = resolveInfo.activityInfo.name
                    )
                    apps.add(appInfo)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            apps.sortBy { it.appName.lowercase() }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        apps
    }

    fun getLaunchIntent(packageName: String): Intent? {
        return packageManager.getLaunchIntentForPackage(packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        }
    }

    private fun isSystemLauncher(packageName: String): Boolean {
        val systemLaunchers = listOf(
            "com.android.launcher",
            "com.google.android.apps.tv.launcherx",
            "com.android.tv.launcher"
        )
        return systemLaunchers.any { packageName.contains(it) }
    }
}