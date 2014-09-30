package org.adaptlab.chpir.android.survey.Rules;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Rule;

/*
 * A rule that is passable.  This means that it returns true if the rule as defined
 * is passed, and false if it is not.
 */
public abstract class PassableRule {
    private Rule mInstrumentRule;
    
    // Return true if passes defined rule, false otherwise.
    public abstract boolean passesRule();
    
    // The failure message to return if the rule does not pass.
    public abstract String getFailureMessage();
        
    /*
     * Cache the mInstrumentRule if it is found.
     */
    protected Rule getInstrumentRule(Instrument instrument, Rule.RuleType ruleType) {
        if (mInstrumentRule == null) {
            mInstrumentRule = Rule.findByRuleTypeAndInstrument(ruleType, instrument);
        }
        return mInstrumentRule;
    }
}
