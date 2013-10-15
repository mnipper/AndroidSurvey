package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.ViewGroup;
import android.widget.TextView;

public class SliderQuestionFragment extends QuestionFragment {

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        // TODO Implement
        TextView temp = new TextView(getActivity());
        temp.setText("This type of question is not yet implemented");
        questionComponent.addView(temp);
    }

}
