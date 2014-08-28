package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.DeviceUser;

public class AuthUtils {
    private static DeviceUser sCurrentUser = null;

    
    public static boolean isSignedIn() {
        return sCurrentUser != null;
    }
    
    public static void signOut() {
        sCurrentUser = null;
    }
    
    public static void signIn(DeviceUser deviceUser) {
        sCurrentUser = deviceUser;
    }
    
    public static DeviceUser getCurrentUser() {
        return sCurrentUser;
    }
}
