package org.adaptlab.chpir.android.survey.Rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Rule;
import org.json.JSONException;

import android.util.Log;

/*
 * Limit the number of surveys that can be created for a given
 * instrument for a given time period.  This keeps track of the timestamps
 * of each created survey and stores them in the stored params for the
 * rule.
 * 
 */
public class InstrumentSurveyLimitPerMinuteRule extends PassableRule {
    private static final String TAG = "InstrumentSurveyLimitPerMinuteRule";
    private static final Rule.RuleType RULE_TYPE = Rule.RuleType.INSTRUMENT_SURVEY_LIMIT_PER_MINUTE_RULE;
    private static final int MILLISECONDS_IN_MINUTE = 60000;
    private SimpleDateFormat mDateFormatter;

    private Instrument mInstrument;
    private String mFailureMessage;

    public InstrumentSurveyLimitPerMinuteRule(Instrument instrument,
            String failureMessage) {
        mInstrument = instrument;
        mFailureMessage = failureMessage;
        mDateFormatter = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy"); // The format to use to store timestamps
    }

    @Override
    public boolean passesRule() {
        if (getInstrumentRule(mInstrument, RULE_TYPE) == null)
            return true;

        try {
            Rule instrumentRule = getInstrumentRule(mInstrument, RULE_TYPE);

            int numberOfSurveys = instrumentRule.getParamJSON().getInt(Rule.NUM_SURVEYS_KEY);
            int minuteInterval = instrumentRule.getParamJSON().getInt(Rule.MINUTE_INTERVAL_KEY);

            List<Date> surveyTimestamps = parseTimestamps(instrumentRule.<String> getStoredValue(Rule.SURVEY_TIMESTAMPS_KEY));

            Date now = new Date();

            if (surveyTimestamps.isEmpty()) {
                // No surveys yet, add current timestamp and save.
                surveyTimestamps = new ArrayList<Date>();
                addTimeToTimestamps(surveyTimestamps, instrumentRule, now);
                return true;
            } else {
                // Survey timestamps exist
                surveyTimestamps = removeTimestampsOutsideInterval(surveyTimestamps, minuteInterval, now);

                if (surveyTimestamps.size() < numberOfSurveys) {
                    // The current number of timestamps is less than the indicated max.
                    addTimeToTimestamps(surveyTimestamps, instrumentRule, now);
                    return true;
                } else {
                    return false;
                }
            }

        } catch (JSONException je) {
            Log.e(TAG, "JSON Exception when parsing JSON for rule: " + je);
            return false;
        } catch (ParseException pe) {
            Log.e(TAG, "Parse Exception when parsing JSON for rule stored values: " + pe);
            return false;
        }
    }

    @Override
    public String getFailureMessage() {
        return mFailureMessage;
    }

    /*
     * Convert a serialized list of timestamps to a list of timestamps.
     */
    private List<Date> parseTimestamps(String timestamps) throws ParseException {
        List<Date> timestampArray = new ArrayList<Date>();
        if (timestamps != null) {
            for (String dateString : Arrays.asList(timestamps.substring(1,
                    timestamps.length() - 1).split(", "))) {
                timestampArray.add(mDateFormatter.parse(dateString));
            }
        }
        return timestampArray;
    }

    private void addTimeToTimestamps(List<Date> surveyTimestamps, Rule instrumentRule, Date date) throws ParseException {
        surveyTimestamps.add(mDateFormatter.parse(date.toString()));
        instrumentRule.setStoredValue(Rule.SURVEY_TIMESTAMPS_KEY, surveyTimestamps);
        instrumentRule.save();
    }

    /*
     * Remove all timestamps outside of a given minute interval for a list of dates.
     * 
     * This will remove all timestamps outside of the range referenceTime - minuteInterval.
     * 
     * E.G. if the referenceTime is the current time, and the minute interval is 5, then it will remove
     * all timestamps from the list that are older than 5 minutes.
     */
    private List<Date> removeTimestampsOutsideInterval(List<Date> surveyTimestamps, int minuteInterval, Date referenceTime) {
        List<Date> cleanedTimestamps = new ArrayList<Date>();
        Date cutoffTime = new Date(referenceTime.getTime() - (minuteInterval * MILLISECONDS_IN_MINUTE));

        for (Date d : surveyTimestamps) {
            if (d.after(cutoffTime)) {
                cleanedTimestamps.add(d);
            }
        }

        return cleanedTimestamps;
    }
}