package dev.panthu.contactavatorapplication.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Manager for app preferences using SharedPreferences.
 * Handles persistent storage of user settings like sort order.
 */
class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * Sort order enum matching the ViewModel.
     */
    enum class SortOrder {
        NAME_ASC,
        NAME_DESC,
        RECENTLY_ADDED
    }

    /**
     * Get the current sort order preference.
     */
    fun getSortOrder(): SortOrder {
        val value = prefs.getString(KEY_SORT_ORDER, SortOrder.NAME_ASC.name)
        return try {
            SortOrder.valueOf(value ?: SortOrder.NAME_ASC.name)
        } catch (e: IllegalArgumentException) {
            SortOrder.NAME_ASC
        }
    }

    /**
     * Save the sort order preference.
     */
    fun setSortOrder(sortOrder: SortOrder) {
        prefs.edit().putString(KEY_SORT_ORDER, sortOrder.name).apply()
    }

    companion object {
        private const val PREFS_NAME = "contact_avatar_prefs"
        private const val KEY_SORT_ORDER = "sort_order"

        @Volatile
        private var INSTANCE: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
}
