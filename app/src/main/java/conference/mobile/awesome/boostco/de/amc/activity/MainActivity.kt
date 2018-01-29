package conference.mobile.awesome.boostco.de.amc.activity.fragment

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.onesignal.OneSignal
import com.vicpin.krealmextensions.queryAll
import conference.mobile.awesome.boostco.de.amc.R
import conference.mobile.awesome.boostco.de.amc.extension.getActiveFragments
import conference.mobile.awesome.boostco.de.amc.model.Category
import conference.mobile.awesome.boostco.de.amc.model.Conference
import conference.mobile.awesome.boostco.de.amc.model.Preferences
import conference.mobile.awesome.boostco.de.amc.net.getRemoteCategories
import conference.mobile.awesome.boostco.de.amc.net.getRemoteConferences
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import matteocrippa.it.karamba.toCamelCase
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ConferenceList.OnFragmentInteractionListener, ConferenceSubmit.OnFragmentInteractionListener, ConferenceDetail.OnFragmentInteractionListener {

    private var lastTitle = ""
    var fragmentList: ArrayList<WeakReference<Fragment>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // check if back is needed
        supportActionBar?.setDisplayHomeAsUpEnabled(intent.getBooleanExtra("backButton", false))

        fab.setOnClickListener {
            // set title
            setTitle("Submit a new Conference")
            // set fragment
            val fragment = ConferenceSubmit()
            val fm = supportFragmentManager.beginTransaction().replace(R.id.frame_content, fragment)
            fm.addToBackStack(fragment.javaClass.name)
            fm.commit()
        }

        // Realm
        Realm.init(applicationContext)
        val config = RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
        Realm.getInstance(config)

        // OneSignal
        OneSignal.startInit(applicationContext)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()

        // Like setup
        Preferences.shared.context = applicationContext

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // use category to populate menu
        addCategoryMenuOptions()

        // retrieve cats
        getRemoteCategories {
            when (it) {
                true -> {
                    // update cats
                    addCategoryMenuOptions()
                    // download conferences
                    getRemoteConferences { }
                }
                else -> {
                }
            }
        }

        // set generic title
        title = "All Conferences"

        // show all category list
        supportFragmentManager.beginTransaction().replace(R.id.frame_content, ConferenceList.newInstance("*")).commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            title = lastTitle
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Home" -> {
                setTitle("All Conferences")
                supportFragmentManager.beginTransaction().replace(R.id.frame_content, ConferenceList.newInstance("*")).commit()
            }
            else -> {
                setTitle(item.title.toString().toCamelCase())
                supportFragmentManager.beginTransaction().replace(R.id.frame_content, ConferenceList.newInstance(item.title.toString().toLowerCase())).commit()
            }
        }

        nav_view.setCheckedItem(item.itemId)

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        fragment?.let { fragmentList.add(WeakReference(it)) }
    }

    // manage dynamic menu contents
    private fun addCategoryMenuOptions() {
        // quick access to nav menu
        val menu = nav_view.menu

        // clear all
        menu.clear()

        // add home menu
        menu.add("Home")

        // add categories
        val catMenu = menu.addSubMenu("Categories")
        catMenu.setGroupCheckable(0, true, true)

        // retrieve all the categories
        Category().queryAll()
                .sortedBy {
                    it.name
                }
                .forEach {
                    catMenu.add(it.name.toCamelCase())
                }
    }

    // manages title stack
    private fun setTitle(title: String) {
        lastTitle = this.title.toString()
        this.title = title
    }

    // 🎧 Conference List
    override fun onConferenceListSelected(conference: Conference) {
        // set title
        setTitle(conference.title)
        // set fragment
        val fragment = ConferenceDetail.newInstance(conference.id)
        val fm = supportFragmentManager.beginTransaction().replace(R.id.frame_content, fragment)
        fm.addToBackStack(fragment.javaClass.name)
        fm.commit()
    }

    override fun onConferenceRefresh(category: String?) {
        getRemoteConferences {
            when (it) {
                true -> {
                    getActiveFragments().forEach { fragment ->
                        if (fragment is ConferenceList) {
                            fragment.filterByCategory(category)
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    override fun onConferenceSubscribeNotification(subscribed: Boolean, category: String?) {
        category?.let {
            when (subscribed) {
                true -> {
                    OneSignal.sendTag(it, it)
                }
                false -> {
                    OneSignal.deleteTag(it)
                }
            }
        }
    }

    // 🎧 Conference Submit
    override fun onConferenceSubmit(hashMap: HashMap<String, Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
