package conference.mobile.awesome.boostco.de.amc.activity.fragment

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.onesignal.OneSignal
import com.vicpin.krealmextensions.queryAll
import conference.mobile.awesome.boostco.de.amc.R
import conference.mobile.awesome.boostco.de.amc.model.Category
import conference.mobile.awesome.boostco.de.amc.net.getRemoteCategories
import conference.mobile.awesome.boostco.de.amc.net.getRemoteConferences
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import matteocrippa.it.karamba.toCamelCase
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var fragmentList: ArrayList<WeakReference<Fragment>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // check if back is needed
        supportActionBar?.setDisplayHomeAsUpEnabled(intent.getBooleanExtra("backButton", false))

        fab.setOnClickListener { view ->
            // TODO: open Conference Submit
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // Setup realm
        Realm.init(applicationContext)
        val config = RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
        Realm.getInstance(config)

        // setup Onesignal
        OneSignal.startInit(applicationContext)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()

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

        // add category list
        supportFragmentManager.beginTransaction().replace(R.id.frame_content, ConferenceList.newInstance("")).commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Home" -> {
                // is home
            }
            else -> {
                // need to figure out
                Log.d("menu", item.title.toString())
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        fragment?.let { fragmentList.add(WeakReference(it)) }
    }

    private fun addCategoryMenuOptions() {
        // quick access to nav menu
        val menu = nav_view.menu

        // clear all
        menu.clear()

        // add home menu
        menu.add("Home")

        // retrieve all the categories
        Category().queryAll()
                .sortedBy {
                    it.name
                }
                .forEach {
                    menu.add(it.name.toCamelCase())
                }
    }
}
