package dev.panthu.contactavatorapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    /**
     * Returns a Flow of all contacts, ordered by name ascending.
     * The Flow will emit whenever the data changes.
     */
    @Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>

    /**
     * Returns a single contact by ID, or null if not found.
     */
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Long): Contact?

    /**
     * Inserts a new contact. Returns the ID of the newly inserted contact.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact): Long

    /**
     * Updates an existing contact.
     */
    @Update
    suspend fun update(contact: Contact)

    /**
     * Deletes a contact.
     */
    @Delete
    suspend fun delete(contact: Contact)

    /**
     * Deletes all contacts (useful for testing or reset).
     */
    @Query("DELETE FROM contacts")
    suspend fun deleteAll()

    /**
     * Returns the count of all contacts.
     */
    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactCount(): Int

    /**
     * Searches contacts by name or phone number.
     * Returns a Flow for real-time search results.
     */
    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchContacts(query: String): Flow<List<Contact>>
}
