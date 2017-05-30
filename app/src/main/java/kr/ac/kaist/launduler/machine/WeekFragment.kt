package kr.ac.kaist.launduler.machine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import kr.ac.kaist.launduler.R

/**
 * Created by kyukyukyu on 29/05/2017.
 */
class WeekFragment : Fragment() {
    companion object {
        fun newInstance() : WeekFragment {
            val fragment = WeekFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater?.inflate(R.layout.fragment_week, container, false)
        if (savedInstanceState == null) {
            val trx = childFragmentManager.beginTransaction()
            val dows = DayOfWeekFragment.DayOfWeek.values()
            for (i in 1..7) {
                val dow = DayOfWeekFragment.newInstance(dows[i - 1], i, when (i) {
                    1 -> DayOfWeekFragment.State.TODAY
                    3 -> DayOfWeekFragment.State.SELECTED
                    else -> DayOfWeekFragment.State.DEFAULT
                })
                trx.add(R.id.week, dow, "DAY_$i")
            }
            trx.commit()
        }
        return layoutView
    }
}


class DayOfWeekFragment : Fragment() {
    lateinit var dayOfWeek: DayOfWeek
    lateinit var dayOfWeekChar: String
    var number: Int = 0
    lateinit var state: State

    enum class DayOfWeek {
        SUN, MON, TUES, WED, THU, FRI, SAT
    }

    enum class State {
        DEFAULT, SELECTED, TODAY
    }

    companion object {
        val DAY_OF_WEEK_INDEX = "DAY_OF_WEEK_INDEX"
        val NUMBER = "NUMBER"
        val STATE = "STATE"

        fun newInstance(dayOfWeek: DayOfWeek, number: Int, state: State = State.DEFAULT) : DayOfWeekFragment {
            val fragment = DayOfWeekFragment()
            val args = Bundle()
            args.putString(DAY_OF_WEEK_INDEX, dayOfWeek.name)
            args.putInt(NUMBER, number)
            args.putString(STATE, state.name)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            dayOfWeek = DayOfWeek.valueOf(arguments.getString(DAY_OF_WEEK_INDEX)!!)
            dayOfWeekChar = resources.getStringArray(R.array.day_of_week_char)[dayOfWeek.ordinal]
            number = arguments.getInt(NUMBER)
            state = State.valueOf(arguments.getString(STATE))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutView = inflater?.inflate(R.layout.day_of_week, container, false)!!

        val vNumber = layoutView.findViewById(R.id.number) as TextView
        vNumber.text = number.toString()

        val typedValue = TypedValue()
        val theme = context.theme
        val textColorAttr = when (state) {
            State.SELECTED -> android.R.attr.colorBackground
            State.TODAY -> android.support.v7.appcompat.R.attr.colorPrimary
            State.DEFAULT -> android.R.attr.colorForeground
        }
        theme.resolveAttribute(textColorAttr, typedValue, true)
        vNumber.setTextColor(typedValue.data)

        (layoutView.findViewById(R.id.letter) as TextView).text = dayOfWeekChar

        layoutView.findViewById(R.id.selected).visibility = if (state == State.SELECTED) { VISIBLE } else { GONE }

        return layoutView
    }
}
