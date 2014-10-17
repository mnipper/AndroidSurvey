package org.adaptlab.chpir.android.survey.Rules;

import java.util.ArrayList;

import org.adaptlab.chpir.android.survey.Rules.RuleCallback;

import android.content.Context;
import android.widget.Toast;

public class RuleBuilder {
    private Context mContext;
    private ArrayList<PassableRule> mRules;
    private RuleCallback mRuleCallback;
    private boolean mShowToastOnFailure;
    private boolean mPassesRules;
    
    public RuleBuilder(Context context) {
        mContext = context;
        mRules = new ArrayList<PassableRule>();
        mShowToastOnFailure = false;
        mPassesRules = false;
    }
    
    /*
     * Add an instance of a rule to this builder.  Its rulePasses()
     * method will be evoked when checkRules() is called.  If any rule fails,
     * then this RuleBuilder will fail; causing mPassesRule to be false and
     * the onRulesFail callback to be called (if callbacks have been set).
     */
    public RuleBuilder addRule(PassableRule rule) {
        this.mRules.add(rule);
        return this;
    }

    /*
     * Failure callback is called immediately if a rule fails.
     * Success callback is called only after all rules pass.
     * 
     * Callbacks are not required.
     */
    public RuleBuilder setCallbacks(RuleCallback ruleCallback) {
        this.mRuleCallback = ruleCallback;
        return this;
    }
    
    /*
     * Show a long toast with the message set to the failure message
     * of the rule on failure.
     */
    public RuleBuilder showToastOnFailure(boolean showToast) {
        this.mShowToastOnFailure = showToast;
        return this;
    }
    
    /*
     * Iterate all passable rules and ensure that they pass their rule.
     * 
     * Return immediately if not, showing a toast with the failure message of the rule and calling
     * the failure callback if set.
     */
    public RuleBuilder checkRules() {
        for (PassableRule rule : mRules) {
            if (!rule.passesRule()) {
                if (mShowToastOnFailure) Toast.makeText(mContext, rule.getFailureMessage(), Toast.LENGTH_LONG).show();
                if (mRuleCallback != null) mRuleCallback.onRulesFail();
                mPassesRules = false;
                return this;
            }
        }
        
        if (mRuleCallback != null) mRuleCallback.onRulesPass(); 
        mPassesRules = true;
        return this;
    }
    
    public boolean getResult() {
        return mPassesRules;
    }
}
