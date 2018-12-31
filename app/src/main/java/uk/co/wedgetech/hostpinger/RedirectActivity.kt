package uk.co.wedgetech.hostpinger

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RedirectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(UIMode(applicationContext).getCurrentScreenIntent(this))
        finish()
    }
}
