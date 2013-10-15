package org.adaptlab.chpir.android.survey;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;

public class DatabaseSeed {
    public static boolean doSeeding() {
        return false;
    }
    
    public static void seedInstrument() {
        Instrument ins = new Instrument();
        ins.setTitle("Test Instrument " + Instrument.getAll().size());
        ins.save();
        Question q1 = createQuestion(ins, "q104", "SELECT_ONE",
                "This is an example select one question");
        setOptions(q1, 3);

        Question q2 = createQuestion(ins, "q111", "SELECT_MULTIPLE",
                "This is an example select multiple question");
        setOptions(q2, 5);

        Question q3 = createQuestion(ins, "q115", "SELECT_ONE_WRITE_OTHER",
                "This is an example select one write other question");
        setOptions(q3, 4);

        Question q4 = createQuestion(ins, "q121",
                "SELECT_MULTIPLE_WRITE_OTHER",
                "This is an example select multiple write other question");
        setOptions(q4, 4);

        Question q5 = createQuestion(ins, "q125", "FREE_RESPONSE",
                "This is an example free response question");
        
        Question q6 = createQuestion(ins, "q125", "FRONT_PICTURE",
                "This is an example front picture question");
        
        Question q7 = createQuestion(ins, "q125", "REAR_PICTURE",
                "This is an example rear picture question");
        
        Question q9 = createQuestion(ins, "q125", "SLIDER",
                "This is an example slider question");
    }

    private static Question createQuestion(Instrument i, String qid,
            String qtype, String text) {
        Question q = new Question();
        q.setInstrument(i);
        q.setQuestionID(qid);
        q.setQuestionType(qtype);
        q.setText(text);
        q.save();
        return q;
    }

    private static void setOptions(Question q, int num) {
        for (int i = 0; i < num; i++) {
            Option option = new Option();
            option.setQuestion(q);
            option.setText("This is option " + i);
            option.save();
        }
    }
}
