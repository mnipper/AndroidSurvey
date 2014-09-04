package org.adaptlab.chpir.android.survey.Rules;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Rule;
import org.json.JSONException;

import android.util.Log;

public class InstrumentSurveyLimitRule implements PassableRule {
    private static final String TAG = "InstrumetSurveyLimitRule";
    
    private Instrument mInstrument;

    public InstrumentSurveyLimitRule(Instrument instrument) {
        mInstrument = instrument;
    }

    @Override
    public boolean passesRule() {
        if (getInstrumentRule() == null) return true;
        
        try {
            int maxSurveys = getInstrumentRule().getParamJSON().getInt(Rule.MAX_SURVEYS_KEY);
            return (mInstrument.surveys().size() <= maxSurveys);
        } catch (JSONException je) {
            Log.i(TAG, "JSON Exception when parsing JSON for rule: " + je);
            return false;
        }
    }
    
    @Override
    public String getFailureMessage() {
        return "You have failed the instrument survey limit rule!";
    }

    private Rule getInstrumentRule() {
        return Rule.findByRuleTypeAndInstrument(Rule.RuleType.INSTRUMENT_SURVEY_LIMIT_RULE, mInstrument);
    }
}
