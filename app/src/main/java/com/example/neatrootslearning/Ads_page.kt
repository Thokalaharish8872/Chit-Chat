package com.example.neatrootslearning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class Ads_page : AppCompatActivity() {
    lateinit var mAdView:AdView
    private var mInterstitialAd:InterstitialAd?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads_page)
        mAdView = findViewById(R.id.adView)
        loadBannerAd()
        loadInterAd()
        var interAds=findViewById<Button>(R.id.adsBtn)

        interAds.setOnClickListener(){
            showInterAd()

        }
    }

    private fun showInterAd() {
        if(mInterstitialAd!=null){
            mInterstitialAd?.fullScreenContentCallback=object:FullScreenContentCallback(){
                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    intent= Intent(this@Ads_page,Ads_page::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }

            }
            mInterstitialAd?.show(this)

        }else{
            Toast.makeText(this@Ads_page,"Failed to Load Ad",Toast.LENGTH_SHORT).show()

        }


    }

    private fun loadInterAd(){
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {

                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                mInterstitialAd = interstitialAd
            }
        })

    }
    private fun loadBannerAd(){
        MobileAds.initialize(this){}
//        mAdView.adUnitId = "ca-app-pub-3940256099942544/6300978111"  // Add your Ad Unit ID here

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
//                Toast.makeText(this@Ads_page, "Ad Loaded", Toast.LENGTH_SHORT).show()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // You can add a message here to indicate that the ad failed to load
            }

            override fun onAdOpened() {
                // You can add a message here to indicate that the ad was opened
            }

            override fun onAdClicked() {
//                Toast.makeText(this@Ads_page, "Ad Clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClosed() {
//                Toast.makeText(this@Ads_page, "Ad Closed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
