package dev.panthu.contactavatorapplication.util

import android.util.Patterns
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.NumberParseException
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
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

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
     * Validates a phone number field using Google's libphonenumber.
     * Requirements:
     * - Not empty
     * - Must include country code with + prefix (e.g., +66804896430)
     * - Must be a valid phone number format for the country
     * - Validates against international phone number standards
     */
    fun validatePhone(phone: String?): ValidationResult {
        if (phone.isNullOrBlank()) {
            return ValidationResult.Error(R.string.error_phone_required)
        }

        val trimmedPhone = phone.trim()

        // Check if it starts with + (country code required)
        if (!trimmedPhone.startsWith("+")) {
            return ValidationResult.Error(R.string.error_phone_country_code_required)
        }

        return try {
            // Parse the phone number without a default region
            // The number must include the country code
            val phoneNumber = phoneNumberUtil.parse(trimmedPhone, null)

            // Check if the number is valid
            if (phoneNumberUtil.isValidNumber(phoneNumber)) {
                ValidationResult.Success
            } else {
                ValidationResult.Error(R.string.error_phone_invalid)
            }
        } catch (e: NumberParseException) {
            // Invalid phone number format
            when (e.errorType) {
                NumberParseException.ErrorType.INVALID_COUNTRY_CODE ->
                    ValidationResult.Error(R.string.error_phone_invalid_country_code)
                NumberParseException.ErrorType.NOT_A_NUMBER ->
                    ValidationResult.Error(R.string.error_phone_invalid)
                NumberParseException.ErrorType.TOO_SHORT_NSN,
                NumberParseException.ErrorType.TOO_SHORT_AFTER_IDD ->
                    ValidationResult.Error(R.string.error_phone_too_short)
                NumberParseException.ErrorType.TOO_LONG ->
                    ValidationResult.Error(R.string.error_phone_too_long)
                else ->
                    ValidationResult.Error(R.string.error_phone_invalid)
            }
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
