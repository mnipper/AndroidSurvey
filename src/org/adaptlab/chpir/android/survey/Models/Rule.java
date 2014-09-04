package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ReceiveModel;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Rules")
public class Rule extends ReceiveModel {
    private static final String TAG = "RuleModel";
    
    @Column(name = "RuleType")
    private RuleType mRuleType;
    @Column(name = "Instrument")
    private Instrument mInstrument;
    @Column(name = "Params")
    private String mParams;
    @Column(name = "RemoteId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long mRemoteId;
    
    public static enum RuleType {
      INSTRUMENT_SURVEY_LIMIT_RULE, INSTRUMENT_TIMING_RULE
    };
    
    public static final String MAX_SURVEYS_KEY = "max_surveys";
    
    public Rule() {
        super();
    }
    
    public static Rule findByRuleTypeAndInstrument(RuleType ruleType, Instrument instrument) {
        Log.i(TAG, "RULES: " + getAll().get(0).getParamJSON());
        for (Rule rule : getAll()) {
            if (rule.getRuleType().equals(ruleType) && rule.getInstrument().equals(instrument)) {
                return rule;
            }
        }
        return null;
    }
    
    public static List<Rule> getAll() {
        return new Select().from(Rule.class).orderBy("Id ASC").execute();
    }
    
    public static Rule findByRemoteId(Long remoteId) {
        return new Select().from(Rule.class).where("RemoteId = ?", remoteId).executeSingle();
    }

    @Override
    public void createObjectFromJSON(JSONObject jsonObject) {
        try {
            Long remoteId = jsonObject.getLong("id");
            
            // If a rule already exists, update it from the remote
            Rule rule = Rule.findByRemoteId(remoteId);
            if (rule == null) {
                rule = this;
            }
            
            if (AppUtil.DEBUG) Log.i(TAG, "Creating object from JSON Object: " + jsonObject);
            rule.setRuleType(jsonObject.getString("rule_type"));
            rule.setInstrument(Instrument.findByRemoteId(jsonObject.getLong("instrument_id")));
            rule.setParams(jsonObject.getString("rule_params"));
            rule.setRemoteId(remoteId);
            rule.save();
            
        } catch (JSONException je) {
            Log.e(TAG, "Error parsing object json", je);
        }
    }
    
    public Long getRemoteId() {
        return mRemoteId;
    }
       
    public JSONObject getParamJSON() {
        try {
            return new JSONObject(mParams);
        } catch (JSONException je) {
            Log.e(TAG, "Could not parse rule params: " + je);
            return null;
        }
    }
    
    public RuleType getRuleType() {
        return mRuleType;
    }
    
    public Instrument getInstrument() {
        return mInstrument;
    }
    
    /*
     * Private
     */
    
    private void setParams(String params) {
        mParams = params;
    }
    
    private void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }
    
    private void setRemoteId(Long id) {
        mRemoteId = id;
    }
    
    private void setRuleType(String ruleType) {
        if (validRuleType(ruleType)) {
            mRuleType = RuleType.valueOf(ruleType);
        } else {
            // This should never happen
            // We should prevent syncing data unless app is up to date
            Log.wtf(TAG, "Received invalid rule type: " + ruleType);
        }
    }
    
    private static boolean validRuleType(String ruleType) {
        for (RuleType type : RuleType.values()) {
            if (type.name().equals(ruleType)) {
                return true;
            }
        }
        return false;
    }
}
