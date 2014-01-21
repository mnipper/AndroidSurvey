package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;

public class RatingQuestionFragment extends QuestionFragment  {
    private final static int NUM_STARS = 5;
    private float mRating;
    private RatingBar mRatingBar;

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        mRatingBar = new RatingBar(getActivity());
        mRatingBar.setNumStars(NUM_STARS);
        mRatingBar.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {       
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                    boolean fromUser) {
                mRating = rating;
                saveResponse();
            }
        });
        questionComponent.addView(mRatingBar);
    }

    @Override
    protected String serialize() {
        return String.valueOf(mRating);
    }

    @Override
    protected void deserialize(String responseText) {
        if (!responseText.equals("")) {
            mRating = Float.parseFloat(responseText);
            mRatingBar.setRating(mRating);
        }
    }
  
}
