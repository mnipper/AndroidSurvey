package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.Calendar;

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
        final String formattedDate = formatDate(month, day, year);
        
        getResponse().setResponse(formattedDate);
        datePicker.init(year, month, day, new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int newYear,
                    int newMonth, int newDay) {
                getResponse().setResponse(formatDate(newMonth, newDay, newYear));
                getResponse().save();
            }           
        });
        questionComponent.addView(datePicker);
    }
    
    
    // Month indexing starts at 0 by default.  Add 1 to start indexing at 1 in response.
    private String formatDate(int month, int day, int year) {
        return ((month + 1) + "-" + day + "-" + year);
    }
}
