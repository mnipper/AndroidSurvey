package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

public abstract class PictureQuestionFragment extends QuestionFragment {
	public static final int REAR_CAMERA = 0;
    public static final int FRONT_CAMERA = 1;
	public static final int REQUEST_PHOTO = 0;
	private static final String TAG = "PictureQuestionFragment";
	private static final String DEFAULT = "org.adaptlab.chpir.android.survey:drawable/ic_action_picture";
	protected CameraFragment mCameraFragment;
	protected ImageView mPhotoView;
	private Bitmap mBitmap;
	
	@Override
	protected abstract void createQuestionComponent(ViewGroup questionComponent);

	@Override
	protected void deserialize(String responseText) {
		//handled by showPhoto()
	}

	@Override
	protected String serialize() {
		return null; //pictures are automatically saved in the CameraFragment
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

	protected void showPhoto() {
		String filename = getResponsePhoto().getPicturePath();
		if (filename != null) {
			String path = getActivity().getFileStreamPath(filename).getAbsolutePath();
			mBitmap = BitmapFactory.decodeFile(path);
			mPhotoView.setImageBitmap(mBitmap);
			rotateImageView();
		} else {
			int resId = getResources().getIdentifier(DEFAULT, null, null);
			mPhotoView.setImageResource(resId);
		}
	}

	private void rotateImageView() {
		int deviceOrientation = getResources().getConfiguration().orientation;
		int originalOrientation = getResponsePhoto().getCameraOrientation();
		int camera = getResponsePhoto().getCamera();
		Log.i(TAG, "CAMERA: " + camera + " ORIENTATION: " + deviceOrientation);		
		if (camera == FRONT_CAMERA && originalOrientation == Configuration.ORIENTATION_PORTRAIT) {
			mPhotoView.setRotation(-90);
		} else if (camera == REAR_CAMERA && originalOrientation == Configuration.ORIENTATION_PORTRAIT) {
			mPhotoView.setRotation(90);
		} else if (originalOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			mPhotoView.setRotation(180);
		} 
	}

}
