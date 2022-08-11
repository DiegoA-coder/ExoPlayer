package com.baz.exoplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class MainActivity : AppCompatActivity() {

    //Player
    private var exoplayer: SimpleExoPlayer? = null

    //private val imaAdsLoader: ImaAdsLoader? = null
    var url: String? = ""


    //HLS
    //val contentUri = Uri.parse("https://ice55.securenetsystems.net/DASH27.m3u")
    val contentUri =
        Uri.parse("https://mdstrm.com/live-stream-playlist/60c9357b09f72f0830c30058.m3u8")

    //Ads
    val adTagUri =
        "https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/single_preroll_skippable&sz=640x480&ciu_szs=300x250%2C728x90&gdfp_req=1&output=vast&unviewed_position_start=1&env=vp&impl=s&correlator="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var player2 = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        var playerView = findViewById<PlayerView>(R.id.player_view)
        playerView.player = player2
        //val imaAdsLoader = ImaAdsLoader(this, Uri.parse(adTagUri))

        val dataSourceFactory = DefaultDataSourceFactory(this, "ExoPlayer")
        val hlsMediaSource =
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(contentUri)
        val dashMediaSource =
            DashMediaSource.Factory(dataSourceFactory).createMediaSource(contentUri)

        //val adsMediaSource = AdsMediaSource(hlsMediaSource, dataSourceFactory, imaAdsLoader, playerView)

        player2?.prepare(hlsMediaSource)
        player2?.setPlayWhenReady(true)


    }
}