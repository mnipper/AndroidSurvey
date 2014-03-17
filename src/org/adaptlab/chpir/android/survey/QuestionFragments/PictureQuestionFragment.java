package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.PictureUtils;
import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.activeandroid.util.Log;

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
		if (getResponsePhoto() != null) {
			mPhotoFileName = getResponsePhoto().getPicturePath();
		}
		Log.i(TAG, "DESERIALIZED PQF");
	}

	protected ImageView getImageView() {
		return getImageViewPhoto();
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
	public void onPause() {
		super.onPause();
		saveResponsePhoto();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setImageView();
	}

	@Override
	protected String serialize() {
		return mPhotoFileName;
	}

	private ImageView getImageViewPhoto() {
		if (mPhoto == null) {
			mPhoto = new ImageView(getActivity());
			setImageView();
		}
		return mPhoto;
	}
	
	private void setImageView() {
		String filename = getResponsePhoto().getPicturePath();
		if (filename != null) {
			Log.i(TAG, "USING IMAGE FROM CAMERA");
			String path = getActivity().getFileStreamPath(filename).getAbsolutePath();
			BitmapDrawable bitmap = PictureUtils.getScaledDrawable(getActivity(), path);
			mPhoto.setImageDrawable(bitmap);
		}
	}

}
