package com.example.jellyfintv.player

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import androidx.leanback.media.PlaybackBaseControlGlue
import androidx.leanback.media.PlaybackSeekDataProvider
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.jellyfintv.R

class VideoPlayerGlue(
    context: Context,
    playerAdapter: PlayerAdapter
) : PlaybackBaseControlGlue<PlayerAdapter>(context, playerAdapter) {

    private var backgroundImage: Drawable? = null
    
    private val repeatAction = Action(R.id.lb_control_repeat)
    private val shuffleAction = Action(R.id.lb_control_shuffle)
    
    private var isPlaying = false
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    play()
                } else {
                    pause()
                }
                callback.onPlayStateChanged(this)
            }
        }
    
    init {
        // Set up fast forward and rewind actions
        fastForwardSpeeds = longArrayOf(10000, 30000, 60000) // 10s, 30s, 1min
        rewindSpeeds = longArrayOf(-10000, -30000, -60000)
    }
    
    override fun getSupportedActions(): LongArray {
        return longArrayOf(
            Action.PLAY_PAUSE,
            Action.SKIP_TO_PREVIOUS,
            Action.SKIP_TO_NEXT,
            Action.FAST_FORWARD,
            Action.REWIND
        )
    }
    
    override fun onActionClicked(action: Action) {
        when (action.id) {
            Action.PLAY_PAUSE -> isPlaying = !isPlaying
            Action.SKIP_TO_PREVIOUS -> skipToPrevious()
            Action.SKIP_TO_NEXT -> skipToNext()
            Action.FAST_FORWARD -> fastForward()
            Action.REWIND -> rewind()
            R.id.lb_control_repeat -> toggleRepeat()
            R.id.lb_control_shuffle -> toggleShuffle()
        }
        callback.onPlayStateChanged(this)
    }
    
    override fun onKey(keyEvent: KeyEvent): Boolean {
        when (keyEvent.keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                isPlaying = !isPlaying
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PLAY -> {
                isPlaying = true
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                isPlaying = false
                return true
            }
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {
                fastForward()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_REWIND -> {
                rewind()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_NEXT -> {
                skipToNext()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                skipToPrevious()
                return true
            }
        }
        return super.onKey(keyEvent)
    }
    
    private fun skipToNext() {
        // Implement skip to next track
    }
    
    private fun skipToPrevious() {
        // Implement skip to previous track
    }
    
    private fun toggleRepeat() {
        // Toggle repeat mode
    }
    
    private fun toggleShuffle() {
        // Toggle shuffle mode
    }
    
    fun setBackgroundImageUrl(url: String) {
        Glide.with(context)
            .load(url)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    backgroundImage = resource
                    callback.onMetadataChanged(this@VideoPlayerGlue)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    backgroundImage = null
                }
            })
    }
    
    fun getBackgroundImage(): Drawable? = backgroundImage
    
    override fun getCurrentPosition(): Long = playerAdapter.currentPosition
    override fun getBufferedPosition(): Long = playerAdapter.bufferedPosition
    override fun getDuration(): Long = playerAdapter.duration
    override fun isPlaying(): Boolean = isPlaying
    override fun isPrepared(): Boolean = true
    
    override fun onUpdateProgress() {
        // Update progress
    }
    
    override fun onSetPlayWhenReady(playWhenReady: Boolean) {
        isPlaying = playWhenReady
    }
    
    override fun onSetSeekTimeMs(seekTimeMs: Long) {
        playerAdapter.seekTo(seekTimeMs)
    }
    
    override fun onSetPlaybackSpeed(rate: Float) {
        // Handle playback speed change
    }
    
    override fun onSetSeekProvider(seekProvider: PlaybackSeekDataProvider?) {
        // Handle seek provider
    }
}
