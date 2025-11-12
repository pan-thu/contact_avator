package dev.panthu.contactavatorapplication.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.panthu.contactavatorapplication.data.Contact
import dev.panthu.contactavatorapplication.data.ContactRepository
import dev.panthu.contactavatorapplication.util.ValidationResult
import dev.panthu.contactavatorapplication.util.ValidationUtils
import kotlinx.coroutines.launch

/**
 * ViewModel for Contact Edit/Create screen.
 * Manages form state, validation, and database operations.
 */
class ContactEditViewModel(
    private val repository: ContactRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_CONTACT_ID = "contact_id"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE = "phone"
        private const val KEY_EMAIL = "email"
        private const val KEY_ADDRESS = "address"
        private const val KEY_DATE_OF_BIRTH = "date_of_birth"
        private const val KEY_AVATAR_ID = "avatar_id"
        private const val KEY_AVATAR_URI = "avatar_uri"
        private const val KEY_IS_DIRTY = "is_dirty"
    }

    // Contact being edited (null for new contact)
    private val _contact = MutableLiveData<Contact?>()
    val contact: LiveData<Contact?> = _contact

    // Form fields with saved state
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _dateOfBirth = MutableLiveData<Long?>()
    val dateOfBirth: LiveData<Long?> = _dateOfBirth

    private val _avatarId = MutableLiveData<Int?>()
    val avatarId: LiveData<Int?> = _avatarId

    private val _avatarUri = MutableLiveData<String?>()
    val avatarUri: LiveData<String?> = _avatarUri

    // Validation states
    private val _nameValidation = MutableLiveData<ValidationResult>(ValidationResult.Success)
    val nameValidation: LiveData<ValidationResult> = _nameValidation

    private val _phoneValidation = MutableLiveData<ValidationResult>(ValidationResult.Success)
    val phoneValidation: LiveData<ValidationResult> = _phoneValidation

    private val _emailValidation = MutableLiveData<ValidationResult>(ValidationResult.Success)
    val emailValidation: LiveData<ValidationResult> = _emailValidation

    // Save button enabled state
    private val _isSaveEnabled = MutableLiveData<Boolean>(false)
    val isSaveEnabled: LiveData<Boolean> = _isSaveEnabled

    // Loading state
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Save result
    private val _saveResult = MutableLiveData<Result<Long>>()
    val saveResult: LiveData<Result<Long>> = _saveResult

    // Track if form has unsaved changes
    private val _isDirty = MutableLiveData<Boolean>(false)
    val isDirty: LiveData<Boolean> = _isDirty

    // Original contact for comparison
    private var originalContact: Contact? = null

    init {
        // Restore saved state if available
        restoreState()
    }

    /**
     * Loads an existing contact for editing.
     */
    fun loadContact(contactId: Long) {
        if (contactId <= 0) return

        savedStateHandle[KEY_CONTACT_ID] = contactId

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val loadedContact = repository.getContactById(contactId)
                if (loadedContact != null) {
                    _contact.value = loadedContact
                    originalContact = loadedContact

                    // Populate fields only if not already populated from saved state
                    if (_name.value.isNullOrEmpty()) {
                        _name.value = loadedContact.name
                        savedStateHandle[KEY_NAME] = loadedContact.name
                    }
                    if (_phone.value.isNullOrEmpty()) {
                        _phone.value = loadedContact.phone
                        savedStateHandle[KEY_PHONE] = loadedContact.phone
                    }
                    if (_email.value.isNullOrEmpty()) {
                        _email.value = loadedContact.email ?: ""
                        savedStateHandle[KEY_EMAIL] = loadedContact.email ?: ""
                    }
                    if (_address.value.isNullOrEmpty()) {
                        _address.value = loadedContact.address ?: ""
                        savedStateHandle[KEY_ADDRESS] = loadedContact.address ?: ""
                    }
                    if (_dateOfBirth.value == null) {
                        _dateOfBirth.value = loadedContact.dateOfBirth
                        savedStateHandle[KEY_DATE_OF_BIRTH] = loadedContact.dateOfBirth
                    }
                    if (_avatarId.value == null) {
                        _avatarId.value = loadedContact.avatarId
                        savedStateHandle[KEY_AVATAR_ID] = loadedContact.avatarId
                    }
                    if (_avatarUri.value == null) {
                        _avatarUri.value = loadedContact.avatarUri
                        savedStateHandle[KEY_AVATAR_URI] = loadedContact.avatarUri
                    }

                    // Validate initial state
                    validateAllFields()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Updates the name field and validates it.
     */
    fun updateName(newName: String) {
        _name.value = newName
        savedStateHandle[KEY_NAME] = newName
        _nameValidation.value = ValidationUtils.validateName(newName)
        updateSaveButtonState()
        updateDirtyState()
    }

    /**
     * Updates the phone field and validates it.
     */
    fun updatePhone(newPhone: String) {
        _phone.value = newPhone
        savedStateHandle[KEY_PHONE] = newPhone
        _phoneValidation.value = ValidationUtils.validatePhone(newPhone)
        updateSaveButtonState()
        updateDirtyState()
    }

    /**
     * Updates the email field and validates it.
     */
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        savedStateHandle[KEY_EMAIL] = newEmail
        _emailValidation.value = ValidationUtils.validateEmail(newEmail)
        updateSaveButtonState()
        updateDirtyState()
    }

    /**
     * Updates the address field (no validation needed).
     */
    fun updateAddress(newAddress: String) {
        _address.value = newAddress
        savedStateHandle[KEY_ADDRESS] = newAddress
        updateDirtyState()
    }

    /**
     * Updates the date of birth field.
     */
    fun updateDateOfBirth(newDateOfBirth: Long?) {
        _dateOfBirth.value = newDateOfBirth
        savedStateHandle[KEY_DATE_OF_BIRTH] = newDateOfBirth
        updateDirtyState()
    }

    /**
     * Updates the avatar selection.
     */
    fun updateAvatar(newAvatarId: Int?, newAvatarUri: String?) {
        _avatarId.value = newAvatarId
        _avatarUri.value = newAvatarUri
        savedStateHandle[KEY_AVATAR_ID] = newAvatarId
        savedStateHandle[KEY_AVATAR_URI] = newAvatarUri
        updateDirtyState()
    }

    /**
     * Saves the contact to the database.
     */
    fun saveContact() {
        // Validate all fields one more time
        validateAllFields()

        if (_isSaveEnabled.value != true) {
            _saveResult.value = Result.failure(IllegalStateException("Form is not valid"))
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val existingContact = _contact.value
                val contactToSave = if (existingContact != null) {
                    // Update existing contact
                    existingContact.copy(
                        name = _name.value.orEmpty().trim(),
                        phone = _phone.value.orEmpty().trim(),
                        email = _email.value?.trim()?.takeIf { it.isNotEmpty() },
                        address = _address.value?.trim()?.takeIf { it.isNotEmpty() },
                        dateOfBirth = _dateOfBirth.value,
                        avatarId = _avatarId.value,
                        avatarUri = _avatarUri.value
                    )
                } else {
                    // Create new contact
                    Contact(
                        name = _name.value.orEmpty().trim(),
                        phone = _phone.value.orEmpty().trim(),
                        email = _email.value?.trim()?.takeIf { it.isNotEmpty() },
                        address = _address.value?.trim()?.takeIf { it.isNotEmpty() },
                        dateOfBirth = _dateOfBirth.value,
                        avatarId = _avatarId.value,
                        avatarUri = _avatarUri.value
                    )
                }

                val result = if (existingContact != null) {
                    repository.update(contactToSave)
                    Result.success(existingContact.id)
                } else {
                    val id = repository.insert(contactToSave)
                    Result.success(id)
                }

                _saveResult.value = result
                _isDirty.value = false
                savedStateHandle[KEY_IS_DIRTY] = false

            } catch (e: Exception) {
                _saveResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Validates all fields.
     */
    private fun validateAllFields() {
        _nameValidation.value = ValidationUtils.validateName(_name.value)
        _phoneValidation.value = ValidationUtils.validatePhone(_phone.value)
        _emailValidation.value = ValidationUtils.validateEmail(_email.value)
        updateSaveButtonState()
    }

    /**
     * Updates the save button enabled state based on validation.
     */
    private fun updateSaveButtonState() {
        _isSaveEnabled.value = ValidationUtils.isFormValid(
            _nameValidation.value ?: ValidationResult.Success,
            _phoneValidation.value ?: ValidationResult.Success,
            _emailValidation.value ?: ValidationResult.Success
        )
    }

    /**
     * Updates the dirty state by comparing current values with original contact.
     */
    private fun updateDirtyState() {
        val original = originalContact
        val isDirty = if (original != null) {
            // Editing existing contact - check if changed
            _name.value?.trim() != original.name ||
                    _phone.value?.trim() != original.phone ||
                    _email.value?.trim()?.takeIf { it.isNotEmpty() } != original.email ||
                    _address.value?.trim()?.takeIf { it.isNotEmpty() } != original.address ||
                    _dateOfBirth.value != original.dateOfBirth ||
                    _avatarId.value != original.avatarId ||
                    _avatarUri.value != original.avatarUri
        } else {
            // Creating new contact - dirty if any field has content
            !_name.value.isNullOrBlank() ||
                    !_phone.value.isNullOrBlank() ||
                    !_email.value.isNullOrBlank() ||
                    !_address.value.isNullOrBlank() ||
                    _dateOfBirth.value != null ||
                    _avatarId.value != null ||
                    _avatarUri.value != null
        }

        _isDirty.value = isDirty
        savedStateHandle[KEY_IS_DIRTY] = isDirty
    }

    /**
     * Restores state from SavedStateHandle.
     */
    private fun restoreState() {
        _name.value = savedStateHandle.get<String>(KEY_NAME) ?: ""
        _phone.value = savedStateHandle.get<String>(KEY_PHONE) ?: ""
        _email.value = savedStateHandle.get<String>(KEY_EMAIL) ?: ""
        _address.value = savedStateHandle.get<String>(KEY_ADDRESS) ?: ""
        _dateOfBirth.value = savedStateHandle.get<Long?>(KEY_DATE_OF_BIRTH)
        _avatarId.value = savedStateHandle.get<Int?>(KEY_AVATAR_ID)
        _avatarUri.value = savedStateHandle.get<String?>(KEY_AVATAR_URI)
        _isDirty.value = savedStateHandle.get<Boolean>(KEY_IS_DIRTY) ?: false

        // Load contact if ID is saved
        savedStateHandle.get<Long>(KEY_CONTACT_ID)?.let { contactId ->
            if (contactId > 0) {
                loadContact(contactId)
            }
        }

        // Validate restored state if any fields have content
        if (!_name.value.isNullOrEmpty() || !_phone.value.isNullOrEmpty()) {
            validateAllFields()
        }
    }

    /**
     * Resets save result (for one-time event consumption).
     */
    fun resetSaveResult() {
        _saveResult.value = null
    }
}
