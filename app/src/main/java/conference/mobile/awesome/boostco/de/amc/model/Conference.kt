package conference.mobile.awesome.boostco.de.amc.model

import com.vicpin.krealmextensions.queryFirst
import conference.mobile.awesome.boostco.de.amc.extension.arrayOfString
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import matteocrippa.it.karamba.toDate
import org.json.JSONObject
import java.util.*

/**
 * Created by matteocrippa on 26/01/2018.
 */

open class Conference : RealmObject {
    @PrimaryKey
    var id = ""
    var title = ""
    var year = 0
    var startDate = Date()
    var endDate = Date()
    var city = ""
    var country = ""
    var where = ""
    var homepage = ""
    var callForPaper = false
    var emojiFlag = ""
    var isNew = false
    var twitter = ""
    var approved = false
    var lat: Double = 0.0
    var lon: Double = 0.0
    var added = Date()
    var topic = RealmList<Topic>()
    var category = RealmList<Category>()

    constructor() : super() {}
    constructor(json: JSONObject) : super() {
        mapping(json)
    }

    private fun mapping(json: JSONObject) {
        this.id = json.optString("_id")
        this.title = json.optString("title")
        this.year = json.optInt("year")
        this.startDate = json.optString("startdate").toDate("yyyy-MM-dd")
        this.endDate = json.optString("enddate").toDate("yyyy-MM-dd")
        this.city = json.optString("city")
        this.where = json.optString("where")
        this.homepage = json.optString("homepage")
        this.callForPaper = json.optBoolean("callforpaper")
        this.emojiFlag = json.optString("emojiflag")
        this.isNew = json.optBoolean("isNew")
        this.twitter = json.optString("twitter")
        this.approved = json.optBoolean("approved")
        this.lat = json.optDouble("lat")
        this.lon = json.optDouble("lon")
        this.added = json.optString("added").toDate("yyyy-MM-dd")


        // categories
        for (cat in json.getJSONArray("category").arrayOfString()) {
            Category().queryFirst { query -> query.equalTo("name", cat) }?.let {
                this.category.add(it)
            }
        }

        // topic
        for (topic in json.getJSONArray("topic").arrayOfString()) {
            Topic().queryFirst { query -> query.equalTo("name", topic) }?.let {
                this.topic.add(it)
            }
        }
    }
}