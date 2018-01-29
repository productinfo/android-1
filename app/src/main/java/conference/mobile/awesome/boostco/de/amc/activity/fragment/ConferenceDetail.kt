package conference.mobile.awesome.boostco.de.amc.activity.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vicpin.krealmextensions.queryFirst
import conference.mobile.awesome.boostco.de.amc.R
import conference.mobile.awesome.boostco.de.amc.model.Conference
import conference.mobile.awesome.boostco.de.amc.model.Like
import kotlinx.android.synthetic.main.fragment_conference_detail.*
import matteocrippa.it.karamba.convertTo

class ConferenceDetail : Fragment() {

    private var conferenceId: String? = null
    private var conference: Conference? = null
    private var savedInstanceState: Bundle? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        if (arguments != null) {
            conferenceId = arguments!!.getString(ARG_PARAM1)

            // retrieve current conf
            conference = Conference().queryFirst { it.equalTo("id", conferenceId) }
        }

        // add menu
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_conference_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.add("Like")
        if (Like.shared.getLike(conferenceId)) {
            menu?.getItem(0)?.setIcon(R.drawable.ic_star_selected)
        } else menu?.getItem(0)?.setIcon(R.drawable.ic_star_deselected)
        menu?.getItem(0)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            0 -> {
                triggerLike(conferenceId)
            }
            else -> {

            }
        }
        return true
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

        // populate fields
        conferenceDetailTitle?.text = conference?.title ?: ""
        conferenceDetailCountry?.text = conference?.country ?: ""
        conferenceDetailCity?.text = conference?.city ?: ""
        conferenceDetailStartDate?.text = conference?.startDate?.convertTo("yyyy/MM/dd")
        conferenceDetailEndDate?.text = conference?.endDate?.convertTo("yyyy/MM/dd")
        conferenceDetailWebsite?.text = conference?.homepage ?: ""

        conferenceDetailCallForPaper?.visibility = if (conference?.callForPaper == true) View.VISIBLE else View.GONE

        // by default hide topics
        conferenceDetailCategoryTitle?.visibility = View.GONE
        conferenceDetailCategory?.visibility = View.GONE

        conference?.category?.let {
            if (it.count() > 0) {
                conferenceDetailCategoryTitle?.visibility = View.VISIBLE
                conferenceDetailCategory?.visibility = View.VISIBLE

                conferenceDetailCategory?.text = it.map { category -> category.name }.reduce { acc, category ->
                    acc + ", $category"
                }
            }
        }

        // setup map
        conferenceDetailMap?.onCreate(savedInstanceState)
        conferenceDetailMap?.onResume()
        conferenceDetailMap?.getMapAsync { map ->
            val position = LatLng(conference?.lat ?: 0.0, conference?.lon ?: 0.0)
            // add marker
            val marker = map.addMarker(MarkerOptions().position(position).title(conference?.where))
            // force title to be visible
            marker.showInfoWindow()
            // move camera
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13.0f))
        }
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

    private fun triggerLike(conferenceId: String?) {
        conference?.let {
            Like.shared.triggerLike(it.id)
            activity?.invalidateOptionsMenu()
        }
    }
}