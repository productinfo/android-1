package conference.mobile.awesome.boostco.de.amc.net

import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.util.FuelRouting
import conference.mobile.awesome.boostco.de.amc.model.Conference

/**
 * Created by matteocrippa on 26/01/2018.
 */

sealed class AweConfApi : FuelRouting {
    override val basePath = "https://core.aweconf.com/api"

    // conferences
    class list : AweConfApi()

    class category : AweConfApi()
    class submit(val conference: Conference) : AweConfApi()

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
            }
        }

    override val headers: Map<String, String>?
        get() {
            return null
        }
}