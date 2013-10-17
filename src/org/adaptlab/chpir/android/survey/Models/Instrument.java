package org.adaptlab.chpir.android.survey.Models;

import java.util.List;

import org.adaptlab.chpir.android.cloudtable.CloudTable;
import org.adaptlab.chpir.android.cloudtable.ReceiveTable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Instruments")
public class Instrument extends Model implements ReceiveTable {

    @Column(name = "Title")
    private String mTitle;

    public Instrument() {
        super();
        CloudTable.addReceiveTable(Instrument.class);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Question> questions() {
        return getMany(Question.class, "Instrument");
    }

    public static List<Instrument> getAll() {
        return new Select().from(Instrument.class).orderBy("Id ASC").execute();
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public Long lastId() {
        return getAll().get(getAll().size() - 1).getId();
    }
}
