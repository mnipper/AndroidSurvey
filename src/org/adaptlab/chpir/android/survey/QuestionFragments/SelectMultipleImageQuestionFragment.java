package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;
import java.util.List;

import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SelectMultipleImageQuestionFragment extends QuestionFragment {
	private final int PADDING = 10;
	private final int WIDTH = 500;
	private final int HEIGHT = 500;
	private final int SELECTED = Color.GREEN;
	private final int UNSELECTED = Color.TRANSPARENT;
	private List<ImageView> mImageViews;
	
	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {
		for (ImageView v : createTempImages()) {
			questionComponent.addView(v);
		}
	}

	private List<ImageView> createTempImages() {
		mImageViews = new ArrayList<ImageView>();
		int id = getResources().getIdentifier("org.adaptlab.chpir.android.survey:drawable/" + "ic_action_picture", null, null);
		for (int i=0; i < 4; i++) {
			ImageView image = new ImageView(getActivity());
			image.setImageResource(id);
			image.setId(i);
			image.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			image.getLayoutParams().width = WIDTH;
			image.getLayoutParams().height = HEIGHT;
			image.setPadding(PADDING, PADDING, PADDING, PADDING);
			image.setBackgroundColor(UNSELECTED); //getting the background color of ColorDrawable when it has not be set throws an exception
			image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					toggleImageBackgroundColor((ImageView) v);
				}
			});
			mImageViews.add(image);
		}
		return mImageViews;
	}
	
	private void toggleImageBackgroundColor(ImageView view) {
		ColorDrawable drawable = (ColorDrawable) view.getBackground();
		if (drawable.getColor() == SELECTED) {
			view.setBackgroundColor(UNSELECTED);
		} else {
			view.setBackgroundColor(SELECTED);
		}
		saveResponse();
	}

	@Override
	protected String serialize() {
		String serialized = "";
		for (int i=0; i<mImageViews.size(); i++) {
			ImageView view = mImageViews.get(i);
			ColorDrawable drawable = (ColorDrawable) view.getBackground();
			if (drawable.getColor() == SELECTED) {
				serialized += view.getId();
				if (i <  mImageViews.size() - 1) serialized += LIST_DELIMITER;
			}
		}
		return serialized;
	}

	@Override
	protected void deserialize(String responseText) {
		if (responseText.equals("")) return;    
        String[] listOfIndices = responseText.split(LIST_DELIMITER);
        for (String index : listOfIndices) {
            if (!index.equals("")) {
                Integer indexInteger = Integer.parseInt(index);
                mImageViews.get(indexInteger).setBackgroundColor(SELECTED);
            }
        }		
	}

}
