package dev.panthu.contactavatorapplication.ui.contact

import android.app.Application
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dev.panthu.contactavatorapplication.data.ContactRepository
import dev.panthu.contactavatorapplication.ui.avatar.AvatarPickerViewModel

/**
 * Factory for creating ViewModels with dependencies.
 * Provides ContactRepository to ViewModels that need it.
 */
class ViewModelFactory(
    private val application: Application,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return when {
            modelClass.isAssignableFrom(ContactEditViewModel::class.java) -> {
                val repository = ContactRepository(application)
                ContactEditViewModel(repository, handle) as T
            }
            modelClass.isAssignableFrom(AvatarPickerViewModel::class.java) -> {
                val repository = ContactRepository(application)
                AvatarPickerViewModel(handle, repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
