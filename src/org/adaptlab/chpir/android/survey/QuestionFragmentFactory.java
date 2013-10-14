package org.adaptlab.chpir.android.survey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuestionFragmentFactory {
	
	public static Fragment createQuestion(Question.QuestionType type) {
		Fragment qf = null;
		
		//TODO: Write automated test to ensure every QuestionType in
		//      Question.QuestionType is covered in factory
		if ("SELECT_ONE".equals(type.toString())) {
			qf = new SelectOneQuestionFragment();
		} else if ("SELECT_MULTIPLE".equals(type.toString())) {
			qf = new SelectMultipleQuestionFragment();
		} else if ("SELECT_ONE_WRITE_OTHER".equals(type.toString())) {
			qf = new SelectOneWriteOtherQuestionFragment();
		} else if ("SELECT_MULTIPLE_WRITE_OTHER".equals(type.toString())) {
			qf = new SelectMultipleWriteOtherQuestionFragment();
		} else if ("FREE_RESPONSE".equals(type.toString())) {
			qf = new FreeResponseQuestionFragment();
		} else if ("FRONT_PICURE".equals(type.toString())) {
			qf = new FrontPictureQuestionFragment();
		} else if ("REAR_PICTURE".equals(type.toString())) {
			qf = new RearPictureQuestionFragment();
		}
		
		return qf;
	}
	
	public interface QuestionTemplate {
		public Fragment createFragment();
	}
	
	public static class SelectOneQuestionFragment extends Fragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_select_one, parent, false);
			return v;
		}
	}
	
	public static class SelectMultipleQuestionFragment extends Fragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_select_multiple, parent, false);
			return v;
		}
	}
	
	public static class SelectOneWriteOtherQuestionFragment extends Fragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_select_one, parent, false);
			return v;
		}
	}
	
	public static class SelectMultipleWriteOtherQuestionFragment extends Fragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_select_one, parent, false);
			return v;
		}
	}
	
	public static class FreeResponseQuestionFragment extends Fragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_select_one, parent, false);
			return v;
		}
	}
	
	public static class SliderQuestionFragment extends Fragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_select_one, parent, false);
			return v;
		}
	}
	
	public static class FrontPictureQuestionFragment extends Fragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_select_one, parent, false);
			return v;
		}
	}
	
	public static class RearPictureQuestionFragment extends Fragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup parent,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_select_one, parent, false);
			return v;
		}
	}
}
