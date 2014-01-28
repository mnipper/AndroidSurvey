package org.adaptlab.chpir.android.survey.QuestionFragments;

import android.text.InputType;
import android.widget.EditText;

public class DecimalNumberQuestionFragment extends FreeResponseQuestionFragment {

    protected void beforeAddViewHook(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }
}
