package com.app.happy_birthday.helper.calender.calendar_decorators

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.app.happy_birthday.R
import com.app.happy_birthday.helper.Constants
import com.app.happy_birthday.helper.CustomTypefaceSpan
import com.app.happy_birthday.helper.Global
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class AllDaysEnabledDecoratorVertical(private val context: Context) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return true //decorate all days in calendar
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.txt_color_primary)))
        view.addSpan(CustomTypefaceSpan("", Global.getTypeFace(context, Constants.fontRegular)))
        ContextCompat.getDrawable(context, R.drawable.bg_circle_calendar_unselected)?.let { view.setBackgroundDrawable(it) }
    }
}