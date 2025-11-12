package dev.panthu.contactavatorapplication.ui.contact

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
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
 * Uses Room database queries for efficient searching instead of in-memory filtering.
 */
@OptIn(FlowPreview::class)
class ContactListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactRepository(application)
    private val preferencesManager = PreferencesManager.getInstance(application)

    // Search query with debouncing
    private val searchQueryFlow = MutableStateFlow("")
    val searchQuery = MutableLiveData<String>("")

    // Sort order
    private val _sortOrder = MutableLiveData<PreferencesManager.SortOrder>()
    val sortOrder: LiveData<PreferencesManager.SortOrder> = _sortOrder

    // Contacts from database (switches between all contacts and search results)
    private val searchedContacts: LiveData<List<Contact>> = searchQuery.switchMap { query ->
        if (query.isNullOrBlank()) {
            // No search query - return all contacts
            repository.getAllContacts().asLiveData()
        } else {
            // Search query exists - use database search
            repository.searchContacts(query).asLiveData()
        }
    }

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

        // Combine searched contacts and sort order
        // Searching is done in the database, we only sort in memory
        filteredContacts.addSource(searchedContacts) { contacts ->
            applySorting(contacts, sortOrder.value)
        }

        filteredContacts.addSource(sortOrder) { order ->
            applySorting(searchedContacts.value, order)
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
     * Apply sorting to the contact list.
     * Searching is handled by Room database queries for better performance.
     */
    private fun applySorting(
        contacts: List<Contact>?,
        order: PreferencesManager.SortOrder?
    ) {
        if (contacts == null) {
            filteredContacts.value = emptyList()
            return
        }

        // Apply sorting based on selected order
        val sortedContacts = when (order ?: PreferencesManager.SortOrder.NAME_ASC) {
            PreferencesManager.SortOrder.NAME_ASC -> {
                contacts.sortedBy { it.name.lowercase() }
            }
            PreferencesManager.SortOrder.NAME_DESC -> {
                contacts.sortedByDescending { it.name.lowercase() }
            }
            PreferencesManager.SortOrder.RECENTLY_ADDED -> {
                contacts.sortedByDescending { it.createdAt }
            }
        }

        filteredContacts.value = sortedContacts
    }

    /**
     * Get the current sort order.
     */
    fun getCurrentSortOrder(): PreferencesManager.SortOrder {
        return _sortOrder.value ?: PreferencesManager.SortOrder.NAME_ASC
    }
}
