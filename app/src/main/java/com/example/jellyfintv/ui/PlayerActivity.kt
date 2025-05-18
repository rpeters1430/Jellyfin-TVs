package com.example.jellyfintv.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.PlaybackControlsRow
import com.bumptech.glide.Glide
import com.example.jellyfintv.R
import com.example.jellyfintv.api.JellyfinApi
import com.example.jellyfintv.data.preferences.PreferencesManager
import com.example.jellyfintv.databinding.ActivityPlayerBinding
import com.example.jellyfintv.player.ExoPlayerAdapter
import com.example.jellyfintv.player.VideoPlayerGlue
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.*

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private val jellyfinApi: JellyfinApi by inject()
    private val preferencesManager: PreferencesManager by inject()
    
    private var player: SimpleExoPlayer? = null
    private var playerGlue: VideoPlayerGlue? = null
    private var playerAdapter: ExoPlayerAdapter? = null
    
    private var mediaId: String? = null
    private var mediaTitle: String? = null
    
    private val playbackStateListener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    binding.progressBar.visibility = View.GONE
                    playerGlue?.isPlaying = playWhenReady
                }
                Player.STATE_BUFFERING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Player.STATE_ENDED -> {
                    finish()
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        mediaId = intent.getStringExtra(EXTRA_MEDIA_ID)
        mediaTitle = intent.getStringExtra(EXTRA_MEDIA_TITLE)
        
        if (mediaId == null) {
            finish()
            return
        }
        
        initializePlayer()
        loadMedia()
    }
    
    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this).build().apply {
            addListener(playbackStateListener)
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_OFF
        }
        
        binding.playerView.player = player
        
        playerAdapter = ExoPlayerAdapter(this, player!!)
        playerGlue = VideoPlayerGlue(this, playerAdapter!!).apply {
            host = object : PlaybackTransportControlGlue.Host {
                override fun onHostStart() {}
                override fun onHostStop() {}
                override fun onHostPause() {}
                override fun onHostResume() {}
                override fun onHostDestroy() {}
            }
            
            // Set up controls
            setSeekEnabled(true)
            setSeekProvider(object : PlaybackSeekDataProvider() {
                override fun getSeekPositions(): LongArray = LongArray(0)
            })
            
            // Set up actions
            addAction(PlaybackControlsRow.RepeatAction(this@PlayerActivity))
            addAction(PlaybackControlsRow.ShuffleAction(this@PlayerActivity))
            
            title = mediaTitle ?: getString(R.string.app_name)
            subtitle = getString(R.string.app_name)
        }
    }
    
    private fun loadMedia() {
        mediaId?.let { id ->
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val media = jellyfinApi.getItem(id).getOrThrow()
                    val streamUrl = jellyfinApi.getStreamUrl(id)
                    
                    // Load media into player
                    val mediaSource = ProgressiveMediaSource.Factory(
                        DefaultDataSourceFactory(
                            this@PlayerActivity,
                            Util.getUserAgent(this@PlayerActivity, getString(R.string.app_name))
                        )
                    ).createMediaSource(Uri.parse(streamUrl))
                    
                    player?.prepare(mediaSource)
                    
                    // Load background
                    media.imageTags?.get(org.jellyfin.sdk.model.api.ImageType.PRIMARY)?.let { imageTag ->
                        val imageUrl = "${preferencesManager.serverUrl}/Items/$id/Images/Primary?tag=$imageTag&quality=90"
                        Glide.with(this@PlayerActivity)
                            .load(imageUrl)
                            .into(binding.backgroundImage)
                    }
                    
                } catch (e: Exception) {
                    Toast.makeText(
                        this@PlayerActivity,
                        "Error loading media: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
    }
    
    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }
    
    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return playerGlue?.onKey(keyCode, event) ?: super.onKeyDown(keyCode, event)
    }
    
    companion object {
        private const val EXTRA_MEDIA_ID = "extra_media_id"
        private const val EXTRA_MEDIA_TITLE = "extra_media_title"
        
        fun createIntent(context: android.content.Context, mediaId: String, title: String? = null) =
            Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_MEDIA_ID, mediaId)
                putExtra(EXTRA_MEDIA_TITLE, title)
            }
    }
}
