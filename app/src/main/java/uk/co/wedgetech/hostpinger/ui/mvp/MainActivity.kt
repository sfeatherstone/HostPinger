package uk.co.wedgetech.hostpinger.ui.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.wedgetech.hostpinger.R
import uk.co.wedgetech.hostpinger.UIMode
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.NetworkError
import uk.co.wedgetech.hostpinger.ui.common.HostsAdapter

class MainActivity : AppCompatActivity() {

    internal lateinit var hostsAdapter : HostsAdapter
    internal val uiMode : UIMode by lazy { UIMode(applicationContext) }
    internal val presenter : HostsPresenterContract<MainActivity> by lazy { HostsPresenter<MainActivity>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        hostsAdapter = HostsAdapter()

        presenter.attachView(this)

        presenter.onHostsChanged { list :List<Host> -> hostsAdapter.setHosts(list) }

        //Fetch hosts
        presenter.fetchHosts({}, //Successful fetch of hosts
                //Error in fetching hosts
                { error :NetworkError -> Toast.makeText(this@MainActivity,
                        getString(R.string.error_host_fetch),Toast.LENGTH_LONG).show() }
        )

        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler.adapter = hostsAdapter

        setupSpinner()
        setupAltFrameworkButton()
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    internal fun setupAltFrameworkButton() {
        val otherMode = uiMode.nextType.asString
        change_model.text = "Swap to $otherMode"
        change_model.setOnClickListener {
            uiMode.flipType()
            startActivity(uiMode.getCurrentScreenIntent(this))
        }
    }

    internal fun setupSpinner() {
        val adapter :ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
                R.array.sort_by_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        sortby_spinner.adapter = adapter

        // TODO -> sort order part of Presenter/ViewModel
        sortby_spinner.setSelection(uiMode.sortOrder)

        // Setup spinner for picking sort order
        sortby_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.sortOrder = position
                // TODO -> sort order part of Presenter/ViewModel
                uiMode.sortOrder = position
            }

        }

    }
}
