package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.FormatUtils;
import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.ViewGroup;
import android.widget.TimePicker;

public class TimeQuestionFragment extends QuestionFragment {
    private int mHour;
    private int mMinute;
    private TimePicker mTimePicker;

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        mTimePicker = new TimePicker(getActivity());
        mHour = mTimePicker.getCurrentHour();
        mMinute = mTimePicker.getCurrentMinute();
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                saveResponse();
            }
        });
        questionComponent.addView(mTimePicker);
    }

    @Override
    protected String serialize() {
        return FormatUtils.formatTime(mHour, mMinute);
    }

    @Override
    protected void deserialize(String responseText) {
        int[] timeComponents = FormatUtils.unformatTime(responseText);
        if(timeComponents != null) {
            mTimePicker.setCurrentHour(timeComponents[0]);
            mTimePicker.setCurrentMinute(timeComponents[1]);
        }
    }
   
}
