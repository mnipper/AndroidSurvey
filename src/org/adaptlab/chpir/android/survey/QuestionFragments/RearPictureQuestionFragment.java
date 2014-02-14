package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraActivity;
import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RearPictureQuestionFragment extends QuestionFragment {
	private Button mCameraButton;
	private ImageView mPhoto;
	
    @Override
    protected void createQuestionComponent(ViewGroup questionComponent) {
        
    	mCameraButton = new Button(getActivity());
    	mCameraButton.setText(R.string.enable_camera);
    	mCameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivity(i);
			}
		});
    	
    	mPhoto = new ImageView(getActivity());
    	int id = getResources().getIdentifier("org.adaptlab.chpir.android.survey:drawable/" + "ic_action_picture", null, null);
    	mPhoto.setImageResource(id);
    	
    	questionComponent.addView(mCameraButton);
       	questionComponent.addView(mPhoto);
    }

    @Override
    protected String serialize() {
        return null;
    }

    @Override
    protected void deserialize(String responseText) {
  
    }
   
}
