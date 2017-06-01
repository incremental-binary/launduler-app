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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kr.ac.kaist.launduler.R
import java.util.*

/**
 * Created by kyukyukyu on 29/05/2017.
 */
class WeekFragment : Fragment() {
    lateinit var selectedDay: Calendar

    companion object {
        const val SELECTED_DAY = "SELECTED_DAY"
        val dayOfWeekSubject = PublishSubject.create<Int>()

        fun newInstance(selectedDay: Calendar) : WeekFragment {
            val fragment = WeekFragment()
            val args = Bundle()
            args.putSerializable(SELECTED_DAY, selectedDay)
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
                        dow,
                        day.get(Calendar.DAY_OF_MONTH),
                        dow == selectedDow,
                        day.compareTo(today) == 0)
                trx.add(R.id.week, fragment, "DAY_$i")
                day.add(Calendar.DAY_OF_MONTH, 1)
            }
            trx.commit()
        }
        return layoutView
    }
}


class DayOfWeekFragment : Fragment() {
    var dayOfWeek: Int = 0
    lateinit var dayOfWeekChar: String
    var number: Int = 0
    var selected = false
    var today = false
    val dowDisposables = CompositeDisposable()

    enum class State {
        DEFAULT, SELECTED, TODAY
    }

    companion object {
        const val DAY_OF_WEEK_INDEX = "DAY_OF_WEEK_INDEX"
        const val NUMBER = "NUMBER"
        const val SELECTED = "SELECTED"
        const val TODAY = "TODAY"

        fun newInstance(dayOfWeek: Int, number: Int, selected: Boolean = false,
                        today: Boolean = false) : DayOfWeekFragment {
            val fragment = DayOfWeekFragment()
            val args = Bundle()
            args.putInt(DAY_OF_WEEK_INDEX, dayOfWeek)
            args.putInt(NUMBER, number)
            args.putBoolean(SELECTED, selected)
            args.putBoolean(TODAY, today)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            dayOfWeek = arguments.getInt(DAY_OF_WEEK_INDEX)
            dayOfWeekChar = resources.getStringArray(R.array.day_of_week_char)[dayOfWeek - 1]
            number = arguments.getInt(NUMBER)
            selected = arguments.getBoolean(SELECTED)
            today = arguments.getBoolean(TODAY)
        }
        dowDisposables.add(WeekFragment.dayOfWeekSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { dow ->
                    selected = dow == dayOfWeek
                    updateView()
                })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater?.inflate(R.layout.day_of_week, container, false)!!

        val vNumber = layoutView.findViewById(R.id.number) as TextView
        vNumber.text = number.toString()
        vNumber.setOnClickListener { WeekFragment.dayOfWeekSubject.onNext(dayOfWeek) }

        (layoutView.findViewById(R.id.letter) as TextView).text = dayOfWeekChar

        return layoutView
    }

    override fun onStart() {
        super.onStart()
        updateView()
    }

    override fun onDestroy() {
        super.onDestroy()
        dowDisposables.clear()
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
