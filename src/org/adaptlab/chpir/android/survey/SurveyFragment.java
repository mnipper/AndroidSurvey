package org.adaptlab.chpir.android.survey;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SurveyFragment extends Fragment {
	private final static String TAG = "SurveyFragment";
	private Survey mSurvey;
	private Instrument mInstrument;
	
	private Button mStartButton;
	private Spinner mInstrumentSpinner;
	private List<Instrument> mInstrumentList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSurvey = new Survey();
		mInstrumentList = Instrument.getAll();
		Log.d(TAG, "Instrument list is: " + mInstrumentList);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_survey, parent, false);

		mInstrumentSpinner = (Spinner)v.findViewById(R.id.instruments_spinner);	
		mInstrumentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mInstrument = mInstrumentList.get(position);
				Log.d(TAG, "Selected instrument: " + mInstrument);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				
			}		
		});
		mInstrumentSpinner.setAdapter(new ArrayAdapter<Instrument>(getActivity(),
				android.R.layout.simple_spinner_item, mInstrumentList));
		
		mStartButton = (Button)v.findViewById(R.id.start_survey_button);
		mStartButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (mInstrumentSpinner.getSelectedItem() != null) {
					mSurvey = new Survey();
					mSurvey.setInstrument(mInstrument);
					mSurvey.save();
				}
			}
		});
		
		mStartButton.setEnabled(!mInstrumentList.isEmpty());
		
		return v;
	}
}
