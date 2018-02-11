package uk.co.wedgetech.hostpinger.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.wedgetech.hostpinger.R
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.tasks.HostService

class MainActivity : AppCompatActivity() {

    internal lateinit var hostsAdapter :HostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adapter :ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
                R.array.sort_by_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        sortby_spinner.adapter = adapter

        hostsAdapter = HostsAdapter()

        val observable = HostService().getHosts()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Host>> {
                    override fun onSuccess(t: List<Host>) {
                        hostsAdapter.setHosts(t)
                    }

                    override fun onError(e: Throwable) {
                        //TODO Show errow
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = hostsAdapter

        // Setup spinner for picking sort order
        sortby_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hostsAdapter.sortMethod = position
            }

        }
    }
}
