package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SurveyFragment extends Fragment {
    private static final String TAG = "SurveyFragment";
    public final static String EXTRA_INSTRUMENT_ID = 
            "org.adaptlab.chpir.android.survey.instrument_id";

    private Question mQuestion;
    private Instrument mInstrument;
    private Survey mSurvey;

    private TextView mQuestionText;
    private TextView mQuestionIndex;
    QuestionFragment mQuestionFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_survey, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_item_previous:
            moveToPreviousQuestion();
            return true;
        case R.id.menu_item_next:
            moveToNextQuestion();
            return true;
        case R.id.menu_item_finish:
            finishSurvey();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_item_previous)
            .setVisible(!isFirstQuestion());
        menu.findItem(R.id.menu_item_next)
            .setVisible(!isLastQuestion())
            .setEnabled(hasValidResponse());
        menu.findItem(R.id.menu_item_finish)
            .setVisible(isLastQuestion())
            .setEnabled(hasValidResponse());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_survey, parent, false);

        mQuestionText = (TextView) v.findViewById(R.id.question_text);
        mQuestionIndex = (TextView) v.findViewById(R.id.question_index);
        
        updateQuestionCountLabel();
        
        setQuestionText(mQuestionText);
        mQuestionText.setTypeface(mInstrument.getTypeFace(getActivity().getApplicationContext()));
        createQuestionFragment();
        getActivity().invalidateOptionsMenu();

        return v;
    }

    /*
     * Place the question fragment for the corresponding mQuestion
     * on the view in the question_container.
     */
	protected void createQuestionFragment() {
        FragmentManager fm = getChildFragmentManager();       
        mQuestionFragment = (QuestionFragment) QuestionFragmentFactory.createQuestionFragment(mQuestion, mSurvey);

        if (fm.findFragmentById(R.id.question_container) == null) {
            // Add the first question fragment
            fm.beginTransaction()
                .add(R.id.question_container, mQuestionFragment)
                .commit();
        } else {
            // Replace the question fragment if it already exist
            fm.beginTransaction()
                .replace(R.id.question_container, mQuestionFragment)
                .commit();            
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
            createQuestionFragment();
            setQuestionText(mQuestionText);
        }
        
        updateQuestionCountLabel();
        getActivity().invalidateOptionsMenu();
    }
    
    /*
     * Move to previous question.  Does not take into account skip
     * patterns.
     */
    private void moveToPreviousQuestion() {
        int questionIndex = mInstrument.questions().indexOf(mQuestion);
        if (questionIndex > 0) {           
            mQuestion = mInstrument.questions().get(questionIndex - 1);            
            createQuestionFragment();
            setQuestionText(mQuestionText);
        }
        
        updateQuestionCountLabel();
        getActivity().invalidateOptionsMenu();
    }
    
    private void finishSurvey() {
        getActivity().finish();
        mSurvey.setAsComplete();
        mSurvey.save();
    }
    
    /*
     * If this question is a follow up question, then attempt
     * to get the response to the question that is being followed up on.
     * 
     * If the question being followed up on was skipped by the user,
     * then also skip the following up question. It does not make sense to
     * ask a follow up question to a question that was not answered.
     * 
     * If this question is not a following up question, then just
     * set the text as normal.
     */
    private void setQuestionText(TextView text) {
        if (mQuestion.isFollowUpQuestion()) {
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
    
    private boolean isFirstQuestion() {
        return mInstrument.questions().indexOf(mQuestion) == 0;
    }
    
    private boolean isLastQuestion() {
        return mInstrument.questions().size() == (mInstrument.questions().indexOf(mQuestion) + 1);
    }

    private boolean hasValidResponse() {
        if (mQuestionFragment.getResponse() != null) {
            return mQuestionFragment.getResponse().isValid();
        } else {
            return true;
        }
    }
    
    private void updateQuestionCountLabel() {
        mQuestionIndex.setText((mInstrument.questions().indexOf(mQuestion) + 1)
                + " " + getString(R.string.of) + " "
                + mInstrument.questions().size()
        );
    }
}
