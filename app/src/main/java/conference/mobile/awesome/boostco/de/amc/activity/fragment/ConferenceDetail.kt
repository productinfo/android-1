package conference.mobile.awesome.boostco.de.amc.activity.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vicpin.krealmextensions.queryFirst
import conference.mobile.awesome.boostco.de.amc.R
import conference.mobile.awesome.boostco.de.amc.model.Conference

class ConferenceDetail : Fragment() {

    private var conferenceId: String? = null
    private var conference: Conference? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            conferenceId = arguments!!.getString(ARG_PARAM1)

            // retrieve current conf
            conference = Conference().queryFirst { it.equalTo("id", conferenceId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_conference_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun setupView() {
/*
        // populate fields
        conferenceDetailTitle?.text = conference.title ?: ""
        conferenceDetailCountry?.text = conference.country ?: ""
        conferenceDetailCity?.text = conference.city ?: ""
        conferenceDetailStartDate?.text = conference.startDate?.convertTo("yyyy/MM/dd")
        conferenceDetailEndDate?.text = conference.endDate?.convertTo("yyyy/MM/dd")
        conferenceDetailWebsite?.text = conference.homepage ?: ""

        conferenceDetailCallForPaper?.visibility = if (conference.callForPaper) View.VISIBLE else View.GONE

        if (conference.topics.count() > 0) {
            conferenceDetailTopicsTitle?.visibility = View.VISIBLE
            conferenceDetailTopics?.visibility = View.VISIBLE
            conferenceDetailTopics?.text = conference.topics.reduce { acc, topic ->
                acc + ", $topic"
            }
        } else {
            conferenceDetailTopicsTitle?.visibility = View.GONE
            conferenceDetailTopics?.visibility = View.GONE
        }

        // setup map
        conferenceDetailMap?.onCreate(savedInstanceState)
        conferenceDetailMap?.onResume()
        conferenceDetailMap?.getMapAsync { map ->
            // create poi marker
            val poiPosition = getLocationFromAddress(conference.where ?: "")
            // add marker
            val marker = map.addMarker(MarkerOptions().position(poiPosition ?: LatLng(0.0, 0.0)).title(conference.where))
            // force title to be visible
            marker.showInfoWindow()
            // move camera
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(poiPosition ?: LatLng(0.0, 0.0), 13.0f))
        }*/
    }

    interface OnFragmentInteractionListener {}

    companion object {
        private val ARG_PARAM1 = "conferenceId"

        fun newInstance(conferenceId: String): ConferenceDetail {
            val fragment = ConferenceDetail()
            val args = Bundle()
            args.putString(ARG_PARAM1, conferenceId)
            fragment.arguments = args
            return fragment
        }
    }
}