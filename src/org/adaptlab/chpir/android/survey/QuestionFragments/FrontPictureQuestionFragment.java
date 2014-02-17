package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.R;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FrontPictureQuestionFragment extends PictureQuestionFragment {
    private static final int FRONT_CAMERA = 1;
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
					mCameraFragment.setTargetFragment(FrontPictureQuestionFragment.this, REQUEST_PHOTO);
					mCameraFragment.CAMERA = FRONT_CAMERA;
					transaction.add(R.id.fragmentContainer, mCameraFragment);
					transaction.commit();
				}
			});
			setDefaultImage();
			questionComponent.addView(mCameraButton);
			questionComponent.addView(getPhoto());
		}
    }
    
	@Override
	protected void deserialize(String responseText) {
		if (responseText == "" || responseText == null) {
			setDefaultImage();
		} else {
			showImage(responseText);
		}
	}

}
