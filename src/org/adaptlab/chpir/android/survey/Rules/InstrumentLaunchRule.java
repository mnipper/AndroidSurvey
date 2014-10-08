package org.adaptlab.chpir.android.survey.Rules;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Rule;

public class InstrumentLaunchRule extends PassableRule {
    private static final String TAG = "InstrumetLaunchRule";
    private static final Rule.RuleType RULE_TYPE = Rule.RuleType.INSTRUMENT_LAUNCH_RULE;
    
    private Instrument mInstrument;
    private String mFailureMessage;
    
    public InstrumentLaunchRule(Instrument instrument, String failureMessage) {
        mInstrument = instrument;
        mFailureMessage = failureMessage;
    }
    
    @Override
    public boolean passesRule() {
        return super.getInstrumentRule(mInstrument, RULE_TYPE) == null;
    }

    @Override
    public String getFailureMessage() {
        return mFailureMessage;
    }
}
