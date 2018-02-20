package uk.co.wedgetech.hostpinger.model

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class LatencyTest {
    @Test
    fun compareToNoErrors() {
        val l1 = Latency(3)
        val l2 = Latency(2)
        val l3 = Latency(1)

        var list :List<Latency> = listOf(l1, l2, l3)
        list = list.sorted()

        Assert.assertEquals(list[0], l3)
        Assert.assertEquals(list[1], l2)
        Assert.assertEquals(list[2], l1)

        list = list.sorted()

        Assert.assertEquals(list[0], l3)
        Assert.assertEquals(list[1], l2)
        Assert.assertEquals(list[2], l1)
    }

    @Test
    fun compareToErrors() {
        val l0 = Latency(0, "Error")
        val l00 = Latency(0, "AError")
        val l1 = Latency(3)
        val l2 = Latency(2)
        val l3 = Latency(1)

        var list :List<Latency> = listOf(l0, l1, l00, l2, l3)
        list = list.sorted()

        Assert.assertEquals(list[0], l3)
        Assert.assertEquals(list[1], l2)
        Assert.assertEquals(list[2], l1)
        Assert.assertEquals(list[3], l00)
        Assert.assertEquals(list[4], l0)

        list = list.sorted()

        Assert.assertEquals(list[0], l3)
        Assert.assertEquals(list[1], l2)
        Assert.assertEquals(list[2], l1)
        Assert.assertEquals(list[3], l00)
        Assert.assertEquals(list[4], l0)
    }

}