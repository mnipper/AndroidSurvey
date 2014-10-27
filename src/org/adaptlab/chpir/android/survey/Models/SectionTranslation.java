package org.adaptlab.chpir.android.survey.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "SectionTranslations")
public class SectionTranslation extends Model {
	@Column(name = "Section")
    private Section mSection;
    @Column(name = "Language")
    private String mLanguage;
    @Column(name = "Text")
    private String mText;
    
    public SectionTranslation() {
        super();
    }
    
    public static SectionTranslation findByLanguage(String language) {
        return new Select().from(SectionTranslation.class).where("Language = ?", language).executeSingle();
    }
    
    public Section getSection() {
        return mSection;
    }
    
    public void setSection(Section section) {
        mSection = section;
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
