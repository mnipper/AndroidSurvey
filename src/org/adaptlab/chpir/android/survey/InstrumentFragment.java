package org.adaptlab.chpir.android.survey;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.survey.Models.Instrument;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class InstrumentFragment extends ListFragment {
    private final static String TAG = "InstrumentFragment";
    private final static boolean REQUIRE_SECURITY_CHECKS = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setListAdapter(new InstrumentAdapter(Instrument.getAll()));  
        if (REQUIRE_SECURITY_CHECKS) {
            if (!AppUtil.runDeviceSecurityChecks(getActivity())) {
                // Device has failed security checks                
                return;
            }
        }
        
        AppUtil.appInit(getActivity());
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
            displayPasswordPrompt();
            return true;
        case R.id.menu_item_refresh:
            new RefreshInstrumentsTask().execute();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((InstrumentAdapter) getListAdapter()).notifyDataSetChanged();
    }

    private class InstrumentAdapter extends ArrayAdapter<Instrument> {
        public InstrumentAdapter(List<Instrument> instruments) {
            super(getActivity(), 0, instruments);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.list_item_instrument, null);
            }

            Instrument instrument = getItem(position);

            TextView titleTextView = (TextView) convertView
                    .findViewById(R.id.instrument_list_item_titleTextView);
            titleTextView.setText(instrument.getTitle());
            titleTextView.setTypeface(instrument.getTypeFace(getActivity().getApplicationContext()));

            TextView questionCountTextView = (TextView) convertView
                    .findViewById(R.id.instrument_list_item_questionCountTextView);
            
            int numQuestions = instrument.questions().size();
            questionCountTextView.setText(numQuestions + " "
                    + FormatUtils.pluralize(numQuestions, getString(R.string.question), getString(R.string.questions)));

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Instrument instrument = ((InstrumentAdapter) getListAdapter()).getItem(position);
        if (instrument == null) {
            return;
        }
        
        new LoadInstrumentTask().execute(instrument);
    }
   
    /*
     * Only display admin area if correct password.
     */
    private void displayPasswordPrompt() {
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new AlertDialog.Builder(getActivity())
            .setTitle(R.string.password_title)
            .setMessage(R.string.password_message)
            .setView(input)
            .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() { 
                public void onClick(DialogInterface dialog, int button) {
                    if (AppUtil.checkAdminPassword(input.getText().toString())) {
                        Intent i = new Intent(getActivity(), AdminActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), R.string.incorrect_password, Toast.LENGTH_LONG).show();
                    }
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) { }
            }).show();
    }
    
    /*
     * Refresh the receive tables from the server
     */
    private class RefreshInstrumentsTask extends AsyncTask<Void, Void, Void> {
        
        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
            setListAdapter(null);            
        }

        @Override
        protected Void doInBackground(Void... params) {
            ActiveRecordCloudSync.syncReceiveTables();
            return null;
        }
        
        @Override
        protected void onPostExecute(Void param) {
            if (isAdded()) {
                setListAdapter(new InstrumentAdapter(Instrument.getAll()));
                getActivity().setProgressBarIndeterminateVisibility(false);    
            }
        }        
    }
    
    /*
     * Check that the instrument has been fully loaded from the server before allowing
     * user to begin survey.
     */
    private class LoadInstrumentTask extends AsyncTask<Instrument, Void, Long> {
        ProgressDialog mProgressDialog;
        
        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(
                    getActivity(),
                    getString(R.string.instrument_loading_progress_header),
                    getString(R.string.instrument_loading_progress_message)
            ); 
        }
        
        /*
         * If instrument is loaded, return the instrument id.
         * If not, return -1.
         */
        @Override
        protected Long doInBackground(Instrument... params) {
            Instrument instrument = params[0];
            if (instrument.loaded()) {
                return instrument.getRemoteId();
            } else {
                return Long.valueOf(-1);
            }
        }
        
        @Override
        protected void onPostExecute(Long instrumentId) {
            mProgressDialog.dismiss();
            if (instrumentId == Long.valueOf(-1)) {
                Toast.makeText(getActivity(), R.string.instrument_not_loaded, Toast.LENGTH_LONG).show();
            } else {
                Intent i = new Intent(getActivity(), SurveyActivity.class);
                i.putExtra(SurveyFragment.EXTRA_INSTRUMENT_ID, instrumentId);
                startActivity(i);
            }
        }
    }
}
