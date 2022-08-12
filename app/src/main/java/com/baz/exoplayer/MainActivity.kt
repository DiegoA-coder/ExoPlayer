package com.baz.exoplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util


class MainActivity : AppCompatActivity() {

    //Player
    private var exoplayer: SimpleExoPlayer? = null

    //Ads
    private val imaAdsLoader: ImaAdsLoader? = null

    //PlayList (https required and m3u extension is not required)
    val dash27 = "https://ice55.securenetsystems.net/DASH27"
    val contentUri = Uri.parse(dash27)

    //Ads
    val adTagUri =
        "https://pubads.g.doubleclick.net/gampad/ads?iu=/21775744923/external/single_preroll_skippable&sz=640x480&ciu_szs=300x250%2C728x90&gdfp_req=1&output=vast&unviewed_position_start=1&env=vp&impl=s&correlator="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var exoplayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        var playerView = findViewById<PlayerView>(R.id.player_view)

        val imaAdsLoader = ImaAdsLoader.Builder(this).buildForAdTag(Uri.parse(adTagUri))
        val dataSource = DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, "ExoPlayer")
        )
        val mediaSource = ExtractorMediaSource.Factory(dataSource).createMediaSource(contentUri)
        val adMediaSource: MediaSource = AdsMediaSource(mediaSource, dataSource, imaAdsLoader, playerView)
        playerView.player = exoplayer
        imaAdsLoader.setPlayer(exoplayer)

        exoplayer.playWhenReady = true
        exoplayer.prepare(mediaSource)
        //exoplayer.prepare(adMediaSource, false, false)
    }
}