package org.adaptlab.chpir.android.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class SurveyListFragment extends ListFragment {
	private static final int REQUEST_NEW_SURVEY = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.survey_list_options, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_survey:
			Intent i = new Intent(getActivity(), SurveyActivity.class);
			startActivityForResult(i, REQUEST_NEW_SURVEY);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(getActivity(), SurveyActivity.class);
		i.putExtra(SurveyActivity.EXTRA_SURVEY_ID, id);
		startActivity(i);
	}
}
