package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.SurveyActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(manifest="../AndroidSurvey/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class SurveyActivityTest {

    @Test
    public void shouldHaveHappySmiles() throws Exception {
    	SurveyActivity activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
        String hello = activity.getResources().getString(R.string.hello);
        assertThat(hello, equalTo("Hello World"));
    }
}