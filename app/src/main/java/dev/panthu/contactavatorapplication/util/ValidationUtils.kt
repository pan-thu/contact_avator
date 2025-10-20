package dev.panthu.contactavatorapplication.util

import android.util.Patterns
import dev.panthu.contactavatorapplication.R

/**
 * Sealed class representing validation results.
 */
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val messageResId: Int) : ValidationResult()

    val isValid: Boolean
        get() = this is Success
}

/**
 * Utility object for form field validation.
 */
object ValidationUtils {

    private const val MAX_NAME_LENGTH = 100
    private const val MIN_PHONE_LENGTH = 7
    private const val MAX_PHONE_LENGTH = 20

    /**
     * Validates a name field.
     * Requirements:
     * - Not empty
     * - Not blank (only whitespace)
     * - Reasonable length (1-100 characters)
     */
    fun validateName(name: String?): ValidationResult {
        return when {
            name.isNullOrBlank() -> ValidationResult.Error(R.string.error_name_required)
            name.trim().isEmpty() -> ValidationResult.Error(R.string.error_name_empty)
            name.length > MAX_NAME_LENGTH -> ValidationResult.Error(R.string.error_name_too_long)
            else -> ValidationResult.Success
        }
    }

    /**
     * Validates a phone number field.
     * Requirements:
     * - Not empty
     * - Contains only digits, spaces, +, -, (, )
     * - Reasonable length (7-20 characters)
     */
    fun validatePhone(phone: String?): ValidationResult {
        if (phone.isNullOrBlank()) {
            return ValidationResult.Error(R.string.error_phone_required)
        }

        val cleanedPhone = phone.replace(Regex("[\\s\\-()]+"), "")

        return when {
            cleanedPhone.length < MIN_PHONE_LENGTH -> ValidationResult.Error(R.string.error_phone_invalid)
            cleanedPhone.length > MAX_PHONE_LENGTH -> ValidationResult.Error(R.string.error_phone_invalid)
            !cleanedPhone.matches(Regex("^\\+?[0-9]+$")) -> ValidationResult.Error(R.string.error_phone_invalid)
            else -> ValidationResult.Success
        }
    }

    /**
     * Validates an email field.
     * Requirements:
     * - Optional (empty is valid)
     * - If provided, must match email pattern
     */
    fun validateEmail(email: String?): ValidationResult {
        // Email is optional
        if (email.isNullOrBlank()) {
            return ValidationResult.Success
        }

        return if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(R.string.error_email_invalid)
        }
    }

    /**
     * Validates an address field.
     * Requirements:
     * - Optional (no validation needed, always valid)
     */
    fun validateAddress(address: String?): ValidationResult {
        // Address is completely optional and has no format requirements
        return ValidationResult.Success
    }

    /**
     * Checks if all required fields are valid for saving.
     */
    fun isFormValid(
        nameResult: ValidationResult,
        phoneResult: ValidationResult,
        emailResult: ValidationResult
    ): Boolean {
        return nameResult.isValid && phoneResult.isValid && emailResult.isValid
    }
}
