# Phase 5: Error Handling & Recovery Guide

**Project**: ContactAvatar+ Android Application
**Date**: 2025-10-21
**Purpose**: Comprehensive error handling strategy and implementation guide

---

## Executive Summary

This document describes the centralized error handling system implemented in ContactAvatar+, covering error categorization, user feedback, recovery mechanisms, and logging strategies.

**Status**: ✅ COMPREHENSIVE ERROR HANDLING IMPLEMENTED

---

## Error Handling Architecture

### Centralized Error Handler

**File**: `app/src/main/java/dev/panthu/contactavatorapplication/util/ErrorHandler.kt`

A singleton utility class providing:
- Unified error logging and reporting
- User-friendly error messages
- Categorized error types for different handling strategies
- Coroutine exception handling support
- Recovery mechanism suggestions

---

## Error Categories

### 1. Database Errors (ErrorType.DATABASE)

**Common Scenarios**:
- Failed to save contact
- Failed to load contact
- Failed to delete contact
- Database connection issues
- Constraint violations

**Severity**: HIGH

**User Message**: "Error saving contact" / "Error loading contact"

**Recovery Actions**:
- Retry operation
- Restart app
- Clear app data (last resort)

**Example Implementation**:
```kotlin
try {
    contactRepository.insertContact(contact)
} catch (e: Exception) {
    ErrorHandler.handleError(
        context,
        e,
        ErrorType.DATABASE,
        showToast = true
    )
}
```

**Logging**:
```
[DATABASE] [HIGH] Failed to insert contact: SQLiteConstraintException
Recovery: Try restarting the app or clearing app data.
```

---

### 2. Network Errors (ErrorType.NETWORK)

**Common Scenarios**:
- No internet connection
- Timeout errors
- Failed to fetch remote data (if applicable)

**Severity**: MEDIUM

**User Message**: "Something went wrong"

**Recovery Actions**:
- Check internet connection
- Retry operation
- Use cached data

**Example Implementation**:
```kotlin
try {
    // Network operation (if applicable)
} catch (e: IOException) {
    ErrorHandler.handleError(
        context,
        e,
        ErrorType.NETWORK
    )
}
```

---

### 3. Validation Errors (ErrorType.VALIDATION)

**Common Scenarios**:
- Invalid phone number format
- Invalid email format
- Required field empty
- Input too long

**Severity**: LOW

**User Message**: Specific validation error (e.g., "Name is required")

**Recovery Actions**:
- Correct input based on error message
- Follow input format guidelines

**Example Implementation**:
```kotlin
// ValidationUtils.kt
when (val result = ValidationUtils.validateName(name)) {
    is ValidationResult.Error -> {
        binding.nameInputLayout.error = getString(result.errorResId)
    }
    is ValidationResult.Success -> {
        binding.nameInputLayout.error = null
    }
}
```

**Validation Rules**:
- **Name**: Required, max 100 characters
- **Phone**: Required, valid phone format
- **Email**: Optional, valid email format if provided
- **Address**: Optional, max 500 characters

---

### 4. File I/O Errors (ErrorType.FILE_IO)

**Common Scenarios**:
- Failed to load custom avatar image
- Failed to read image file
- Image too large
- Corrupt image file
- Permission denied

**Severity**: MEDIUM

**User Message**: "Could not load avatar"

**Recovery Actions**:
- Try selecting different image
- Check file permissions
- Reduce image size

**Example Implementation**:
```kotlin
try {
    val uri = result.data?.data
    avatarView.setAvatarUri(uri)
} catch (e: IOException) {
    ErrorHandler.handleError(
        context,
        e,
        ErrorType.FILE_IO
    )
}
```

---

### 5. Permission Errors (ErrorType.PERMISSION)

**Common Scenarios**:
- Camera permission denied
- Storage permission denied
- Location permission denied (for address)

**Severity**: HIGH

**User Message**: "Permission denied. Please grant required permissions."

