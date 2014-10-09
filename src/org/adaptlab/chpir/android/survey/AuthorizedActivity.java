package org.adaptlab.chpir.android.survey;

import android.content.Intent;

public abstract class AuthorizedActivity extends SingleFragmentActivity {
    
    @Override
    public void onResume() {
        super.onResume();
        authorize();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        AuthUtils.signOut();
    }
    
    private void authorize() {
        if (AppUtil.getAdminSettingsInstance().getRequirePassword() && !AuthUtils.isSignedIn()) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }
}
