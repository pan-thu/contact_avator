package dev.panthu.contactavatorapplication.ui.avatar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import dev.panthu.contactavatorapplication.databinding.ItemAvatarGridBinding

/**
 * RecyclerView adapter for displaying avatar options in a grid layout.
 * Features:
 * - Single selection management
 * - Visual selection indicators
 * - Click handling with callback
 * - Accessibility support
 */
class AvatarGridAdapter(
    private val avatarResourceIds: List<Int>,
    private val onAvatarSelected: (Int) -> Unit
) : RecyclerView.Adapter<AvatarGridAdapter.AvatarViewHolder>() {

    @DrawableRes
    private var selectedAvatarId: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = ItemAvatarGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val avatarResId = avatarResourceIds[position]
        holder.bind(avatarResId, avatarResId == selectedAvatarId)
    }

    override fun getItemCount(): Int = avatarResourceIds.size

    /**
     * Update the selected avatar and refresh the affected items.
     */
    fun setSelectedAvatar(@DrawableRes avatarId: Int?) {
        val previousSelected = selectedAvatarId
        selectedAvatarId = avatarId

        // Notify only the affected items for efficiency
        if (previousSelected != null) {
            val previousIndex = avatarResourceIds.indexOf(previousSelected)
            if (previousIndex >= 0) {
                notifyItemChanged(previousIndex)
            }
        }

        if (avatarId != null) {
            val newIndex = avatarResourceIds.indexOf(avatarId)
            if (newIndex >= 0) {
                notifyItemChanged(newIndex)
            }
        }
    }

    /**
     * Get the currently selected avatar resource ID.
     */
    fun getSelectedAvatar(): Int? = selectedAvatarId

    inner class AvatarViewHolder(
        private val binding: ItemAvatarGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Set up click listener
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val avatarId = avatarResourceIds[position]
                    setSelectedAvatar(avatarId)
                    onAvatarSelected(avatarId)
                }
            }
        }

        fun bind(@DrawableRes avatarResId: Int, isSelected: Boolean) {
            // Load avatar image
            binding.avatarImage.setAvatarResource(avatarResId)

            // Show/hide selection indicator
            binding.selectionIndicator.visibility = if (isSelected) {
                View.VISIBLE
            } else {
                View.GONE
            }

            // Update content description for accessibility
            val context = binding.root.context
            val selectedText = if (isSelected) " (Selected)" else ""
            binding.root.contentDescription = "Avatar option ${bindingAdapterPosition + 1}$selectedText"

            // Ensure minimum touch target size (48dp) for accessibility
            binding.root.minimumWidth = context.resources.getDimensionPixelSize(
                android.R.dimen.app_icon_size
            )
            binding.root.minimumHeight = context.resources.getDimensionPixelSize(
                android.R.dimen.app_icon_size
            )
        }
    }
}
