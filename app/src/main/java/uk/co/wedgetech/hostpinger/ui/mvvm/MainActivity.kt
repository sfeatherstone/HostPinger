package uk.co.wedgetech.hostpinger.ui.mvvm

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.wedgetech.hostpinger.R
import uk.co.wedgetech.hostpinger.UIMode
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.NetworkError

class MainActivity : AppCompatActivity() {

    internal lateinit var hostsAdapter : HostsAdapter
    internal val viewModel : HostsViewModel by lazy { ViewModelProviders.of(this).get(HostsViewModel::class.java) }
    internal val uiMode : UIMode by lazy { UIMode(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        hostsAdapter = HostsAdapter()

        //Catch host data
        val hostsObserver = Observer<List<Host>> { hosts ->
            if (hosts!=null)    {
                hostsAdapter.setHosts(hosts)
            }
        }
        viewModel.hosts.observe(this, hostsObserver)

        //Catch errors
        val errorObserver = Observer<NetworkError> { t ->
            if (t!=null) {
                Toast.makeText(this@MainActivity, getString(R.string.error_host_fetch),
                        Toast.LENGTH_LONG).show()
            }
        }
        viewModel.error.observe(this, errorObserver)

        //Fetch hosts
        viewModel.fetchHosts()

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = hostsAdapter

        setupSpinner()
        setupAltFrameworkButton()
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
                viewModel.sortOrder = position
                // TODO -> sort order part of Presenter/ViewModel
                uiMode.sortOrder = position
            }
        }
    }

}
