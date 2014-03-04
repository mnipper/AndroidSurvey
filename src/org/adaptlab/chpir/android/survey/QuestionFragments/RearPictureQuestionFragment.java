package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.PictureUtils;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.Photo;

import com.activeandroid.util.Log;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RearPictureQuestionFragment extends PictureQuestionFragment {
    private static final int REAR_CAMERA = 0;
    private static final String TAG = "RearPictureQuestionFragment";
	private Button mCameraButton;
	
	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {

		if (isCameraAvailable()) {
			mCameraButton = new Button(getActivity());
			mCameraButton.setText(R.string.enable_camera);
			mCameraButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					FragmentManager fm = getActivity().getSupportFragmentManager();
					FragmentTransaction transaction = fm.beginTransaction();
					mCameraFragment = new CameraFragment();
					mCameraFragment.setTargetFragment(RearPictureQuestionFragment.this, REQUEST_PHOTO);
					mCameraFragment.CAMERA = REAR_CAMERA;
					transaction.add(R.id.fragmentContainer, mCameraFragment);
					transaction.commit();
				}
			});
			questionComponent.addView(mCameraButton);
			questionComponent.addView(getPhoto());
		}
	}
	
}
