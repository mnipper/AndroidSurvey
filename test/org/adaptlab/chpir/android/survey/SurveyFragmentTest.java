package org.adaptlab.chpir.android.survey;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;

import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.nonstaticclassimplementations.SubQuestionFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

@RunWith(RobolectricTestRunner.class)
public class SurveyFragmentTest {
	private static final long ID = 10L;
	private static final String TEXT = "text";
	private SurveyActivity activity;
	private SurveyFragment sFragment;
	private Question question;
	private LayoutInflater inflater;
	private ViewGroup parent;
	
	@Before
	public void setUp() throws Exception {
		activity = Robolectric.buildActivity(SurveyActivity.class).create().get();
		setUpMocks();
		sFragment = new SurveyFragment();
		Bundle args = new Bundle();
        args.putLong(SurveyFragment.EXTRA_INSTRUMENT_ID, ID);
        sFragment.setArguments(args);
		startFragment(sFragment);
		inflater = LayoutInflater.from(Robolectric.application);
		parent = new LinearLayout(Robolectric.application);
	}
	
	private void setUpMocks() {
		LinkedList<Question> questionList = new LinkedList<Question>();
		question = mock(Question.class);
		when(question.getText()).thenReturn(TEXT);
		questionList.add(question);
		//when()
	}

	private void startFragment(SurveyFragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
	}
	
	@Test
	public void shouldHaveSameTextViewId() throws Exception {
		//View v = sFragment.onCreateView(inflater, parent, null);
		//assertEquals(v.findViewById(R.id.question_text));
		//TODO Wait a moment
	}
	
}
