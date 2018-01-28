package conference.mobile.awesome.boostco.de.amc.extension

import android.content.Intent
import android.support.v4.app.Fragment
import conference.mobile.awesome.boostco.de.amc.activity.fragment.MainActivity

/**
 * Created by matteocrippa on 28/01/2018.
 */

fun MainActivity.startActivityFor(list: List<Pair<String, String>>? = null, cls: Class<*>, allowBack: Boolean = false, isFinal: Boolean = false) {

    // create intent
    val intent = Intent(applicationContext, cls)

    // check if list is not null
    list?.let {
        // loop params
        for ((name, value) in it) {
            // pass param
            intent.putExtra(name, value)
        }
    }

    // set back
    intent.putExtra("backButton", allowBack)

    // prepare intent
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

    // start intent
    startActivity(intent)

    // check if final
    if (isFinal) {
        finish()
    }
}

fun MainActivity.getActiveFragments(): List<Fragment> {
    val ret = ArrayList<Fragment>()
    fragmentList.forEach { ref ->
        val f = ref.get()
        f?.let {
            ret.add(it)
        }
    }
    return ret
}

fun MainActivity.cleanActiveFragments() {
    fragmentList.removeAll(ArrayList())
}
