package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.QuestionFragments.AddressQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.DateQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.DecimalNumberQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.EmailAddressQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.FreeResponseQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.FrontPictureQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.InstructionsQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.IntegerQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.LabeledSliderQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.ListOfIntegerBoxesQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.ListOfTextBoxesQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.MonthAndYearQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.PhoneNumberQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.RatingQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.RearPictureQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectMultipleImageQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectMultipleQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectMultipleWriteOtherQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectOneImageQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectOneQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SelectOneWriteOtherQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.SliderQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.TimeQuestionFragment;
import org.adaptlab.chpir.android.survey.QuestionFragments.YearQuestionFragment;

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

        if (Question.QuestionType.SELECT_ONE.toString().equals(type)) {
            fragment = new SelectOneQuestionFragment();
        } else if (Question.QuestionType.SELECT_MULTIPLE.toString().equals(type)) {
            fragment = new SelectMultipleQuestionFragment();
        } else if (Question.QuestionType.SELECT_ONE_WRITE_OTHER.toString().equals(type)) {
            fragment = new SelectOneWriteOtherQuestionFragment();
        } else if (Question.QuestionType.SELECT_MULTIPLE_WRITE_OTHER.toString().equals(type)) {
            fragment = new SelectMultipleWriteOtherQuestionFragment();
        } else if (Question.QuestionType.FREE_RESPONSE.toString().equals(type)) {
            fragment = new FreeResponseQuestionFragment();
        } else if (Question.QuestionType.SLIDER.toString().equals(type)) {
            fragment = new SliderQuestionFragment();
        } else if (Question.QuestionType.FRONT_PICTURE.toString().equals(type)) {
            fragment = new FrontPictureQuestionFragment();
        } else if (Question.QuestionType.REAR_PICTURE.toString().equals(type)) {
            fragment = new RearPictureQuestionFragment();
        } else if (Question.QuestionType.DATE.toString().equals(type)) {
            fragment = new DateQuestionFragment();
        } else if (Question.QuestionType.RATING.toString().equals(type)) {
            fragment = new RatingQuestionFragment();
        } else if (Question.QuestionType.TIME.toString().equals(type)) {
            fragment = new TimeQuestionFragment();
        } else if (Question.QuestionType.LIST_OF_TEXT_BOXES.toString().equals(type)) {
            fragment = new ListOfTextBoxesQuestionFragment();
        } else if (Question.QuestionType.INTEGER.toString().equals(type)) {
            fragment = new IntegerQuestionFragment();
        } else if (Question.QuestionType.EMAIL_ADDRESS.toString().equals(type)) {
            fragment = new EmailAddressQuestionFragment();
        } else if (Question.QuestionType.DECIMAL_NUMBER.toString().equals(type)) {
            fragment = new DecimalNumberQuestionFragment();
        } else if (Question.QuestionType.INSTRUCTIONS.toString().equals(type)) {
            fragment = new InstructionsQuestionFragment();
        } else if (Question.QuestionType.MONTH_AND_YEAR.toString().equals(type)) {
            fragment = new MonthAndYearQuestionFragment();
        } else if (Question.QuestionType.YEAR.toString().equals(type)) {
            fragment = new YearQuestionFragment();
        } else if (Question.QuestionType.PHONE_NUMBER.toString().equals(type)) {
            fragment = new PhoneNumberQuestionFragment();
        } else if (Question.QuestionType.ADDRESS.toString().equals(type)) {
            fragment = new AddressQuestionFragment();
        } else if (Question.QuestionType.SELECT_ONE_IMAGE.toString().equals(type)) {
        	fragment = new SelectOneImageQuestionFragment();
        } else if (Question.QuestionType.SELECT_MULTIPLE_IMAGE.toString().equals(type)) {
        	fragment = new SelectMultipleImageQuestionFragment();
        } else if (Question.QuestionType.LIST_OF_INTEGER_BOXES.toString().equals(type)) {
            fragment = new ListOfIntegerBoxesQuestionFragment();
        } else if (Question.QuestionType.LABELED_SLIDER.toString().equals(type)) {
            fragment = new LabeledSliderQuestionFragment();
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
