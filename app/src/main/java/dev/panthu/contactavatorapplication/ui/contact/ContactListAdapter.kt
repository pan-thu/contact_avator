package dev.panthu.contactavatorapplication.ui.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.panthu.contactavatorapplication.R
import dev.panthu.contactavatorapplication.data.Contact
import dev.panthu.contactavatorapplication.databinding.ItemContactBinding

/**
 * RecyclerView adapter for displaying contacts with DiffUtil support.
 * Provides smooth animations for list updates.
 */
class ContactListAdapter(
    private val onContactClick: (Contact) -> Unit
) : ListAdapter<Contact, ContactListAdapter.ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding, onContactClick)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for contact list items.
     */
    class ContactViewHolder(
        private val binding: ItemContactBinding,
        private val onContactClick: (Contact) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                // Set contact data
                nameTextView.text = contact.name
                phoneTextView.text = contact.phone

                // Load avatar using AvatarView component
                avatarView.apply {
                    // Set avatar based on contact's avatar settings
                    when {
                        contact.avatarUri != null -> {
                            setAvatarUri(contact.avatarUri)
                        }
                        contact.avatarId != null -> {
                            setAvatarResource(contact.avatarId)
                        }
                        else -> {
                            setAvatarResource(R.drawable.avatar_default)
                        }
                    }

                    // Accessibility
                    contentDescription = root.context.getString(
                        R.string.avatar_description,
                        contact.name
                    )
                }

                // Set click listener
                root.setOnClickListener {
                    onContactClick(contact)
                }

                // Accessibility for the entire item
                root.contentDescription = root.context.getString(
                    R.string.contact_details_title
                ) + ": ${contact.name}, ${contact.phone}"
            }
        }
    }
}
