package org.adaptlab.chpir.android.survey;

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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

public class InstrumentFragment extends ListFragment {
    private final static String TAG = "InstrumentFragment";
    private final static boolean REQUIRE_SECURITY_CHECKS = false;
    private String ADMIN_PASSWORD_HASH;
    private String ACCESS_TOKEN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setListAdapter(new InstrumentAdapter(Instrument.getAll()));
        ADMIN_PASSWORD_HASH = getActivity().getResources().getString(R.string.admin_password_hash);
        ACCESS_TOKEN = getActivity().getResources().getString(R.string.backend_api_key);  
        createTabs();
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
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
        createTabs();
    }
    
    public void createTabs() {
        if (AdminSettings.getInstance().getShowSurveys()) {
            final ActionBar actionBar = getActivity().getActionBar();     
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {    
                @Override
                public void onTabSelected(Tab tab,
                        android.app.FragmentTransaction ft) {
                    if (tab.getText().equals(getActivity().getResources().getString(R.string.surveys))) {
                        if (!Survey.getAll().isEmpty())
                            setListAdapter(new SurveyAdapter(Survey.getAll()));
                    } else {
                        setListAdapter(new InstrumentAdapter(Instrument.getAll()));
                    }
                }
    
                // Required by interface
                public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) { }
                public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) { }
            };
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.removeAllTabs();
            actionBar.addTab(actionBar.newTab().setText(getActivity().getResources().getString(R.string.instruments)).setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText(getActivity().getResources().getString(R.string.surveys)).setTabListener(tabListener));
        }
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
    
    private class SurveyAdapter extends ArrayAdapter<Survey> {
        public SurveyAdapter(List<Survey> surveys) {
            super(getActivity(), 0, surveys);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.list_item_instrument, null);
            }

            Survey survey = getItem(position);

            TextView titleTextView = (TextView) convertView
                    .findViewById(R.id.instrument_list_item_titleTextView);
            titleTextView.setText(survey.identifier());
            titleTextView.setTypeface(survey.getInstrument().getTypeFace(getActivity().getApplicationContext()));

            TextView questionCountTextView = (TextView) convertView
                    .findViewById(R.id.instrument_list_item_questionCountTextView);
            
            questionCountTextView.setText(survey.responses().size() + " " + getString(R.string.of) + " " + survey.getInstrument().questions().size());

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (l.getAdapter() instanceof InstrumentAdapter) {
            Instrument instrument = ((InstrumentAdapter) getListAdapter()).getItem(position);
            if (instrument == null) {
                return;
            }
            
            new LoadInstrumentTask().execute(instrument);
        } else if (l.getAdapter() instanceof SurveyAdapter) {
            
        }
    }

    private final void appInit() {
        if (REQUIRE_SECURITY_CHECKS) {
            if (!runDeviceSecurityChecks()) {
                // Device has failed security checks
                
                return;
            }
        }
        
        Log.i(TAG, "Initializing application...");
        
        Crashlytics.start(getActivity());
        
        DatabaseSeed.seed(getActivity());

        if (AdminSettings.getInstance().getDeviceIdentifier() == null) {
            AdminSettings.getInstance().setDeviceIdentifier(UUID.randomUUID().toString());
        }

        ActiveRecordCloudSync.setAccessToken(ACCESS_TOKEN);
        ActiveRecordCloudSync.setVersionCode(getVersionCode());
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
     * Get the version code from the AndroidManifest
     */
    private int getVersionCode() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            return pInfo.versionCode;
        } catch (NameNotFoundException nnfe) {
            Log.e(TAG, "Error finding version code: " + nnfe);
        }
        return -1;
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
