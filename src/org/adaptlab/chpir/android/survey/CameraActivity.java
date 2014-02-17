package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.QuestionFragments.RearPictureQuestionFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CameraActivity extends SingleFragmentActivity {
    private static final int REQUEST_PHOTO = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {
		//CameraFragment fragment = new CameraFragment();
        //fragment.setTargetFragment(RearPictureQuestionFragment.class, REQUEST_PHOTO);

		return new CameraFragment();
	}

}
