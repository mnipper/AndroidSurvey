package org.adaptlab.chpir.android.survey.QuestionFragments;

import android.text.InputType;
import android.widget.EditText;

public class ListOfIntegerBoxesQuestionFragment extends ListOfItemsQuestionFragment {
    
    @Override
    protected EditText createEditText() {
        EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        return editText;
    }
}
