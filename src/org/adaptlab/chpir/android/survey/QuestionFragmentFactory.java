package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.QuestionFragments.DateQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.FreeResponseQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.FrontPictureQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.RearPictureQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectMultipleQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectMultipleWriteOtherQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectOneQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectOneWriteOtherQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SliderQuestionFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class QuestionFragmentFactory {
    private static final String TAG = "QuestionFragmentFactory";
    public static final String EXTRA_QUESTION_ID = 
            "org.adaptlab.chpir.android.survey.question_id";
    public static final String EXTRA_SURVEY_ID = 
            "org.adaptlab.chpir.android.survey.survey_id";

    public static Fragment createQuestionFragment(Question question, Survey survey) {
        String type = question.getQuestionType().toString();
        Fragment fragment = null;

        // TODO: Write automated test to ensure every QuestionType in
        // Question.QuestionType is covered in factory
        if ("SELECT_ONE".equals(type)) {
            fragment = new SelectOneQuestionFragment();
        } else if ("SELECT_MULTIPLE".equals(type)) {
            fragment = new SelectMultipleQuestionFragment();
        } else if ("SELECT_ONE_WRITE_OTHER".equals(type)) {
            fragment = new SelectOneWriteOtherQuestionFragment();
        } else if ("SELECT_MULTIPLE_WRITE_OTHER".equals(type)) {
            fragment = new SelectMultipleWriteOtherQuestionFragment();
        } else if ("FREE_RESPONSE".equals(type)) {
            fragment = new FreeResponseQuestionFragment();
        } else if ("SLIDER".equals(type)) {
            fragment = new SliderQuestionFragment();
        } else if ("FRONT_PICTURE".equals(type)) {
            fragment = new FrontPictureQuestionFragment();
        } else if ("REAR_PICTURE".equals(type)) {
            fragment = new RearPictureQuestionFragment();
        } else if ("DATE".equals(type)) {
            fragment = new DateQuestionFragment();
        } else {
            // Return free response fragment if unknown question type
            // This should never happen
            Log.wtf(TAG, "Received unknown question type: " + type);
            fragment = new FreeResponseQuestionFragment();
        }

        Bundle args = new Bundle();
        args.putLong(EXTRA_QUESTION_ID, question.getRemoteId());
        args.putLong(EXTRA_SURVEY_ID, survey.getId());
        fragment.setArguments(args);

        return fragment;
    }
}
