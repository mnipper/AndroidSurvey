package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Question;
import com.activeandroid.Model;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class QuestionFragment extends Fragment {
    protected abstract void createQuestionComponent(ViewGroup questionComponent);

    private Question mQuestion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long questionId = getArguments().getLong(
                QuestionFragmentFactory.EXTRA_QUESTION_ID, -1);

        if (questionId != -1) {
            mQuestion = Model.load(Question.class, questionId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_factory, parent,
                false);

        ViewGroup questionComponent = (LinearLayout) v
                .findViewById(R.id.question_component);
        createQuestionComponent(questionComponent);
        return v;
    }

    public Question getQuestion() {
        return mQuestion;
    }
}
