package org.adaptlab.chpir.android.survey.Rules;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Rule;
import org.json.JSONException;

import android.util.Log;

public class InstrumentSurveyLimitRule implements PassableRule {
    private static final String TAG = "InstrumetSurveyLimitRule";
    
    private Instrument mInstrument;
    private Rule mInstrumentRule;

    public InstrumentSurveyLimitRule(Instrument instrument) {
        mInstrument = instrument;
    }

    @Override
    public boolean passesRule() {
        if (getInstrumentRule() == null) return true;
        
        try {
            int maxSurveys = getInstrumentRule().getParamJSON().getInt(Rule.MAX_SURVEYS_KEY);
            int instrumentSurveyCount = getInstrumentSurveyCount();
            if (++instrumentSurveyCount <= maxSurveys) {
                mInstrumentRule.setStoredValue(Rule.INSTRUMENT_SURVEY_COUNT_KEY, instrumentSurveyCount);
                mInstrumentRule.save();
                return true;
            } else {
                return false;
            }
        } catch (JSONException je) {
            Log.i(TAG, "JSON Exception when parsing JSON for rule: " + je);
            return false;
        }
    }
    
    @Override
    public String getFailureMessage() {
        return "You have failed the instrument survey limit rule!";
    }
    
    private int getInstrumentSurveyCount() {
        Integer instrumentSurveyCount = getInstrumentRule().<Integer>getStoredValue(Rule.INSTRUMENT_SURVEY_COUNT_KEY);
        
        if (instrumentSurveyCount == null) {
            instrumentSurveyCount = 0;
        }
        
        return instrumentSurveyCount;
    }
    
    private Rule getInstrumentRule() {
        if (mInstrumentRule == null) {
            mInstrumentRule = Rule.findByRuleTypeAndInstrument(Rule.RuleType.INSTRUMENT_SURVEY_LIMIT_RULE, mInstrument);
        }
        return mInstrumentRule;
    }
}
