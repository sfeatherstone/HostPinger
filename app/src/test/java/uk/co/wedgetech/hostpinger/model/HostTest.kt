package uk.co.wedgetech.hostpinger.model

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class HostTest {

    val host1 = Host("a", "3", "")
    val host2 = Host("b", "2", "")
    val host3 = Host("c", "1", "")

    @Before
    fun setup() {
        HostLatencyMemoryCache.clear()
    }

    @Test
    fun compareToAllAdded() {
        HostLatencyMemoryCache[host1.name] = Latency(3)
        HostLatencyMemoryCache[host2.name] = Latency(2)
        HostLatencyMemoryCache[host3.name] = Latency(1)

        var hosts :List<Host> = listOf(host1, host2, host3)
        hosts = hosts.sorted()

        Assert.assertEquals(hosts[0], host3)
        Assert.assertEquals(hosts[1], host2)
        Assert.assertEquals(hosts[2], host1)
    }

    @Test
    fun compareToAllAddedEqual() {
        HostLatencyMemoryCache[host1.name] = Latency(1)
        HostLatencyMemoryCache[host2.name] = Latency(1)
        HostLatencyMemoryCache[host3.name] = Latency(1)

        var hosts :List<Host> = listOf(host1, host2, host3)
        hosts = hosts.sorted()

        Assert.assertEquals(hosts[0], host1)
        Assert.assertEquals(hosts[1], host2)
        Assert.assertEquals(hosts[2], host3)
    }

    @Test
    fun compareToUnmeasured() {
        HostLatencyMemoryCache[host2.name] = Latency(1)

        var hosts :List<Host> = listOf(host1, host2, host3)
        hosts = hosts.sorted()

        Assert.assertEquals(hosts[0], host2)
        Assert.assertEquals(hosts[1], host1)
        Assert.assertEquals(hosts[2], host3)
    }

    @Test
    fun compareToErrored() {
        HostLatencyMemoryCache[host2.name] = Latency(0, "Error")

        var hosts :List<Host> = listOf(host1, host2, host3)
        hosts = hosts.sorted()

        Assert.assertEquals(hosts[0], host2)
        Assert.assertEquals(hosts[1], host1)
        Assert.assertEquals(hosts[2], host3)
    }

    @Test
    fun compareToMix() {
        HostLatencyMemoryCache[host3.name] = Latency(1)
        HostLatencyMemoryCache[host2.name] = Latency(0, "Error")

        var hosts :List<Host> = listOf(host1, host2, host3)
        hosts = hosts.sorted()

        Assert.assertEquals(hosts[0], host3)
        Assert.assertEquals(hosts[1], host2)
        Assert.assertEquals(hosts[2], host1)
    }

}