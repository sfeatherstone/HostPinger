package uk.co.wedgetech.hostpinger.ui

import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.card_host.*
import kotlinx.android.extensions.LayoutContainer
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.wedgetech.hostpinger.R
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.tasks.Ping
import java.net.URL

class HostCardHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    internal lateinit var host: Host

    fun bindHost(host: Host) {
        this.host = host
        host_text.text = host.name
        Picasso.with(containerView.context)
                .load(host.icon)
                .placeholder(R.drawable.ic_cloud)
                .into(image)
        updateLatency(false)
        containerView.setOnClickListener({view ->
            updateLatency(true)
            })
    }

    fun updateLatency(forceUpdate : Boolean) {
        //TODO move?
        val observable = Ping.getPing(host, forceUpdate)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Long> {
                    override fun onSuccess(t: Long) {
                        latency_avg_text.text = "${t}ms"
                    }

                    override fun onError(e: Throwable) {
                        latency_avg_text.text = "Error"
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })

    }
}