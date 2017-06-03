package kr.ac.kaist.launduler.machine

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alamkanak.weekview.WeekViewEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlinx.android.synthetic.main.fragment_status_machine.*
import kr.ac.kaist.launduler.R
import kr.ac.kaist.launduler.RxBus
import kr.ac.kaist.launduler.services.laundulerService

/**
 * Created by kyukyukyu on 31/05/2017.
 */
class MachineStatusFragment : RxFragment() {
    lateinit var selectedDay: Calendar
    lateinit var machineId: String

    companion object {
        const val SELECTED_DAY = "SELECTED_DAY"
        const val MACHINE_ID = "MACHINE_ID"
        val selectedDaySubject = RxBus.getSubject(RxBus.SUBJECT_EXPLORE_SELECTED_DAY)

        fun newInstance(selectedDay: Calendar? = null, machineId: String) : MachineStatusFragment {
            val fragment = MachineStatusFragment()
            val args = Bundle()
            args.putSerializable(SELECTED_DAY, selectedDay)
            args.putString(MACHINE_ID, machineId)
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
        machineId = bundle!!.getString(MACHINE_ID)
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
        laundulerService.getMachine(machineId)
                .subscribeOn(Schedulers.io())
                .map { it.reservations }
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(this)
                .subscribe({ reservations -> day.setMonthChangeListener { newYear, newMonth ->
                    reservations
                            .filter {
                                val scheduledAt = it.scheduledAt
                                scheduledAt.get(Calendar.YEAR) == newYear &&
                                        scheduledAt.get(Calendar.MONTH) == newMonth
                            }
                            .map {
                                WeekViewEvent().apply {
                                    startTime = it.scheduledAt
                                    endTime = it.endsAt
                                }
                            }
                } }, { Snackbar.make(view!!, R.string.error_machine_reservations, Snackbar.LENGTH_LONG) })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.apply {
            putSerializable(SELECTED_DAY, selectedDay)
            putString(MACHINE_ID, machineId)
        }
    }
}