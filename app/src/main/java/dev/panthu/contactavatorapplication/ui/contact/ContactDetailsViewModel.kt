package dev.panthu.contactavatorapplication.ui.contact

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.panthu.contactavatorapplication.data.Contact
import dev.panthu.contactavatorapplication.data.ContactRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the contact details screen.
 * Manages loading and deleting a single contact.
 */
class ContactDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactRepository(application)

    private val _contact = MutableLiveData<Contact?>()
    val contact: LiveData<Contact?> = _contact

    private val _deleteResult = MutableLiveData<Result<Unit>>()
    val deleteResult: LiveData<Result<Unit>> = _deleteResult

    private val _isDeleting = MutableLiveData<Boolean>(false)
    val isDeleting: LiveData<Boolean> = _isDeleting

    /**
     * Load a contact by ID.
     */
    fun loadContact(contactId: Long) {
        viewModelScope.launch {
            try {
                val loadedContact = repository.getContactById(contactId)
                _contact.postValue(loadedContact)
            } catch (e: Exception) {
                _contact.postValue(null)
            }
        }
    }

    /**
     * Delete the current contact.
     */
    fun deleteContact() {
        val contactToDelete = _contact.value ?: return

        _isDeleting.value = true
        viewModelScope.launch {
            try {
                repository.delete(contactToDelete)
                _deleteResult.postValue(Result.success(Unit))
            } catch (e: Exception) {
                _deleteResult.postValue(Result.failure(e))
            } finally {
                _isDeleting.postValue(false)
            }
        }
    }

    /**
     * Reset delete result (for one-time event handling).
     */
    fun resetDeleteResult() {
        _deleteResult.value = null
    }
}
