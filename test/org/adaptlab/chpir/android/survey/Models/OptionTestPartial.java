package org.adaptlab.chpir.android.survey.Models;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Option.class, Question.class })
public class OptionTestPartial extends ActiveAndroidTestBase {
	private static final String NEXT_QUESTION = "nextQst01";
	private static final String TABLE = "Option";
	
	@Before
	public void onSetUp() {
		when(tableInfo.getTableName()).thenReturn(TABLE);
	}

	@Test
	public void shouldSetAndGetNextQuestion() throws Exception {
		PowerMockito.mockStatic(Option.class);
		PowerMockito.verifyStatic();
		Option option = PowerMockito.spy(new Option()); // TODO Fails here
		when(option, method(Option.class, "setNextQuestion", String.class)).withArguments(anyString());
		Whitebox.invokeMethod(option, "setNextQuestion", NEXT_QUESTION);
		assertEquals(Question.findByQuestionIdentifier(NEXT_QUESTION), option.getNextQuestion());
	}
}
