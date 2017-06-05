package kr.ac.kaist.launduler.reservation

import android.os.Bundle
import android.support.v7.app.ActionBar
import kr.ac.kaist.launduler.BaseSelectPlaceActivity
import kr.ac.kaist.launduler.R
import kr.ac.kaist.launduler.models.Place

class ChangeSelectPlaceActivity : BaseSelectPlaceActivity() {
    override fun setupToolbar(ab: ActionBar?) {
        ab?.setDisplayHomeAsUpEnabled(true)
        ab?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter?.positionClicks?.subscribe(this::selectMachine)
    }

    fun selectMachine(place : Place) {
        // TODO: Jump to Select a Machine screen.
    }
}
