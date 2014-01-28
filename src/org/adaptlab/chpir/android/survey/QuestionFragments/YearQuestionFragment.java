package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.lang.reflect.Field;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

public class YearQuestionFragment extends DateQuestionFragment {
    private static final String TAG = "YearQuestionFragment";
    
    private DatePicker mDatePicker;
    
    @Override
    protected void beforeAddViewHook(DatePicker datePicker) {     
        mDatePicker = datePicker;
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            // Honeycomb +
            findAndHideField(datePicker, "mDaySpinner");
            findAndHideField(datePicker, "mMonthSpinner");
        } else {
            // Before Honeycomb
            findAndHideField(datePicker, "mDayPicker");
            findAndHideField(datePicker, "mMonthPicker");
        }
    }
    
    private void findAndHideField(DatePicker datePicker, String name) {
        try {
            Field field = DatePicker.class.getDeclaredField(name);
            field.setAccessible(true);
            View fieldInstance = (View) field.get(datePicker);
            fieldInstance.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, "Error removing day or month field: " + e);
        }
    }
    
    @Override
    protected String serialize() {
        return String.valueOf(mYear);
    }

    @Override
    protected void deserialize(String responseText) {
      if (responseText.equals("")) return;
      mDatePicker.updateDate(Integer.parseInt(responseText), 1, 1);
    }
}