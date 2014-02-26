package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.util.ArrayList;
import java.util.List;

import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Image;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SelectOneImageQuestionFragment extends QuestionFragment {
	private final int PADDING = 10;
	private final int WIDTH = 500;
	private final int HEIGHT = 500;
	private final int SELECTED = Color.GREEN;
	private final int UNSELECTED = Color.TRANSPARENT;
	private List<ImageView> mImageViews;
    private int mResponseIndex;

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
			imgView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clearBackgroundColor();
					setBackgroundColor((ImageView) v);
					int selectedIndex = ((ImageView) v).getId();
					setResponseIndex(selectedIndex);
				}
			});
			mImageViews.add(imgView);
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
