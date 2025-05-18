package com.example.jellyfintv.ui

import android.os.Bundle
import android.view.View
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.jellyfintv.R
import com.example.jellyfintv.api.JellyfinApi
import com.example.jellyfintv.data.preferences.PreferencesManager
import com.example.jellyfintv.databinding.ActivityMainBinding
import com.example.jellyfintv.ui.adapter.CardPresenter
import com.example.jellyfintv.ui.adapter.ImageCardViewAdapter
import com.example.jellyfintv.ui.model.MediaItem
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeActivity

class MainActivity : ScopeActivity() {
    private lateinit var binding: ActivityMainBinding
    private val jellyfinApi: JellyfinApi by inject()
    private val preferencesManager: PreferencesManager by inject()
    
    private val mainFragment = MainFragment()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, mainFragment)
                .commitNow()
        }
        
        // Check if user is logged in
        if (preferencesManager.accessToken.isNullOrEmpty()) {
            startActivity(LoginActivity.createIntent(this))
            finish()
            return
        }
        
        // Load data
        loadData()
    }
    
    private fun loadData() {
        lifecycleScope.launch {
            try {
                // Load libraries
                val libraries = jellyfinApi.getLibraries().getOrThrow()
                mainFragment.updateLibraries(libraries)
                
                // Load continue watching
                val continueWatching = jellyfinApi.getContinueWatching().getOrThrow()
                mainFragment.updateContinueWatching(continueWatching)
                
                // Load latest media
                val latestMedia = jellyfinApi.getLatestMedia().getOrThrow()
                mainFragment.updateLatestMedia(latestMedia)
                
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }
    
    companion object {
        fun createIntent(context: android.content.Context) =
            android.content.Intent(context, MainActivity::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
    }
}

class MainFragment : BrowseSupportFragment() {
    private val cardPresenter = CardPresenter()
    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    
    private var continueWatchingAdapter: ArrayObjectAdapter? = null
    private var librariesAdapter: ArrayObjectAdapter? = null
    private var latestMediaAdapter: ArrayObjectAdapter? = null
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUIElements()
        setupEventListeners()
        
        // Initialize adapters
        continueWatchingAdapter = ArrayObjectAdapter(cardPresenter).apply {
            setOnItemViewClickedListener { _, item, _, _ ->
                // Handle item click
                (item as? MediaItem)?.let { mediaItem ->
                    // Open media player
                    startActivity(
                        PlayerActivity.createIntent(
                            requireContext(),
                            mediaItem.id,
                            mediaItem.title
                        )
                    )
                }
            }
        }
        
        librariesAdapter = ArrayObjectAdapter(cardPresenter).apply {
            setOnItemViewClickedListener { _, item, _, _ ->
                // Handle library click
                (item as? MediaItem)?.let { mediaItem ->
                    // Open library browser
                    startActivity(
                        LibraryBrowserActivity.createIntent(
                            requireContext(),
                            mediaItem.id,
                            mediaItem.title
                        )
                    )
                }
            }
        }
        
        latestMediaAdapter = ArrayObjectAdapter(cardPresenter).apply {
            setOnItemViewClickedListener { _, item, _, _ ->
                // Handle latest media click
                (item as? MediaItem)?.let { mediaItem ->
                    startActivity(
                        PlayerActivity.createIntent(
                            requireContext(),
                            mediaItem.id,
                            mediaItem.title
                        )
                    )
                }
            }
        }
        
        // Add rows to the fragment
        rowsAdapter.add(ListRow(HeaderItem("Continue Watching"), continueWatchingAdapter!!))
        rowsAdapter.add(ListRow(HeaderItem("Your Libraries"), librariesAdapter!!))
        rowsAdapter.add(ListRow(HeaderItem("Latest Media"), latestMediaAdapter!!))
        
        adapter = rowsAdapter
    }
    
    private fun setupUIElements() {
        title = getString(R.string.app_name)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = requireContext().getColor(R.color.fastlane_background)
        
        // Set search icon color
        searchAffordanceColor = requireContext().getColor(R.color.search_opaque)
    }
    
    private fun setupEventListeners() {
        setOnSearchClickedListener {
            // Handle search click
        }
        
        setOnItemViewClickedListener { itemViewHolder, item, _, _ ->
            // Handle item click
        }
    }
    
    fun updateContinueWatching(items: List<MediaItem>) {
        continueWatchingAdapter?.clear()
        continueWatchingAdapter?.addAll(0, items)
    }
    
    fun updateLibraries(libraries: List<MediaItem>) {
        librariesAdapter?.clear()
        librariesAdapter?.addAll(0, libraries)
    }
    
    fun updateLatestMedia(items: List<MediaItem>) {
        latestMediaAdapter?.clear()
        latestMediaAdapter?.addAll(0, items)
    }
}
