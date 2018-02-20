package uk.co.wedgetech.hostpinger.model

import uk.co.wedgetech.hostpinger.utils.compareValuesNullsGreater

data class Host(val name:String, val url:String, val icon:String) : Comparable<Host> {

    var latency : Latency?
        get() = HostLatencyMemoryCache[name]
        set(value) { value?.let { HostLatencyMemoryCache[name] = value }  }

    override fun compareTo(other: Host): Int {
        if (latency==null && other.latency==null) return compareValues(name, other.name)
        return compareValuesNullsGreater(latency, other.latency)
    }
}


