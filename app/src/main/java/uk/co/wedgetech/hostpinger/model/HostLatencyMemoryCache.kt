package uk.co.wedgetech.hostpinger.model

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


object HostLatencyMemoryCache {
    private val changeObservable = PublishSubject.create<HostLatencyMemoryCache>()
    /***
     * Key == Host name
     * Value set to null if tested but errored
     * Value set to latency
     */
    operator fun get(key: String): Long? = latencyCache[key]

    operator fun set(key: String, value: Long?): Long? {
        val original = latencyCache[key]
        if (original!=value) {
            latencyCache[key] = value
            changeObservable.onNext(this)
        }
        return original
    }

    fun containsKey(key:String) :Boolean = latencyCache.containsKey(key)

    fun getModelChanges() : Observable<HostLatencyMemoryCache> {
        return changeObservable
    }

    private val latencyCache:  MutableMap<String, Long?> = HashMap<String, Long?>()
}