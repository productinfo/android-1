package conference.mobile.awesome.boostco.de.amc.model

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey

/**
 * Created by matteocrippa on 26/01/2018.
 */

open class Category : RealmObject {
    @PrimaryKey
    var name = ""

    @LinkingObjects("category")
    val conferences: RealmResults<Conference>? = null

    constructor() : super()

    constructor(value: String) : super() {
        this.name = value
    }
}