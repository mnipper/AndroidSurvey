package org.adaptlab.chpir.android.survey;

import java.text.SimpleDateFormat;
import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.NetworkNotificationUtils;
import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Survey;
import org.adaptlab.chpir.android.survey.Rules.InstrumentLaunchRule;
import org.adaptlab.chpir.android.survey.Rules.RuleBuilder;
import org.adaptlab.chpir.android.survey.Rules.RuleCallback;
import org.adaptlab.chpir.android.survey.Tasks.DownloadImagesTask;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class InstrumentFragment extends ListFragment {
    public final static String TAG = "InstrumentFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setListAdapter(new InstrumentAdapter(Instrument.getAll()));       
        AppUtil.appInit(getActivity());
    }
    
    private void downloadInstrumentImages() {
    	new DownloadImagesTask(getActivity()).execute();
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_instrument, menu);
    }
    
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);
    	if (getResources().getBoolean(R.bool.default_admin_settings)) {
    		menu.findItem(R.id.menu_item_admin).setEnabled(false);
    		menu.findItem(R.id.menu_item_admin).setVisible(false);
    	}
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
        if (getListAdapter() != null) {
            ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
        } else {
            setListAdapter(new InstrumentAdapter(Instrument.getAll())); 
        }
        createTabs();
    }
    
    public void createTabs() {
        if (AppUtil.getAdminSettingsInstance().getShowSurveys()) {
            final ActionBar actionBar = getActivity().getActionBar();     
            ActionBar.TabListener tabListener = new ActionBar.TabListener() {    
                @Override
                public void onTabSelected(Tab tab,
                        android.app.FragmentTransaction ft) {
                    if (tab.getText().equals(getActivity().getResources().getString(R.string.surveys))) {
                        if (Survey.getAll().isEmpty())
                            setListAdapter(null);
                        else
                            setListAdapter(new SurveyAdapter(Survey.getAll()));
                    } else {
                        setListAdapter(new InstrumentAdapter(Instrument.getAllProjectInstruments(Long.parseLong(AppUtil.getAdminSettingsInstance().getProjectId()))));
                    }
                }
    
                // Required by interface
                public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) { }
                public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) { }
            };
            
            actionBar.removeAllTabs();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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
            titleTextView.setTextColor(Color.BLACK);
            
            new SetInstrumentLabelTask().execute(new InstrumentListLabel(instrument, titleTextView));

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
                        R.layout.list_item_survey, null);
            }

            Survey survey = getItem(position);

            TextView titleTextView = (TextView) convertView
                    .findViewById(R.id.survey_list_item_titleTextView);
            titleTextView.setText(survey.identifier(getActivity()));
            titleTextView.setTypeface(survey.getInstrument().getTypeFace(getActivity().getApplicationContext()));

            TextView progressTextView = (TextView) convertView.findViewById(R.id.survey_list_item_progressTextView);            
            progressTextView.setText(survey.responses().size() + " " + getString(R.string.of) + " " + survey.getInstrument().questions().size());

            TextView instrumentTitleTextView = (TextView) convertView.findViewById(R.id.survey_list_item_instrumentTextView);
            instrumentTitleTextView.setText(survey.getInstrument().getTitle());
            
            TextView lastUpdatedTextView = (TextView) convertView.findViewById(R.id.survey_list_item_lastUpdatedTextView);
            SimpleDateFormat df = new SimpleDateFormat("HH:mm yyyy-MM-dd");
            lastUpdatedTextView.setText(df.format(survey.getLastUpdated()));
            
            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (l.getAdapter() instanceof InstrumentAdapter) {
            Instrument instrument = ((InstrumentAdapter) getListAdapter()).getItem(position);
            if (instrument == null) return;            
            new LoadInstrumentTask().execute(instrument);
        } else if (l.getAdapter() instanceof SurveyAdapter) {
            Survey survey = ((SurveyAdapter) getListAdapter()).getItem(position);
            if (survey == null) return;
            new LoadSurveyTask().execute(survey);            
        }
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
            if (isAdded() && NetworkNotificationUtils.checkForNetworkErrors(getActivity()))
                ActiveRecordCloudSync.syncReceiveTables(getActivity());
            
            return null;
        }
        
        @Override
        protected void onPostExecute(Void param) {
        	if (isAdded()) {
            	downloadInstrumentImages();
            	if (AppUtil.getAdminSettingsInstance().getProjectId() != null) {
            		setListAdapter(new InstrumentAdapter(Instrument.getAllProjectInstruments(Long.parseLong(AppUtil.getAdminSettingsInstance().getProjectId()))));
            	}
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
        protected void onPostExecute(final Long instrumentId) {
            try {
                mProgressDialog.dismiss();  
            } catch (IllegalArgumentException iae) { 
                Log.e(TAG, "Tried to close progress dialog that does not exist.");
            }
            if (isAdded()){
                if (instrumentId == Long.valueOf(-1)) {
                    Toast.makeText(getActivity(), R.string.instrument_not_loaded, Toast.LENGTH_LONG).show();
                } else {
                    new RuleBuilder(getActivity())
                    .addRule(new InstrumentLaunchRule(Instrument.findByRemoteId(instrumentId),
                            getActivity().getString(R.string.rule_failure_instrument_launch)))
                    .showToastOnFailure(true)
                    .setCallbacks(new RuleCallback() {
                        public void onRulesPass() {
                            Intent i = new Intent(getActivity(), SurveyActivity.class);
                            i.putExtra(SurveyFragment.EXTRA_INSTRUMENT_ID, instrumentId);
                            startActivity(i);
                        }

                        public void onRulesFail() { }                        
                    })
                    .checkRules();
                }
            }
        }
    }
    
    private class LoadSurveyTask extends AsyncTask<Survey, Void, Survey> {
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
         * If instrument is loaded, return the survey.
         * If not, return null.
         */
        @Override
        protected Survey doInBackground(Survey... params) {
            Survey survey = params[0];
            Instrument instrument = survey.getInstrument();
            if (instrument.loaded()) {
                return survey;
            } else {
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(Survey survey) {
            try {
                mProgressDialog.dismiss();  
            } catch (IllegalArgumentException iae) { 
                Log.e(TAG, "Tried to close progress dialog that does not exist.");
            }
            
            if (isAdded()) {
                if (survey == null) {
                    Toast.makeText(getActivity(), R.string.instrument_not_loaded, Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(getActivity(), SurveyActivity.class);
                    i.putExtra(SurveyFragment.EXTRA_INSTRUMENT_ID, survey.getInstrument().getRemoteId());
                    i.putExtra(SurveyFragment.EXTRA_SURVEY_ID, survey.getId());
                    i.putExtra(SurveyFragment.EXTRA_QUESTION_ID, survey.getLastQuestion().getId());
                    startActivity(i);
                }
            }
        }
    }
       
    /*
     * Check that the instrument has been fully loaded from the server and sets
     * the color of instrument label red if it has not.
     * 
     */
    private class SetInstrumentLabelTask extends AsyncTask<InstrumentListLabel, Void, InstrumentListLabel> {
        
        @Override
        protected InstrumentListLabel doInBackground(InstrumentListLabel... params) {
            InstrumentListLabel instrumentListLabel = params[0];
            Instrument instrument = instrumentListLabel.getInstrument();
            instrumentListLabel.setLoaded(instrument.loaded());
            return instrumentListLabel;
        }

        @Override
        protected void onPostExecute(InstrumentListLabel instrumentListLabel) {
            if (isAdded()){
                if (instrumentListLabel.isLoaded()) {
                    instrumentListLabel.getTextView().setTextColor(Color.BLACK);
                } else {
                    instrumentListLabel.getTextView().setTextColor(Color.RED);                    
                }
            }
        }
    }
    
    private static class InstrumentListLabel {
        private Instrument mInstrument;
        private TextView mTextView;
        private Boolean mLoaded;
        
        public InstrumentListLabel(Instrument instrument, TextView textView) {
            this.mInstrument = instrument;
            this.mTextView = textView;
        }
        
        public Instrument getInstrument() {
            return mInstrument;
        }
        
        public TextView getTextView() {
            return mTextView;
        }
        
        public void setLoaded(boolean loaded) {
            mLoaded = loaded;
        }
        
        public Boolean isLoaded() {
            return mLoaded;
        }
    }
}
