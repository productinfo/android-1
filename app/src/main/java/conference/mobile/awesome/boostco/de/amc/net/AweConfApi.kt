package conference.mobile.awesome.boostco.de.amc.net

import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.util.FuelRouting
import conference.mobile.awesome.boostco.de.amc.model.Conference

/**
 * Created by matteocrippa on 26/01/2018.
 */

sealed class AweConfApi : FuelRouting {
    override val basePath = "https://aweconf.heroku.com/api"

    // conferences
    class list: AweConfApi()
    class category: AweConfApi()
    class submit(val conference: Conference): AweConfApi()

    override val method: Method
        get() {
            return when (this) {
                is list -> Method.GET
                is category -> Method.GET
                else -> Method.POST
            }
        }

    override val path: String
        get() {
            return when (this) {
                is list -> "/conference"
                is category -> "/categories"
                is submit -> "/conference/submit"
            }
        }

    override val params: List<Pair<String, Any?>>?
        get() {
            return when (this) {
                is list -> listOf()
                is category -> listOf()
                is submit -> listOf()
                /*
                title: req.body.title,
    year: new Date(req.body.startdate).getFullYear(),
    startdate: req.body.startdate,
    enddate: req.body.enddate,
    city: req.body.city,
    country: req.body.country,
    where: req.body.where,
    category: req.body.category,
    homepage: req.body.homepage,
    callforpaper: req.body.callforpaper,
    twitter: req.body.twitter,
    emojiflag: emojiflag,
    isnew: true
                 */
            }
        }

    override val headers: Map<String, String>?
        get() {
            return null
        }
}