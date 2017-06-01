package kr.ac.kaist.launduler.machine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlinx.android.synthetic.main.fragment_status_machine.*
import kr.ac.kaist.launduler.R

/**
 * Created by kyukyukyu on 31/05/2017.
 */
class MachineStatusFragment : Fragment() {
    lateinit var selectedDay: Calendar
    val disposables = CompositeDisposable()
    val selectedDaySubject = PublishSubject.create<Calendar>()

    companion object {
        const val SELECTED_DAY = "SELECTED_DAY"

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
            disposables.add(selectedDaySubject
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { newSelectedDay ->
                        val dayOfWeek = newSelectedDay.get(Calendar.DAY_OF_WEEK)
                        WeekFragment.dayOfWeekSubject.onNext(dayOfWeek)
                    })
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater!!.inflate(R.layout.fragment_status_machine, container, false)!!

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weekFragment = WeekFragment.newInstance(Calendar.getInstance())
        childFragmentManager
                .beginTransaction()
                .replace(R.id.week, weekFragment, "WEEK")
                .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        day.setMonthChangeListener { _, _ -> listOf() }
        day.setScrollListener { newFirstVisibleDay, _ -> selectedDaySubject.onNext(newFirstVisibleDay) }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(SELECTED_DAY, selectedDay)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}