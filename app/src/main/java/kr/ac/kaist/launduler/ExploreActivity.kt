package kr.ac.kaist.launduler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kr.ac.kaist.launduler.machine.ExploreMachineStatusFragment
import kr.ac.kaist.launduler.machine.MachineStatusFragment
import kr.ac.kaist.launduler.models.Reservation
import kr.ac.kaist.launduler.services.laundulerService
import java.util.*

class ExploreActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val REQUEST_SELECT_PLACE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val fragment = ExploreFragment.newInstance()
            val fragmentManager = supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment, "EXPLORE")
                    .commit()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.explore, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentManager = supportFragmentManager

        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_explore) {
            val selectedMachineId = selectedMachineId
            val fragment = MachineStatusFragment.newInstance<ExploreMachineStatusFragment>(null, selectedMachineId)

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment, "EXPLORE")
                    .commit()
        } else if (id == R.id.nav_reservation) {
        } else if (id == R.id.nav_timer) {



            laundulerService.getReservation(userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    //.bindToLifecycle()
                    .subscribe(Consumer<kotlin.Any> { o ->
                Log.d("TimerFragment", "Success")
                val result = o as List<Reservation>
                var start: Calendar
                var end = Calendar.getInstance()
                var cur = Calendar.getInstance()
                val curStamp = cur.timeInMillis
                var idx: Int
                Log.d("TimerFragment", cur.toString())
                idx = 0
                while (idx < result.size) {
                    start = result[idx].scheduledAt
                    end = result[idx].endsAt
                    Log.d("TimerFragment", start.timeInMillis.toString())
                    Log.d("TimerFragment", curStamp.toString())
                    Log.d("TimerFragment", end.timeInMillis.toString())

                    if (start.timeInMillis < curStamp && end.timeInMillis > curStamp) {
                        Log.d("TimerFragment", "Bingo! " + idx.toString())
                        break
                    }
                    idx++

                }
                val layout:Int
                var remain:Long = 0
                if (idx != result.size) {

                    remain = (end.timeInMillis - curStamp) / 1000 / 60
                    Log.d("TimerFragment", end.toString())
                    if (remain >= 10)
                        layout = R.layout.fragment_timer
                    else
                        layout = R.layout.fragment_finished
                } else
                    layout = R.layout.fragment_noreservation
                val fragment = TimerFragment.newInstance(layout,remain)
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment, "TIMER")
                                .commit()
            }, Consumer<kotlin.Throwable> { throwable -> throwable.printStackTrace() })


        } else if (id == R.id.nav_tips) {

        } else if (id == R.id.nav_alternative) {

        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    protected val selectedMachineId: String
        get() = "selectedMachine1"

    protected val userId: String
        get() = "mgjin"
}
