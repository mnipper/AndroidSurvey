package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.activeandroid.Model;

public class SurveyFragment extends Fragment {
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
        mInstrument = Model.load(Instrument.class, instrumentId);
        
        mSurvey = new Survey();
        mSurvey.setInstrument(mInstrument);
        mSurvey.save();
        
        if (mInstrument.questions().size() == 0) {
            // This instrument has no questions
            return;
        }
        
        mQuestion = mInstrument.questions().get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_survey, parent, false);

        mQuestionText = (TextView) v.findViewById(R.id.question_text);
        mQuestionText.setText(mQuestion.getText());

        mNextButton = (Button) v.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                int questionIndex = mInstrument.questions().indexOf(mQuestion);
                int questionsInInstrument = mInstrument.questions().size() - 1;

                if (questionIndex < questionsInInstrument) {
                    mQuestion = mInstrument.questions().get(questionIndex + 1);
                    
                    FragmentManager fm = getChildFragmentManager();
                    fm.beginTransaction()
                            .replace(
                                    R.id.question_container,
                                    QuestionFragmentFactory
                                            .createQuestionFragment(mQuestion, mSurvey))
                            .commit();
                    
                    mQuestionText.setText(mQuestion.getText());

                    if (questionIndex + 1 == questionsInInstrument) {
                        mNextButton.setText(R.string.finish_button);
                    }
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
}
