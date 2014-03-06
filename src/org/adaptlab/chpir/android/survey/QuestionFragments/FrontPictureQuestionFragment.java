package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.R;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class FrontPictureQuestionFragment extends PictureQuestionFragment {
    private static final int FRONT_CAMERA = 1;
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
					mCameraFragment.setTargetFragment(FrontPictureQuestionFragment.this, REQUEST_PHOTO);
					mCameraFragment.CAMERA = FRONT_CAMERA;
					transaction.add(R.id.fragmentContainer, mCameraFragment);
					transaction.commit();
				}
			});
			questionComponent.addView(mCameraButton);
			questionComponent.addView(getPhoto());
		}
    }

}
