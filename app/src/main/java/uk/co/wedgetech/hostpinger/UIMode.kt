package uk.co.wedgetech.hostpinger

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.content.edit

/***
 * Class stores the current UI mode. It also can be used to find what the next one is, or flip it and get the Intent.
 */
//TODO get appContext from DI
class UIMode(applicationContext : Context) {

    sealed class Framework {
        object MVVM : Framework() {
            override val asString: String
                    get() = MVVM_NAME
        }
        object MVP : Framework(){
            override val asString: String
                    get() = MVP_NAME
        }
        abstract val asString : String
    }

    internal val sharedPreferences : SharedPreferences

    init {
        sharedPreferences = applicationContext.getSharedPreferences(FILE_NAME, 0)
    }

    fun getCurrentScreenIntent(context: Context): Intent {
        return when(currentType) {
            Framework.MVVM -> Intent(context, uk.co.wedgetech.hostpinger.ui.mvvm.MainActivity::class.java)
            Framework.MVP -> Intent(context, uk.co.wedgetech.hostpinger.ui.mvp.MainActivity::class.java)
        }
    }

    val currentType
        get() = stringToFramework(sharedPreferences.getString(KEY_MODE, Framework.MVP.asString))

    val nextType
        get() = when(currentType) {
            Framework.MVVM -> Framework.MVP
            Framework.MVP -> Framework.MVVM
        }

    fun flipType() {
        sharedPreferences.edit() {
            putString(KEY_MODE, nextType.asString)
        }
    }

    var sortOrder : Int
        get() = sharedPreferences.getInt(KEY_SORT_ORDER, 0)
        set(value) { sharedPreferences.edit(){putInt(KEY_SORT_ORDER, value)}}

    internal fun stringToFramework(str :String): Framework = when(str) {
        Framework.MVP.asString -> Framework.MVP
        Framework.MVVM.asString -> Framework.MVVM
        else -> Framework.MVVM
    }

    companion object {
        //Sort order
        const val BY_NAME = 0
        const val BY_URL = 1
        const val BY_LATENCY= 2


        private val MVVM_NAME = "MVVM"
        private val MVP_NAME = "MVP"

        private val KEY_SORT_ORDER = "sortOrder"
        private val KEY_MODE = "mode"

        private val FILE_NAME = "UIMode"
     }
}