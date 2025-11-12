package dev.panthu.contactavatorapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for storing contacts with avatar support.
 * Version 1: Initial schema with Contact entity including avatar fields.
 * Version 2: Added index on name field for optimized search performance.
 * Version 3: Added dateOfBirth field to Contact entity.
 *
 * Schema files are exported to app/schemas/ directory for version tracking.
 */
@Database(
    entities = [Contact::class],
    version = 3,
    exportSchema = true
)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        /**
         * Gets the singleton database instance.
         * Uses double-checked locking pattern for thread safety.
         */
        fun getDatabase(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_database"
                )
                    .fallbackToDestructiveMigration() // For development; remove in production
                    .build()

                INSTANCE = instance
                instance
            }
        }

        /**
         * For testing purposes only - clears the database instance.
         */
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
