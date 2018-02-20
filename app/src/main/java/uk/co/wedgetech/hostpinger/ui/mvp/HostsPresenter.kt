package uk.co.wedgetech.hostpinger.ui.mvp

import uk.co.wedgetech.hostpinger.UIMode
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.NetworkError

class HostsPresenter :HostsPresenterContract{

    var hosts: List<Host> = listOf()

    override fun onHostsChanged(changeCallback: (List<Host>) -> Unit) {
    }

    override fun fetchHosts(successCallback: () -> Unit, errorCallback: (NetworkError) -> Unit) {
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
    }


}