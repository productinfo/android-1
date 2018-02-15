package conference.mobile.awesome.boostco.de.amc.activity.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.BaseAdapter
import com.vicpin.krealmextensions.query
import conference.mobile.awesome.boostco.de.amc.R
import conference.mobile.awesome.boostco.de.amc.model.Conference
import conference.mobile.awesome.boostco.de.amc.model.Preferences
import kotlinx.android.synthetic.main.cell_conference_list.view.*
import kotlinx.android.synthetic.main.cell_conference_list_header.view.*
import kotlinx.android.synthetic.main.fragment_conference_list.*
import matteocrippa.it.fragmentcontextivity.context
import matteocrippa.it.karamba.convertTo
import matteocrippa.it.karamba.month
import matteocrippa.it.karamba.monthName
import matteocrippa.it.karamba.year
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onQueryTextListener
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.onRefresh
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter
import kotlin.properties.Delegates

class ConferenceList : Fragment() {

    private var category: String? = null
    private var adapter: ConferenceAdapter? = null
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            category = arguments!!.getString(ARG_PARAM1)
        }

        // force download in background
        mListener?.onConferenceRefresh(category)

        // add menu
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_conference_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.add("Subscribe Notifications")
        if (Preferences.shared.getSubscription(category)) {
            menu?.getItem(0)?.setIcon(R.drawable.ic_notifications_off)

        } else {
            menu?.getItem(0)?.setIcon(R.drawable.ic_notifications_on)
        }
        menu?.getItem(0)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            0 -> {

                val catText = if (category == "*") "all" else category
                val actionText = if (Preferences.shared.getSubscription(category)) "unsubscribe" else "subscribe"

                alert("Would you like to $actionText to $catText notifications?", "Push Notifications") {
                    positiveButton("Yes") {
                        // update value in memory
                        Preferences.shared.triggerSubscription(category)
                        // add onesignal event
                        mListener?.onConferenceSubscribeNotification(Preferences.shared.getSubscription(category), category)
                        // reload menu
                        activity?.invalidateOptionsMenu()
                    }
                    negativeButton("No") { }
                }.show()

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
        // set swipe color
        conferenceListSwipeContainer?.setColorSchemeResources(R.color.colorAccent)
        // set swipe action
        conferenceListSwipeContainer?.onRefresh {
            mListener?.onConferenceRefresh(category)
        }
        // set adapter
        adapter = ConferenceAdapter(this@ConferenceList.context())
        conferenceList.adapter = adapter

        // set filter search
        conferenceListSearch.onQueryTextListener {
            onQueryTextChange { q ->
                filterByCategory(category, q)
                false
            }
            onQueryTextSubmit { _ ->
                false
            }
        }

        // show data locally
        filterByCategory(category)

    }

    interface OnFragmentInteractionListener {
        fun onConferenceListSelected(conference: Conference)
        fun onConferenceRefresh(category: String?)
        fun onConferenceSubscribeNotification(subscribed: Boolean, category: String?)
    }

    companion object {
        private val ARG_PARAM1 = "category"

        fun newInstance(category: String): ConferenceList {
            val fragment = ConferenceList()
            val args = Bundle()
            args.putString(ARG_PARAM1, category)
            fragment.arguments = args
            return fragment
        }
    }

    fun filterByCategory(category: String?, search: String? = null) {
        this.category = category

        adapter?.data = Conference().query { query ->
            // filter by category
            category?.let {
                if (it != "*") {
                    query.`in`("category.name", arrayOf(it))
                }
            }

            // filter by text
            search?.let {
                query
                        .contains("title", it.toLowerCase())
                        .or()
                        .contains("where", it.toLowerCase())
            }
        }.sortedBy {
                    it.startDate
                }


        // force hide the spinner
        conferenceListSwipeContainer?.isRefreshing = false
    }

    // Conference adapter
    private inner class ConferenceAdapter(context: Context) : BaseAdapter(), StickyListHeadersAdapter {
        private val mInflator: LayoutInflater = LayoutInflater.from(context)

        var data: List<Conference> by Delegates.observable(listOf()) { _, _, _ ->
            this.notifyDataSetChanged()
        }

        override fun getCount(): Int {
            return data.count()
        }

        override fun getItem(p0: Int): Any {
            return data[p0]
        }

        override fun getItemId(p0: Int): Long {
            return (p0).toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            // current item
            val item = getItem(p0) as Conference

            // view
            var view = p1

            // use view holder
            if (view == null) {
                view = this.mInflator.inflate(R.layout.cell_conference_list, p2, false)
            }

            // set name
            view?.cellConferenceTitle?.text = item.title
            view?.cellConferenceFlag?.text = item.emojiFlag

            if (item.startDate != item.endDate) {
                view?.cellConferenceDate?.text = item.startDate?.convertTo("dd") + " - " + item.endDate?.convertTo("dd")
            } else {
                view?.cellConferenceDate?.text = item.startDate?.convertTo("dd")
            }


            // set new badge
            view?.cellConferenceIsNew?.visibility = if (item.isNew) View.VISIBLE else View.GONE

            // set onclick
            view?.onClick {
                mListener?.onConferenceListSelected(item)
            }

            // manage favorite button color
            view?.cellConferenceFavoriteButton?.imageResource = if (Preferences.shared.getLike(item.id)) R.drawable.ic_star_selected else R.drawable.ic_star_deselected

            // add on click for favorite
            view?.cellConferenceFavoriteButton?.onClick {
                Preferences.shared.triggerLike(item.id)
                // force update the list
                this@ConferenceAdapter.notifyDataSetChanged()
            }

            return view!!
        }

        override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup?): View {
            // current item
            val item = getItem(position) as Conference

            // view
            var view = convertView

            // use view holder
            if (view == null) {
                view = this.mInflator.inflate(R.layout.cell_conference_list_header, parent, false)
            }

            // populate cell
            item.let { conference ->
                val title = "${conference.startDate.year()} / ${item.startDate.monthName().capitalize()}"
                view?.cellConferenceHeaderTitle?.text = title
            }

            return view!!
        }

        override fun getHeaderId(position: Int): Long {
            val item = getItem(position) as Conference
            item.let { conference ->
                return (conference.startDate.year().toLong() * 1000) + conference.startDate.month().toLong()
            }
        }
    }
}