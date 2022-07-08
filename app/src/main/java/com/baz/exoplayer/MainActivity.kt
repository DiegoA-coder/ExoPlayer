package com.baz.exoplayer

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaItem.AdsConfiguration
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util

class MainActivity : AppCompatActivity() {

    private var playerView: PlayerView? = null
    private var player: ExoPlayer? = null
    private var adsLoader: ImaAdsLoader? = null
    private var skipAds: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById<PlayerView>(R.id.player_view)
        skipAds = findViewById(R.id.buttonSkipAds)

        // Create an AdsLoader.
        adsLoader = ImaAdsLoader.Builder( /* context= */this).build()

        //Skip Ads
        skipAds!!.setOnClickListener(View.OnClickListener {
            try {
                onDestroy()
            } catch (e: Exception) {
                println(e)
            }
        })
    }

    private fun releasePlayer() {
        adsLoader!!.setPlayer(null)
        playerView!!.player = null
        player!!.release()
        player = null
    }

    override fun onStart() {
        super.onStart()
        //
        if (Util.SDK_INT > 23) {
            initializePlayer()
            if (playerView != null) {
                playerView!!.onResume()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
            if (playerView != null) {
                playerView!!.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            if (playerView != null) {
                playerView!!.onPause()
            }
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView!!.onPause()
            }
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adsLoader!!.release()
    }

    private fun initializePlayer() {
        val contentUri = Uri.parse(getString(R.string.content_url))
        val adTagUri = Uri.parse(getString(R.string.ad_tag_url))

        // Create a SimpleExoPlayer and set it as the player for content and ads.
        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(buildMediaSource())
            .build()
        playerView!!.player = player
        adsLoader!!.setPlayer(player)

        val mediaItem = MediaItem.Builder()
            .setUri(contentUri)
            .setAdsConfiguration(
                AdsConfiguration.Builder(adTagUri)
                    .setAdsId(adTagUri)
                    .build()
            )
            .build()

        // Prepare the content and ad to be played with the SimpleExoPlayer.
        player!!.setMediaItem(mediaItem)
        player!!.prepare()

        // Set PlayWhenReady. If true, content and ads will autoplay.
        player!!.setPlayWhenReady(false)
    }

    //creating mediaSource
    private fun buildMediaSource(): MediaSourceFactory {
        val mediaSourceFactory: MediaSourceFactory =
            DefaultMediaSourceFactory(this)
                .setAdsLoaderProvider { unusedAdTagUri: AdsConfiguration? -> adsLoader }
                .setAdViewProvider(playerView)

        return mediaSourceFactory
    }
}