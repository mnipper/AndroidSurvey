package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;

import org.adaptlab.chpir.android.survey.Location.LocationServiceManager;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.Tasks.SendResponsesTask;

import com.activeandroid.Model;

import android.content.IntentFilter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SurveyFragment extends Fragment {
    private static final String TAG = "SurveyFragment";
    public final static String EXTRA_INSTRUMENT_ID = 
            "org.adaptlab.chpir.android.survey.instrument_id";
    public final static String EXTRA_QUESTION_ID = 
            "org.adaptlab.chpir.android.survey.question_id";
    public final static String EXTRA_QUESTION_NUMBER = 
            "org.adaptlab.chpir.android.survey.question_number";
    public final static String EXTRA_SURVEY_ID = 
            "org.adaptlab.chpir.android.survey.survey_id";
    public final static String EXTRA_PREVIOUS_QUESTION_IDS = 
            "org.adaptlab.chpir.android.survey.previous_questions";
   
    private Question mQuestion;
    private Instrument mInstrument;
    private Survey mSurvey;
    private int mQuestionNumber;
    
    // mPreviousQuestions is a Stack, however Android does not allow you
    // to save a Stack to the savedInstanceState, so it is represented as
    // an Integer array.
    private ArrayList<Integer> mPreviousQuestions;

    private TextView mQuestionText;
    private TextView mQuestionIndex;
    private ProgressBar mProgressBar;
    QuestionFragment mQuestionFragment;
    private LocationServiceManager mLocationServiceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        
        if (savedInstanceState != null) {
            mInstrument = Instrument.findByRemoteId(savedInstanceState.getLong(EXTRA_INSTRUMENT_ID));
            mQuestion = Question.findByRemoteId(savedInstanceState.getLong(EXTRA_QUESTION_ID));
            mSurvey = Survey.load(Survey.class, savedInstanceState.getLong(EXTRA_SURVEY_ID));
            mQuestionNumber = savedInstanceState.getInt(EXTRA_QUESTION_NUMBER);
            mPreviousQuestions = savedInstanceState.getIntegerArrayList(EXTRA_PREVIOUS_QUESTION_IDS);
        } else {
            Long instrumentId = getActivity().getIntent().getLongExtra(EXTRA_INSTRUMENT_ID, -1);
            if (instrumentId == -1) return;
            
            mInstrument = Instrument.findByRemoteId(instrumentId);
            if (mInstrument == null) return;
            
            loadOrCreateSurvey();
            loadOrCreateQuestion();
          
        }
        startLocationServices();
    }
    
    public void loadOrCreateSurvey() {
        Long surveyId = getActivity().getIntent().getLongExtra(EXTRA_SURVEY_ID, -1);
        if (surveyId == -1) {
            mSurvey = new Survey();
            mSurvey.setInstrument(mInstrument);
            mSurvey.save();
        } else {
            mSurvey = Model.load(Survey.class, surveyId);
        }
    }
    
    public void loadOrCreateQuestion() {
        mPreviousQuestions = new ArrayList<Integer>();  
        Long questionId = getActivity().getIntent().getLongExtra(EXTRA_QUESTION_ID, -1);
        if (questionId == -1) {
            mQuestion = mInstrument.questions().get(0);
            mQuestionNumber = 0;              
        } else {
            mQuestion = Model.load(Question.class, questionId);
            mQuestionNumber = mQuestion.getNumberInInstrument() - 1;
            for (int i = 0; i < mQuestionNumber; i++)
                mPreviousQuestions.add(i);
        }  
    }
    
    private void startLocationServices() {
    	mLocationServiceManager = LocationServiceManager.get(getActivity());
        mLocationServiceManager.startLocationUpdates();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationServiceManager.mLocationReceiver, 
                new IntentFilter(LocationServiceManager.ACTION_LOCATION));
    }
    
    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationServiceManager.mLocationReceiver);
        super.onStop();
    }

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_INSTRUMENT_ID, mInstrument.getRemoteId());
        outState.putLong(EXTRA_QUESTION_ID, mQuestion.getRemoteId());
        outState.putLong(EXTRA_SURVEY_ID, mSurvey.getId());
        outState.putInt(EXTRA_QUESTION_NUMBER, mQuestionNumber);
        outState.putIntegerArrayList(EXTRA_PREVIOUS_QUESTION_IDS, mPreviousQuestions);
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
        case R.id.menu_item_skip:
        	skipQuestion();
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
            .setEnabled(!isFirstQuestion());
        menu.findItem(R.id.menu_item_next)
            .setVisible(!isLastQuestion())
            .setEnabled(hasValidResponse());
        menu.findItem(R.id.menu_item_skip)
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
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        
        updateQuestionCountLabel();
        
        setQuestionText(mQuestionText);
        mQuestionText.setTypeface(mInstrument.getTypeFace(getActivity().getApplicationContext()));
        createQuestionFragment();
        
        ActivityCompat.invalidateOptionsMenu(getActivity());
        getActivity().getActionBar().setTitle(mInstrument.getTitle());
                
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
        
        mSurvey.setLastQuestion(mQuestion);
        mSurvey.save();
        removeTextFocus();
	}
	
	/*
	 * This will remove the focus of the input as the survey is
	 * traversed.  If this is not called, then it will be possible
	 * for someone to change the answer to a question that they are
	 * not currently viewing.
	 */
	private void removeTextFocus() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        
        if (mQuestion.hasSkipPattern() && mSurvey.getResponseByQuestion(mQuestion) != null) {
            try {
                int responseIndex = Integer.parseInt(mSurvey.
                        getResponseByQuestion(mQuestion).getText());
                
                if (responseIndex < mQuestion.options().size() && mQuestion.options().get(responseIndex).getNextQuestion() != null) {
                    nextQuestion = mQuestion.options().get(responseIndex).getNextQuestion();
                    mQuestionNumber = mInstrument.questions().indexOf(nextQuestion);
                } else {
                    // Skip pattern can not yet apply to 'other' responses
                    mQuestionNumber = questionIndex + 1;
                    nextQuestion = mInstrument.questions().get(mQuestionNumber);
                }
                
            } catch (NumberFormatException nfe) {
                mQuestionNumber = questionIndex + 1;
                nextQuestion = mInstrument.questions().get(mQuestionNumber);
                Log.wtf(TAG, "Received a non-numeric skip response index for " + 
                        mQuestion.getQuestionIdentifier());
            }
        } else {
            mQuestionNumber = questionIndex + 1;
            nextQuestion = mInstrument.questions().get(mQuestionNumber);
        }
        
        return nextQuestion;
    }
    
    /*
     * Switch out the next question with a fragment from the
     * QuestionFragmentFactory.  Increment the question to
     * the next question.
     */
    public void moveToNextQuestion() {
        int questionsInInstrument = mInstrument.questions().size();

        if (mQuestionNumber < questionsInInstrument - 1) {    
            mPreviousQuestions.add(mQuestionNumber);
            mQuestion = getNextQuestion(mQuestionNumber);            
            createQuestionFragment();
            if (!setQuestionText(mQuestionText))
                moveToNextQuestion();
        } else if (isLastQuestion() && !setQuestionText(mQuestionText)) {
        	finishSurvey();
        }
        
        updateQuestionCountLabel();
    }
    
    /*
     * Move to previous question.  Takes into account if
     * this question is following up another question.  If
     * this question is not a follow up question, just move
     * to the previous question in the sequence.
     */
    public void moveToPreviousQuestion() {
        if (mQuestionNumber >= 0) {
            mQuestionNumber = mPreviousQuestions.remove(mPreviousQuestions.size() - 1);
            mQuestion = mInstrument.questions().get(mQuestionNumber);
            createQuestionFragment();
            if (!setQuestionText(mQuestionText))
                moveToPreviousQuestion();
        }
        
        updateQuestionCountLabel();
    }

    /*
    * Destroy this activity, and save the survey and mark it as
    * complete.  Send to server if network is available.
    */
    public void finishSurvey() {
        getActivity().finish();
        setSurveyLocation();
        mSurvey.setAsComplete();
        mSurvey.save();
        new SendResponsesTask(getActivity()).execute();
    }
       
    public boolean isFirstQuestion() {
        return mQuestionNumber == 0;
    }
    
    public boolean isLastQuestion() {
        return mInstrument.questions().size() == mQuestionNumber + 1;
    }

    public boolean hasValidResponse() {
        if (mQuestionFragment.getResponse() != null) {
            return mQuestionFragment.getResponse().isValid();
        } else {
            return true;
        }
    }
    
    private void setSurveyLocation() {
    	mSurvey.setLatitude(mLocationServiceManager.getLatitude());
    	mSurvey.setLongitude(mLocationServiceManager.getLongitude());
    }
    
    /*
     * If this question is a follow up question, then attempt
     * to get the response to the question that is being followed up on.
     * 
     * If the question being followed up on was skipped by the user,
     * then return false. This gives the calling function an opportunity
     * to handle this accordingly.  Likely this will involve skipping
     * the question that is a follow up question.
     * 
     * If this question is not a following up question, then just
     * set the text as normal.
     */
    private boolean setQuestionText(TextView text) {
        if (mQuestion.isFollowUpQuestion()) {
            String followUpText = mQuestion.getFollowingUpText(mSurvey, getActivity());
            
            if (followUpText == null) {
                return false;
            } else {
                text.setText(styleTextWithHtml(followUpText));
            }
        } else {
            text.setText(styleTextWithHtml(mQuestion.getText()));
        }
        return true;
    }
    
    private Spanned styleTextWithHtml(String text) {
    	return Html.fromHtml(text);
    }
    
    private void skipQuestion() {
        mQuestionFragment.questionIsSkipped();
        
        if (isLastQuestion()) {
            finishSurvey();
        } else {
            moveToNextQuestion();
        }
    }
            
    private void updateQuestionCountLabel() {    	
        int numberQuestions = mInstrument.questions().size();
        
        mQuestionIndex.setText((mQuestionNumber + 1) + " " + getString(R.string.of) + " " + numberQuestions);        
        mProgressBar.setProgress((int) (100 * (mQuestionNumber + 1) / (float) numberQuestions));
        
        ActivityCompat.invalidateOptionsMenu(getActivity());
    }
    
}
