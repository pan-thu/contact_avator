# ContactAvatar+ Application Logbook Template

## Project Information
- **Application Name**: ContactAvatar+
- **Student Name**: [Your Name]
- **Student ID**: [Your ID]
- **Module**: Mobile Application Development
- **Submission Date**: [Date]

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Requirements Analysis](#requirements-analysis)
3. [Architecture & Design](#architecture--design)
4. [Implementation Details](#implementation-details)
5. [Code Snippets](#code-snippets)
6. [Testing & Validation](#testing--validation)
7. [Screenshots](#screenshots)
8. [Challenges & Solutions](#challenges--solutions)
9. [Conclusion](#conclusion)

---

## 1. Project Overview

### Application Purpose
ContactAvatar+ is an Android contact management application that allows users to create, view, edit, and delete contacts with personalized avatar selections. The application emphasizes intuitive user experience through visual avatar representation and efficient contact organization.

### Key Features
- Contact CRUD operations (Create, Read, Update, Delete)
- Avatar selection from 10 pre-designed options
- Real-time search and filtering
- Multiple sort options (A-Z, Z-A, Recently Added)
- Form validation with inline error messages
- Offline-first architecture with local database
- Material Design UI with accessibility support

### Technology Stack
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI**: Android Views (Traditional XML layouts)
- **Database**: Room Persistence Library
- **Async**: Kotlin Coroutines + LiveData
- **DI**: Manual dependency injection
- **Build**: Gradle with Kotlin DSL

---

## 2. Requirements Analysis

### Functional Requirements

#### FR-01: Contact Creation
**Requirement**: Users can create new contacts with name, phone, email, and avatar.

**Implementation Evidence**:
- CreateContactActivity with form fields
- Avatar picker integration
- Room database INSERT operation
- Screenshot references: 02, 03, 04

**Code Reference**: See Section 5.1 - Contact Entity

---

#### FR-02: Avatar Selection
**Requirement**: Users can choose from 10 pre-designed avatars.

**Implementation Evidence**:
- AvatarPickerDialog with RecyclerView grid
- 10 avatar drawables in res/drawable
- Avatar selection state management
- Screenshot references: 03, 04

**Code Reference**: See Section 5.2 - Avatar Picker Dialog

---

#### FR-03: Contact Viewing
**Requirement**: Users can view contact list and individual contact details.

**Implementation Evidence**:
- MainActivity with RecyclerView for list
- ContactDetailActivity for individual view
- LiveData observation for real-time updates
- Screenshot references: 01, 05

**Code Reference**: See Section 5.3 - RecyclerView Adapter

---

#### FR-04: Contact Editing
**Requirement**: Users can update existing contact information.

**Implementation Evidence**:
- EditContactActivity with pre-populated fields
- Room database UPDATE operation
- Avatar change capability
- Screenshot references: 06

**Code Reference**: See Section 5.1 - Contact Entity (update query)

---

#### FR-05: Contact Deletion
**Requirement**: Users can delete contacts with confirmation.

**Implementation Evidence**:
- Delete confirmation dialog
- Room database DELETE operation
- Cascade handling
- Screenshot references: 15 (optional)

**Code Reference**: See Section 5.5 - ViewModel Logic

---

#### FR-06: Search Functionality
**Requirement**: Real-time search filtering by name.

**Implementation Evidence**:
- SearchView integration in toolbar
- Query-based filtering in ViewModel
- LiveData transformation
- Screenshot references: 07, 08

**Code Reference**: See Section 5.4 - Search Implementation

---

#### FR-07: Sort Functionality
**Requirement**: Multiple sort options (Name A-Z, Name Z-A, Recently Added).

**Implementation Evidence**:
- Sort menu in options menu
- Room database ORDER BY queries
- Sort state persistence
- Screenshot references: 09, 10, 11

**Code Reference**: See Section 5.4 - Sort Implementation

---

#### FR-08: Form Validation
**Requirement**: Validate required fields and format constraints.

**Implementation Evidence**:
- Name required validation
- Phone format validation
- Email format validation (optional)
- Inline error messages
- Screenshot references: 12, 13

**Code Reference**: See Section 5.6 - Validation Logic

---

### Non-Functional Requirements

#### NFR-01: Material Design Compliance
**Implementation Evidence**:
- Material Components library integration
- Material Design color scheme
- Proper spacing and typography
- FAB, Cards, Dialogs following guidelines
- Screenshot references: All screenshots

**Code Reference**: See Section 5.7 - Theme Configuration

---

#### NFR-02: Responsive UI
**Implementation Evidence**:
- ConstraintLayout for flexible layouts
- RecyclerView for efficient list rendering
- DiffUtil for optimized updates
- Smooth animations and transitions

**Code Reference**: See Section 5.3 - DiffUtil Implementation

---

#### NFR-03: Offline Operation
**Implementation Evidence**:
- Room database for local storage
- No network dependencies
- Instant data access
- Data persistence across app restarts

---

#### NFR-04: Performance
**Implementation Evidence**:
- DiffUtil for RecyclerView efficiency
- Database queries on background threads (Coroutines)
- Lazy loading with LiveData
- Optimized avatar resource loading

---

#### NFR-05: Accessibility
**Implementation Evidence**:
- Content descriptions for ImageViews
- Clear error messages
- Adequate touch target sizes
- Readable font sizes and contrast

---

#### NFR-06: Privacy
**Implementation Evidence**:
- All data stored locally
- No external data transmission
- No permissions required
- User-controlled data management

---

## 3. Architecture & Design

### MVVM Architecture

```
┌─────────────────────────────────────────────────┐
│                    VIEW                         │
│  (Activity/Fragment - XML Layouts)              │
│  - MainActivity                                 │
│  - CreateContactActivity                        │
│  - ContactDetailActivity                        │
└─────────────┬───────────────────────────────────┘
              │ observes LiveData
              │ user events
              ▼
┌─────────────────────────────────────────────────┐
│                 VIEWMODEL                       │
│  - ContactViewModel                             │
│  - Business logic layer                         │
│  - LiveData exposure                            │
└─────────────┬───────────────────────────────────┘
              │ repository calls
              │
              ▼
┌─────────────────────────────────────────────────┐
│                REPOSITORY                       │
│  - ContactRepository                            │
│  - Data access abstraction                      │
└─────────────┬───────────────────────────────────┘
              │ DAO operations
              │
              ▼
┌─────────────────────────────────────────────────┐
│          DATABASE (Room)                        │
│  - ContactDao                                   │
│  - ContactDatabase                              │
│  - Contact Entity                               │
└─────────────────────────────────────────────────┘
```

### Project Structure
```
app/src/main/
├── java/com/example/contactavatar/
│   ├── data/
│   │   ├── Contact.kt              # Entity
│   │   ├── ContactDao.kt           # Database Access
│   │   ├── ContactDatabase.kt      # Room Database
│   │   └── ContactRepository.kt    # Repository
│   ├── ui/
│   │   ├── MainActivity.kt
│   │   ├── CreateContactActivity.kt
│   │   ├── EditContactActivity.kt
│   │   ├── ContactDetailActivity.kt
│   │   ├── ContactAdapter.kt       # RecyclerView Adapter
│   │   └── AvatarPickerDialog.kt   # Avatar Selection
│   ├── viewmodel/
│   │   ├── ContactViewModel.kt
│   │   └── ContactViewModelFactory.kt
│   └── utils/
│       └── ValidationUtils.kt
├── res/
│   ├── drawable/                   # Avatar images
│   ├── layout/                     # XML layouts
│   ├── values/                     # Strings, colors, themes
│   └── menu/                       # Menu resources
└── AndroidManifest.xml
```

---

## 4. Implementation Details

### Database Design

**Contact Table Schema**:
```
┌──────────────┬───────────────┬────────────────┐
│   Column     │     Type      │  Constraints   │
├──────────────┼───────────────┼────────────────┤
│ id           │ Int           │ PRIMARY KEY    │
│ name         │ String        │ NOT NULL       │
│ phoneNumber  │ String        │ NOT NULL       │
│ email        │ String        │ NULLABLE       │
│ avatarResId  │ Int           │ NOT NULL       │
│ createdAt    │ Long          │ NOT NULL       │
└──────────────┴───────────────┴────────────────┘
```

### Avatar Strategy
- 10 pre-designed avatar images stored in `res/drawable/`
- Named: `avatar_01.png` through `avatar_10.png`
- Resource IDs stored as integers in database
- Fallback to default avatar if resource not found
- Grid display in dialog for selection

### Navigation Flow
```
MainActivity (List)
    ├─→ CreateContactActivity → AvatarPickerDialog
    ├─→ ContactDetailActivity
    │       └─→ EditContactActivity → AvatarPickerDialog
    └─→ Search/Filter (in-place)
```

---

## 5. Code Snippets

### 5.1 Contact Entity

```kotlin
package com.example.contactavatar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Contact entity representing a contact in the database.
 *
 * @property id Unique identifier (auto-generated)
 * @property name Contact's full name (required)
 * @property phoneNumber Contact's phone number (required)
 * @property email Contact's email address (optional)
 * @property avatarResId Resource ID of selected avatar drawable
 * @property createdAt Timestamp of contact creation (for sorting)
 */
@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val phoneNumber: String,
    val email: String = "",
    val avatarResId: Int,
    val createdAt: Long = System.currentTimeMillis()
)
```

**Explanation**:
- `@Entity` annotation marks this as a Room database table
- `@PrimaryKey(autoGenerate = true)` enables auto-incrementing ID
- All fields map directly to database columns
- `createdAt` timestamp enables "Recently Added" sorting
- `avatarResId` stores drawable resource ID (e.g., R.drawable.avatar_01)

---

### 5.2 Avatar Picker Dialog

```kotlin
package com.example.contactavatar.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactavatar.R
import com.example.contactavatar.databinding.DialogAvatarPickerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Dialog for selecting contact avatar from grid of options.
 *
 * @param context Context for dialog creation
 * @param currentAvatarId Currently selected avatar resource ID
 * @param onAvatarSelected Callback when avatar is selected
 */
class AvatarPickerDialog(
    context: Context,
    private val currentAvatarId: Int,
    private val onAvatarSelected: (Int) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogAvatarPickerBinding
    private var selectedAvatarId: Int = currentAvatarId

    // List of all available avatar resource IDs
    private val avatarList = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAvatarPickerBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        setupRecyclerView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        binding.avatarRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3) // 3 columns
            adapter = AvatarAdapter(avatarList, selectedAvatarId) { avatarId ->
                selectedAvatarId = avatarId
                binding.selectedAvatarPreview.setImageResource(avatarId)
            }
        }
    }

    private fun setupButtons() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSelect.setOnClickListener {
            onAvatarSelected(selectedAvatarId)
            dismiss()
        }
    }

    /**
     * RecyclerView adapter for avatar grid.
     */
    private inner class AvatarAdapter(
        private val avatars: List<Int>,
        private var selectedId: Int,
        private val onSelect: (Int) -> Unit
    ) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

        inner class AvatarViewHolder(val imageView: ImageView) :
            RecyclerView.ViewHolder(imageView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
            val imageView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_avatar, parent, false) as ImageView
            return AvatarViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
            val avatarId = avatars[position]
            holder.imageView.setImageResource(avatarId)

            // Highlight selected avatar
            holder.imageView.alpha = if (avatarId == selectedId) 1.0f else 0.5f

            holder.imageView.setOnClickListener {
                val oldSelectedId = selectedId
                selectedId = avatarId
                onSelect(avatarId)

                // Update UI for old and new selection
                notifyItemChanged(avatars.indexOf(oldSelectedId))
                notifyItemChanged(position)
            }
        }

        override fun getItemCount() = avatars.size
    }
}
```

**Explanation**:
- Custom Dialog class extending Android Dialog
- RecyclerView with GridLayoutManager (3 columns) for avatar grid
- Inner AvatarAdapter class handles avatar item rendering
- Visual feedback: selected avatar shown at full opacity, others at 50%
- Preview ImageView shows currently selected avatar
- Callback pattern (`onAvatarSelected`) passes selection back to activity

---

### 5.3 RecyclerView Adapter with DiffUtil

```kotlin
package com.example.contactavatar.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactavatar.data.Contact
import com.example.contactavatar.databinding.ItemContactBinding

/**
 * RecyclerView adapter for contact list with DiffUtil optimization.
 *
 * @param onContactClick Callback when contact item is clicked
 */
class ContactAdapter(
    private val onContactClick: (Contact) -> Unit
) : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ContactViewHolder(
        private val binding: ItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.apply {
                // Set contact data
                textViewName.text = contact.name
                textViewPhone.text = contact.phoneNumber
                imageViewAvatar.setImageResource(contact.avatarResId)

                // Set content descriptions for accessibility
                imageViewAvatar.contentDescription = "Avatar for ${contact.name}"

                // Handle click event
                root.setOnClickListener {
                    onContactClick(contact)
                }
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates.
     * Calculates minimal changes between old and new lists.
     */
    private class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {

        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            // Compare unique IDs
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            // Compare all fields for changes
            return oldItem == newItem
        }
    }
}
```

**Explanation**:
- Extends `ListAdapter` instead of `RecyclerView.Adapter` for DiffUtil integration
- `ContactDiffCallback` calculates minimal changes between lists
- `areItemsTheSame()` checks if items represent same entity (by ID)
- `areContentsTheSame()` checks if item data changed (using data class equality)
- DiffUtil automatically animates insertions, deletions, and updates
- ViewBinding used for type-safe view access
- Click listener passed via constructor for flexible handling

---

### 5.4 Search and Filter Implementation

```kotlin
package com.example.contactavatar.viewmodel

import androidx.lifecycle.*
import com.example.contactavatar.data.Contact
import com.example.contactavatar.data.ContactRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for contact list with search and sort functionality.
 */
class ContactViewModel(
    private val repository: ContactRepository
) : ViewModel() {

    // Search query state
    private val _searchQuery = MutableLiveData<String>("")

    // Sort mode state
    private val _sortMode = MutableLiveData<SortMode>(SortMode.NAME_ASC)

    /**
     * Combined LiveData that reacts to both search and sort changes.
     * Uses switchMap to transform queries into database results.
     */
    val contacts: LiveData<List<Contact>> = _searchQuery.switchMap { query ->
        _sortMode.switchMap { sortMode ->
            when {
                query.isBlank() -> {
                    // No search query - return all contacts sorted
                    when (sortMode) {
                        SortMode.NAME_ASC -> repository.getContactsSortedByNameAsc()
                        SortMode.NAME_DESC -> repository.getContactsSortedByNameDesc()
                        SortMode.RECENTLY_ADDED -> repository.getContactsSortedByDateDesc()
                    }
                }
                else -> {
                    // Search query active - filter and sort
                    when (sortMode) {
                        SortMode.NAME_ASC -> repository.searchContactsSortedAsc(query)
                        SortMode.NAME_DESC -> repository.searchContactsSortedDesc(query)
                        SortMode.RECENTLY_ADDED -> repository.searchContactsSortedByDate(query)
                    }
                }
            }
        }
    }

    /**
     * Update search query (triggers automatic list update).
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Update sort mode (triggers automatic list update).
     */
    fun setSortMode(mode: SortMode) {
        _sortMode.value = mode
    }

    enum class SortMode {
        NAME_ASC,
        NAME_DESC,
        RECENTLY_ADDED
    }
}
```

**Explanation**:
- `switchMap` operator transforms LiveData based on search/sort state
- Reactive approach: UI automatically updates when query or sort changes
- Combined logic handles both search and sort simultaneously
- Repository methods handle database queries (see below)
- ViewModel holds no direct reference to views (MVVM principle)

**Repository Search Methods** (ContactRepository.kt):
```kotlin
/**
 * Search contacts by name with case-insensitive matching.
 */
fun searchContactsSortedAsc(query: String): LiveData<List<Contact>> {
    return contactDao.searchContactsAsc("%$query%")
}

fun searchContactsSortedDesc(query: String): LiveData<List<Contact>> {
    return contactDao.searchContactsDesc("%$query%")
}

fun searchContactsSortedByDate(query: String): LiveData<List<Contact>> {
    return contactDao.searchContactsByDate("%$query%")
}
```

**DAO Query Examples** (ContactDao.kt):
```kotlin
@Query("SELECT * FROM contacts WHERE name LIKE :query ORDER BY name ASC")
fun searchContactsAsc(query: String): LiveData<List<Contact>>

@Query("SELECT * FROM contacts WHERE name LIKE :query ORDER BY name DESC")
fun searchContactsDesc(query: String): LiveData<List<Contact>>

@Query("SELECT * FROM contacts WHERE name LIKE :query ORDER BY createdAt DESC")
fun searchContactsByDate(query: String): LiveData<List<Contact>>
```

**Explanation**:
- SQL `LIKE` operator with `%` wildcards for partial matching
- Case-insensitive search (SQLite default)
- Combined WHERE and ORDER BY clauses for filtered sorting

---

### 5.5 ViewModel CRUD Operations

```kotlin
/**
 * Insert new contact into database.
 */
fun insertContact(contact: Contact) {
    viewModelScope.launch {
        repository.insert(contact)
    }
}

/**
 * Update existing contact.
 */
fun updateContact(contact: Contact) {
    viewModelScope.launch {
        repository.update(contact)
    }
}

/**
 * Delete contact from database.
 */
fun deleteContact(contact: Contact) {
    viewModelScope.launch {
        repository.delete(contact)
    }
}

/**
 * Get single contact by ID.
 */
fun getContactById(id: Int): LiveData<Contact> {
    return repository.getContactById(id)
}
```

**Explanation**:
- `viewModelScope.launch` runs database operations on background thread
- Coroutines automatically cancelled when ViewModel destroyed
- Repository pattern abstracts database access
- LiveData returned for reactive single-contact observation

---

### 5.6 Validation Logic

```kotlin
package com.example.contactavatar.utils

import android.util.Patterns

/**
 * Utility object for form validation.
 */
object ValidationUtils {

    /**
     * Validate contact name.
     * @return Error message if invalid, null if valid
     */
    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Name is required"
            name.length < 2 -> "Name must be at least 2 characters"
            name.length > 50 -> "Name is too long (max 50 characters)"
            else -> null
        }
    }

    /**
     * Validate phone number.
     * @return Error message if invalid, null if valid
     */
    fun validatePhone(phone: String): String? {
        return when {
            phone.isBlank() -> "Phone number is required"
            !phone.matches(Regex("^[0-9\\-\\+\\(\\)\\s]{7,20}$")) ->
                "Invalid phone format (use digits, +, -, (, ), spaces)"
            else -> null
        }
    }

    /**
     * Validate email address (optional field).
     * @return Error message if invalid, null if valid or empty
     */
    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> null // Email optional
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Invalid email format"
            else -> null
        }
    }

    /**
     * Validate all fields and return map of errors.
     * @return Map of field name to error message (empty if all valid)
     */
    fun validateContact(
        name: String,
        phone: String,
        email: String
    ): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        validateName(name)?.let { errors["name"] = it }
        validatePhone(phone)?.let { errors["phone"] = it }
        validateEmail(email)?.let { errors["email"] = it }

        return errors
    }
}
```

**Usage in Activity**:
```kotlin
private fun saveContact() {
    val errors = ValidationUtils.validateContact(
        name = binding.editTextName.text.toString(),
        phone = binding.editTextPhone.text.toString(),
        email = binding.editTextEmail.text.toString()
    )

    if (errors.isNotEmpty()) {
        // Show errors
        errors["name"]?.let { binding.textInputLayoutName.error = it }
        errors["phone"]?.let { binding.textInputLayoutPhone.error = it }
        errors["email"]?.let { binding.textInputLayoutEmail.error = it }
        return
    }

    // Clear errors
    binding.textInputLayoutName.error = null
    binding.textInputLayoutPhone.error = null
    binding.textInputLayoutEmail.error = null

    // Proceed with save
    // ...
}
```

**Explanation**:
- Centralized validation logic in utility object
- Each validation method returns error message or null
- Regex patterns validate phone format
- Android's Patterns.EMAIL_ADDRESS validates email
- Error messages displayed inline using TextInputLayout.error property

---

### 5.7 Theme Configuration

**themes.xml (res/values/themes.xml)**:
```xml
<resources>
    <style name="Theme.ContactAvatar" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <!-- Primary brand color -->
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>

        <!-- Secondary brand color -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_700</item>
        <item name="colorOnSecondary">@color/black</item>

        <!-- Status bar color -->
        <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>

        <!-- Material button style -->
        <item name="materialButtonStyle">@style/Widget.MaterialComponents.Button</item>

        <!-- FAB style -->
        <item name="floatingActionButtonStyle">@style/Widget.MaterialComponents.FloatingActionButton</item>
    </style>
</resources>
```

**colors.xml (res/values/colors.xml)**:
```xml
<resources>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>

    <!-- Additional UI colors -->
    <color name="error_red">#FFB00020</color>
    <color name="background_gray">#FFF5F5F5</color>
</resources>
```

**strings.xml (res/values/strings.xml)** - Sample:
```xml
<resources>
    <string name="app_name">ContactAvatar+</string>

    <!-- Common actions -->
    <string name="action_save">Save</string>
    <string name="action_cancel">Cancel</string>
    <string name="action_delete">Delete</string>
    <string name="action_edit">Edit</string>
    <string name="action_search">Search</string>

    <!-- Contact form labels -->
    <string name="label_name">Name</string>
    <string name="label_phone">Phone Number</string>
    <string name="label_email">Email (Optional)</string>
    <string name="label_avatar">Select Avatar</string>

    <!-- Validation messages -->
    <string name="error_name_required">Name is required</string>
    <string name="error_phone_required">Phone number is required</string>
    <string name="error_phone_invalid">Invalid phone format</string>
    <string name="error_email_invalid">Invalid email format</string>

    <!-- Dialog messages -->
    <string name="dialog_delete_title">Delete Contact</string>
    <string name="dialog_delete_message">Are you sure you want to delete %1$s?</string>

    <!-- Sort options -->
    <string name="sort_name_asc">Name (A-Z)</string>
    <string name="sort_name_desc">Name (Z-A)</string>
    <string name="sort_recently_added">Recently Added</string>
</resources>
```

**Explanation**:
- Material Components theme provides Material Design styling
- DayNight theme enables automatic light/dark mode support
- All strings externalized for localization support
- Color palette follows Material Design guidelines
- No hard-coded strings in code files

---

## 6. Testing & Validation

### Manual Testing Checklist

#### Contact Creation
- [ ] Create contact with all fields filled
- [ ] Create contact with only required fields
- [ ] Verify avatar selection works
- [ ] Verify contact appears in list immediately
- [ ] Verify data persists after app restart

#### Contact Editing
- [ ] Edit all fields of existing contact
- [ ] Change avatar
- [ ] Verify changes persist
- [ ] Verify UI updates in list view

#### Contact Deletion
- [ ] Delete contact with confirmation
- [ ] Verify contact removed from list
- [ ] Verify deletion persists after app restart

#### Search Functionality
- [ ] Search with full name match
- [ ] Search with partial name match
- [ ] Search with no matches
- [ ] Clear search shows all contacts
- [ ] Search is case-insensitive

#### Sort Functionality
- [ ] Sort Name A-Z shows alphabetical order
- [ ] Sort Name Z-A shows reverse alphabetical
- [ ] Sort Recently Added shows newest first
- [ ] Sort persists during search

#### Validation
- [ ] Empty name shows error
- [ ] Invalid phone format shows error
- [ ] Invalid email format shows error
- [ ] Errors clear when corrected
- [ ] Cannot save with validation errors

#### Edge Cases
- [ ] App handles empty contact list
- [ ] App handles large contact list (50+ contacts)
- [ ] Special characters in names handled correctly
- [ ] Very long names truncated appropriately
- [ ] Missing avatar resource shows fallback

---

## 7. Screenshots

### User Journey: Create Contact

**Screenshot 01: Initial Contact List**
[Insert screenshot_01_contact_list.png]
*Caption: Main screen showing contact list with avatars. FAB visible for adding new contacts.*

**Screenshot 02: Create Contact Form**
[Insert screenshot_02_create_contact.png]
*Caption: Create contact screen with empty form fields and avatar selection area.*

**Screenshot 03: Avatar Picker Dialog**
[Insert screenshot_03_avatar_picker.png]
*Caption: Avatar picker showing all 10 avatar options in 3-column grid layout.*

**Screenshot 04: Avatar Selected**
[Insert screenshot_04_avatar_selected.png]
*Caption: Create form with selected avatar displayed, ready for data entry.*

---

### User Journey: View and Edit

**Screenshot 05: Contact Detail**
[Insert screenshot_05_contact_detail.png]
*Caption: Contact detail screen showing large avatar, information, and action buttons.*

**Screenshot 06: Edit Contact**
[Insert screenshot_06_edit_contact.png]
*Caption: Edit screen with pre-populated fields and option to change avatar.*

---

### Search and Sort Features

**Screenshot 07: Search Active**
[Insert screenshot_07_search_active.png]
*Caption: Search bar with query entered, filtering contact list in real-time.*

**Screenshot 08: Search Results**
[Insert screenshot_08_search_results.png]
*Caption: Filtered list showing only contacts matching search query.*

**Screenshot 09: Sort Menu**
[Insert screenshot_09_sort_menu.png]
*Caption: Options menu showing three sort options available.*

**Screenshot 10: Sorted A-Z**
[Insert screenshot_10_sorted_az.png]
*Caption: Contact list sorted alphabetically from A to Z.*

**Screenshot 11: Sorted Z-A**
[Insert screenshot_11_sorted_za.png]
*Caption: Contact list sorted in reverse alphabetical order.*

---

### Validation

**Screenshot 12: Name Validation Error**
[Insert screenshot_12_validation_name.png]
*Caption: Inline error message displayed when name field is empty.*

**Screenshot 13: Phone Validation Error**
[Insert screenshot_13_validation_phone.png]
*Caption: Error message for invalid phone number format.*

---

## 8. Challenges & Solutions

### Challenge 1: DiffUtil Integration
**Issue**: Initial RecyclerView implementation caused full list redraws on updates, resulting in poor performance and lost scroll position.

**Solution**: Implemented DiffUtil.ItemCallback to calculate minimal changes between lists. This enabled efficient updates with smooth animations and preserved scroll position.

**Code Reference**: Section 5.3

---

### Challenge 2: Search and Sort Combination
**Issue**: Implementing simultaneous search and sort required complex state management and query coordination.

**Solution**: Used LiveData switchMap operators to combine search query and sort mode into single reactive stream. Repository provides specialized query methods for each combination.

**Code Reference**: Section 5.4

---

### Challenge 3: Avatar Resource Management
**Issue**: Storing avatar images efficiently while maintaining type safety and database compatibility.

**Solution**: Stored drawable resource IDs as integers in database. Created centralized avatar list in dialog for consistency. Implemented fallback logic for missing resources.

**Code Reference**: Section 5.2

---

### Challenge 4: Form Validation UX
**Issue**: Needed clear, immediate validation feedback without being intrusive.

**Solution**: Implemented validation utility with inline error messages using TextInputLayout. Errors appear on focus loss or save attempt, clear automatically when corrected.

**Code Reference**: Section 5.6

---

## 9. Conclusion

### Achievement Summary
ContactAvatar+ successfully implements all functional and non-functional requirements specified in the PRD. The application provides intuitive contact management with visual avatar personalization, efficient search and sort capabilities, and robust form validation.

### Technical Highlights
- **MVVM Architecture**: Clean separation of concerns enables maintainability and testability
- **Room Database**: Offline-first approach with reactive LiveData observation
- **DiffUtil**: Optimized RecyclerView performance for smooth user experience
- **Material Design**: Consistent, accessible UI following Android design guidelines

### Learning Outcomes
- Practical implementation of MVVM architecture pattern
- Room database integration with Kotlin Coroutines
- RecyclerView optimization techniques (DiffUtil, ViewBinding)
- Material Design component usage and theming
- Form validation and error handling best practices

### Future Enhancements
Potential improvements beyond current scope:
- Contact grouping/categories
- Favorite contacts feature
- Contact sharing (export/import)
- Custom avatar upload from camera/gallery
- Dark theme support
- Contact backup/restore functionality

---

## Appendix

### Development Environment
- **IDE**: Android Studio Hedgehog | 2023.1.1
- **Kotlin Version**: 1.9.0
- **Gradle Version**: 8.2.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

### Dependencies (build.gradle.kts)
```kotlin
dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Material Design
    implementation("com.google.android.material:material:1.11.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}
```

### Build Configuration (build.gradle.kts - app level)
```kotlin
android {
    namespace = "com.example.contactavatar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.contactavatar"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
```

---

**End of Logbook**
