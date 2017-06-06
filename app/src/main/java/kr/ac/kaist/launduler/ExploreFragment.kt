package kr.ac.kaist.launduler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_explore.*
import kr.ac.kaist.launduler.ExploreActivity.Companion.REQUEST_SELECT_PLACE
import kr.ac.kaist.launduler.ExploreSelectPlaceActivity.EXTRA_PLACE_ID
import kr.ac.kaist.launduler.services.laundulerService

/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment : RxFragment(), OptionsMenuFragment {
    override val menuResId: Int = R.menu.explore
    var selectedPlaceId = -1L

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Load selected place ID from shared preferences or have it set if it's not set.
        val sharedPreferences =
                activity.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val strSelectedPlaceId = getString(R.string.selected_place_id)
        if (sharedPreferences.contains(strSelectedPlaceId)) {
            val placeId = sharedPreferences.getLong(strSelectedPlaceId, -1)
            retrieveMachineList(placeId)
        } else {
            selectPlace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            // This cannot happen.
            throw RuntimeException("unexpected behavior")
        }
        when (requestCode) {
            REQUEST_SELECT_PLACE -> {
                val placeId = data?.getLongExtra(EXTRA_PLACE_ID, -1) ?: -1
                if (placeId < 0) {
                    throw IllegalArgumentException("invalid place ID")
                }
                selectedPlaceId = placeId
                val sharedPreferences =
                        activity.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                val strSelectedPlaceId = getString(R.string.selected_place_id)
                sharedPreferences.edit()
                        .putLong(strSelectedPlaceId, placeId)
                        .apply()
                retrieveMachineList(placeId)
            }
        }
    }

    fun retrieveMachineList(placeId: Long) {
    }

    private fun selectPlace() {
        val intent = Intent(activity, ExploreSelectPlaceActivity::class.java)
        intent.putExtra(EXTRA_PLACE_ID, selectedPlaceId)
        startActivityForResult(intent, REQUEST_SELECT_PLACE)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_select_place -> selectPlace()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): ExploreFragment {
            val fragment = ExploreFragment()
            return fragment
        }
    }
}// Required empty public constructor
