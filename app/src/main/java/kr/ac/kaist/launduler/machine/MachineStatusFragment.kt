package kr.ac.kaist.launduler.machine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlinx.android.synthetic.main.fragment_status_machine.*
import kr.ac.kaist.launduler.R
import kr.ac.kaist.launduler.RxBus

/**
 * Created by kyukyukyu on 31/05/2017.
 */
class MachineStatusFragment : RxFragment() {
    lateinit var selectedDay: Calendar

    companion object {
        const val SELECTED_DAY = "SELECTED_DAY"
        val selectedDaySubject = RxBus.getSubject(RxBus.SUBJECT_EXPLORE_SELECTED_DAY)

        fun newInstance(selectedDay: Calendar? = null) : MachineStatusFragment {
            val fragment = MachineStatusFragment()
            val args = Bundle()
            args.putSerializable(SELECTED_DAY, selectedDay)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = savedInstanceState ?: arguments
        val bundleSelectedDay = bundle?.getSerializable(SELECTED_DAY) as Calendar?
        if (bundleSelectedDay != null) {
            selectedDay = bundleSelectedDay
        } else {
            // This is the first creation of this fragment.
            selectedDay = Calendar.getInstance()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater!!.inflate(R.layout.fragment_status_machine, container, false)!!

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weekFragment = WeekFragment.newInstance(Calendar.getInstance(), RxBus.SUBJECT_EXPLORE_SELECTED_DAY)
        childFragmentManager
                .beginTransaction()
                .replace(R.id.week, weekFragment, "WEEK")
                .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        day.setMonthChangeListener { _, _ -> listOf() }
        day.setScrollListener { newFirstVisibleDay, _ -> selectedDaySubject.onNext(newFirstVisibleDay) }
        selectedDaySubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(this)
                .subscribe { day.goToDate(it as Calendar) }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(SELECTED_DAY, selectedDay)
    }
}