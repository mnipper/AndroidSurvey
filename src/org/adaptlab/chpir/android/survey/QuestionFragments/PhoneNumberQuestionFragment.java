package org.adaptlab.chpir.android.survey.QuestionFragments;

import android.text.InputType;
import android.widget.EditText;

public class PhoneNumberQuestionFragment extends FreeResponseQuestionFragment {
    
    protected void beforeAddViewHook(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
    }
}