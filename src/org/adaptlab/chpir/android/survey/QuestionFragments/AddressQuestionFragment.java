package org.adaptlab.chpir.android.survey.QuestionFragments;

import android.text.InputType;
import android.widget.EditText;

public class AddressQuestionFragment extends FreeResponseQuestionFragment {
    
    protected void beforeAddViewHook(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }
}