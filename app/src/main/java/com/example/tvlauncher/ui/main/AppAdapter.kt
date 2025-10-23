package com.example.tvlauncher.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tvlauncher.data.model.AppInfo
import com.example.tvlauncher.databinding.ItemAppBinding

/**
 * RecyclerView Adapter for displaying apps
 * Uses ListAdapter with DiffUtil for efficient updates
 */
class AppAdapter(
    private val onAppClick: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppViewHolder(binding, onAppClick)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for app items
     */
    class AppViewHolder(
        private val binding: ItemAppBinding,
        private val onAppClick: (AppInfo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appInfo: AppInfo) {
            binding.apply {
                appIcon.setImageDrawable(appInfo.icon)
                appName.text = appInfo.appName

                root.setOnClickListener {
                    onAppClick(appInfo)
                }

                // TV focus handling
                root.isFocusable = true
                root.isFocusableInTouchMode = true
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    private class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}