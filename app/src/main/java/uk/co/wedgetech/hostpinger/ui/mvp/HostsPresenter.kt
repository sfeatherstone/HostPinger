package uk.co.wedgetech.hostpinger.ui.mvp

import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.wedgetech.hostpinger.UIMode
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.NetworkError
import uk.co.wedgetech.hostpinger.model.tasks.HostService

typealias DataCallbackHandler = (list : List<Host>) -> Unit

class HostsPresenter<T> :HostsPresenterContract<T>{

    internal var hosts : List<Host> = listOf()
    internal var attachedView : T? = null
    internal var changeCallback : DataCallbackHandler? = null
    internal val hostsService by lazy { HostService() }

    override fun attachView(view: T) {
        attachedView = view
    }

    override fun detachView() {
        attachedView = null
    }

    override fun onHostsChanged(changeCallback: DataCallbackHandler) {
        this.changeCallback = changeCallback
    }

    override fun fetchHosts(successCallback: () -> Unit, errorCallback: (NetworkError) -> Unit) {
        val observable = hostsService.getHosts()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Host>> {
                    override fun onSuccess(t: List<Host>) {
                        if (attachedView!=null) {
                            successCallback()
                            hosts = sort(t)
                            changeCallback?.invoke(hosts)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (attachedView!=null) {
                            errorCallback(NetworkError("HostList", e.message))
                        }
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }

    override var sortOrder: Int
        get() = internalSortOrder
        set(value) {
            if (internalSortOrder!=value) {
                internalSortOrder = value
                sort()
            }
        }

    internal var internalSortOrder = UIMode.BY_NAME

    internal fun sort(toSort : List<Host>): List<Host> {
        return when (sortOrder) {
            UIMode.BY_NAME ->  toSort.sortedWith(compareBy(Host::name))
            UIMode.BY_URL -> toSort.sortedWith(compareBy(Host::url))
            UIMode.BY_LATENCY -> toSort.sorted()
            else -> toSort
        }
    }

    internal fun sort() {
        hosts = sort(hosts)
        if (attachedView!=null) {
            changeCallback?.invoke(hosts)
        }
    }


}