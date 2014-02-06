package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.R;

import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SelectOneWriteOtherQuestionFragment extends
        SelectOneQuestionFragment {
	
    @Override
    protected void beforeAddViewHook(ViewGroup questionComponent) {
        RadioButton radioButton = new RadioButton(getActivity());
        final EditText otherText = new EditText(getActivity());
        
        radioButton.setText(R.string.other_specify);
        radioButton.setTypeface(getInstrument().getTypeFace(getActivity().getApplicationContext()));
        final int otherId = getQuestion().options().size();
        radioButton.setId(otherId);
        radioButton.setLayoutParams(new RadioGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        getRadioGroup().setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                otherText.setEnabled(checkedId == otherId);
                setResponseIndex(checkedId);
                if (checkedId != otherId) {
                    otherText.getText().clear();
                }
            }
        });
        getRadioGroup().addView(radioButton, otherId);
        addOtherResponseView(otherText);
        questionComponent.addView(otherText);
    }
}
