package org.adaptlab.chpir.android.survey.QuestionFragments;

import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;

public class ListOfIntegerBoxesQuestionFragment extends ListOfItemsQuestionFragment {

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        createQuestionComponent(questionComponent, editText);
    }
}
