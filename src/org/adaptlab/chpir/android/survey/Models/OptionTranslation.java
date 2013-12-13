package org.adaptlab.chpir.android.survey.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "OptionTranslations")
public class OptionTranslation extends Model {
    @Column(name = "Option")
    private Option mOption;
    @Column(name = "Language")
    private String mLanguage;
    @Column(name = "Text")
    private String mText;
    
    public OptionTranslation() {
        super();
    }
    
    /*
     * Finders
     */    
    public static OptionTranslation findByLanguage(String language) {
        return new Select().from(OptionTranslation.class).where("Language = ?", language).executeSingle();
    }
    
    /*
     * Getters/Setters
     */
    public Option getOption() {
        return mOption;
    }
    public void setOption(Option option) {
        mOption = option;
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
}
