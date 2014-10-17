package org.adaptlab.chpir.android.survey.Rules;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Rule;
import org.json.JSONException;

import android.util.Log;

/*
 * Limit the number of surveys that can be created for a given
 * instrument on a single device.  This actually keeps track of the
 * number of surveys that have been created by storing the number of
 * surveys created for each instrument in the stored values of the rule
 * model.  It does not rely on the number of surveys currently on the
 * device, since that may not be an indication of how many surveys have
 * actually been created.
 * 
 * This will automatically update the number of surveys created for a given
 * instrument/rule.
 */
public class InstrumentSurveyLimitRule extends PassableRule {
    private static final String TAG = "InstrumetSurveyLimitRule";
    private static final Rule.RuleType RULE_TYPE = Rule.RuleType.INSTRUMENT_SURVEY_LIMIT_RULE;
    
    private Instrument mInstrument;
    private String mFailureMessage;

    public InstrumentSurveyLimitRule(Instrument instrument, String failureMessage) {
        mInstrument = instrument;
        mFailureMessage = failureMessage;
    }

   /*
    * This will pass the rule if no rule exists for a given instrument (since
    * no restriction has been placed on this instrument, it should definitely pass).
    * 
    * This rule will fail if there is a JSONException as a fail-safe.
    */
    @Override
    public boolean passesRule() {
        if (getInstrumentRule(mInstrument, RULE_TYPE) == null) return true;
        
        try {
            Rule instrumentRule = getInstrumentRule(mInstrument, RULE_TYPE);
            int maxSurveys = instrumentRule.getParamJSON().getInt(Rule.MAX_SURVEYS_KEY);
            int instrumentSurveyCount = getInstrumentSurveyCount(instrumentRule);
            if (++instrumentSurveyCount <= maxSurveys) {
                instrumentRule.setStoredValue(Rule.INSTRUMENT_SURVEY_COUNT_KEY, instrumentSurveyCount);
                instrumentRule.save();
                return true;
            } else {
                return false;
            }
        } catch (JSONException je) {
            Log.e(TAG, "JSON Exception when parsing JSON for rule: " + je);
            return false;
        }
    }
    
    @Override
    public String getFailureMessage() {
        return mFailureMessage;
    }
    
    /*
     * Get the stored value for this given instrument rule for the number of surveys.
     * If no value exists, initialize to 0 and return.
     */
    private int getInstrumentSurveyCount(Rule instrumentRule) {
        Integer instrumentSurveyCount = instrumentRule.<Integer>getStoredValue(Rule.INSTRUMENT_SURVEY_COUNT_KEY);
        
        if (instrumentSurveyCount == null) {
            instrumentSurveyCount = 0;
        }
        
        return instrumentSurveyCount;
    }
}
