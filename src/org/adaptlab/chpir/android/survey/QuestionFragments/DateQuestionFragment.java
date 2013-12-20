package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.Calendar;

import org.adaptlab.chpir.android.survey.FormatUtils;
import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DateQuestionFragment extends QuestionFragment {

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        final DatePicker datePicker = new DatePicker(getActivity());
        datePicker.setCalendarViewShown(false);
        Calendar c = Calendar.getInstance();

        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
       
        datePicker.init(year, month, day, new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int newYear,
                    int newMonth, int newDay) {
                getResponse().setResponse(FormatUtils.formatDate(newMonth, newDay, newYear));
                getResponse().save();
            }           
        });

        getResponse().setResponse(FormatUtils.formatDate(month, day, year));
        getResponse().save();
        
        questionComponent.addView(datePicker);
    }
}
