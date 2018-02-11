package uk.co.wedgetech.hostpinger.model

import uk.co.wedgetech.hostpinger.utils.makeUrl


data class Host(val name:String, val url:String, val icon:String) : Comparable<Host> {

    override fun compareTo(other: Host): Int {
        val thisCached : Boolean = HostLatencyMemoryCache.containsKey(name)
        val otherCached : Boolean = HostLatencyMemoryCache.containsKey(other.name)
        if (!(thisCached || otherCached)) return compareValues(other.name, name)
        if (!thisCached) return 1
        if (!otherCached) return -1

        val thisLatency = HostLatencyMemoryCache[name]
        val otherLatency = HostLatencyMemoryCache[other.name]
        if (thisLatency==null && otherLatency==null) return compareValues(other.name, name)
        return compareValues(thisLatency, otherLatency)
    }
}