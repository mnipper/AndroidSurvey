package org.adaptlab.chpir.android.survey.QuestionFragments;

import org.adaptlab.chpir.android.survey.CameraFragment;
import org.adaptlab.chpir.android.survey.PictureUtils;
import org.adaptlab.chpir.android.survey.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RearPictureQuestionFragment extends PictureQuestionFragment {
	private static final int REAR_CAMERA = 0;
	private static final String TAG = "RearPictureQuestionFragment";
	private Button mCameraButton;
	private ImageView mPhotoView;
	ViewGroup qComponent;
	
	@Override
	protected void createQuestionComponent(ViewGroup questionComponent) {
		qComponent = questionComponent;
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
					mCameraFragment = CameraFragment.newCameraFragmentInstance(getResponsePhoto());
					mCameraFragment.CAMERA = REAR_CAMERA;
					transaction.replace(R.id.fragmentContainer, mCameraFragment);
					transaction.addToBackStack(null); 
					transaction.commit();
				}
			});
			
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View v = inflater.inflate(R.layout.fragment_question_factory, questionComponent, false);
			mPhotoView = (ImageView) v.findViewById(R.id.camera_picture_image_view);
			mPhotoView.setVisibility(View.VISIBLE);
			Log.i(TAG, mPhotoView + " is not null");
			showPhoto();
			
			questionComponent.addView(mCameraButton);
		} else {
			Log.i(TAG, "Camera Not Available");
		}
	}
	
	private void showPhoto() {
		String filename = getResponsePhoto().getPicturePath();
		if (filename != null) {
			Log.i(TAG, "USING IMAGE FROM CAMERA");
			String path = getActivity().getFileStreamPath(filename).getAbsolutePath();
			Log.i(TAG, path);
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			mPhotoView.setImageBitmap(bitmap);
		} else {
			int resId = getResources().getIdentifier("org.adaptlab.chpir.android.survey:drawable/" + "ic_action_picture", null, null);
			mPhotoView.setImageResource(resId);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
	}

}
