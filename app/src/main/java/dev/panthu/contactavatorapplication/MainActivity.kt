package dev.panthu.contactavatorapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.panthu.contactavatorapplication.databinding.ActivityMainBinding

/**
 * Main Activity for ContactAvatar+ application.
 * Uses traditional Android Views with Navigation Component and View Binding.
 * Each fragment manages its own toolbar for maximum flexibility.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Note: Fragments manage their own toolbars
        // No need to set up action bar here
    }

    override fun onSupportNavigateUp(): Boolean {
        // Navigation back button support - will be implemented in Phase 4
        return super.onSupportNavigateUp()
    }
}