package uk.co.wedgetech.hostpinger.model

data class Host(val name:String, val url:String, val icon:String) : Comparable<Host> {

    override fun compareTo(other: Host): Int {
        val thisCached : Boolean = HostLatencyMemoryCache.containsKey(name)
        val otherCached : Boolean = HostLatencyMemoryCache.containsKey(other.name)
        if (!(thisCached || otherCached)) return compareValues(name, other.name)
        if (!thisCached) return 1
        if (!otherCached) return -1

        val thisLatency = HostLatencyMemoryCache[name]
        val otherLatency = HostLatencyMemoryCache[other.name]
        if (thisLatency==null && otherLatency==null) return compareValues(name, other.name)
        return compareValues(thisLatency, otherLatency)
    }
}