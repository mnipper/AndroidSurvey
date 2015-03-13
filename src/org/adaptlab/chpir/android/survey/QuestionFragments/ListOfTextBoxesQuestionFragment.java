package org.adaptlab.chpir.android.survey.QuestionFragments;

import android.widget.EditText;

public class ListOfTextBoxesQuestionFragment extends ListOfItemsQuestionFragment {
    
    @Override
    protected EditText createEditText() {
        return new EditText(getActivity());
    }
}
