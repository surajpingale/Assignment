package com.example.assignment.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navHost: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHost = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)
                as NavHostFragment

        appBarConfiguration = AppBarConfiguration.Builder(navHost.navController.graph).build()
        // for set up fragment label as title to toolbar
        setupActionBarWithNavController(navHost.navController, appBarConfiguration)
    }

    /**
     * fun for back arrow except home fragment
     */
    override fun onSupportNavigateUp(): Boolean {
        return navHost.navController.navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}