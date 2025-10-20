/**
 * Avatar Picker & Management System - Usage Examples
 *
 * This file contains practical examples of how to use the avatar picker system
 * in your ContactAvatar+ application.
 */

package dev.panthu.contactavatorapplication.examples

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dev.panthu.contactavatorapplication.R
import dev.panthu.contactavatorapplication.databinding.FragmentContactEditBinding
import dev.panthu.contactavatorapplication.ui.avatar.AvatarPickerBottomSheetDialogFragment

// ===========================================================================================
// EXAMPLE 1: Basic Avatar Picker Usage in Contact Edit Screen
// ===========================================================================================

class ContactEditFragmentExample : Fragment() {

    private var _binding: FragmentContactEditBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // STEP 1: Set up the result listener FIRST (before showing picker)
        setupAvatarPickerResultListener()

        // STEP 2: Show picker when user clicks on avatar
        binding.contactAvatar.setOnClickListener {
            showAvatarPicker()
        }
    }

    /**
     * Set up listener to receive the selected avatar from the picker dialog.
     * This must be called in onViewCreated() before showing the picker.
     */
    private fun setupAvatarPickerResultListener() {
        childFragmentManager.setFragmentResultListener(
            AvatarPickerBottomSheetDialogFragment.REQUEST_KEY,
            viewLifecycleOwner  // Use viewLifecycleOwner, not this
        ) { _, result ->
            // Extract selected avatar ID (may be null if custom URI selected)
            val avatarId = result.getInt(
                AvatarPickerBottomSheetDialogFragment.RESULT_KEY_AVATAR_ID,
                0
            ).takeIf { it != 0 }

            // Extract selected custom URI (may be null if resource selected)
            val avatarUri = result.getString(
                AvatarPickerBottomSheetDialogFragment.RESULT_KEY_AVATAR_URI
            )

            // Update the UI with selected avatar
            when {
                avatarUri != null -> {
                    // User selected custom image from gallery
                    binding.contactAvatar.setAvatarUri(avatarUri)
                }
                avatarId != null -> {
                    // User selected from avatar grid
                    binding.contactAvatar.setAvatarResource(avatarId)
                }
                else -> {
                    // Reset to default (should not happen, but handle gracefully)
                    binding.contactAvatar.setAvatarResource(R.drawable.avatar_default)
                }
            }

            // Save to ViewModel or directly to contact
            // viewModel.updateAvatar(avatarId, avatarUri)
        }
    }

    /**
     * Show the avatar picker dialog with current avatar as initial selection.
     */
    private fun showAvatarPicker() {
        // Get current avatar from your data model
        val currentAvatarId = null  // Replace with: viewModel.currentContact.value?.avatarId
        val currentAvatarUri = null // Replace with: viewModel.currentContact.value?.avatarUri

        // Create picker instance with initial selection
        val picker = AvatarPickerBottomSheetDialogFragment.newInstance(
            initialAvatarId = currentAvatarId,
            initialAvatarUri = currentAvatarUri
        )

        // Show as bottom sheet
        picker.show(childFragmentManager, AvatarPickerBottomSheetDialogFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// ===========================================================================================
// EXAMPLE 2: Displaying Avatars in RecyclerView List
// ===========================================================================================

/**
 * Example ViewHolder for displaying contacts in a list.
 * Shows how to efficiently load avatars in RecyclerView.
 */
class ContactListViewHolderExample {

    fun bind(contact: Contact) {
        // EFFICIENT: Check URI first, then resource ID, then default
        when {
            contact.avatarUri != null -> {
                // Load custom avatar from URI
                avatarView.setAvatarUri(contact.avatarUri)
            }
            contact.avatarId != null -> {
                // Load predefined avatar resource
                avatarView.setAvatarResource(contact.avatarId)
            }
            else -> {
                // Load default avatar
                avatarView.setAvatarResource(R.drawable.avatar_default)
            }
        }

        // OR use the convenience method from Contact model:
        // if (contact.avatarUri != null) {
        //     avatarView.setAvatarUri(contact.avatarUri)
        // } else {
        //     avatarView.setAvatarResource(contact.getDisplayAvatarResourceId())
        // }
    }
}

// ===========================================================================================
// EXAMPLE 3: Using AvatarView Component in XML
// ===========================================================================================

/**
 * XML Layout Example:
 *
 * <!-- Contact Edit Screen with clickable avatar -->
 * <LinearLayout
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content"
 *     android:orientation="vertical">
 *
 *     <!-- Avatar with border (clickable) -->
 *     <dev.panthu.contactavatorapplication.ui.components.AvatarView
 *         android:id="@+id/contact_avatar"
 *         android:layout_width="@dimen/avatar_size_large"
 *         android:layout_height="@dimen/avatar_size_large"
 *         android:layout_gravity="center"
 *         android:contentDescription="@string/avatar_label"
 *         android:clickable="true"
 *         android:focusable="true"
 *         app:showBorder="true"
 *         app:borderColor="?attr/colorPrimary"
 *         app:borderWidth="@dimen/avatar_border_width"
 *         app:avatarResourceId="@drawable/avatar_default" />
 *
 *     <!-- Change Avatar Button (optional) -->
 *     <Button
 *         android:id="@+id/change_avatar_button"
 *         android:layout_width="wrap_content"
 *         android:layout_height="wrap_content"
 *         android:layout_gravity="center"
 *         android:text="@string/avatar_change" />
 * </LinearLayout>
 *
 *
 * <!-- List Item with small avatar (no border) -->
 * <dev.panthu.contactavatorapplication.ui.components.AvatarView
 *     android:id="@+id/contact_avatar"
 *     android:layout_width="@dimen/avatar_size_small"
 *     android:layout_height="@dimen/avatar_size_small"
 *     android:contentDescription="@string/cd_avatar"
 *     app:showBorder="false" />
 */

// ===========================================================================================
// EXAMPLE 4: Programmatic AvatarView Customization
// ===========================================================================================

class AvatarCustomizationExample {

    fun customizeAvatar(avatarView: AvatarView, context: Context) {
        // Set avatar from resource
        avatarView.setAvatarResource(R.drawable.avatar_01)

        // Enable border
        avatarView.setShowBorder(true)

        // Customize border color
        avatarView.setBorderColor(context.getColor(R.color.primary))

        // Customize border width
        avatarView.setBorderWidth(
            context.resources.getDimension(R.dimen.avatar_border_width)
        )

        // Load from URI
        val uri = Uri.parse("content://media/external/images/media/123")
        avatarView.setAvatarUri(uri)

        // Handle click
        avatarView.setOnClickListener {
            // Show picker or perform action
        }
    }
}

// ===========================================================================================
// EXAMPLE 5: Saving Contact with Selected Avatar
// ===========================================================================================

class SaveContactExample(private val repository: ContactRepository) {

    suspend fun saveContactWithAvatar(
        name: String,
        phone: String,
        email: String?,
        avatarId: Int?,
        avatarUri: String?
    ) {
        // Create contact with avatar
        val contact = Contact(
            name = name,
            phone = phone,
            email = email,
            avatarId = avatarId,    // Resource ID or null
            avatarUri = avatarUri,  // Custom URI or null
            createdAt = System.currentTimeMillis()
        )

        // Save to database
        repository.insertContact(contact)
    }

    suspend fun updateContactAvatar(
        contactId: Long,
        newAvatarId: Int?,
        newAvatarUri: String?
    ) {
        // Get existing contact
        val contact = repository.getContactById(contactId)

        // Update with new avatar
        val updated = contact.copy(
            avatarId = newAvatarId,
            avatarUri = newAvatarUri
        )

        // Save updated contact
        repository.updateContact(updated)
    }
}

// ===========================================================================================
// EXAMPLE 6: Standalone Custom Avatar Import (Without Picker Dialog)
// ===========================================================================================

class StandaloneAvatarImportExample : Fragment() {

    private lateinit var avatarImportHandler: AvatarImportHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize import handler
        avatarImportHandler = AvatarImportHandler(
            fragment = this,
            onImageSelected = { uri ->
                if (uri != null) {
                    // User selected an image successfully
                    handleCustomAvatarSelected(uri)
                } else {
                    // User cancelled or error occurred
                    showMessage("No image selected")
                }
            },
            onError = { exception ->
                // Handle error
                showMessage("Error: ${exception.message}")
            }
        )

        // Must initialize before fragment is STARTED
        avatarImportHandler.initialize()
    }

    private fun showImportDialog() {
        // Check if image picker is available
        if (avatarImportHandler.isImagePickerAvailable()) {
            avatarImportHandler.launchImagePicker()
        } else {
            showMessage("No image picker app available on this device")
        }
    }

    private fun handleCustomAvatarSelected(uri: Uri) {
        // Validate URI is still accessible
        if (AvatarImportHandler.isUriAccessible(requireContext(), uri)) {
            // Save to database
            saveCustomAvatar(uri)

            // Update UI
            binding.avatarView.setAvatarUri(uri)
        } else {
            showMessage("Cannot access selected image")
        }
    }

    private fun saveCustomAvatar(uri: Uri) {
        // Save URI string to database
        // viewModel.updateAvatar(avatarId = null, avatarUri = uri.toString())
    }

    private fun showMessage(message: String) {
        // Show Snackbar or Toast
    }
}

// ===========================================================================================
// EXAMPLE 7: Handling Avatar Display Logic
// ===========================================================================================

/**
 * Helper class for avatar display logic.
 * Encapsulates the logic for determining which avatar to display.
 */
object AvatarDisplayHelper {

    /**
     * Load appropriate avatar into AvatarView based on Contact data.
     */
    fun loadContactAvatar(avatarView: AvatarView, contact: Contact) {
        when {
            // Priority 1: Custom URI
            contact.avatarUri != null -> {
                avatarView.setAvatarUri(contact.avatarUri)
            }
            // Priority 2: Custom resource ID
            contact.avatarId != null -> {
                avatarView.setAvatarResource(contact.avatarId)
            }
            // Priority 3: Default
            else -> {
                avatarView.setAvatarResource(R.drawable.avatar_default)
            }
        }
    }

    /**
     * Get avatar resource ID for notifications or other uses.
     */
    fun getAvatarResourceId(contact: Contact): Int {
        return contact.avatarId ?: R.drawable.avatar_default
    }

    /**
     * Check if contact has custom avatar (not default).
     */
    fun hasCustomAvatar(contact: Contact): Boolean {
        return contact.avatarId != null || contact.avatarUri != null
    }
}

// ===========================================================================================
// EXAMPLE 8: Testing Avatar Picker Integration
// ===========================================================================================

/**
 * Unit test example for avatar selection logic.
 */
class AvatarPickerIntegrationTest {

    @Test
    fun `when avatar selected from grid, UI updates with resource ID`() {
        // Given: Contact edit screen is displayed
        val fragment = ContactEditFragmentExample()

        // When: User selects avatar from grid
        val result = Bundle().apply {
            putInt(AvatarPickerBottomSheetDialogFragment.RESULT_KEY_AVATAR_ID, R.drawable.avatar_01)
        }
        fragment.childFragmentManager.setFragmentResult(
            AvatarPickerBottomSheetDialogFragment.REQUEST_KEY,
            result
        )

        // Then: Avatar view displays selected avatar
        // Assert UI state...
    }

    @Test
    fun `when custom avatar imported, URI is persisted`() {
        // Given: Avatar import handler is initialized
        val uri = Uri.parse("content://media/external/images/media/123")

        // When: User selects custom image
        // importHandler.onImageSelected(uri)

        // Then: URI is saved to database and displayed
        // Assert database state...
    }
}

// ===========================================================================================
// DATA MODEL (For Reference)
// ===========================================================================================

/**
 * Contact data model with avatar support (from Phase 1).
 */
data class Contact(
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String? = null,
    val address: String? = null,

    // Avatar fields (Phase 2)
    val avatarId: Int? = null,        // Resource ID (e.g., R.drawable.avatar_01)
    val avatarUri: String? = null,    // Custom URI (e.g., "content://...")

    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Returns the avatar resource ID to use for displaying this contact.
     * Returns null if custom URI should be used instead.
     */
    fun getAvatarResourceId(): Int? {
        return when {
            avatarId != null -> avatarId
            avatarUri != null -> null  // Use URI instead
            else -> R.drawable.avatar_default
        }
    }

    /**
     * Returns true if this contact has a custom avatar (not default).
     */
    fun hasCustomAvatar(): Boolean {
        return avatarId != null || avatarUri != null
    }

    /**
     * Returns the display avatar resource ID with guaranteed default fallback.
     */
    fun getDisplayAvatarResourceId(): Int {
        return avatarId ?: R.drawable.avatar_default
    }
}
