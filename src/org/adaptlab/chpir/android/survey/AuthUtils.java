package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.DeviceUser;

public class AuthUtils {
    private static DeviceUser CURRENT_USER = null;

    
    public static boolean isSignedIn() {
        return CURRENT_USER != null;
    }
    
    public static void signOut() {
        CURRENT_USER = null;
    }
    
    public static void signIn(DeviceUser deviceUser) {
        CURRENT_USER = deviceUser;
    }
    
    public static DeviceUser getCurrentUser() {
        return CURRENT_USER;
    }
}
