package conference.mobile.awesome.boostco.de.amc.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
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
        this.id = json.getString("_id")

        /*
        // categories
        for (cat in json.getJSONArray("categories").iteratorInt()) {
            Category().queryFirst { query -> query.equalTo("id", cat) }?.let { item ->
                this.categories.add(item)
            }
        }
         */
    }
}