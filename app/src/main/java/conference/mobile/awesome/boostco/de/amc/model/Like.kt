package conference.mobile.awesome.boostco.de.amc.model

import android.content.Context
import com.securepreferences.SecurePreferences

/**
 * Created by matteocrippa on 29/01/2018.
 */

class Like private constructor() {
    // singleton
    private object Holder {
        val INSTANCE = Like()
    }

    companion object {
        val shared: Like by lazy { Holder.INSTANCE }
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

}