**Recovery Actions**:
- Grant permissions in app settings
- Request permission again
- Use alternative feature

**Example Implementation**:
```kotlin
when {
    ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED -> {
        // Permission granted
    }
    else -> {
        ErrorHandler.handleError(
            context,
            SecurityException("Storage permission denied"),
            ErrorType.PERMISSION
        )
    }
}
```

---

### 6. Unknown Errors (ErrorType.UNKNOWN)

**Common Scenarios**:
- Unexpected exceptions
- System errors
- Edge cases

**Severity**: MEDIUM

**User Message**: "Something went wrong"

**Recovery Actions**:
- Try again
- Restart app
- Contact support if persists

---

## Error Handling Patterns

### 1. Try-Catch with Error Handler

**Standard Pattern**:
```kotlin
try {
    // Risky operation
    contactRepository.deleteContact(contactId)
} catch (e: Exception) {
    ErrorHandler.handleError(
        requireContext(),
        e,
        ErrorType.DATABASE
    )
}
```

**Custom Message**:
```kotlin
try {
    contactRepository.updateContact(contact)
} catch (e: Exception) {
    ErrorHandler.handleError(
        requireContext(),
        e,
        R.string.error_updating_contact,
        ErrorType.DATABASE
    )
}
```

---

### 2. Coroutine Exception Handling

**ViewModel Pattern**:
```kotlin
class ContactListViewModel(
    application: Application,
    private val repository: ContactRepository
) : AndroidViewModel(application) {

    fun deleteContact(contactId: Long) {
        viewModelScope.launch(
            ErrorHandler.createCoroutineExceptionHandler(
                getApplication(),
                ErrorType.DATABASE
            )
        ) {
            repository.deleteContact(contactId)
        }
    }
}
```

**Result Wrapper** (Alternative):
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}

suspend fun saveContact(contact: Contact): Result<Long> {
    return try {
        val id = repository.insertContact(contact)
        Result.Success(id)
    } catch (e: Exception) {
        Result.Error(e)
    }
}
```

---

### 3. LiveData Error States

**Error State Pattern**:
```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// ViewModel
private val _uiState = MutableLiveData<UiState<Contact>>()
val uiState: LiveData<UiState<Contact>> = _uiState

fun loadContact(id: Long) {
    _uiState.value = UiState.Loading
    viewModelScope.launch {
        try {
            val contact = repository.getContact(id)
            _uiState.value = UiState.Success(contact)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message ?: "Unknown error")
            ErrorHandler.handleError(
                getApplication(),
                e,
                ErrorType.DATABASE
            )
        }
    }
}

// Fragment
viewModel.uiState.observe(viewLifecycleOwner) { state ->
    when (state) {
        is UiState.Loading -> showLoading()
        is UiState.Success -> showContent(state.data)
        is UiState.Error -> showError(state.message)
    }
}
```

---

## User Feedback Mechanisms

### 1. Toast Messages

**Quick Feedback**:
```kotlin
Toast.makeText(context, R.string.contact_saved, Toast.LENGTH_SHORT).show()
```

**Error Toast** (via ErrorHandler):
```kotlin
ErrorHandler.handleError(context, exception, showToast = true)
```

**When to Use**:
- Success confirmations
- Simple errors
- Non-critical notifications
- Quick feedback

---

### 2. Snackbar Messages

**Actionable Feedback**:
```kotlin
Snackbar.make(binding.root, "Contact deleted", Snackbar.LENGTH_LONG)
    .setAction("UNDO") {
        // Undo delete
    }
    .show()
```

**When to Use**:
- Undoable actions
- Contextual messages
- Actions with retry option
- Longer messages

---

### 3. Dialog Messages

**Critical Errors**:
```kotlin
MaterialAlertDialogBuilder(requireContext())
    .setTitle("Error")
    .setMessage("Failed to save contact. Please try again.")
    .setPositiveButton("Retry") { _, _ ->
        retryOperation()
    }
    .setNegativeButton("Cancel", null)
    .show()
