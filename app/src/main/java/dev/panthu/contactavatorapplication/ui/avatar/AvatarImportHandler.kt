package dev.panthu.contactavatorapplication.ui.avatar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 * Utility class to handle custom avatar import from device gallery.
 * Features:
 * - Activity Result API integration
 * - URI permission persistence (FLAG_GRANT_READ_URI_PERMISSION)
 * - Image accessibility validation
 * - Fallback to default avatar on errors
 * - Proper lifecycle management
 */
class AvatarImportHandler(
    private val fragment: Fragment,
    private val onImageSelected: (Uri?) -> Unit,
    private val onError: ((Exception) -> Unit)? = null
) {

    private var imagePickerLauncher: ActivityResultLauncher<Intent>? = null

    /**
     * Initialize the activity result launcher.
     * Must be called before the fragment is in STARTED state (typically in onCreate/onCreateView).
     */
    fun initialize() {
        imagePickerLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            handleImagePickerResult(result.resultCode, result.data)
        }
    }

    /**
     * Launch the image picker to select a custom avatar.
     */
    fun launchImagePicker() {
        if (imagePickerLauncher == null) {
            throw IllegalStateException(
                "AvatarImportHandler must be initialized before launching picker"
            )
        }

        try {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                // Request only image types
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png", "image/jpg"))
                // Add pick images intent for Android 11+
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    putExtra(Intent.ACTION_GET_CONTENT, true)
                }
            }

            imagePickerLauncher?.launch(intent)
        } catch (e: Exception) {
            onError?.invoke(e) ?: run {
                // Default error handling
                onImageSelected(null)
            }
        }
    }

    /**
     * Handle the result from the image picker.
     */
    private fun handleImagePickerResult(resultCode: Int, data: Intent?) {
        if (resultCode != android.app.Activity.RESULT_OK) {
            // User cancelled or error occurred
            onImageSelected(null)
            return
        }

        val selectedUri = data?.data
        if (selectedUri == null) {
            onError?.invoke(Exception("No image URI returned"))
            onImageSelected(null)
            return
        }

        try {
            // Validate that we can access the image
            if (!validateImageAccessibility(selectedUri)) {
                onError?.invoke(Exception("Cannot access selected image"))
                onImageSelected(null)
                return
            }

            // Request persistent URI permissions
            persistUriPermissions(selectedUri)

            // Return the valid URI
            onImageSelected(selectedUri)
        } catch (e: Exception) {
            onError?.invoke(e)
            onImageSelected(null)
        }
    }

    /**
     * Validate that the selected image URI is accessible.
     */
    private fun validateImageAccessibility(uri: Uri): Boolean {
        return try {
            val context = fragment.requireContext()
            context.contentResolver.openInputStream(uri)?.use { stream ->
                // Try to read first byte to ensure accessibility
                stream.read() != -1
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Request and persist URI read permissions.
     */
    private fun persistUriPermissions(uri: Uri) {
        try {
            val context = fragment.requireContext()
            val contentResolver = context.contentResolver

            // Take persistable URI permission
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: SecurityException) {
            // Some content providers don't support persistable permissions
            // The URI should still work for the current session
            e.printStackTrace()
        }
    }

    /**
     * Release URI permissions when no longer needed.
     */
    fun releaseUriPermissions(uri: Uri) {
        try {
            val context = fragment.requireContext()
            context.contentResolver.releasePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: Exception) {
            // Permission may not have been granted or already released
            e.printStackTrace()
        }
    }

    /**
     * Check if an image picker app is available on the device.
     */
    fun isImagePickerAvailable(): Boolean {
        return try {
            val context = fragment.requireContext()
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            val packageManager = context.packageManager
            intent.resolveActivity(packageManager) != null
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        /**
         * Create an intent for picking images from gallery.
         */
        fun createImagePickerIntent(): Intent {
            return Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
            }
        }

        /**
         * Check if a URI is still accessible.
         */
        fun isUriAccessible(context: Context, uri: Uri): Boolean {
            return try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    stream.read() != -1
                } ?: false
            } catch (e: Exception) {
                false
            }
        }
    }
}
