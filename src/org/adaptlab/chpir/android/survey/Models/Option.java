package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.cloudtable.CloudTable;
import org.adaptlab.chpir.android.cloudtable.ReceiveTable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Options")
public class Option extends Model implements ReceiveTable {

    @Column(name = "Question")
    private Question mQuestion;
    @Column(name = "Text")
    private String mText;

    public Option() {
        super();
        CloudTable.addReceiveTable(Option.class);
    }

    public Question getQuestion() {
        return mQuestion;
    }

    public void setQuestion(Question question) {
        mQuestion = question;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
    
    public static List<Option> getAll() {
        return new Select().from(Option.class).orderBy("Id ASC").execute();
    }
    
    @Override
    public Long lastId() {
        return getAll().get(getAll().size() - 1).getId();
    }
}