```

**When to Use**:
- Critical errors requiring action
- Errors preventing further operation
- Detailed error information
- Multiple recovery options

---

### 4. Inline Error Messages

**Form Validation**:
```xml
<TextInputLayout
    app:errorEnabled="true">

    <TextInputEditText />
</TextInputLayout>
```

```kotlin
binding.nameInputLayout.error = getString(R.string.error_name_required)
```

**When to Use**:
- Form validation errors
- Field-specific errors
- Real-time feedback
- Input requirements

---

### 5. Loading States

**Progress Indicators**:
```xml
<FrameLayout
    android:id="@+id/loadingOverlay"
    android:visibility="gone">

    <CircularProgressIndicator
        android:indeterminate="true" />
</FrameLayout>
```

```kotlin
fun showLoading() {
    binding.loadingOverlay.visibility = View.VISIBLE
}

fun hideLoading() {
    binding.loadingOverlay.visibility = View.GONE
}
```

---

### 6. Empty States

**No Content State**:
```xml
<LinearLayout
    android:id="@+id/emptyStateView"
    android:visibility="gone">

    <ImageView android:src="@drawable/ic_person" />
    <TextView android:text="@string/contacts_empty" />
    <TextView android:text="@string/contacts_empty_description" />
</LinearLayout>
```

---

## Error Recovery Strategies

### 1. Automatic Retry

**Transient Errors**:
```kotlin
suspend fun saveContactWithRetry(
    contact: Contact,
    maxRetries: Int = 3
): Result<Long> {
    var lastException: Exception? = null

    repeat(maxRetries) { attempt ->
        try {
            return Result.Success(repository.insertContact(contact))
        } catch (e: Exception) {
            lastException = e
            if (attempt < maxRetries - 1) {
                delay(1000 * (attempt + 1)) // Exponential backoff
            }
        }
    }

    return Result.Error(lastException!!)
}
```

---

### 2. Graceful Degradation

**Fallback Behavior**:
```kotlin
fun loadAvatar(contact: Contact) {
    when {
        contact.avatarUri != null -> {
            try {
                avatarView.setAvatarUri(contact.avatarUri)
            } catch (e: Exception) {
                // Fallback to resource avatar
                avatarView.setAvatarResource(R.drawable.avatar_default)
                ErrorHandler.logWarning("Failed to load custom avatar", e)
            }
        }
        contact.avatarId != null -> {
            avatarView.setAvatarResource(contact.avatarId)
        }
        else -> {
            avatarView.setAvatarResource(R.drawable.avatar_default)
        }
    }
}
```

---

### 3. User-Initiated Retry

**Retry Button**:
```kotlin
Snackbar.make(binding.root, "Failed to load contact", Snackbar.LENGTH_INDEFINITE)
    .setAction("RETRY") {
        viewModel.loadContact(contactId)
    }
    .show()
