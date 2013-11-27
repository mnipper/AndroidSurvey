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
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
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

@SuppressLint("NewApi")
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

        appInit();
        mInstrumentList = Instrument.getAll();

        InstrumentAdapter adapter = new InstrumentAdapter(mInstrumentList);
        setListAdapter(adapter);
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
            displayPasswordPrompt();
        case R.id.menu_item_refresh:
            mInstrumentList = Instrument.getAll();
            InstrumentAdapter adapter = new InstrumentAdapter(mInstrumentList);
            setListAdapter(adapter);
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
            questionCountTextView.setText(instrument.questions().size() + " "
                    + getString(R.string.questions));

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Instrument instrument = ((InstrumentAdapter) getListAdapter())
                .getItem(position);
        if (instrument == null || instrument.questions().size() == 0) {
            return;
        }

        long instrumentId = instrument.getRemoteId();
        Intent i = new Intent(getActivity(), SurveyActivity.class);
        i.putExtra(SurveyFragment.EXTRA_INSTRUMENT_ID, instrumentId);
        startActivity(i);
    }

    private final void appInit() {
        if (REQUIRE_SECURITY_CHECKS) {
            if (!runDeviceSecurityChecks()) {
                return;
            }
        }
        
        Log.i(TAG, "Initializing application...");
        DatabaseSeed.seed(getActivity());

        AdminSettings.getInstance().setDeviceIdentifier("TestDevice1");
        AdminSettings.getInstance().setSyncInterval(2);
        AdminSettings.getInstance().save();

        PollService.setPollInterval(AdminSettings.getInstance()
                .getSyncInterval());

        ActiveRecordCloudSync.setEndPoint(AdminSettings.getInstance()
                .getApiUrl());
        ActiveRecordCloudSync.addReceiveTable("instruments", Instrument.class);
        ActiveRecordCloudSync.addReceiveTable("questions", Question.class);
        ActiveRecordCloudSync.addReceiveTable("options", Option.class);

        ActiveRecordCloudSync.addSendTable("surveys", Survey.class);
        ActiveRecordCloudSync.addSendTable("responses", Response.class);

        PollService.setServiceAlarm(getActivity(), true);
    }

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
    
    private boolean checkAdminPassword(String password) {
        String hash = new String(Hex.encodeHex(DigestUtils.sha256(password)));
        return hash.equals(ADMIN_PASSWORD_HASH);
    }
}
