package com.app.happy_birthday.helper.calender

import com.app.happy_birthday.databinding.LayoutCalenderBinding
import com.app.happy_birthday.helper.Constants
import com.app.happy_birthday.helper.Global
import com.app.happy_birthday.helper.calender.calendar_decorators.AllDaysEnabledDecoratorVertical
import com.app.happy_birthday.helper.calender.model.RangeDateModel
import com.app.happy_birthday.helper.calendar_decorators.DisablePassedDaysDecorator
import com.app.happy_birthday.helper.calendar_decorators.RangeSelectionDecorator
import com.app.happy_birthday.helper.calendar_decorators.SelectedDayDecoratorVertical
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import java.text.SimpleDateFormat
import java.util.*

object CalendarHelper {
    var currentDate = ""
    val model = RangeDateModel()
    private val monthFormatter = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)

    fun LayoutCalenderBinding.initCalender(listener: CalenderListener, isCurrentDateIsMinimum: Boolean = false, isRangeMode: Boolean = false) {
        setCalenderMonth(isCurrentDateIsMinimum)
        calendarView.clearSelection()
        val context = calendarView.context
        calendarView.topbarVisible = false
        if (isCurrentDateIsMinimum) {
            calendarView.addDecorators(
                AllDaysEnabledDecoratorVertical(context),  //All Days
                SelectedDayDecoratorVertical(null, context),  //current Day
                DisablePassedDaysDecorator(context, CalendarDay.today())
            )
        } else {
            calendarView.addDecorators(
                AllDaysEnabledDecoratorVertical(context),  //All Days
                SelectedDayDecoratorVertical(null, context),  //current Day
            )
        }
        // past days

        calendarView.setOnMonthChangedListener { _, date ->
            val calendar = Calendar.getInstance()
            calendar.set(date.year, date.month - 1, date.day)
            val strMonth = monthFormatter.format(calendar.time)
            txtMonth.text = Global.monthsEnglishToArabic(strMonth, context)
        }

        calendarView.setOnDateChangedListener { calendarView, strDay, isDateSelected ->
            calendarView.clearSelection()
            if (isDateSelected) {
                val calendar = Calendar.getInstance()
                calendar.set(strDay.year, strDay.month - 1, strDay.day)
                calendarView.removeDecorators()
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val selectedDate = simpleDateFormat.format(calendar.time)
                val displayDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
                val displaySelectedDate = Global.monthShortEnglishToArabic("(" + displayDateFormat.format(calendar.time) + ")", context)
                if (isRangeMode) {
                    if (model.startDate == null && model.endDate == null) {
                        model.startDate = strDay
                        calendarView.addDecorators(
                            AllDaysEnabledDecoratorVertical(context),  //All Days
                            SelectedDayDecoratorVertical(model.startDate, context)
                        )
                    } else if (model.startDate != null && model.endDate != null) {
                        model.startDate = null
                        model.endDate = null
                        model.startDate = strDay
                        calendarView.addDecorators(
                            AllDaysEnabledDecoratorVertical(context),  //All Days
                            SelectedDayDecoratorVertical(model.startDate, context)
                        )
                    } else {
                        model.endDate = strDay
                        calendarView.addDecorators(
                            AllDaysEnabledDecoratorVertical(context), //All Days
                            RangeSelectionDecorator(model.startDate, model.endDate, context)
                        )
                        listener.onRangeSelected(model)
                        /*  model.startDate = null
                          model.endDate = null*/
                    }
                } else {
                    if (isCurrentDateIsMinimum) {
                        calendarView.addDecorators(
                            AllDaysEnabledDecoratorVertical(context),  //All Days
                            SelectedDayDecoratorVertical(strDay, context),  //current Day
                            DisablePassedDaysDecorator(context, CalendarDay.today())
                        )
                    } else {
                        calendarView.addDecorators(
                            AllDaysEnabledDecoratorVertical(context),  //All Days
                            SelectedDayDecoratorVertical(strDay, context),  //current Day
                        )
                    }
                    if (currentDate != selectedDate) {
                        currentDate = selectedDate
                        listener.onDateSelected(selectedDate, displaySelectedDate)
                    } else {
                        resetDecorators(isCurrentDateIsMinimum)
                        listener.onDateSelected("", displaySelectedDate)
                    }
                }
            }
        }
    }

    private fun LayoutCalenderBinding.resetDecorators(isCurrentDateIsMinimum: Boolean = false) {
        currentDate = ""
        val context = calendarView.context
        calendarView.removeDecorators()
        if (isCurrentDateIsMinimum) {
            calendarView.addDecorators(
                AllDaysEnabledDecoratorVertical(context),  //All Days
                SelectedDayDecoratorVertical(null, context),  //current Day
                DisablePassedDaysDecorator(context, CalendarDay.today())
            )
        } else {
            calendarView.addDecorators(
                AllDaysEnabledDecoratorVertical(context),  //All Days
                SelectedDayDecoratorVertical(null, context),  //current Day
            )
        }
        calendarView.addDecorators(
            AllDaysEnabledDecoratorVertical(context),  //All Days
            SelectedDayDecoratorVertical(null, context),  //current Day
        )
    }

    private fun LayoutCalenderBinding.setCalenderMonth(isCurrentDateIsMinimum: Boolean = false) {
        val context = calendarView.context
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year, month - 1, day)

        val calenderDay = CalendarDay.from(year, month, day)

        val strMonth = monthFormatter.format(calendar.time)
        txtMonth.text = Global.monthsEnglishToArabic(strMonth, context)
        val displayDateFormat = SimpleDateFormat(Constants.DisplayDateFormat, Locale.ENGLISH)
        val selectedDate = Global.monthShortEnglishToArabic("(" + displayDateFormat.format(calendar.time) + ")", context)
        if (isCurrentDateIsMinimum) {
            calendarView.state().edit()
                .setMinimumDate(calenderDay)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
        } else {
            calendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
        }
    }

    interface CalenderListener {
        fun onDateSelected(date: String, displayDate: String) {}
        fun onRangeSelected(model: RangeDateModel) {}
        fun onMonthSelected() {}
    }
}