package org.adaptlab.chpir.android.survey.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "QuestionTranslations")
public class QuestionTranslation extends Model {
    @Column(name = "Question")
    private Question mQuestion;
    @Column(name = "Language")
    private String mLanguage;
    @Column(name = "Text")
    private String mText;
    @Column(name = "RegExValidationMessage")
    private String mRegExValidationMessage;
    
    /*
     * Finders
     */   
    public static QuestionTranslation findByLanguage(String language) {
        return new Select().from(QuestionTranslation.class).where("Language = ?", language).executeSingle();
    }
    
    /*
     * Getters/Setters
     */
    public QuestionTranslation() {
        super();
    }
    
    public Question getQuestion() {
        return mQuestion;
    }
    
    public void setQuestion(Question question) {
        mQuestion = question;
    }
    
    public String getLanguage() {
        return mLanguage;
    }
    
    public void setLanguage(String language) {
        mLanguage = language;
    }
    
    public String getText() {
        return mText;
    }
    
    public void setText(String text) {
        mText = text;
    }
    
    public String getRegExValidationMessage() {
        return mRegExValidationMessage;
    }
    
    public void setRegExValidationMessage(String message) {
        if (message.equals("null") || message.equals(""))
            mRegExValidationMessage = null;
         else
             mRegExValidationMessage = message;
    }
}
