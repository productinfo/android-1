package conference.mobile.awesome.boostco.de.amc.extension

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by matteocrippa on 26/01/2018.
 */

fun JSONArray.arrayOfString(): Iterator<String> = (0 until length()).asSequence().map { get(it) as String }.iterator()

operator fun JSONArray.iterator(): Iterator<JSONObject> = (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()