package uk.co.wedgetech.hostpinger.tasks

import io.reactivex.Single
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.HostLatencyMemoryCache
import uk.co.wedgetech.hostpinger.utils.makeUrl
import java.net.InetAddress
import java.net.Socket
import java.net.URL


object Ping {
    fun getPing(host: Host, forceUpdate: Boolean): Single<Long> {
        if (!forceUpdate) {
            val single : Single<Long>? = getCached(host.name)
            if (single!=null) return single
        }
        return pingServer(host)
    }

    internal fun getCached(hostName :String) : Single<Long>? {
        if (HostLatencyMemoryCache.containsKey(hostName)) {
            val latency = HostLatencyMemoryCache[hostName]
            val single = Single.create<Long> { emitter ->
                if (latency!=null)
                    emitter.onSuccess(latency)
                else
                    emitter.onError(Exception())
            }
            return single
        }
        return null
    }

    internal fun pingServer(host :Host) : Single<Long> {
        val single = Single.create<Long> { emitter ->
            val thread = Thread {
                try {
                    var totalTime: Long = 0
                    for (x in 0..4) {
                        totalTime += ping(makeUrl(host.url))
                    }

                    val avgTime = totalTime / 5
                    //Set cache
                    HostLatencyMemoryCache[host.name] = avgTime

                    emitter.onSuccess(avgTime)
                } catch (e: Exception) {
                    HostLatencyMemoryCache[host.name] = null
                    emitter.onError(e)
                }
            }
            thread.start()
        }
        return single
    }

    internal fun ping(url:URL): Long {
        val hostAddress = InetAddress.getByName(url.getHost()).getHostAddress()
        val dnsResolved = System.currentTimeMillis()
        val port = if (url.port == -1) url.defaultPort else url.port
        val socket = Socket(hostAddress, port)
        socket.close()
        val probeFinish = System.currentTimeMillis()
        return probeFinish-dnsResolved;
    }

}