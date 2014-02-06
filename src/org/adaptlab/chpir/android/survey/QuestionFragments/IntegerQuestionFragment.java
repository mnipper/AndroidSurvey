package org.adaptlab.chpir.android.survey.QuestionFragments;

import android.text.InputType;
import android.widget.EditText;

public class IntegerQuestionFragment extends FreeResponseQuestionFragment {
    
    protected void beforeAddViewHook(EditText editText) {
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_NUMBER);
    }
}