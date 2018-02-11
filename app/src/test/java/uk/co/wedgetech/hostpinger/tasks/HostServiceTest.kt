package uk.co.wedgetech.hostpinger.tasks

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import junit.framework.Assert
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.After
import org.junit.Test

import org.junit.Before
import uk.co.wedgetech.hostpinger.model.Host
import java.io.IOException

class HostServiceTest {

    lateinit internal var server: MockWebServer
    lateinit var service : HostService

    @Before
    @Throws(Exception::class)
    fun setUp() {
        server = MockWebServer()
        server.start()
        service = HostService()
        HostService.BASE_URL = server.url("/").toString()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        server.shutdown()
    }


    @Test
    fun getHostsSuccess() {
        enqueueJson("hosts_success.json")
        val singleCreditValues : Single<List<Host>> = HostService().getHosts()

        // given:
        val observer = TestObserver<List<Host>>()

        // when:
        singleCreditValues.subscribe(observer)

        // then:
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValueCount(1)
        Assert.assertEquals(7,observer.values()[0].size)
        Assert.assertEquals("eBay UK",observer.values()[0][0].name)
        Assert.assertEquals("www.ebay.co.uk",observer.values()[0][0].url)
        Assert.assertEquals("https://pages.ebay.com/favicon.ico",observer.values()[0][0].icon)
    }


    internal fun enqueueJson(filename: String) {
        val json = javaClass.classLoader.getResourceAsStream(filename)
        try {
            val response = MockResponse().setBody(Buffer().readFrom(json))
            server.enqueue(response)
        } catch (e: IOException) {
            Assert.fail(e.toString())
        }

    }

}