package org.adaptlab.chpir.android.survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

public class SurveyActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new SurveyFragment();
    }
}
