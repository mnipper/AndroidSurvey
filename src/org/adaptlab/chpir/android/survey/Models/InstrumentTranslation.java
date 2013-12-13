package org.adaptlab.chpir.android.survey.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


@Table(name = "InstrumentTranslations")
public class InstrumentTranslation extends Model {

    @Column(name = "Title")
    private String mTitle;
    @Column(name = "Language")
    private String mLanguage;
    @Column(name = "Alignment")
    private String mAlignment;
    @Column(name = "Instrument")
    private Instrument mInstrument;
    
    public InstrumentTranslation() {
        super();
    }
    
    /*
     * Finders
     */   
    public static InstrumentTranslation findByLanguage(String language) {
        return new Select().from(InstrumentTranslation.class).where("Language = ?", language).executeSingle();
    }
    
    /*
     * Getters/Setters
     */
    public String getTitle() {
        return mTitle;
    }
    
    public void setTitle(String title) {
        mTitle = title;
    }
    
    public String getLanguage() {
        return mLanguage;
    }
    
    public void setLanguage(String language) {
        mLanguage = language;
    }
    
    public String getAlignment() {
        return mAlignment;
    }
    
    public void setAlignment(String alignment) {
        mAlignment = alignment;
    }
    
    public Instrument getInstrument() {
        return mInstrument;
    }
    
    public void setInstrument(Instrument instrument) {
        mInstrument = instrument;
    }
}
