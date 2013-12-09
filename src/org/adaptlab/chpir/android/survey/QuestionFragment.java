package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.activeandroid.Model;

public abstract class QuestionFragment extends Fragment {
    protected abstract void createQuestionComponent(ViewGroup questionComponent);

    private Question mQuestion;
    private Survey mSurvey;
    private Response mResponse;
    private Instrument mInstrument;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long questionId = getArguments().getLong(QuestionFragmentFactory.EXTRA_QUESTION_ID, -1);
        long surveyId = getArguments().getLong(QuestionFragmentFactory.EXTRA_SURVEY_ID, -1);

        if (questionId != -1 && surveyId != -1) {
            mQuestion = Question.findByRemoteId(questionId);
            mSurvey = Model.load(Survey.class, surveyId);
            mResponse = new Response();
            mResponse.setQuestion(mQuestion);
            mResponse.setSurvey(mSurvey);
            mInstrument = mSurvey.getInstrument();
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
    
    protected Instrument getInstrument() {
        return mInstrument;
    }
    
    protected void addOtherResponseView(EditText otherText) {
        otherText.setHint(R.string.other_specify_edittext);
        otherText.setEnabled(false);
        otherText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) { 
                getResponse().setOtherResponse(s.toString());
                getResponse().save();
            }
            
            // Required by interface
            public void beforeTextChanged(CharSequence s, int start,
                    int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
    }
}
