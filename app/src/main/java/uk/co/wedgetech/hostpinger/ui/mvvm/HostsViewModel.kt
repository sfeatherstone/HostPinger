package uk.co.wedgetech.hostpinger.ui.mvvm

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.wedgetech.hostpinger.UIMode.Companion.BY_LATENCY
import uk.co.wedgetech.hostpinger.UIMode.Companion.BY_NAME
import uk.co.wedgetech.hostpinger.UIMode.Companion.BY_URL
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.NetworkError
import uk.co.wedgetech.hostpinger.model.tasks.HostService

class HostsViewModel: ViewModel() {

    private val hostsService by lazy { HostService() }

    private val hostsMutable : MutableLiveData<List<Host>> = MutableLiveData()
    val hosts: LiveData<List<Host>> = hostsMutable

    private val errorMutable : MutableLiveData<NetworkError> = MutableLiveData()
    val error: LiveData<NetworkError> by lazy { errorMutable }


    fun fetchHosts() {
        val observable = hostsService.getHosts()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Host>> {
                    override fun onSuccess(t: List<Host>) {
                        hostsMutable.value = sort(t)
                    }

                    override fun onError(e: Throwable) {
                        errorMutable.value = NetworkError("HostList", e.message)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }

    internal var internalSortOrder = BY_NAME

    var sortOrder: Int
        get() = internalSortOrder
        set(value) {
            if (internalSortOrder!=value) {
                internalSortOrder = value
                sort()
            }
        }

    internal fun sort(toSort : List<Host>): List<Host> {
        return when (sortOrder) {
            BY_NAME ->  toSort.sortedWith(compareBy(Host::name))
            BY_URL -> toSort.sortedWith(compareBy(Host::url))
            BY_LATENCY -> toSort.sorted()
            else -> toSort
        }
    }

    internal fun sort() {
        hosts.value?.let { hostsMutable.value = sort(it) }
    }


}