package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;
import java.util.List;

import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SelectOneImageQuestionFragment extends QuestionFragment {
	private final int PADDING = 10;
	private final int SELECTED = Color.GREEN;
	private final int UNSELECTED = Color.TRANSPARENT;
	private List<ImageView> mImageViews;
    private int mResponseIndex;


	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {		
		createTempImages();
		for (ImageView v : createTempImages()) {
			questionComponent.addView(v);
		}
	}

	private List<ImageView> createTempImages() {
		mImageViews = new ArrayList<ImageView>();
		int id = getResources().getIdentifier("org.adaptlab.chpir.android.survey:drawable/" + "ic_action_picture", null, null);
		for (int i=0; i < 3; i++) {
			ImageView image = new ImageView(getActivity());
			image.setImageResource(id);
			image.setId(i);
			image.setPadding(PADDING, PADDING, PADDING, PADDING);
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearBackgroundColor();
					setBackgroundColor((ImageView) v);
					int selectedIndex = ((ImageView) v).getId();
					setResponseIndex(selectedIndex);
				}
			});
			mImageViews.add(image);
		}
		return mImageViews;
	}
	
	private void setBackgroundColor(ImageView view) {
		view.setBackgroundColor(SELECTED);
	}
	
	private void clearBackgroundColor() {
		for (ImageView view : mImageViews) {
			view.setBackgroundColor(UNSELECTED);
		}
	}
	
	private void setResponseIndex(int index) {
		mResponseIndex = index;
		saveResponse();
	}

	@Override
	protected String serialize() {
		return String.valueOf(mResponseIndex);
	}

	@Override
	protected void deserialize(String responseText) {
		if (!responseText.equals("")) {
			for (ImageView view : mImageViews) {
				if(view.getId() == Integer.parseInt(responseText)) {
					setBackgroundColor(view);
				}
			}
		}
	}

}
