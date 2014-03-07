package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.R;

import com.activeandroid.util.Log;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class RearPictureQuestionFragment extends PictureQuestionFragment {
    private static final int REAR_CAMERA = 0;
	private Button mCameraButton;

	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {

		if (isCameraAvailable()) {
			mCameraButton = new Button(getActivity());
			mCameraButton.setText(R.string.enable_camera);
			LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(500, 120);
			buttonLayout.gravity = Gravity.CENTER;
			mCameraButton.setLayoutParams(buttonLayout);
			mCameraButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					FragmentManager fm = getActivity().getSupportFragmentManager();                    
					FragmentTransaction transaction = fm.beginTransaction();
					mCameraFragment = new CameraFragment();
					mCameraFragment.setTargetFragment(RearPictureQuestionFragment.this, REQUEST_PHOTO);
					mCameraFragment.CAMERA = REAR_CAMERA;
					transaction.replace(R.id.fragmentContainer, mCameraFragment);
					transaction.addToBackStack(null); //TODO Figure out how to send data to a fragment in the back stack
					transaction.commit();
				}
			});
			questionComponent.addView(mCameraButton);
			questionComponent.addView(getImageView());
		}
	}

	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.i("HEY", "CALLED WEIRD ONACTIVITY");
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
