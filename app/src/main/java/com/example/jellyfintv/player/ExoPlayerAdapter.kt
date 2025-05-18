package com.example.jellyfintv.player

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.media.PlayerAdapter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.sun.tools.javac.util.Context

class ExoPlayerAdapter(
    context: Context,
    private val player: Player
) : PlayerAdapter() {
    
    private val leanbackPlayerAdapter: LeanbackPlayerAdapter
    private var currentPosition: Long = 0
    private var isPrepared = false
    
    init {
        leanbackPlayerAdapter = LeanbackPlayerAdapter(context, player, 16) // 16ms for 60fps
        leanbackPlayerAdapter.setRepeatAction(PlaybackControlRepeatAction(context))
        leanbackPlayerAdapter.setProgressUpdatingEnabled(true)
        
        player.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        if (!isPrepared) {
                            isPrepared = true
                            notifyPreparedStateChanged(true)
                        }
                    }
                    Player.STATE_ENDED -> {
                        notifyPlaybackComplete()
                    }
                    Player.STATE_IDLE, Player.STATE_BUFFERING -> {
                        // Handle other states if needed
                    }
                }
            }
            
            override fun onPlayerError(error: ExoPlaybackException) {
                Log.e("ExoPlayerAdapter", "Playback error", error)
                notifyError(error.message ?: "Unknown error")
            }
        })
    }
    
    override fun isPrepared(): Boolean = isPrepared
    
    override fun isPlaying(): Boolean = player.playWhenReady
    
    override fun getCurrentPosition(): Long = player.currentPosition
    
    override fun getBufferedPosition(): Long = player.bufferedPosition
    
    override fun getDuration(): Long = player.duration
    
    override fun play() {
        player.playWhenReady = true
    }
    
    override fun pause() {
        player.playWhenReady = false
    }
    
    override fun seekTo(position: Long) {
        player.seekTo(position)
    }
    
    override fun next() {
        // Implement next track logic if needed
    }
    
    override fun previous() {
        // Implement previous track logic if needed
    }
    
    override fun setRepeatAction(repeatAction: Int) {
        player.repeatMode = when (repeatAction) {
            PlaybackControlRepeatAction.INDEX_NONE -> Player.REPEAT_MODE_OFF
            PlaybackControlRepeatAction.INDEX_ONE -> Player.REPEAT_MODE_ONE
            PlaybackControlRepeatAction.INDEX_ALL -> Player.REPEAT_MODE_ALL
            else -> Player.REPEAT_MODE_OFF
        }
    }
    
    override fun setProgressUpdatingEnabled(enabled: Boolean) {
        leanbackPlayerAdapter.setProgressUpdatingEnabled(enabled)
    }
    
    override fun setRepeatAction(repeatAction: PlaybackControlRepeatAction) {
        leanbackPlayerAdapter.setRepeatAction(repeatAction)
    }
    
    override fun setControlsOverlayAutoHideEnabled(enabled: Boolean) {
        leanbackPlayerAdapter.setControlsOverlayAutoHideEnabled(enabled)
    }
    
    override fun setHostCallback(callback: PlaybackTransportControlGlue.Host?) {
        leanbackPlayerAdapter.setHostCallback(callback)
    }
    
    override fun onDetachedFromHost() {
        leanbackPlayerAdapter.onDetachedFromHost()
        player.release()
    }
    
    override fun onAttachedToHost(host: PlaybackTransportControlGlue.Host) {
        leanbackPlayerAdapter.onAttachedToHost(host)
    }
    
    fun loadMedia(mediaSource: MediaSource) {
        player.prepare(mediaSource)
    }
    
    fun createMediaSource(uri: Uri, context: Context): MediaSource {
        val userAgent = Util.getUserAgent(context, context.applicationInfo.loadLabel(
            context.packageManager).toString())
        
        val dataSourceFactory = DefaultDataSourceFactory(context, userAgent)
        
        return when (val type = Util.inferContentType(uri)) {
            C.TYPE_DASH -> {
                DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
            }
            C.TYPE_SS -> {
                SsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
            }
            C.TYPE_HLS -> {
                HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
            }
            C.TYPE_OTHER -> {
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
            }
            else -> {
                throw IllegalStateException("Unsupported type: $type")
            }
        }
    }
}