```

---

### 4. State Preservation

**Save State on Error**:
```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)

    // Save form state in case of error
    outState.putString("name", binding.nameEditText.text.toString())
    outState.putString("phone", binding.phoneEditText.text.toString())
    // ... save other fields
}
```

---

## Logging Strategy

### 1. Error Logging

**Automatic Logging** (via ErrorHandler):
```kotlin
private fun logError(handled: HandledError) {
    val logMessage = buildString {
        append("[${handled.type}] ")
        append("[${handled.severity}] ")
        append(handled.message)
        if (handled.recoveryAction != null) {
            append(" | Recovery: ${handled.recoveryAction}")
        }
    }

    when (handled.severity) {
        ErrorSeverity.LOW -> Log.d(TAG, logMessage, handled.exception)
        ErrorSeverity.MEDIUM -> Log.w(TAG, logMessage, handled.exception)
        ErrorSeverity.HIGH,
        ErrorSeverity.CRITICAL -> Log.e(TAG, logMessage, handled.exception)
    }
}
```

---

### 2. Debug Logging

**Development Logging**:
```kotlin
if (BuildConfig.DEBUG) {
    ErrorHandler.logDebug("Loading contact with ID: $contactId")
}
```

---

### 3. Production Logging

**Crash Reporting** (Firebase Crashlytics - Optional):
```kotlin
try {
    // Operation
} catch (e: Exception) {
    FirebaseCrashlytics.getInstance().recordException(e)
    ErrorHandler.handleError(context, e)
}
```

---

## Error Scenarios & Responses

### Scenario 1: Database Write Failure

**Error**: Failed to save contact due to database error

**Detection**:
```kotlin
try {
    repository.insertContact(contact)
} catch (e: Exception) {
    // Handle error
}
```

**User Feedback**:
- Toast: "Error saving contact"
- Log: [DATABASE] [HIGH] error details

**Recovery**:
- Retry save operation
- Check database integrity
- Clear app cache if persists

---

### Scenario 2: Invalid Input

**Error**: User enters invalid phone number format

**Detection**:
```kotlin
when (val result = ValidationUtils.validatePhone(phone)) {
    is ValidationResult.Error -> {
        // Show inline error
    }
}
```

**User Feedback**:
- Inline error: "Invalid phone number format"
- TextInputLayout error styling

**Recovery**:
- User corrects input
- Format hint provided

---

### Scenario 3: Custom Avatar Load Failure

**Error**: Selected image file cannot be loaded

**Detection**:
```kotlin
avatarView.load(uri) {
    listener(
        onError = { _, _ ->
            // Handle error
        }
    )
}
```

**User Feedback**:
- Toast: "Could not load avatar"
- Fallback to default avatar

**Recovery**:
- Select different image
- Use default avatar
- Use resource avatar

---

### Scenario 4: Contact Not Found

**Error**: Attempting to load deleted or non-existent contact

**Detection**:
```kotlin
val contact = repository.getContact(id)
if (contact == null) {
    // Handle missing contact
}
```

**User Feedback**:
- Toast: "Contact not found"
- Navigate back to list

**Recovery**:
- Refresh contact list
- Remove stale data
- Return to previous screen

---

## Best Practices

### Do's ✅

1. **Always catch exceptions** in ViewModel and Repository layers
2. **Provide user-friendly messages** - avoid technical jargon
3. **Log all errors** with context and severity
4. **Offer recovery actions** when possible
5. **Use ErrorHandler** for consistency
6. **Test error scenarios** thoroughly
7. **Gracefully degrade** functionality on errors
8. **Preserve user input** on errors

### Don'ts ❌

1. **Never expose raw exceptions** to users
2. **Don't crash silently** - always log errors
3. **Don't show generic "error"** without context
4. **Don't block UI** with error dialogs
5. **Don't retry indefinitely** without user consent
6. **Don't lose user data** on errors
7. **Don't ignore validation** on client side
8. **Don't forget accessibility** in error messages

---

## Testing Error Handling

### Unit Tests

```kotlin
@Test
fun `handleError returns correct error type for SQLException`() {
    val exception = SQLException("Database error")
    val handled = ErrorHandler.handleError(
        context,
        exception,
        showToast = false
    )

    assertEquals(ErrorType.DATABASE, handled.type)
    assertEquals(ErrorSeverity.HIGH, handled.severity)
}
```

### Integration Tests

```kotlin
@Test
fun `saving invalid contact shows error message`() {
    // Arrange
    val invalidContact = Contact(name = "", phone = "invalid")

    // Act
    onView(withId(R.id.btnSave)).perform(click())

    // Assert
    onView(withId(R.id.nameInputLayout))
        .check(matches(hasErrorText("Name is required")))
}
```

---

## Conclusion

The centralized error handling system provides:

✅ Consistent error handling across the app
✅ User-friendly error messages
✅ Comprehensive logging
✅ Recovery mechanisms
✅ Graceful degradation
✅ Production-ready error management

**Status**: PRODUCTION READY

**Recommendation**: Monitor error logs in production to identify and address common error patterns.
