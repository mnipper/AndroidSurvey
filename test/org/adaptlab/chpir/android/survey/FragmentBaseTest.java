package org.adaptlab.chpir.android.survey;

import org.junit.Before;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class FragmentBaseTest {
	@Before
	public abstract void setUp() throws Exception;
	
	public void startFragment(SingleFragmentActivity activity, Fragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(fragment, null);
		fragmentTransaction.commit();
	}
}
