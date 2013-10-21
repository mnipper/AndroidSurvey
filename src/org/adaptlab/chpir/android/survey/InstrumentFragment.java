package org.adaptlab.chpir.android.survey;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.PollService;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;

import android.content.Intent;
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

public class InstrumentFragment extends Fragment {
    private final static String TAG = "InstrumentFragment";

    private Instrument mInstrument;
    private List<Instrument> mInstrumentList;

    private Button mStartButton;
    private Spinner mInstrumentSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInit();
        mInstrumentList = Instrument.getAll();
        Log.d(TAG, "Instrument list is: " + mInstrumentList);
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
                if (mInstrument == null) {
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
        ActiveRecordCloudSync.addReceiveTable("instruments", Instrument.class);
        ActiveRecordCloudSync.addReceiveTable("questions", Question.class);
        ActiveRecordCloudSync.addReceiveTable("options", Option.class);
        ActiveRecordCloudSync.setEndPoint("http://192.168.1.117:3000/");
        Intent i = new Intent(getActivity(), PollService.class);
        getActivity().startService(i);
    }
}
