package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.QuestionFragment;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.activeandroid.util.Log;

public abstract class PictureQuestionFragment extends QuestionFragment {
	public static final int REQUEST_PHOTO = 0;
	private static final String TAG = "PictureQuestionFragment";
	protected CameraFragment mCameraFragment;
	protected ImageView mPhotoView;

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
		Log.i(TAG, filename + " is filename");
		if (filename != null) {
			String path = getActivity().getFileStreamPath(filename).getAbsolutePath();
			Log.i(TAG, path + "is path of picture");
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			mPhotoView.setImageBitmap(bitmap);
		} else {
			int resId = getResources().getIdentifier("org.adaptlab.chpir.android.survey:drawable/" + "ic_action_picture", null, null);
			mPhotoView.setImageResource(resId);
		}
	}

}
