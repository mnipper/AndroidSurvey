package org.adaptlab.chpir.android.survey.Rules;

/*
 * A rule that is passable.  This means that it returns true if the rule as defined
 * is passed, and false if it is not.
 */
public interface PassableRule {
    // Return true if passes defined rule, false otherwise.
    public boolean passesRule();
    
    // The failure message to return if the rule does not pass.
    public String getFailureMessage();
}
