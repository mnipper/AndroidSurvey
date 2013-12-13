package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SurveyFragment extends Fragment {
    private static final String TAG = "SurveyFragment";
    public final static String EXTRA_INSTRUMENT_ID = 
            "org.adaptlab.chpir.android.survey.instrument_id";

    private Question mQuestion;
    private Instrument mInstrument;
    private Survey mSurvey;

    private TextView mQuestionText;
    private Button mNextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Long instrumentId = getActivity().getIntent()
                .getLongExtra(EXTRA_INSTRUMENT_ID, -1);
        if (instrumentId == -1) {
            return;
        }
        mInstrument = Instrument.findByRemoteId(instrumentId);
        
        mSurvey = new Survey();
        mSurvey.setInstrument(mInstrument);
        mSurvey.save();
        
        mQuestion = mInstrument.questions().get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_survey, parent, false);

        mQuestionText = (TextView) v.findViewById(R.id.question_text);
        setQuestionText(mQuestionText);
        mQuestionText.setTypeface(mInstrument.getTypeFace(getActivity().getApplicationContext()));

        mNextButton = (Button) v.findViewById(R.id.next_button);
        
        // Only one question in this instrument
        if (mInstrument.questions().size() == 1) {
            mNextButton.setText(R.string.finish_button);
        }
        
        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                moveToNextQuestion();
            }
        });

        createQuestionFragment();

        return v;
    }

	protected void createQuestionFragment() {	//TODO Re-factored for testing purposes
        // Set up the question fragment for the first question and commit it.
        Fragment questionFragment = QuestionFragmentFactory.createQuestionFragment(mQuestion, mSurvey);
        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentById(R.id.question_container) == null) {
            fm.beginTransaction()
                    .add(R.id.question_container, questionFragment).commit();
        }
	}
    
    /*
     * If a question has a skip pattern, then read the response
     * when pressing the "next" button.  If the index of the response
     * is able to have a skip pattern, then set the next question to
     * the question indicated by the skip pattern.  "Other" responses
     * cannot have skip patterns, and the question is just set to the
     * next question in the sequence.
     */
    private Question getNextQuestion(int questionIndex) {
        Question nextQuestion = null;
        
        if (mQuestion.hasSkipPattern()) {
            try {
                int responseIndex = Integer.parseInt(mSurvey.
                        getResponseByQuestion(mQuestion).getText());
                
                if (responseIndex < mQuestion.options().size()) {
                    nextQuestion = mQuestion.options().get(responseIndex).getNextQuestion();
                } else {
                    // Skip pattern can not yet apply to 'other' responses
                    nextQuestion = mInstrument.questions().get(questionIndex + 1);                    
                }
                
            } catch (NumberFormatException nfe) {
                nextQuestion = mInstrument.questions().get(questionIndex + 1);
                Log.wtf(TAG, "Received a non-numeric skip response index for " + 
                        mQuestion.getQuestionIdentifier());
            }
        } else {
            nextQuestion = mInstrument.questions().get(questionIndex + 1);
        }
        
        return nextQuestion;
    }
    
    /*
     * Switch out the next question with a fragment from the
     * QuestionFragmentFactory.  If this is the last question
     * then change the button text to "finish."  When "finish"
     * is pressed, mark the survey as complete and finish the
     * activity.
     */
    private void moveToNextQuestion() {
        int questionIndex = mInstrument.questions().indexOf(mQuestion);
        int questionsInInstrument = mInstrument.questions().size();

        if (questionIndex < questionsInInstrument - 1) {
            
            mQuestion = getNextQuestion(questionIndex);
            
            FragmentManager fm = getChildFragmentManager();
            fm.beginTransaction()
                    .replace(
                            R.id.question_container,
                            QuestionFragmentFactory
                                    .createQuestionFragment(mQuestion, mSurvey))
                    .commit();
            
            setQuestionText(mQuestionText);

            // Change next button text to finish if last question
            if (mInstrument.questions().indexOf(mQuestion) + 1 == questionsInInstrument) {
                mNextButton.setText(R.string.finish_button);
            }
            
        } else {
            // Hide survey activity when finish button pressed
            getActivity().finish();
            mSurvey.setAsComplete();
            mSurvey.save();
            return;
        }
    }
    
    /*
     * If this question is a following up question, then attempt
     * to get response to question being followed up on.  If this
     * response was skipped, then skip this question.  It does
     * not make sense to ask a follow up question to a question
     * that was not answered.
     * 
     * If this question is not a following up question, then just
     * set the text as normal.
     */
    private void setQuestionText(TextView text) {
        if (mQuestion.getFollowingUpQuestion() != null) {
            
            String followUpText = mQuestion.getFollowingUpText(mSurvey);
            
            if (followUpText == null) {
                moveToNextQuestion();
            } else {
                text.setText(followUpText);
            }
        } else {
            text.setText(mQuestion.getText());
        }
    }
}
