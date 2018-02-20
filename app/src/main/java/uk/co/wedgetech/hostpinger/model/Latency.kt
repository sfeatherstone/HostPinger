package uk.co.wedgetech.hostpinger.model

data class Latency(val value : Long, val error :String? = null) : Comparable<Latency> {
    override fun compareTo(other: Latency): Int {
        if (error == null && other.error == null) {
            return compareValues(value, other.value)
        }
        return compareValues(error, other.error)
    }
}