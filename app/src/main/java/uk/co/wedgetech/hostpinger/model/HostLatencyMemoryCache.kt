package uk.co.wedgetech.hostpinger.model

object HostLatencyMemoryCache {
    /***
     * Key == Host name
     * Value set to null if tested but errored
     * Value set to latency
     */
    operator fun get(key: String): Long? = latencyCache[key]

    operator fun set(key: String, value: Long?): Long? {
        val original = latencyCache[key]
        if (original!=value || !latencyCache.containsKey(key)) {
            latencyCache[key] = value
        }
        return original
    }

    fun containsKey(key:String) :Boolean = latencyCache.containsKey(key)

    private val latencyCache:  MutableMap<String, Long?> = HashMap<String, Long?>()

    internal fun clear() {
        latencyCache.clear()
    }
}