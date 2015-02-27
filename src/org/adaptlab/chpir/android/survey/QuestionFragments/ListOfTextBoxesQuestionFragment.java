package org.adaptlab.chpir.android.survey.QuestionFragments;

import android.view.ViewGroup;
import android.widget.EditText;

public class ListOfTextBoxesQuestionFragment extends ListOfItemsQuestionFragment {

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        EditText editText = new EditText(getActivity());
        createQuestionComponent(questionComponent, editText);
    }
}
