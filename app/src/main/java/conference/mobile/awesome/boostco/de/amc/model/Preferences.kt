package conference.mobile.awesome.boostco.de.amc.model

import android.content.Context
import com.securepreferences.SecurePreferences
import java.util.*

/**
 * Created by matteocrippa on 29/01/2018.
 */

class Preferences private constructor() {
    // singleton
    private object Holder {
        val INSTANCE = Preferences()
    }

    companion object {
        val shared: Preferences by lazy { Holder.INSTANCE }
    }

    // init class
    init {
    }

    // private
    private val shared by lazy {
        SecurePreferences(context)
    }

    var context: Context? = null

    fun getLike(id: String?): Boolean {
        id?.let {
            val favoriteIdentifier = "LIKE/$it"
            return shared.getBoolean(favoriteIdentifier, false)
        }
        return false
    }

    fun triggerLike(id: String?) {
        id?.let {
            val favoriteIdentifier = "LIKE/$id"
            val current = shared.getBoolean(favoriteIdentifier, false)
            shared.edit().putBoolean(favoriteIdentifier, !current).commit()
        }
    }

    fun getSubscription(category: String?): Boolean {
        val favoriteIdentifier = "SUB/$category"
        return shared.getBoolean(favoriteIdentifier, false)
    }

    fun triggerSubscription(category: String?) {
        val favoriteIdentifier = "SUB/$category"
        val current = shared.getBoolean(favoriteIdentifier, false)
        shared.edit().putBoolean(favoriteIdentifier, !current).commit()
    }

    fun getLastVisit(): Date {
        return Date(shared.getLong("lastVisit", 0))
    }

    fun setLastVisit() {
        shared.edit().putLong("lastVisit", Date().time).commit()
    }

}