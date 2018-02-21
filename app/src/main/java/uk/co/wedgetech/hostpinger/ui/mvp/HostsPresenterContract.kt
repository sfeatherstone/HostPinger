package uk.co.wedgetech.hostpinger.ui.mvp

import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.NetworkError

interface HostsPresenterContract<T> : BasePresenter<T>{

    fun onHostsChanged(changeCallback: (List<Host>)->Unit)

    fun fetchHosts(successCallback: ()->Unit, errorCallback: (NetworkError)->Unit)

    var sortOrder: Int
}