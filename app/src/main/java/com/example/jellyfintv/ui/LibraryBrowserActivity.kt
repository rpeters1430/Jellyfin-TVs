package com.example.jellyfintv.ui

import android.os.Bundle
import android.view.View
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.jellyfintv.R
import com.example.jellyfintv.api.JellyfinApi
import com.example.jellyfintv.databinding.ActivityLibraryBrowserBinding
import com.example.jellyfintv.ui.adapter.CardPresenter
import com.example.jellyfintv.ui.model.MediaItem
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LibraryBrowserActivity : BaseActivity() {
    private lateinit var binding: ActivityLibraryBrowserBinding
    private val jellyfinApi: JellyfinApi by inject()
    
    private lateinit var libraryId: String
    private lateinit var libraryTitle: String
    
    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private var contentAdapter: ArrayObjectAdapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        libraryId = intent.getStringExtra(EXTRA_LIBRARY_ID) ?: run {
            finish()
            return
        }
        
        libraryTitle = intent.getStringExtra(EXTRA_LIBRARY_TITLE) ?: getString(R.string.app_name)
        
        setupUI()
        loadContent()
    }
    
    private fun setupUI() {
        title = libraryTitle
        
        // Set up the content fragment
        val fragment = BrowseSupportFragment().apply {
            adapter = rowsAdapter
            headersState = BrowseSupportFragment.HEADERS_DISABLED
            brandColor = getColor(R.color.fastlane_background)
            searchAffordanceColor = getColor(R.color.search_opaque)
            
            setOnItemViewClickedListener { itemViewHolder, item, _, _ ->
                (item as? MediaItem)?.let { mediaItem ->
                    if (mediaItem.isFolder) {
                        // Navigate to subfolder
                        startActivity(
                            createIntent(
                                this@LibraryBrowserActivity,
                                mediaItem.id,
                                mediaItem.title
                            )
                        )
                    } else {
                        // Play media
                        startActivity(
                            PlayerActivity.createIntent(
                                this@LibraryBrowserActivity,
                                mediaItem.id,
                                mediaItem.title
                            )
                        )
                    }
                }
            }
        }
        
        // Add the fragment to the container
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitNow()
    }
    
    private fun loadContent() {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val items = jellyfinApi.getChildren(libraryId).getOrThrow()
                
                contentAdapter = ArrayObjectAdapter(CardPresenter()).apply {
                    addAll(0, items)
                }
                
                rowsAdapter.clear()
                rowsAdapter.add(ListRow(HeaderItem(libraryTitle), contentAdapter!!))
                
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
                showError("Failed to load content: ${e.message}")
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    
    private fun showError(message: String) {
        // Show error message to user
    }
    
    companion object {
        private const val EXTRA_LIBRARY_ID = "extra_library_id"
        private const val EXTRA_LIBRARY_TITLE = "extra_library_title"
        
        fun createIntent(context: android.content.Context, libraryId: String, title: String?) =
            android.content.Intent(context, LibraryBrowserActivity::class.java).apply {
                putExtra(EXTRA_LIBRARY_ID, libraryId)
                putExtra(EXTRA_LIBRARY_TITLE, title)
            }
    }
}
