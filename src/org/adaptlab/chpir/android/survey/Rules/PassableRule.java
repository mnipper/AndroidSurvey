package org.adaptlab.chpir.android.survey.Rules;

public interface PassableRule {
    public boolean passesRule();
    public String getFailureMessage();
}
