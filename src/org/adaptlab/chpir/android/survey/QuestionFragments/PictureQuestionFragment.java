package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.PictureUtils;
import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.Photo;

import com.activeandroid.util.Log;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.ImageView;

public abstract class PictureQuestionFragment extends QuestionFragment {
    public static final int REQUEST_PHOTO = 0;
    private static final String TAG = "PictureQuestionFragment";
	public CameraFragment mCameraFragment;
	private ImageView mPhoto;
	private String mPhotoFileName;
	
	@Override
	protected abstract void createQuestionComponent(ViewGroup questionComponent);
	
	@Override
	protected void deserialize(String responseText) {
		Log.i(TAG, "response text: " + responseText);
		if (responseText.equals("")) {
			setDefaultImage();
		} else {
			showImage(responseText);
		}
	}
	
	protected ImageView getPhoto() {
		return mPhoto;
	}
	
	protected boolean isCameraAvailable() {
		PackageManager manager = getActivity().getPackageManager();
		if (!manager.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
				!manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			return false;
		} else {
			return true;
		}   
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(CameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
            	showImage(filename);
            }
        }
        removeCameraFragment();
	}
	
	private void removeCameraFragment() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.remove(mCameraFragment);
		transaction.commit();
	}
	
	@Override
	protected String serialize() {
		return mPhotoFileName;
	}
	
	protected void setDefaultImage() {
		mPhoto = new ImageView(getActivity());
		int id = getResources().getIdentifier("org.adaptlab.chpir.android.survey:drawable/" + "ic_action_picture", null, null);
		mPhoto.setImageResource(id);
	}

	protected void showImage(String fileName) {
		mPhotoFileName = fileName;
		saveResponse();
		Photo photo = new Photo(fileName);
		BitmapDrawable bitmap = null;
        if (photo != null) {
            String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
            bitmap = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhoto.setImageDrawable(bitmap);
	}

}
