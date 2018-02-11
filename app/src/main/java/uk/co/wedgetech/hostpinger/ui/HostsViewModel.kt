package uk.co.wedgetech.hostpinger.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.NetworkError
import uk.co.wedgetech.hostpinger.tasks.HostService

class HostsViewModel:ViewModel() {

    private val hostsService by lazy { HostService() }

    private val hostsMutable : MutableLiveData<List<Host>> = MutableLiveData()
    val hosts: LiveData<List<Host>> = hostsMutable

    private val errorMutable : MutableLiveData<NetworkError> = MutableLiveData()
    val error: LiveData<NetworkError> by lazy { errorMutable }


    fun getHosts() {
        val observable = hostsService.getHosts()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Host>> {
                    override fun onSuccess(t: List<Host>) {
                        hostsMutable.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMutable.value = NetworkError("HostList", e.message)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }


}