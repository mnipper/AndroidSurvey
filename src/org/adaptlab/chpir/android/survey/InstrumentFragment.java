package org.adaptlab.chpir.android.survey;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.PollService;
import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class InstrumentFragment extends Fragment {
    private final static String TAG = "InstrumentFragment";

    private Instrument mInstrument;
    private List<Instrument> mInstrumentList;

    private Button mStartButton;
    private Spinner mInstrumentSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        appInit();
        mInstrumentList = Instrument.getAll();
        Log.d(TAG, "Instrument list is: " + mInstrumentList);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_instrument, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_admin:
                Intent i = new Intent(getActivity(), AdminActivity.class);
                startActivity(i); 
            case R.id.menu_item_refresh:
                mInstrumentList = Instrument.getAll();
                mInstrumentSpinner.setAdapter(new ArrayAdapter<Instrument>(
                        getActivity(), android.R.layout.simple_spinner_item,
                        mInstrumentList));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instrument, parent, false);

        mInstrumentSpinner = (Spinner) v.findViewById(R.id.instruments_spinner);
        mInstrumentSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                        mInstrument = mInstrumentList.get(position);
                        Log.d(TAG, "Selected instrument: " + mInstrument);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        
                    }
                });
        mInstrumentSpinner.setAdapter(new ArrayAdapter<Instrument>(
                getActivity(), android.R.layout.simple_spinner_item,
                mInstrumentList));

        mStartButton = (Button) v.findViewById(R.id.start_survey_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mInstrument == null || mInstrument.questions().size() == 0) {
                    if (mInstrument.questions().size() == 0) Log.i(TAG, "No questions");
                    return;
                }

                long instrumentId = mInstrument.getId();
                Intent i = new Intent(getActivity(), SurveyActivity.class);
                i.putExtra(SurveyFragment.EXTRA_INSTRUMENT_ID, instrumentId);
                startActivity(i);
            }
        });

        return v;
    }
    
    private final void appInit() {
        Log.i(TAG, "Initializing application...");
        DatabaseSeed.seed(getActivity());
        
        AdminSettings.getInstance().setDeviceIdentifier("TestDevice1");
        AdminSettings.getInstance().setSyncInterval(2);
        AdminSettings.getInstance().save();
        
        PollService.setPollInterval(AdminSettings.getInstance().getSyncInterval());

        ActiveRecordCloudSync.setEndPoint(AdminSettings.getInstance().getApiUrl());
        ActiveRecordCloudSync.addReceiveTable("instruments", Instrument.class);
        ActiveRecordCloudSync.addReceiveTable("questions", Question.class);
        ActiveRecordCloudSync.addReceiveTable("options", Option.class);

        ActiveRecordCloudSync.addSendTable("surveys", Survey.class);
        ActiveRecordCloudSync.addSendTable("responses", Response.class);
        
        PollService.setServiceAlarm(getActivity(), true);
    }
}
