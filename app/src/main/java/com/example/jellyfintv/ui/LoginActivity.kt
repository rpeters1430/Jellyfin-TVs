package com.example.jellyfintv.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.jellyfintv.R
import com.example.jellyfintv.api.JellyfinApi
import com.example.jellyfintv.data.preferences.PreferencesManager
import com.example.jellyfintv.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginActivity : FragmentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val jellyfinApi: JellyfinApi by inject()
    private val preferencesManager: PreferencesManager by inject()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Auto-fill server URL if we have one
        preferencesManager.serverUrl?.let { url ->
            binding.serverUrlEditText.setText(url)
        }
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val serverUrl = binding.serverUrlEditText.text.toString().trim()
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            
            if (serverUrl.isBlank() || username.isBlank() || password.isBlank()) {
                showError("Please fill in all fields")
                return@setOnClickListener
            }
            
            connectToServer(serverUrl, username, password)
        }
        
        binding.skipButton.setOnClickListener {
            // Skip login for demo purposes
            startActivity(MainActivity.createIntent(this))
            finish()
        }
    }
    
    private fun connectToServer(serverUrl: String, username: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
        
        lifecycleScope.launch {
            val result = jellyfinApi.connectToServer(serverUrl, username, password)
            
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                binding.loginButton.isEnabled = true
                
                result.onSuccess {
                    // Login successful, proceed to main activity
                    startActivity(MainActivity.createIntent(this@LoginActivity))
                    finish()
                }.onFailure { exception ->
                    showError("Login failed: ${exception.message}")
                }
            }
        }
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    companion object {
        fun createIntent(context: android.content.Context) =
            android.content.Intent(context, LoginActivity::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }
    }
}
