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
    
    public RuleBuilder addRule(PassableRule rule) {
        this.mRules.add(rule);
        return this;
    }

    public RuleBuilder setCallbacks(RuleCallback ruleCallback) {
        this.mRuleCallback = ruleCallback;
        return this;
    }
    
    public RuleBuilder showToastOnFailure(boolean showToast) {
        this.mShowToastOnFailure = showToast;
        return this;
    }
    
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
