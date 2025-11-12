package dev.panthu.contactavatorapplication.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import dev.panthu.contactavatorapplication.R
import kotlinx.coroutines.CoroutineExceptionHandler
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Centralized error handling utility for ContactAvatar+ application.
 *
 * Features:
 * - Unified error logging and reporting
 * - User-friendly error messages
 * - Categorized error types for different handling strategies
 * - Coroutine exception handling support
 * - Recovery mechanism suggestions
 *
 * Usage:
 * ```kotlin
 * try {
 *     // risky operation
 * } catch (e: Exception) {
 *     ErrorHandler.handleError(context, e, ErrorType.DATABASE)
 * }
 * ```
 */
object ErrorHandler {

    private const val TAG = "ContactAvatar"

    /**
     * Error categories for different handling strategies.
     */
    enum class ErrorType {
        DATABASE,
        NETWORK,
        VALIDATION,
        FILE_IO,
        PERMISSION,
        UNKNOWN
    }

    /**
     * Error severity levels.
     */
    enum class ErrorSeverity {
        LOW,      // Non-critical, user can continue
        MEDIUM,   // Feature degradation, user should know
        HIGH,     // Operation failed, user must take action
        CRITICAL  // App state compromised, requires restart
    }

    /**
     * Represents a handled error with context and recovery information.
     */
    data class HandledError(
        val type: ErrorType,
        val severity: ErrorSeverity,
        val message: String,
        val exception: Throwable?,
        val userMessage: String,
        val recoveryAction: String?
    )

    /**
     * Handle an error with automatic categorization and user notification.
     *
     * @param context Android context for showing messages
     * @param error The exception that occurred
     * @param type Optional error type override
     * @param showToast Whether to show a toast message to user
     * @return HandledError containing error details and recovery information
     */
    fun handleError(
        context: Context,
        error: Throwable,
        type: ErrorType? = null,
        showToast: Boolean = true
    ): HandledError {
        val errorType = type ?: categorizeError(error)
        val handled = processError(context, error, errorType)

        // Log the error
        logError(handled)

        // Show user notification if requested
        if (showToast) {
            showErrorToast(context, handled.userMessage)
        }

        return handled
    }

    /**
     * Handle an error with a custom user message.
     */
    fun handleError(
        context: Context,
        error: Throwable,
        @StringRes userMessageResId: Int,
        type: ErrorType? = null,
        showToast: Boolean = true
    ): HandledError {
        val errorType = type ?: categorizeError(error)
        val userMessage = context.getString(userMessageResId)
        val handled = processError(context, error, errorType, userMessage)

        logError(handled)

        if (showToast) {
            showErrorToast(context, handled.userMessage)
        }

        return handled
    }

    /**
     * Create a CoroutineExceptionHandler that uses this error handler.
     */
    fun createCoroutineExceptionHandler(
        context: Context,
        type: ErrorType = ErrorType.UNKNOWN
    ): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            handleError(context, throwable, type)
        }
    }

    /**
     * Categorize an error based on its type.
     */
    private fun categorizeError(error: Throwable): ErrorType {
        return when (error) {
            is SQLException,
            is android.database.sqlite.SQLiteException -> ErrorType.DATABASE

            is IOException,
            is SocketTimeoutException,
            is UnknownHostException -> ErrorType.NETWORK

            is IllegalArgumentException,
            is IllegalStateException -> ErrorType.VALIDATION

            is SecurityException -> ErrorType.PERMISSION

            else -> ErrorType.UNKNOWN
        }
    }

    /**
     * Process an error and create a HandledError object.
     */
    private fun processError(
        context: Context,
        error: Throwable,
        type: ErrorType,
        customUserMessage: String? = null
    ): HandledError {
        val severity = determineSeverity(error, type)
        val userMessage = customUserMessage ?: getUserMessage(context, error, type)
        val recoveryAction = getRecoveryAction(context, type)

        return HandledError(
            type = type,
            severity = severity,
            message = error.message ?: "Unknown error",
            exception = error,
            userMessage = userMessage,
            recoveryAction = recoveryAction
        )
    }

    /**
     * Determine error severity based on exception and type.
     */
    private fun determineSeverity(error: Throwable, type: ErrorType): ErrorSeverity {
        return when (type) {
            ErrorType.DATABASE -> ErrorSeverity.HIGH
            ErrorType.NETWORK -> ErrorSeverity.MEDIUM
            ErrorType.VALIDATION -> ErrorSeverity.LOW
            ErrorType.FILE_IO -> ErrorSeverity.MEDIUM
            ErrorType.PERMISSION -> ErrorSeverity.HIGH
            ErrorType.UNKNOWN -> ErrorSeverity.MEDIUM
        }
    }

    /**
     * Get user-friendly error message.
     */
    private fun getUserMessage(context: Context, error: Throwable, type: ErrorType): String {
        return when (type) {
            ErrorType.DATABASE -> context.getString(R.string.error_saving_contact)
            ErrorType.NETWORK -> context.getString(R.string.error_generic)
            ErrorType.VALIDATION -> error.message ?: context.getString(R.string.error_generic)
            ErrorType.FILE_IO -> context.getString(R.string.error_avatar_load)
            ErrorType.PERMISSION -> "Permission denied. Please grant required permissions."
            ErrorType.UNKNOWN -> context.getString(R.string.error_generic)
        }
    }

    /**
     * Get recovery action suggestion.
     */
    private fun getRecoveryAction(context: Context, type: ErrorType): String? {
        return when (type) {
            ErrorType.DATABASE -> "Try restarting the app or clearing app data."
            ErrorType.NETWORK -> "Check your internet connection and try again."
            ErrorType.VALIDATION -> "Please check your input and try again."
            ErrorType.FILE_IO -> "Try selecting a different image."
            ErrorType.PERMISSION -> "Grant permissions in app settings."
            ErrorType.UNKNOWN -> "Try again or contact support if the issue persists."
        }
    }

    /**
     * Log error with appropriate level.
     */
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
            ErrorSeverity.HIGH, ErrorSeverity.CRITICAL -> Log.e(TAG, logMessage, handled.exception)
        }
    }

    /**
     * Show error toast to user.
     */
    private fun showErrorToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Log an info message.
     */
    fun logInfo(message: String) {
        Log.i(TAG, message)
    }

    /**
     * Log a warning message.
     */
    fun logWarning(message: String, error: Throwable? = null) {
        Log.w(TAG, message, error)
    }

    /**
     * Log a debug message.
     */
    fun logDebug(message: String) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, message)
        }
    }
}

/**
 * Fake SQLException for compilation (Room handles this internally).
 */
private class SQLException(message: String) : Exception(message)
