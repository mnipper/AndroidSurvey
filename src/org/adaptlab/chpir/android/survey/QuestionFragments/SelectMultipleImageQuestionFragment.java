package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;
import java.util.List;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Image;

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
		for (ImageView v : createImageViews()) {
			questionComponent.addView(v);
		}
	}

	private List<ImageView> createImageViews() {
		mImageViews = new ArrayList<ImageView>();
		List<Image> imageList = getQuestion().images();
		for (int i=0; i < imageList.size(); i++) {
			ImageView imgView = new ImageView(getActivity());
			imgView.setImageBitmap(imageList.get(i).getBitmap());
			imgView.setId(i);
			imgView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			imgView.getLayoutParams().width = WIDTH;
			imgView.getLayoutParams().height = HEIGHT;
			imgView.setPadding(PADDING, PADDING, PADDING, PADDING);
			imgView.setBackgroundColor(UNSELECTED); //getting the background color of ColorDrawable when it has not be set throws an exception
			imgView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					toggleImageBackgroundColor((ImageView) v);
				}
			});
			mImageViews.add(imgView);
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
