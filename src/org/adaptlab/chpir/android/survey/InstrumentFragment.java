package org.adaptlab.chpir.android.survey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.PollService;
import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;
import org.adaptlab.chpir.android.survey.Models.Response;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.InputType;
import android.util.Log;
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
    private final static String ADMIN_PASSWORD_HASH =
            "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"; // SHA-256 of admin password
    
    private List<Instrument> mInstrumentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mInstrumentList = Instrument.getAll();
        setListAdapter(new InstrumentAdapter(mInstrumentList));
        appInit();
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
        case R.id.menu_item_refresh:
            mInstrumentList = Instrument.getAll();
            setListAdapter(new InstrumentAdapter(mInstrumentList));
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

    private final void appInit() {
        if (REQUIRE_SECURITY_CHECKS) {
            if (!runDeviceSecurityChecks()) {
                // Device has failed security checks
                
                return;
            }
        }
        
        Log.i(TAG, "Initializing application...");
        
        //Crashlytics.start(getActivity());
        
        DatabaseSeed.seed(getActivity());

        if (AdminSettings.getInstance().getDeviceIdentifier() == null) {
            AdminSettings.getInstance().setDeviceIdentifier(UUID.randomUUID().toString());
        }

        ActiveRecordCloudSync.setEndPoint(AdminSettings.getInstance().getApiUrl());
        ActiveRecordCloudSync.addReceiveTable("instruments", Instrument.class);
        ActiveRecordCloudSync.addReceiveTable("questions", Question.class);
        ActiveRecordCloudSync.addReceiveTable("options", Option.class);
        ActiveRecordCloudSync.addSendTable("surveys", Survey.class);
        ActiveRecordCloudSync.addSendTable("responses", Response.class);

        PollService.setServiceAlarm(getActivity().getApplicationContext(), true);
    }

    /*
     * Security checks that must pass for the application to start.
     * 
     * If the application fails any security checks, display
     * AlertDialog indicating why and immediately stop execution
     * of the application.
     * 
     * Current security checks: require encryption
     */
    private final boolean runDeviceSecurityChecks() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getActivity()
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (devicePolicyManager.getStorageEncryptionStatus() != DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE) {
            new AlertDialog.Builder(getActivity())
            .setTitle(R.string.encryption_required_title)
            .setMessage(R.string.encryption_required_text)
            .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                    getActivity().finish();
                }
             })
             .show();
            return false;
        }
        return true;
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
                    if (checkAdminPassword(input.getText().toString())) {
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
     * Hash the entered password and compare it with admin password hash
     */
    private boolean checkAdminPassword(String password) {
        String hash = new String(Hex.encodeHex(DigestUtils.sha256(password)));
        return hash.equals(ADMIN_PASSWORD_HASH);
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
