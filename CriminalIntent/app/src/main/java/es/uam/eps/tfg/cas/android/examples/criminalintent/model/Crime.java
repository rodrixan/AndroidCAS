package es.uam.eps.tfg.cas.android.examples.criminalintent.model;


import java.util.Date;
import java.util.UUID;

public class Crime {
    private final UUID mId; //Id
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
        mSolved = false;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    public Date getDate() {
        return (mDate != null) ? new Date(mDate.getTime()) : null;
    }

    public void setDate(final Date date) {
        mDate = (date != null) ? new Date(date.getTime()) : null;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(final boolean solved) {
        mSolved = solved;
    }
}
