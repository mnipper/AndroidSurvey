package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.activeandroid.Model;

public abstract class QuestionFragment extends Fragment {
    protected abstract void createQuestionComponent(ViewGroup questionComponent);

    private Question mQuestion;
    private Survey mSurvey;
    private Response mResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long questionId = getArguments().getLong(
                QuestionFragmentFactory.EXTRA_QUESTION_ID, -1);
        long surveyId = getArguments().getLong(
                QuestionFragmentFactory.EXTRA_SURVEY_ID, -1);

        if (questionId != -1 && surveyId != -1) {
            mQuestion = Model.load(Question.class, questionId);
            mSurvey = Model.load(Survey.class, surveyId);
            mResponse = new Response();
            mResponse.setQuestion(mQuestion);
            mResponse.setSurvey(mSurvey);
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

    protected Question getQuestion() {
        return mQuestion;
    }
    
    protected Survey getSurvey() {
        return mSurvey;
    }
    
    protected Response getResponse() {
        return mResponse;
    }
}
