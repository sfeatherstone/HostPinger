package uk.co.wedgetech.hostpinger.ui.mvp

/**
 * Created by simon on 21/02/2018.
 */
interface BasePresenter<T> {
    fun attachView(view: T)
    fun detachView()
}