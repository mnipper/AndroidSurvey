package org.adaptlab.chpir.android.robolectric;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.bytecode.ClassInfo;
import org.robolectric.bytecode.Setup;

public class RoboTestRunner extends RobolectricTestRunner {

	public RoboTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		//addClassOrPackageToInstrument("org.adaptlab.chpir.android.robolectric.shadows");
	}
	
	@Override
	public Setup createSetup() {
		return new TestSetUp();
	}
	
	class TestSetUp extends Setup {
	   
	    @Override
	    public boolean shouldInstrument(ClassInfo info) {
	        boolean instrument = super.shouldInstrument(info) || info.getName().equals("android.robolectric.shadows.ShadowAdminSettings");
	        return instrument;
	    }
	}
}
