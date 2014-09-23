package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.io.IOException;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.view.ViewGroup;
import android.widget.ImageView;

public abstract class PictureQuestionFragment extends QuestionFragment {
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
			checkDeviceOrientation();
		} else {
			int resId = getResources().getIdentifier(DEFAULT, null, null);
			mPhotoView.setImageResource(resId);
		}
	}

	private void checkDeviceOrientation() {
		int deviceOrientation = getResources().getConfiguration().orientation;
		String path = getActivity().getFileStreamPath(getResponsePhoto().getPicturePath()).getAbsolutePath();
		ExifInterface ei = null;
		try {
			ei = new ExifInterface(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int fileOrientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		if (deviceOrientation == Configuration.ORIENTATION_PORTRAIT) {
			switch(fileOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				mPhotoView.setRotation(90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				mPhotoView.setRotation(180);
				break; 
			case ExifInterface.ORIENTATION_ROTATE_270:
				mPhotoView.setRotation(270);
				break;
			default:
				mPhotoView.setRotation(90);
			}
		} else if (deviceOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			switch(fileOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				mPhotoView.setRotation(90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				mPhotoView.setRotation(180);
				break; 
			case ExifInterface.ORIENTATION_ROTATE_270:
				mPhotoView.setRotation(270);
				break;
			default:
				mPhotoView.setRotation(180);
			}
		}
	}

}
