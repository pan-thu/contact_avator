package dev.panthu.contactavatorapplication.ui.contact

import androidx.recyclerview.widget.DiffUtil
import dev.panthu.contactavatorapplication.data.Contact

/**
 * DiffUtil callback for efficient Contact list updates.
 * Enables smooth RecyclerView animations by calculating minimal changes.
 */
class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {

    /**
     * Check if two items represent the same contact (same ID).
     */
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Check if two items have the same content (all fields equal).
     * This includes avatar changes to trigger UI updates.
     */
    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.phone == newItem.phone &&
                oldItem.email == newItem.email &&
                oldItem.address == newItem.address &&
                oldItem.avatarId == newItem.avatarId &&
                oldItem.avatarUri == newItem.avatarUri
    }
}
