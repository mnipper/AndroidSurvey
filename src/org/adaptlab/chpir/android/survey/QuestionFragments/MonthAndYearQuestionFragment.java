package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.lang.reflect.Field;

import org.adaptlab.chpir.android.survey.FormatUtils;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

public class MonthAndYearQuestionFragment extends DateQuestionFragment {
    private static final String TAG = "MonthAndYearQuestionFragment";
    
    @Override
    protected void beforeAddViewHook(DatePicker datePicker) {
        String dateSpinnerName = "";
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            // Honeycomb +
            dateSpinnerName = "mDaySpinner";
        } else {
            // Before Honeycomb
            dateSpinnerName = "mDayPicker";            
        }
        
        try {
            Field field = DatePicker.class.getDeclaredField(dateSpinnerName);
            field.setAccessible(true);
            View fieldInstance = (View) field.get(datePicker);
            fieldInstance.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "Error removing day field: " + e);
        }   
    }
    
    @Override
    protected String serialize() {
        return FormatUtils.formatDate(mMonth, mYear);
    }
}
