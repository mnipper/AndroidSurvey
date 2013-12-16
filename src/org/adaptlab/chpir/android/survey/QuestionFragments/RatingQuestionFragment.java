package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.view.ViewGroup;
import android.widget.RatingBar;

public class RatingQuestionFragment extends QuestionFragment  {
    private final static int NUM_STARS = 5;

    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        RatingBar ratingBar = new RatingBar(getActivity());
        ratingBar.setNumStars(NUM_STARS);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {       
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                    boolean fromUser) {
                getResponse().setResponse(String.valueOf(rating));
                getResponse().save();
            }
        });
        questionComponent.addView(ratingBar);
    }
}
