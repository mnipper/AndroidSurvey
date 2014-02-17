package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.adaptlab.chpir.android.survey.FormatUtils;
import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DateQuestionFragment extends QuestionFragment {
    protected int mDay;
    protected int mMonth;
    protected int mYear;
    
    private DatePicker mDatePicker;
    
    // This is used to hide various date fields in subclasses.
    protected void beforeAddViewHook(DatePicker datePicker) {
    }

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        mDatePicker = new DatePicker(getActivity());
        mDatePicker.setCalendarViewShown(false);
        Calendar c = Calendar.getInstance();
        mDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int newYear,
                    int newMonth, int newDay) {
                mDay = newDay;
                mMonth = newMonth;
                mYear = newYear;
                saveResponse();
            }           
        });
        questionComponent.addView(mDatePicker);
        beforeAddViewHook(mDatePicker);
    }

    @Override
    protected String serialize() {
        return FormatUtils.formatDate(mMonth, mDay, mYear);
    }

    @Override
    protected void deserialize(String responseText) {
        GregorianCalendar dateComponents = FormatUtils.unformatDate(responseText);
        if(dateComponents != null) {
            mDay = dateComponents.get(GregorianCalendar.DAY_OF_MONTH);
            mMonth = dateComponents.get(GregorianCalendar.MONTH);
            mYear = dateComponents.get(GregorianCalendar.YEAR);
            mDatePicker.updateDate(mYear, mMonth, mDay);
        }
    }

}
