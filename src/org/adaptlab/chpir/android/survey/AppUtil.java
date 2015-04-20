package org.adaptlab.chpir.android.survey;

import java.util.UUID;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.adaptlab.chpir.android.survey.Models.DefaultAdminSettings;
import org.adaptlab.chpir.android.survey.Models.DeviceSyncEntry;
import org.adaptlab.chpir.android.survey.Models.DeviceUser;
import org.adaptlab.chpir.android.survey.Models.Image;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.ResponsePhoto;
import org.adaptlab.chpir.android.survey.Models.Rule;
import org.adaptlab.chpir.android.survey.Models.Section;
import org.adaptlab.chpir.android.survey.Models.Skip;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.Tasks.ApkUpdateTask;
import org.adaptlab.chpir.android.survey.Vendor.BCrypt;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.crashlytics.android.Crashlytics;

public class AppUtil {
    private final static String TAG = "AppUtil";
    public final static boolean PRODUCTION = false;
    public final static boolean REQUIRE_SECURITY_CHECKS = PRODUCTION;
    public static boolean DEBUG = !PRODUCTION;
    public final static boolean ENCRYPT_DB = true;
    
    public static String ADMIN_PASSWORD_HASH;
    public static String ACCESS_TOKEN;
    private static Context mContext;
    private static AdminSettings adminSettingsInstance;
    
    /*
     * Get the version code from the AndroidManifest
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (NameNotFoundException nnfe) {
            Log.e(TAG, "Error finding version code: " + nnfe);
        }
        return -1;
    }
    
    public static final void appInit(Context context) {
    	mContext = context;
        if (AppUtil.REQUIRE_SECURITY_CHECKS) {
            if (!AppUtil.runDeviceSecurityChecks(context)) {
                // Device has failed security checks
                return;
            }
        }
        
        if (ENCRYPT_DB) {
            Intent i = new Intent(context, EncryptionPasswordActivity.class);
            context.startActivity(i);
        } else {
            ActiveAndroid.initialize(new Configuration.Builder(context)
                .setEncrypted(false)
                .create());
            
            setupDatabase(context);
        }

    }
    
    public static void setupDatabase(Context context) {        
        setAdminSettingsInstance();
        
        ADMIN_PASSWORD_HASH = context.getResources().getString(R.string.admin_password_hash);
        ACCESS_TOKEN = adminSettingsInstance.getApiKey(); 
                        
        if (PRODUCTION) {
            Crashlytics.start(context);
            Crashlytics.setUserIdentifier(adminSettingsInstance.getDeviceIdentifier());
            Crashlytics.setString("device label", adminSettingsInstance.getDeviceLabel());
        }
        
        DatabaseSeed.seed(context);

        if (adminSettingsInstance.getDeviceIdentifier() == null) {
            adminSettingsInstance.setDeviceIdentifier(UUID.randomUUID().toString());
        }
        
        if (adminSettingsInstance.getDeviceLabel() == null) {
            adminSettingsInstance.setDeviceLabel("");
        }

        ActiveRecordCloudSync.setAccessToken(ACCESS_TOKEN);
        ActiveRecordCloudSync.setVersionCode(AppUtil.getVersionCode(context));
        ActiveRecordCloudSync.setEndPoint(adminSettingsInstance.getApiUrl());
        ActiveRecordCloudSync.addReceiveTable("instruments", Instrument.class);
        ActiveRecordCloudSync.addReceiveTable("questions", Question.class);
        ActiveRecordCloudSync.addReceiveTable("options", Option.class);
        ActiveRecordCloudSync.addReceiveTable("images", Image.class);
        ActiveRecordCloudSync.addReceiveTable("sections", Section.class);
        ActiveRecordCloudSync.addReceiveTable("device_users", DeviceUser.class);
        ActiveRecordCloudSync.addReceiveTable("skips", Skip.class);
        ActiveRecordCloudSync.addReceiveTable("rules", Rule.class);
        ActiveRecordCloudSync.addSendTable("surveys", Survey.class);
        ActiveRecordCloudSync.addSendTable("responses", Response.class);
        ActiveRecordCloudSync.addSendTable("response_images", ResponsePhoto.class);
        ActiveRecordCloudSync.addSendTable("device_sync_entries", DeviceSyncEntry.class);

        new ApkUpdateTask(mContext).execute();
    }

	private static void setAdminSettingsInstance() {
		if (mContext.getResources().getBoolean(R.bool.default_admin_settings)) {
        	adminSettingsInstance = DefaultAdminSettings.getInstance();
        } else {
        	adminSettingsInstance = AdminSettings.getInstance();
        }
	}
    
    /*
     * Security checks that must pass for the application to start.
     * 
     * If the application fails any security checks, display
     * AlertDialog indicating why and immediately stop execution
     * of the application.
     * 
     * Current security checks: require encryption
     */
    public static final boolean runDeviceSecurityChecks(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (devicePolicyManager.getStorageEncryptionStatus() != DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE) {
            new AlertDialog.Builder(context)
            .setTitle(R.string.encryption_required_title)
            .setMessage(R.string.encryption_required_text)
            .setCancelable(false)
            .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Kill app on OK
                    int pid = android.os.Process.myPid(); 
                    android.os.Process.killProcess(pid);
                }
             })
             .show();
            return false;
        }
        return true;
    }
    
    
    /*
     * Hash the entered password and compare it with admin password hash
     */
    public static boolean checkAdminPassword(String password) {
        return BCrypt.checkpw(password, ADMIN_PASSWORD_HASH);
    }
    
    public static Context getContext() {
    	return mContext;
    }
    
    public static AdminSettings getAdminSettingsInstance() {
    	if (adminSettingsInstance == null) {
    		setAdminSettingsInstance();
    	}
    	return adminSettingsInstance;
    }
    
}
