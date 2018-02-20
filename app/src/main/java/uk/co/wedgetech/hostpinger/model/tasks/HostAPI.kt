package uk.co.wedgetech.hostpinger.model.tasks

import io.reactivex.Single
import retrofit2.http.GET
import uk.co.wedgetech.hostpinger.model.Host

interface HostAPI {
    @GET("sk_hosts/")
    fun loadHosts(): Single<List<Host>>
}