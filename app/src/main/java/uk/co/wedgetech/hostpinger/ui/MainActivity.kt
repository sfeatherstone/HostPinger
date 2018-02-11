package uk.co.wedgetech.hostpinger.ui

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
import uk.co.wedgetech.hostpinger.model.Host
import uk.co.wedgetech.hostpinger.model.NetworkError

class MainActivity : AppCompatActivity() {

    internal lateinit var hostsAdapter :HostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        hostsAdapter = HostsAdapter()

        //Catch host data
        val viewModel : HostsViewModel = ViewModelProviders.of(this).get(HostsViewModel::class.java)
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
        viewModel.getHosts()

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = hostsAdapter

        setupSpinner()
    }

    internal fun setupSpinner() {
        val adapter :ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,
                R.array.sort_by_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        sortby_spinner.adapter = adapter

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
