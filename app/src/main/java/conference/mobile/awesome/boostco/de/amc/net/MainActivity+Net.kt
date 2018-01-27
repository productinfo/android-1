package conference.mobile.awesome.boostco.de.amc.net

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.vicpin.krealmextensions.deleteAll
import com.vicpin.krealmextensions.save
import conference.mobile.awesome.boostco.de.amc.activity.fragment.MainActivity
import conference.mobile.awesome.boostco.de.amc.extension.arrayOfString
import conference.mobile.awesome.boostco.de.amc.extension.iterator
import conference.mobile.awesome.boostco.de.amc.model.Category
import conference.mobile.awesome.boostco.de.amc.model.Conference

/**
 * Created by matteocrippa on 26/01/2018.
 */

// wrap up easy method to get categories and conferences
fun MainActivity.getRemoteData(callback: (Boolean) -> Unit) {
    getRemoteCategories { success ->
        when (success) {
            false -> callback(false)
            true -> {
                getRemoteConferences {
                    when (it) {
                        false -> callback(false)
                        true -> callback(true)
                    }
                }
            }
        }
    }
}

// retrieves remote categories
fun MainActivity.getRemoteCategories(callback: (Boolean) -> Unit) {
    Fuel.request(AweConfApi.category()).responseJson { _, _, result ->
        result.fold(success = { json ->
            // easy access to response
            val response = json.obj()
            // check response
            if (response.optBoolean("success")) {
                // clean up db
                Category().deleteAll()
                // loop categories
                for (category in response.optJSONArray("category").arrayOfString()) {
                    Category(category).save()
                }
                callback(true)
            } else {
                callback(false)
            }
        }, failure = { error ->
            Log.e("api", error.message)
            callback(false)
        })
    }
}

// retrieve remote conferences
fun MainActivity.getRemoteConferences(callback: (Boolean) -> Unit) {
    Fuel.request(AweConfApi.list()).responseJson { _, _, result ->
        result.fold(success = { json ->
            // easy access to response
            val response = json.obj()
            // check response
            if (response.optBoolean("success")) {
                // clean up db
                Conference().deleteAll()
                // loop categories
                for (conference in response.optJSONArray("conferences").iterator()) {
                    Conference(conference).save()
                }
                callback(true)
            } else {
                callback(false)
            }
        }, failure = { error ->
            Log.e("api", error.message)
            callback(false)
        })
    }
}