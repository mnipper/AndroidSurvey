package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.R;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

public class SelectMultipleWriteOtherQuestionFragment extends
        SelectMultipleQuestionFragment {

    @Override
    protected void beforeAddViewHook(ViewGroup questionComponent) {
        CheckBox checkbox = new CheckBox(getActivity());
        checkbox.setText(R.string.other_specify);
        checkbox.setId(getQuestion().options().size());
        questionComponent.addView(checkbox, getQuestion().options().size());

        EditText otherText = new EditText(getActivity());
        otherText.setHint(R.string.other_specify_edittext);
        questionComponent
                .addView(otherText, getQuestion().options().size() + 1);
    }
}
