package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.FormatUtils;
import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.ViewGroup;
import android.widget.TimePicker;

public class TimeQuestionFragment extends QuestionFragment {

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        TimePicker timePicker = new TimePicker(getActivity());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                getResponse().setResponse(FormatUtils.formatTime(hourOfDay, minute));
                getResponse().save();
            }
        });
        
        getResponse().setResponse(FormatUtils.formatTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
        getResponse().save();
        
        questionComponent.addView(timePicker);
    }
}
