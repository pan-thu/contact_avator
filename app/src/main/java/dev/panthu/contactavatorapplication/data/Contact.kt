package dev.panthu.contactavatorapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.panthu.contactavatorapplication.R

@Entity(
    tableName = "contacts",
    indices = [androidx.room.Index(value = ["name"])]
)
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "email")
    val email: String? = null,

    @ColumnInfo(name = "address")
    val address: String? = null,

    @ColumnInfo(name = "date_of_birth")
    val dateOfBirth: Long? = null,

    @ColumnInfo(name = "avatar_id")
    val avatarId: Int? = null,

    @ColumnInfo(name = "avatar_uri")
    val avatarUri: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Returns the avatar resource ID to use for displaying this contact.
     * If a custom avatar resource ID is set, returns that.
     * If a custom URI is set, returns null (caller should load from URI).
     * Otherwise, returns the default avatar resource.
     */
    fun getAvatarResourceId(): Int? {
        return when {
            avatarId != null -> avatarId
            avatarUri != null -> null // Use URI instead
            else -> R.drawable.avatar_default
        }
    }

    /**
     * Returns true if this contact has a custom avatar (either resource or URI).
     */
    fun hasCustomAvatar(): Boolean {
        return avatarId != null || avatarUri != null
    }

    /**
     * Returns the display avatar resource ID with guaranteed default fallback.
     */
    fun getDisplayAvatarResourceId(): Int {
        return avatarId ?: R.drawable.avatar_default
    }
}
