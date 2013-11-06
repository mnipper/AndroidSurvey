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
        mQuestionText.setText(mQuestion.getText());

        mNextButton = (Button) v.findViewById(R.id.next_button);
        
        // Only one question in this instrument
        if (mInstrument.questions().size() == 1) {
            mNextButton.setText(R.string.finish_button);
        }
        
        mNextButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
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
                    
                    mQuestionText.setText(mQuestion.getText());
                } else {
                    // Hide survey activity when finish button pressed
                    getActivity().finish();
                    mSurvey.setAsComplete();
                    mSurvey.save();
                    return;
                }               

                if (questionIndex + 2 == questionsInInstrument) {
                    mNextButton.setText(R.string.finish_button);
                }

            }
        });

        Fragment questionFragment = QuestionFragmentFactory
                .createQuestionFragment(mQuestion, mSurvey);
        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentById(R.id.question_container) == null) {
            fm.beginTransaction()
                    .add(R.id.question_container, questionFragment).commit();
        }

        return v;
    }
    
    private Question getNextQuestion(int questionIndex) {
        Question nextQuestion = null;
        
        if (mQuestion.hasSkipPattern()) {
            try {
                int responseIndex = Integer.parseInt(mSurvey.
                        getResponseByQuestion(mQuestion).getText());
                nextQuestion = mQuestion.options().get(responseIndex).getNextQuestion();
            } catch (NumberFormatException e) {
                nextQuestion = mInstrument.questions().get(questionIndex + 1);
                Log.wtf(TAG, "Received a non-numeric skip response index for " + 
                        mQuestion.getQuestionIdentifier());
            }
        } else {
            nextQuestion = mInstrument.questions().get(questionIndex + 1); 
        }
        
        return nextQuestion;
    }
}
