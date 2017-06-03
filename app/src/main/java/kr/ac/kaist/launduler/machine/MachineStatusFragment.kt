package kr.ac.kaist.launduler.machine

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alamkanak.weekview.WeekViewEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.Timepoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlinx.android.synthetic.main.fragment_status_machine.*
import kr.ac.kaist.launduler.R
import kr.ac.kaist.launduler.RxBus
import kr.ac.kaist.launduler.models.Reservation
import kr.ac.kaist.launduler.services.laundulerService

/**
 * Created by kyukyukyu on 31/05/2017.
 */
class MachineStatusFragment :
        RxFragment(),
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
    var selectedDay: Calendar = Calendar.getInstance()
    lateinit var machineId: String
    var pickedDatetime: Calendar = Calendar.getInstance()

    companion object {
        const val SELECTED_DAY = "SELECTED_DAY"
        const val MACHINE_ID = "MACHINE_ID"
        const val PICKED_DATETIME = "PICKED_DATETIME"
        val selectedDaySubject = RxBus.getSubject(RxBus.SUBJECT_EXPLORE_SELECTED_DAY)
        val selectableTimes = (0..(24 * 60) step Reservation.LENGTH_MINUTE)
                .map { Timepoint(it / 60, it % 60) }
                .toTypedArray()

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
        bundle.getSerializable(SELECTED_DAY)?.let { selectedDay = it as Calendar }
        machineId = bundle.getString(MACHINE_ID)
        bundle.getSerializable(PICKED_DATETIME)?.let { pickedDatetime = it as Calendar }
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
        floating_action_button.setOnClickListener {
            val now = Calendar.getInstance()
            val dt = maxOf(now, selectedDay)
            val dpd = DatePickerDialog.newInstance(
                    this,
                    dt.get(Calendar.YEAR),
                    dt.get(Calendar.MONTH),
                    dt.get(Calendar.DAY_OF_MONTH))
            dpd.minDate = dt
            dpd.show(fragmentManager, "ReservationDatePickerDialog")
        }
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
            putSerializable(PICKED_DATETIME, pickedDatetime)
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        pickedDatetime.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthOfYear + 1)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        val tpd = TimePickerDialog.newInstance(this, false)
        tpd.setSelectableTimes(selectableTimes)
        tpd.show(fragmentManager, "ReservationTimePickerDialog")
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        pickedDatetime.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        // TODO: make an API request for the reservation.
    }
}