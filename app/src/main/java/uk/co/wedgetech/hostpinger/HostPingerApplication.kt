package uk.co.wedgetech.hostpinger

import android.app.Application
import com.squareup.picasso.Picasso
import com.jakewharton.picasso.OkHttp3Downloader


class HostPingerApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Setup picasso to use OKHttp3 so redirect works. (TradeMe http->https)
        val builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this))
        Picasso.setSingletonInstance(builder.build())
    }
}