package android.robolectric.shadows;

import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;


@Implements(AdminSettings.class)
public class ShadowAdminSettings {
	
	@RealObject private static AdminSettings adminSettings;
	
	public void __constructor__() {
		adminSettings = new AdminSettings();
	}
	
	//@Override 
	@Implementation
	public static AdminSettings getInstance() {
		return adminSettings;
	}
	
	/*@Implementation
	public String getDeviceIdentifier() {
        return "This is the Device ID";
    }*/

}
