package conference.mobile.awesome.boostco.de.amc.activity.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import conference.mobile.awesome.boostco.de.amc.R

class ConferenceSubmit : Fragment() {

    private var category: String? = null
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            category = arguments!!.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conference_submit, container, false)
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

    interface OnFragmentInteractionListener {
        fun onConferenceSubmit(hashMap: HashMap<String, Any>)
    }

    companion object {
        private val ARG_PARAM1 = "category"

        fun newInstance(category: String?): ConferenceSubmit {
            val fragment = ConferenceSubmit()
            val args = Bundle()
            args.putString(ARG_PARAM1, category)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
