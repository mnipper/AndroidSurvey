package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.Model;

public abstract class QuestionFragment extends Fragment {
    private final static String TAG = "QuestionFragment";
    
    protected abstract void createQuestionComponent(ViewGroup questionComponent);
    protected abstract String serialize();
    protected abstract void deserialize(String responseText);
    
    private TextView mValidationTextView;

    private Question mQuestion;
    private Survey mSurvey;
    private Response mResponse;
    private Instrument mInstrument;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    
    public void init() {
        long questionId = getArguments().getLong(QuestionFragmentFactory.EXTRA_QUESTION_ID, -1);
        long surveyId = getArguments().getLong(QuestionFragmentFactory.EXTRA_SURVEY_ID, -1);

        if (questionId != -1 && surveyId != -1) {
            mQuestion = Question.findByRemoteId(questionId);
            mSurvey = Model.load(Survey.class, surveyId);
            mResponse = loadOrCreateResponse();
            mResponse.setQuestion(mQuestion);
            mResponse.setSurvey(mSurvey);
            mInstrument = mSurvey.getInstrument();
        }
        
        mValidationTextView = (TextView) getActivity().findViewById(R.id.validation_text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_factory, parent,
                false);

        ViewGroup questionComponent = (LinearLayout) v
                .findViewById(R.id.question_component);

        // Overridden by subclasses to place their graphical elements on the fragment.
        createQuestionComponent(questionComponent);
        deserialize(mResponse.getText());
        
        return v;
    }

    public Question getQuestion() {	
        return mQuestion;
    }
    
    protected Survey getSurvey() {
        return mSurvey;
    }
    
    public Response getResponse() {
        return mResponse;
    }
    
    public Instrument getInstrument() { 
        return mInstrument;
    }
    
    /*
     * An otherText is injected from a subclass.  This gives
     * the majority of the control to the otherText to the subclass,
     * but the things that all other text fields have in common
     * can go here.
     */
    public void addOtherResponseView(EditText otherText) {
        otherText.setHint(R.string.other_specify_edittext);
        otherText.setEnabled(false);        
        otherText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) { 
                saveOtherResponse(s.toString());
            }
            
            // Required by interface
            public void beforeTextChanged(CharSequence s, int start,
                    int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
        
        if (getResponse().getOtherResponse() != null) {
            otherText.setText(getResponse().getOtherResponse());
        }
    }
    
    public void saveOtherResponse(String response) {
        getResponse().setOtherResponse(response);
        getResponse().save();
    }
    
    /*
     * Display warning to user if response does not match regular 
     * expression in question.  Disable next button if not valid.
     * Only save if valid.
     */
    public void saveResponseWithValidation() {
        if (getResponse().saveWithValidation()) {
            mValidationTextView.setVisibility(TextView.INVISIBLE);
        } else {
            mValidationTextView.setVisibility(TextView.VISIBLE);
            mValidationTextView.setText(R.string.not_valid_response);
        }
        
        // Refresh options menu to reflect response validation status.
        getActivity().invalidateOptionsMenu();
    }
    
    protected void saveResponse() {
        getResponse().setResponse(serialize());
        saveResponseWithValidation();
    }
    
    private Response loadOrCreateResponse() {
        if (mSurvey.getResponseByQuestion(getQuestion()) != null) {
            return mSurvey.getResponseByQuestion(getQuestion());
        } else {
            return new Response();
        }
    }
}
