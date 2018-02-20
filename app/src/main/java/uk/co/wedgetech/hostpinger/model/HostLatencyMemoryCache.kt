package uk.co.wedgetech.hostpinger.model

object HostLatencyMemoryCache {
    /***
     * Key == Host name
     * Value set to null if tested but errored
     * Value set to latency
     */
    operator fun get(key: String): Latency? = latencyCache[key]

    /***
     * You cannot clear a value as not set. It has a value or error.
     */
    operator fun set(key: String, value: Latency): Latency? {
        val original = latencyCache[key]
        if (original!=value || !latencyCache.containsKey(key)) {
            latencyCache[key] = value
        }
        return original
    }

    fun containsKey(key:String) :Boolean = latencyCache.containsKey(key)

    private val latencyCache:  MutableMap<String, Latency> = HashMap<String, Latency>()

    internal fun clear() {
        latencyCache.clear()
    }
}