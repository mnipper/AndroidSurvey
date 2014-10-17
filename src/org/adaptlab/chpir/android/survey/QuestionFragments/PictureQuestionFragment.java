package org.adaptlab.chpir.android.survey.QuestionFragments;

import java.io.File;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.QuestionFragment;
import org.adaptlab.chpir.android.survey.Models.ResponsePhoto;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public abstract class PictureQuestionFragment extends QuestionFragment {
	public static final int REAR_CAMERA = 0;
    public static final int FRONT_CAMERA = 1;
	public static final int REQUEST_PHOTO = 0;
	private static final String TAG = "PictureQuestionFragment";
	public static final String DEFAULT = "org.adaptlab.chpir.android.survey:drawable/ic_action_picture";
	protected CameraFragment mCameraFragment;
	protected ImageView mPhotoView;
	private ResponsePhoto mPhoto;
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
	
	protected ResponsePhoto getResponsePhoto() {
		return mPhoto;
	}
	
	protected void loadOrCreateResponsePhoto() {
		if (getResponse().getResponsePhoto() == null) {
    		mPhoto = new ResponsePhoto();
    		mPhoto.setResponse(getResponse());
    		mPhoto.save();
        } else {
        	mPhoto = getResponse().getResponsePhoto();
        }
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

	protected boolean showPhoto() {
		String filename = mPhoto.getPicturePath();
		if (filename != null && filename != "") {
			String path = getActivity().getFileStreamPath(filename).getAbsolutePath();
			mBitmap = BitmapFactory.decodeFile(path);
			mPhotoView.setImageBitmap(mBitmap);
			rotateImageView();
			return true;
		} else {
			int resId = getResources().getIdentifier(DEFAULT, null, null);
			mPhotoView.setImageResource(resId);
			return false;
		}
	}

	private void rotateImageView() {
		int deviceOrientation = getResources().getConfiguration().orientation;
		int originalOrientation = mPhoto.getCameraOrientation();
		int camera = mPhoto.getCamera();
		Log.i(TAG, "CAMERA: " + camera + " ORIENTATION: " + deviceOrientation);		
		if (camera == FRONT_CAMERA && originalOrientation == Configuration.ORIENTATION_PORTRAIT) {
			mPhotoView.setRotation(-90);
		} else if (camera == REAR_CAMERA && originalOrientation == Configuration.ORIENTATION_PORTRAIT) {
			mPhotoView.setRotation(90);
		} else if (originalOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			mPhotoView.setRotation(180);
		} 
	}
	
	protected Button setDeleteButton(final ResponsePhoto photo, final ImageView photoView) {
		final Button deleteButton = new Button(getActivity());
		deleteButton.setText("Delete Picture");
		deleteButton.setBackgroundColor(Color.RED);
		LinearLayout.LayoutParams deleteButtonLayout = new LinearLayout.LayoutParams(500, 120);
		deleteButtonLayout.gravity = Gravity.CENTER|Gravity.BOTTOM;
		deleteButton.setLayoutParams(deleteButtonLayout);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String filename = photo.getPicturePath();
				if (filename != null && filename != "") {
					String path = getActivity().getFileStreamPath(filename).getAbsolutePath();
					File file = new File(path);
					if (file.exists()) {
						file.delete();
					}
					photo.setPicturePath(null);
					photo.save();
					int resId = getResources().getIdentifier(DEFAULT, null, null);
					photoView.setImageResource(resId);
					deleteButton.setVisibility(View.INVISIBLE);
				}
			}
		});
		return deleteButton;
	}

}
