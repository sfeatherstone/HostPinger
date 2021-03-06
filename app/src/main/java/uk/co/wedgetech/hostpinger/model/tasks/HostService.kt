package uk.co.wedgetech.hostpinger.model.tasks

import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.wedgetech.hostpinger.BuildConfig
import uk.co.wedgetech.hostpinger.model.Host

class HostService {

    private val hostApi: HostAPI

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        hostApi = retrofit.create<HostAPI>(HostAPI::class.java)
    }

    fun getHosts(): Single<List<Host>> {
        return hostApi.loadHosts()
    }

    companion object {
        //Added to allow for testing
        internal var BASE_URL : String = BuildConfig.BASE_REST_URL
        val okHttpClient = OkHttpClient()
    }
}