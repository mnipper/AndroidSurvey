package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.R;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

public class SelectOneWriteOtherQuestionFragment extends
        SelectOneQuestionFragment {

    @Override
    protected void beforeAddViewHook(ViewGroup questionComponent) {
        RadioButton radioButton = new RadioButton(getActivity());
        radioButton.setText(R.string.other_specify);
        radioButton.setId(getQuestion().options().size());
        getRadioGroup().addView(radioButton, getQuestion().options().size());

        EditText otherText = new EditText(getActivity());
        otherText.setHint(R.string.other_specify_edittext);
        getRadioGroup().addView(otherText, getQuestion().options().size() + 1);
    }
}
