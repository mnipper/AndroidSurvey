package org.adaptlab.chpir.android.survey;

import java.util.Random;

import org.adaptlab.chpir.android.survey.Models.Instrument;
import org.adaptlab.chpir.android.survey.Models.Option;
import org.adaptlab.chpir.android.survey.Models.Question;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class DatabaseSeed {
    private final static String TAG = "DatabaseSeed";
    
    // Do seeding if running in debug mode and if enabled in AndroidManifest
    public static void seed(Context context) {
        if (BuildConfig.DEBUG) { //&& seedDatabase(context)) { TODO
            Log.i(TAG, "Seeding database...");
            seedInstrument();
        }
    }
    
    @SuppressWarnings("unused")
    public static void seedInstrument() {
        Instrument ins = new Instrument();
        ins.setTitle("Test Instrument ");
        //TODO
        Random random = new Random();
        long value = random.nextLong();
        ins.setRemoteId(value);
        ins.setProjectId(new Long(2)); 
        ins.setPublished(true);
        ins.setLanguage("en");
        ins.setQuestionCount(2);
        ins.setVersionNumber(0);
        ins.save();
       
        Question q2 = createQuestion(ins, "q111", "GRID_SELECT_SINGLE",
                "This is an example grid select question", random.nextLong());
        setOptions(q2, 5);
        q2.setOptionCount(5);
        q2.setRegExValidation("");
        q2.setImageCount(0);
        q2.setInstrumentVersion(0);
        q2.save();
        
        Question q1 = createQuestion(ins, "q104", "SELECT_ONE",
                "This is an example select one question", value);
        setOptions(q1, 3);
        q1.setOptionCount(3);
        q1.setRegExValidation("");
        q1.setImageCount(0);
        q1.setInstrumentVersion(0);
        q1.save();

//        Question q3 = createQuestion(ins, "q115", "SELECT_ONE_WRITE_OTHER",
//                "This is an example select one write other question", new Long(3));
//        setOptions(q3, 4);
//
//        Question q4 = createQuestion(ins, "q121",
//                "SELECT_MULTIPLE_WRITE_OTHER",
//                "This is an example select multiple write other question", new Long(4));
//        setOptions(q4, 4);
//
//        Question q5 = createQuestion(ins, "q125", "FREE_RESPONSE",
//                "This is an example free response question", new Long(5));
//        
//        Question q6 = createQuestion(ins, "q125", "FRONT_PICTURE",
//                "This is an example front picture question", new Long(6));
//        
//        Question q7 = createQuestion(ins, "q125", "REAR_PICTURE",
//                "This is an example rear picture question", new Long(7));
//        
//        Question q9 = createQuestion(ins, "q125", "SLIDER",
//                "This is an example slider question", new Long(8));
    }

    private static Question createQuestion(Instrument i, String qid,
            String qtype, String text, Long remoteId) {
        Question q = new Question();
        q.setInstrument(i);
        q.setQuestionIdentifier(qid);
        q.setQuestionType(qtype);
        q.setText(text);
        q.setRemoteId(remoteId);
        q.save();
        return q;
    }

    private static void setOptions(Question q, int num) {
    	Random random = new Random();
    	for (int i = 0; i < num; i++) {
            Option option = new Option();
            option.setQuestion(q);
            option.setText("This is option " + i);
            option.setRemoteId(random.nextLong());
            option.save();
        }
    }
    
    public static boolean seedDatabase(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                    return appInfo.metaData.getBoolean("SEED_DB");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cannot find database seed boolean in Android Manifest");
        }

        return false;
    }
}
