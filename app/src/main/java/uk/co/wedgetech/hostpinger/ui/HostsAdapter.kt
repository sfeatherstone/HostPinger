package uk.co.wedgetech.hostpinger.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import uk.co.wedgetech.hostpinger.R
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.HostLatencyMemoryCache

class HostsAdapter : RecyclerView.Adapter<HostCardHolder>() {

    private var items: List<Host> = List<Host>(0,{Host("","","")})

    private var sortMethodInternal = BY_NAME
    var sortMethod: Int
        get() = sortMethodInternal
        set(value) {
            if (sortMethodInternal!=value) {
                sortMethodInternal = value
                sort()
            }
        }

    fun setHosts(hosts :List<Host>) {
        items = hosts
        sort()
    }

    internal fun sort() {
        when(sortMethod) {
            BY_NAME -> items = items.sortedWith(compareBy(Host::name))

            BY_URL -> items = items.sortedWith(compareBy(Host::url))

            BY_LATENCY -> items = items.sorted()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostCardHolder {
        val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_host, parent, false)
        return HostCardHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HostCardHolder, position: Int) {
        return holder.bindHost(items[position])
    }

    companion object {
        val BY_NAME = 0
        val BY_URL = 1
        val BY_LATENCY= 2
    }

}