package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.R;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class SelectMultipleWriteOtherQuestionFragment extends
        SelectMultipleQuestionFragment {

    @Override
    protected void beforeAddViewHook(ViewGroup questionComponent) {
        CheckBox checkbox = new CheckBox(getActivity());
        final EditText otherText = new EditText(getActivity());
        checkbox.setText(R.string.other_specify);
        checkbox.setTypeface(getInstrument().getTypeFace(getActivity().getApplicationContext()));
        final int otherId = getQuestion().options().size();
        checkbox.setId(otherId);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                otherText.setEnabled(isChecked);
                toggleResponseIndex(otherId);
                if (!isChecked) {
                    otherText.getText().clear();
                }
            }
         });
        questionComponent.addView(checkbox, otherId);
        addOtherResponseView(otherText);
        addCheckBox(checkbox);
        questionComponent.addView(otherText);
    }
}
