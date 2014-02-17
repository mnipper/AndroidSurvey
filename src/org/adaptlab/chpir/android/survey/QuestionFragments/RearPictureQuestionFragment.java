package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.activeandroid.util.Log;

public class RearPictureQuestionFragment extends QuestionFragment {
    private static final String TAG = "RearPictureQuestionFragment";
    private static final int REQUEST_PHOTO = 0;

	private Button mCameraButton;
	private ImageView mPhoto;
	private CameraFragment mCameraFragment;
	
	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {

		if (isCameraAvailable()) {
			mCameraButton = new Button(getActivity());
			mCameraButton.setText(R.string.enable_camera);
			mCameraButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Log.i(TAG, "SHOW CAMERA");
					//getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
			        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
					FragmentManager fm = getActivity().getSupportFragmentManager();
					FragmentTransaction transaction = fm.beginTransaction();
					mCameraFragment = new CameraFragment();
					mCameraFragment.setTargetFragment(RearPictureQuestionFragment.this, REQUEST_PHOTO);
					transaction.add(R.id.fragmentContainer, mCameraFragment);
					transaction.commit();
				}
			});
			
			mPhoto = new ImageView(getActivity());
			int id = getResources().getIdentifier("org.adaptlab.chpir.android.survey:drawable/" + "ic_action_picture", null, null);
			mPhoto.setImageResource(id);

			questionComponent.addView(mCameraButton);
			questionComponent.addView(mPhoto);
		}
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		removeCameraFragment();
		Log.i(TAG, "RECEIVED PICTURE");
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(CameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                //Photo p = new Photo(filename);
                //mCrime.setPhoto(p);
                //showPhoto();
            	Log.i(TAG, "FILENAME: " + filename);
            }
        }
	}

	private boolean isCameraAvailable() {
		PackageManager manager = getActivity().getPackageManager();
		if (!manager.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
				!manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			return false;
		} else {
			return true;
		}   
	}
	
	private void removeCameraFragment() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.remove(mCameraFragment);
		transaction.commit();
	}

	@Override
	protected String serialize() {
		return null;
	}

	@Override
	protected void deserialize(String responseText) {

	}

}
