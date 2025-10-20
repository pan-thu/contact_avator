package dev.panthu.contactavatorapplication.ui.contact

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.panthu.contactavatorapplication.data.Contact
import dev.panthu.contactavatorapplication.data.ContactRepository
import dev.panthu.contactavatorapplication.util.PreferencesManager
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * ViewModel for the contact list screen.
 * Manages contact list data, search/filter, and sort operations.
 */
@OptIn(FlowPreview::class)
class ContactListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactRepository(application)
    private val preferencesManager = PreferencesManager.getInstance(application)

    // All contacts from repository
    private val allContacts: LiveData<List<Contact>> =
        repository.getAllContacts().asLiveData()

    // Search query with debouncing
    private val searchQueryFlow = MutableStateFlow("")
    val searchQuery = MutableLiveData<String>("")

    // Sort order
    private val _sortOrder = MutableLiveData<PreferencesManager.SortOrder>()
    val sortOrder: LiveData<PreferencesManager.SortOrder> = _sortOrder

    // Filtered and sorted contacts
    val filteredContacts = MediatorLiveData<List<Contact>>()

    init {
        // Load saved sort preference
        _sortOrder.value = preferencesManager.getSortOrder()

        // Setup debounced search
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300) // 300ms debounce
                .distinctUntilChanged()
                .collect { query ->
                    searchQuery.postValue(query)
                }
        }

        // Combine allContacts, searchQuery, and sortOrder
        filteredContacts.addSource(allContacts) { contacts ->
            updateFilteredContacts(contacts, searchQuery.value, sortOrder.value)
        }

        filteredContacts.addSource(searchQuery) { query ->
            updateFilteredContacts(allContacts.value, query, sortOrder.value)
        }

        filteredContacts.addSource(sortOrder) { order ->
            updateFilteredContacts(allContacts.value, searchQuery.value, order)
        }
    }

    /**
     * Update search query (will be debounced).
     */
    fun setSearchQuery(query: String) {
        viewModelScope.launch {
            searchQueryFlow.emit(query)
        }
    }

    /**
     * Update sort order and persist preference.
     */
    fun setSortOrder(order: PreferencesManager.SortOrder) {
        _sortOrder.value = order
        preferencesManager.setSortOrder(order)
    }

    /**
     * Apply filtering and sorting to the contact list.
     */
    private fun updateFilteredContacts(
        contacts: List<Contact>?,
        query: String?,
        order: PreferencesManager.SortOrder?
    ) {
        if (contacts == null) {
            filteredContacts.value = emptyList()
            return
        }

        var result = contacts

        // Apply search filter
        if (!query.isNullOrBlank()) {
            val searchQuery = query.trim().lowercase()
            result = result.filter { contact ->
                contact.name.lowercase().contains(searchQuery) ||
                contact.phone.lowercase().contains(searchQuery)
            }
        }

        // Apply sorting
        result = when (order ?: PreferencesManager.SortOrder.NAME_ASC) {
            PreferencesManager.SortOrder.NAME_ASC -> {
                result.sortedBy { it.name.lowercase() }
            }
            PreferencesManager.SortOrder.NAME_DESC -> {
                result.sortedByDescending { it.name.lowercase() }
            }
            PreferencesManager.SortOrder.RECENTLY_ADDED -> {
                result.sortedByDescending { it.createdAt }
            }
        }

        filteredContacts.value = result
    }

    /**
     * Get the current sort order.
     */
    fun getCurrentSortOrder(): PreferencesManager.SortOrder {
        return _sortOrder.value ?: PreferencesManager.SortOrder.NAME_ASC
    }
}
