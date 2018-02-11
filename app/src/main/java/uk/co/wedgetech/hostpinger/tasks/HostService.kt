package uk.co.wedgetech.hostpinger.tasks

import io.reactivex.Single
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.wedgetech.hostpinger.BuildConfig
import uk.co.wedgetech.hostpinger.model.Host
import java.util.*

class HostService private constructor() {

    private val hostApi: HostAPI

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(makeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        hostApi = retrofit.create<HostAPI>(HostAPI::class.java)
    }

    private fun makeOkHttpClient(): OkHttpClient {
        //Allow our mockserver to work
        if (WeakHttpClient) return OkHttpClient()

        val spec: ConnectionSpec.Builder = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)

        spec.cipherSuites(
                CipherSuite.TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,  //20+
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)

        return OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec.build()))
                .build()
    }

    fun getHosts(): Single<List<Host>> {
        return hostApi.loadHosts()
    }

    companion object {
        //Added to allow for testing
        internal var BASE_URL : String = BuildConfig.BASE_REST_URL
        internal var WeakHttpClient = false

        val instance: HostService by lazy { HostService() }
    }
}