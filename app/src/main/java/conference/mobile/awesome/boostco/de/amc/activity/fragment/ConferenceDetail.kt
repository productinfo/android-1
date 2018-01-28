package conference.mobile.awesome.boostco.de.amc.activity.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import conference.mobile.awesome.boostco.de.amc.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ConferenceDetail.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ConferenceDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConferenceDetail : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conference_detail, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConferenceDetail.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ConferenceDetail {
            val fragment = ConferenceDetail()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
