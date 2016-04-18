package es.uam.eps.tfg.cas.android.examples.geoquiz.model;

/**
 * Created by jutna on 01/03/2016.
 */
public class Question {

    private final int mTextResId;
    private final boolean mAnswer;

    public Question(final int textResId, final boolean answer) {
        mTextResId = textResId;
        mAnswer = answer;
    }

    public boolean isAnswerTrue() {
        return mAnswer;
    }

    public int getTextResId() {
        return mTextResId;
    }

}
