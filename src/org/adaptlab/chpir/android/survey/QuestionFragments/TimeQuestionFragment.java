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
                getResponse().setResponse(formatTime(hourOfDay, minute));
                getResponse().save();
            }
        });
        
        getResponse().setResponse(formatTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
        getResponse().save();
        
        questionComponent.addView(timePicker);
    }
    
    // Format: HH:MM AM/PM
    private String formatTime(int hour, int minute) {
        String period = (hour < 12 ? "AM" : "PM");
        hour = (hour > 13 ? hour - 12 : hour);
        if (hour == 0) hour = 12;
        return hour + ":" + formatMinute(minute) + " " + period;
    }

    // Add a 0 to minute values less than 10 to look more natural
    private String formatMinute(int minute) {
        if (minute < 10) {
            return "0" + minute;
        } else {
            return String.valueOf(minute);
        }
    }
}
