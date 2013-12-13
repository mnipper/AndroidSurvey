package org.adaptlab.chpir.android.survey.QuestionFragments;

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
                getResponse().setResponse(hourOfDay + ":" + formatMinute(minute));
                getResponse().save();
            }
        });
        
        getResponse().setResponse(timePicker.getCurrentHour() + ":" + formatMinute(timePicker.getCurrentMinute()));
        getResponse().save();
        
        questionComponent.addView(timePicker);
    }

    // Add a 0 to minute values less than 10 to look more natural
    private String formatMinute(int minute) {
        if (minute < 10) {
            return "0" + minute;
        } else {
            return ""+minute;
        }
    }
}
