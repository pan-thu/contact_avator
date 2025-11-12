package dev.panthu.contactavatorapplication.ui.avatar

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.panthu.contactavatorapplication.R
import dev.panthu.contactavatorapplication.data.ContactRepository

/**
 * ViewModel for the Avatar Picker dialog.
 * Features:
 * - Manages available avatars from repository
 * - Tracks current selection with LiveData
 * - State preservation across configuration changes
 * - Handles both resource IDs and custom URIs
 * - Reset to default avatar functionality
 */
class AvatarPickerViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ContactRepository? = null
) : ViewModel() {

    companion object {
        private const val KEY_SELECTED_AVATAR_ID = "selected_avatar_id"
        private const val KEY_SELECTED_AVATAR_URI = "selected_avatar_uri"
        private const val KEY_INITIAL_AVATAR_ID = "initial_avatar_id"
        private const val KEY_INITIAL_AVATAR_URI = "initial_avatar_uri"
    }

    // List of available avatar resource IDs
    private val _availableAvatars = MutableLiveData<List<Int>>()
    val availableAvatars: LiveData<List<Int>> = _availableAvatars

    // Currently selected avatar resource ID
    private val _selectedAvatarId = MutableLiveData<Int?>()
    val selectedAvatarId: LiveData<Int?> = _selectedAvatarId

    // Currently selected custom avatar URI
    private val _selectedAvatarUri = MutableLiveData<String?>()
    val selectedAvatarUri: LiveData<String?> = _selectedAvatarUri

    // Track if selection has changed from initial
    private val _hasChanges = MutableLiveData<Boolean>(false)
    val hasChanges: LiveData<Boolean> = _hasChanges

    init {
        // Restore state if available
        restoreState()

        // Load available avatars if not already loaded
        if (_availableAvatars.value == null) {
            loadAvailableAvatars()
        }
    }

    /**
     * Set the initial avatar selection when opening the picker.
     */
    fun setInitialSelection(@DrawableRes avatarId: Int?, avatarUri: String?) {
        savedStateHandle[KEY_INITIAL_AVATAR_ID] = avatarId
        savedStateHandle[KEY_INITIAL_AVATAR_URI] = avatarUri

        // Only set if current selection is null (first time opening)
        if (_selectedAvatarId.value == null && _selectedAvatarUri.value == null) {
            _selectedAvatarId.value = avatarId
            _selectedAvatarUri.value = avatarUri
            savedStateHandle[KEY_SELECTED_AVATAR_ID] = avatarId
            savedStateHandle[KEY_SELECTED_AVATAR_URI] = avatarUri
        }
    }

    /**
     * Select an avatar by resource ID.
     */
    fun selectAvatar(@DrawableRes avatarId: Int) {
        _selectedAvatarId.value = avatarId
        _selectedAvatarUri.value = null // Clear custom URI
        savedStateHandle[KEY_SELECTED_AVATAR_ID] = avatarId
        savedStateHandle[KEY_SELECTED_AVATAR_URI] = null
        updateHasChanges()
    }

    /**
     * Select a custom avatar by URI.
     */
    fun selectCustomAvatar(uri: Uri?) {
        if (uri == null) {
            // Reset to default if URI is null
            resetToDefault()
            return
        }

        _selectedAvatarUri.value = uri.toString()
        _selectedAvatarId.value = null // Clear resource ID
        savedStateHandle[KEY_SELECTED_AVATAR_URI] = uri.toString()
        savedStateHandle[KEY_SELECTED_AVATAR_ID] = null
        updateHasChanges()
    }

    /**
     * Reset to the default avatar.
     */
    fun resetToDefault() {
        _selectedAvatarId.value = R.drawable.avatar_default
        _selectedAvatarUri.value = null
        savedStateHandle[KEY_SELECTED_AVATAR_ID] = R.drawable.avatar_default
        savedStateHandle[KEY_SELECTED_AVATAR_URI] = null
        updateHasChanges()
    }

    /**
     * Get the current display avatar (for preview).
     * Returns either the selected resource ID or default.
     */
    fun getCurrentDisplayAvatar(): Int {
        return _selectedAvatarId.value ?: R.drawable.avatar_default
    }

    /**
     * Get the current custom avatar URI if any.
     */
    fun getCurrentAvatarUri(): String? {
        return _selectedAvatarUri.value
    }

    /**
     * Check if a custom URI is currently selected.
     */
    fun hasCustomUri(): Boolean {
        return !_selectedAvatarUri.value.isNullOrEmpty()
    }

    /**
     * Load available avatar resources.
     */
    private fun loadAvailableAvatars() {
        // Define all available avatar resource IDs
        val avatars = listOf(
            R.drawable.avatar_default,
            R.drawable.avatar_01,
            R.drawable.avatar_02,
            R.drawable.avatar_03,
            R.drawable.avatar_04,
            R.drawable.avatar_05,
            R.drawable.avatar_06,
            R.drawable.avatar_07,
            R.drawable.avatar_08,
            R.drawable.avatar_09,
            R.drawable.avatar_10
        )
        _availableAvatars.value = avatars
    }

    /**
     * Restore state from SavedStateHandle.
     */
    private fun restoreState() {
        savedStateHandle.get<Int>(KEY_SELECTED_AVATAR_ID)?.let {
            _selectedAvatarId.value = it
        }
        savedStateHandle.get<String>(KEY_SELECTED_AVATAR_URI)?.let {
            _selectedAvatarUri.value = it
        }
    }

    /**
     * Update the hasChanges flag by comparing current selection with initial.
     */
    private fun updateHasChanges() {
        val initialId = savedStateHandle.get<Int>(KEY_INITIAL_AVATAR_ID)
        val initialUri = savedStateHandle.get<String>(KEY_INITIAL_AVATAR_URI)
        val currentId = _selectedAvatarId.value
        val currentUri = _selectedAvatarUri.value

        _hasChanges.value = (currentId != initialId) || (currentUri != initialUri)
    }

    /**
     * Result data class for returning selection.
     */
    data class AvatarSelection(
        @DrawableRes val avatarId: Int?,
        val avatarUri: String?
    )

    /**
     * Get the current selection as a result object.
     */
    fun getSelectionResult(): AvatarSelection {
        return AvatarSelection(
            avatarId = _selectedAvatarId.value,
            avatarUri = _selectedAvatarUri.value
        )
    }
}
