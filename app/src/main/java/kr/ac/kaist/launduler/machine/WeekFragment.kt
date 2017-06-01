package kr.ac.kaist.launduler.machine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.trello.rxlifecycle2.components.support.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kr.ac.kaist.launduler.R
import kr.ac.kaist.launduler.RxBus
import java.util.*

/**
 * Created by kyukyukyu on 29/05/2017.
 */
class WeekFragment : Fragment() {
    lateinit var selectedDay: Calendar
    var selectedDaySubjectKey = 0L

    companion object {
        const val SELECTED_DAY = "SELECTED_DAY"
        const val SELECTED_DAY_SUBJECT_KEY = "SELECTED_DAY_SUBJECT_KEY"

        fun newInstance(selectedDay: Calendar, selectedDaySubjectKey: Long) : WeekFragment {
            val fragment = WeekFragment()
            val args = Bundle()
            args.putSerializable(SELECTED_DAY, selectedDay)
            args.putLong(SELECTED_DAY_SUBJECT_KEY, selectedDaySubjectKey)
            fragment.arguments = args
            return fragment
        }

        fun discardTime(calendar: Calendar) {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedDay = arguments!!.getSerializable(SELECTED_DAY) as Calendar
        selectedDaySubjectKey = arguments!!.getLong(SELECTED_DAY_SUBJECT_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater?.inflate(R.layout.fragment_week, container, false)
        if (savedInstanceState == null) {
            val selectedDow = selectedDay.get(Calendar.DAY_OF_WEEK)

            var day = selectedDay.clone() as Calendar
            discardTime(day)
            day.set(Calendar.DAY_OF_WEEK, day.firstDayOfWeek)

            val today = Calendar.getInstance()
            discardTime(today)

            val trx = childFragmentManager.beginTransaction()
            for (i in 1..7) {
                val dow = day.get(Calendar.DAY_OF_WEEK)
                val fragment = DayOfWeekFragment.newInstance(
                        day,
                        dow == selectedDow,
                        day.compareTo(today) == 0,
                        selectedDaySubjectKey)
                trx.add(R.id.week, fragment, "DAY_$i")
                day = day.clone() as Calendar
                day.add(Calendar.DAY_OF_MONTH, 1)
            }
            trx.commit()
        }
        return layoutView
    }
}


class DayOfWeekFragment : RxFragment() {
    lateinit var day: Calendar
    var dayOfWeek: Int = 0
    lateinit var dayOfWeekChar: String
    var number: Int = 0
    var selected = false
    var today = false
    lateinit var selectedDaySubject: PublishSubject<Any>

    companion object {
        const val DAY = "DAY"
        const val SELECTED = "SELECTED"
        const val TODAY = "TODAY"
        const val SELECTED_DAY_SUBJECT_KEY = "SELECTED_DAY_SUBJECT_KEY"

        fun newInstance(day: Calendar, selected: Boolean = false,
                        today: Boolean = false, selectedDaySubjectKey: Long) : DayOfWeekFragment {
            val fragment = DayOfWeekFragment()
            val args = Bundle()
            args.putSerializable(DAY, day)
            args.putBoolean(SELECTED, selected)
            args.putBoolean(TODAY, today)
            args.putLong(SELECTED_DAY_SUBJECT_KEY, selectedDaySubjectKey)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.apply {
            day = getSerializable(DAY) as Calendar
            dayOfWeek = day.get(Calendar.DAY_OF_WEEK)
            dayOfWeekChar = resources.getStringArray(R.array.day_of_week_char)[dayOfWeek - 1]
            number = day.get(Calendar.DAY_OF_MONTH)
            selected = getBoolean(SELECTED)
            today = getBoolean(TODAY)
            selectedDaySubject = RxBus.getSubject(getLong(SELECTED_DAY_SUBJECT_KEY))
        }
        selectedDaySubject
                .subscribeOn(Schedulers.io())
                .map { (it as Calendar).get(Calendar.DAY_OF_WEEK) }
                .map { selected = it == dayOfWeek }
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(this)
                .subscribe { updateView() }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater?.inflate(R.layout.day_of_week, container, false)!!

        val vNumber = layoutView.findViewById(R.id.number) as TextView
        vNumber.text = number.toString()
        vNumber.setOnClickListener { selectedDaySubject.onNext(day) }

        (layoutView.findViewById(R.id.letter) as TextView).text = dayOfWeekChar

        return layoutView
    }

    override fun onStart() {
        super.onStart()
        updateView()
    }

    fun updateView() {
        val vNumber = view?.findViewById(R.id.number) as TextView
        val vSelected = view?.findViewById(R.id.selected) as ImageView
        val typedValue = TypedValue()
        val theme = context.theme
        val textColorAttr =
                if (selected) { android.R.attr.colorBackground }
                else if (today) { android.support.v7.appcompat.R.attr.colorPrimary }
                else { android.R.attr.colorForeground }
        theme.resolveAttribute(textColorAttr, typedValue, true)
        vNumber.setTextColor(typedValue.data)

        vSelected.visibility = if (selected) { VISIBLE } else { GONE }
    }
}
