package org.adaptlab.chpir.android.survey;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.adaptlab.chpir.android.survey.Models.ResponsePhoto;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CameraFragment extends Fragment {
	private static final String TAG = "CameraFragment";
	public static final String EXTRA_PHOTO_FILENAME = "CameraFragment.filename";
	public static final String EXTRA_RESPONSE_PHOTO = "CameraFragment.photo";
	public static final String EXTRA_CAMERA = "CameraFragment.camera_index";
	public static int CAMERA;
	private View mProgressIndicator;
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private static ResponsePhoto mPhoto;
	
	public static CameraFragment newCameraFragmentInstance(ResponsePhoto picture, int camera_index) {
		mPhoto = picture;
		CAMERA = camera_index;
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_RESPONSE_PHOTO,  mPhoto);
		args.putSerializable(EXTRA_CAMERA, CAMERA);
		CameraFragment fragment = new CameraFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CAMERA, CAMERA);
	}

	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			mProgressIndicator.setVisibility(View.VISIBLE);
		}
	};

	private Camera.PictureCallback mJPEGCallBack = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			String filename = UUID.randomUUID().toString() + ".jpg";
			FileOutputStream os = null;
			boolean success = true;
			try {
				os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			} catch (Exception e) {
				success = false;
			} finally {
				try {
					if (os != null)
						os.close();
				} catch (Exception e) {
					success = false;
				} 
			}
			if (success) {
				ResponsePhoto picture =  (ResponsePhoto) getArguments().getSerializable(EXTRA_RESPONSE_PHOTO);
				picture.setPicturePath(filename);
				picture.setCameraOrientation(getResources().getConfiguration().orientation);
				picture.setCamera(CAMERA);
				picture.save();
				releaseCamera();
				popBackQuestionFragment();
			} else {
				Log.i(TAG, "Not Successful");
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_camera, parent, false);
		mProgressIndicator = v.findViewById(R.id.camera_progressIndicator);
		mProgressIndicator.setVisibility(View.INVISIBLE);
		Button takePictureButton = (Button) v.findViewById(R.id.camera_takePictureButton);
		takePictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, mJPEGCallBack);
				}
			}
		});

		mSurfaceView = (SurfaceView)v.findViewById(R.id.camera_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (mCamera != null) {
						mCamera.setPreviewDisplay(holder);
					}
				} catch(IOException e) {
					Log.e(TAG, "Error setting up surface preview display", e);
				}
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				if (mCamera != null) {
					mCamera.stopPreview();
					releaseCamera();
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
				if (mCamera == null) {
					return;
				}
				try {
					Camera.Parameters parameters = mCamera.getParameters();
					Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
					parameters.setPreviewSize(s.width, s.height);
					mCamera.setParameters(parameters);
					setCameraDisplayOrientation(getActivity(), CAMERA, mCamera);
					mCamera.startPreview();
				} catch (Exception e) {
					Log.e(TAG, "Unable to start preview",e);
					mCamera.release();
                    mCamera = null;
                    popBackQuestionFragment();
				}
			}	
		});

		return v;
	}
	
	public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }
	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }

	private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) width / height;
		if (sizes == null) return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;
		int targetHeight = height;

		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {    
			mCamera = Camera.open(CAMERA);
		} else {
			mCamera = Camera.open();
		}
	}
	//TODO Open the Camera on a different thread from main 

	@Override
	public void onPause() {
		super.onPause();
		releaseCamera();
	}
	
	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	protected void popBackQuestionFragment() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		fm.popBackStack();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.detach(CameraFragment.this);
		transaction.commit();
	}

}
