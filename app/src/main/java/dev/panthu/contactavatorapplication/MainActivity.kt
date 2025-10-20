package dev.panthu.contactavatorapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dev.panthu.contactavatorapplication.databinding.ActivityMainBinding

/**
 * Main Activity for ContactAvatar+ application.
 * Uses traditional Android Views with Navigation Component and View Binding.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar as action bar
        setSupportActionBar(binding.toolbar)

        // Note: Navigation will be fully set up in Phase 4 when fragments are created
        // For now, we just have the basic layout structure ready
    }

    override fun onSupportNavigateUp(): Boolean {
        // Navigation back button support - will be implemented in Phase 4
        return super.onSupportNavigateUp()
    }
}