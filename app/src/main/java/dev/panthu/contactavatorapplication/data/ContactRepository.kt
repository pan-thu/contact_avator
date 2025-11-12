package dev.panthu.contactavatorapplication.data

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import dev.panthu.contactavatorapplication.R
import kotlinx.coroutines.flow.Flow

/**
 * Repository layer for Contact data operations.
 * Provides abstraction over the DAO and adds business logic for avatar validation.
 */
class ContactRepository(context: Context) {

    private val contactDao: ContactDao = ContactDatabase.getDatabase(context).contactDao()
    private val applicationContext: Context = context.applicationContext

    /**
     * Returns a Flow of all contacts.
     */
    fun getAllContacts(): Flow<List<Contact>> {
        return contactDao.getAllContacts()
    }

    /**
     * Returns a single contact by ID.
     */
    suspend fun getContactById(id: Long): Contact? {
        return contactDao.getContactById(id)
    }

    /**
     * Inserts a new contact with avatar validation.
     * Returns the ID of the newly inserted contact.
     */
    suspend fun insert(contact: Contact): Long {
        val validatedContact = validateAndFixAvatar(contact)
        return contactDao.insert(validatedContact)
    }

    /**
     * Updates an existing contact with avatar validation.
     */
    suspend fun update(contact: Contact) {
        val validatedContact = validateAndFixAvatar(contact)
        contactDao.update(validatedContact)
    }

    /**
     * Deletes a contact.
     */
    suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

    /**
     * Deletes all contacts.
     */
    suspend fun deleteAll() {
        contactDao.deleteAll()
    }

    /**
     * Returns the count of all contacts.
     */
    suspend fun getContactCount(): Int {
        return contactDao.getContactCount()
    }

    /**
     * Searches contacts by name or phone number.
     */
    fun searchContacts(query: String): Flow<List<Contact>> {
        return contactDao.searchContacts(query)
    }

    /**
     * Validates the avatar resource ID and URI, ensuring:
     * - If avatarId is set, it points to a valid drawable resource
     * - If avatarId is invalid, falls back to default avatar
     * - If avatarUri is set, checks if it's still accessible
     * - If avatarUri is inaccessible, clears it and uses default avatar
     * - Returns a copy of the contact with validated avatar
     */
    private fun validateAndFixAvatar(contact: Contact): Contact {
        var validatedAvatarId = contact.avatarId
        var validatedAvatarUri = contact.avatarUri

        // Validate avatar resource ID
        if (contact.avatarId != null) {
            if (!isValidDrawableResource(contact.avatarId)) {
                // Invalid resource ID, use default
                validatedAvatarId = R.drawable.avatar_default
            }
        }

        // Validate avatar URI accessibility
        if (contact.avatarUri != null) {
            if (!isUriAccessible(contact.avatarUri)) {
                // URI is no longer accessible, clear it and use default
                validatedAvatarUri = null
                validatedAvatarId = R.drawable.avatar_default
            }
        }

        // Return updated contact if anything changed
        return if (validatedAvatarId != contact.avatarId || validatedAvatarUri != contact.avatarUri) {
            contact.copy(avatarId = validatedAvatarId, avatarUri = validatedAvatarUri)
        } else {
            contact
        }
    }

    /**
     * Checks if a drawable resource ID is valid.
     */
    private fun isValidDrawableResource(resourceId: Int): Boolean {
        return try {
            ResourcesCompat.getDrawable(applicationContext.resources, resourceId, null)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks if a URI is still accessible.
     * Attempts to open an input stream to verify the URI can still be read.
     */
    private fun isUriAccessible(uriString: String): Boolean {
        return try {
            val uri = android.net.Uri.parse(uriString)
            applicationContext.contentResolver.openInputStream(uri)?.use { stream ->
                // Try to read first byte to ensure accessibility
                stream.read() != -1
            } ?: false
        } catch (e: Exception) {
            // URI is not accessible (permissions revoked, file deleted, etc.)
            false
        }
    }

    /**
     * Returns the array of available avatar resource IDs.
     */
    fun getAvailableAvatars(): IntArray {
        return applicationContext.resources.getIntArray(R.array.avatar_resources)
    }
}
