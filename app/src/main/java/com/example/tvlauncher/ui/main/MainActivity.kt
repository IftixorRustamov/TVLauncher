package com.example.tvlauncher.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tvlauncher.R
import com.example.tvlauncher.data.model.AppInfo
import com.example.tvlauncher.databinding.ActivityMainBinding
import com.example.tvlauncher.ui.viewmodel.LauncherViewModel
import com.example.tvlauncher.utils.Constants
import com.example.tvlauncher.utils.DeviceUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LauncherViewModel
    private lateinit var appAdapter: AppAdapter
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        setupUI()
        observeData()
        showWelcomeMessage()
        setupBackPressHandler()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LauncherViewModel::class.java]
    }

    private fun setupUI() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val columnCount = DeviceUtils.getGridColumnCount(this)
        val layoutManager = GridLayoutManager(this, columnCount)

        binding.recyclerViewApps.layoutManager = layoutManager

        // Add spacing decoration
        val spacing = DeviceUtils.dpToPx(this, Constants.GRID_SPACING_DP)
        binding.recyclerViewApps.addItemDecoration(
            GridSpacingItemDecoration(columnCount, spacing, true)
        )

        // Setup adapter
        appAdapter = AppAdapter { appInfo ->
            launchApp(appInfo)
        }
        binding.recyclerViewApps.adapter = appAdapter

        // Restore scroll position
        val scrollPosition = viewModel.getScrollPosition()
        if (scrollPosition > 0) {
            binding.recyclerViewApps.scrollToPosition(scrollPosition)
        }

        // Save column count
        viewModel.saveGridColumns(columnCount)
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing - prevent launcher from closing
                // Optionally show a toast to inform user
                Toast.makeText(
                    this@MainActivity,
                    "Press Home to stay on launcher",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun observeData() {
        viewModel.apps.observe(this) { apps ->
            appAdapter.submitList(apps)
            updateAppCount(apps.size)
        }

        viewModel.isLoading.observe(this) { isLoading ->
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateAppCount(count: Int) {
        binding.titleText.text = getString(R.string.launcher_title_with_count, count)
    }

    private fun showWelcomeMessage() {
        if (viewModel.isFirstLaunch()) {
            Toast.makeText(
                this,
                getString(R.string.welcome_message),
                Toast.LENGTH_LONG
            ).show()
            viewModel.setFirstLaunchCompleted()
        }
    }

    private fun launchApp(appInfo: AppInfo) {
        try {
            // Save state
            viewModel.saveLastSelectedApp(appInfo.packageName)
            saveCurrentScrollPosition()

            // Get launch intent
            val intent = viewModel.getLaunchIntent(appInfo.packageName)

            if (intent != null) {
                handler.postDelayed({
                    startActivity(intent)
                }, Constants.LAUNCH_DELAY_MS)
            } else {
                showLaunchError(appInfo.appName)
            }
        } catch (e: Exception) {
            showLaunchError(appInfo.appName)
            e.printStackTrace()
        }
    }

    private fun saveCurrentScrollPosition() {
        val layoutManager = binding.recyclerViewApps.layoutManager as? GridLayoutManager
        layoutManager?.let {
            val position = it.findFirstVisibleItemPosition()
            viewModel.saveScrollPosition(position)
        }
    }

    private fun showLaunchError(appName: String) {
        Toast.makeText(
            this,
            getString(R.string.error_launch_app, appName),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadApps()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}