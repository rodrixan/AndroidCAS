package es.uam.eps.tfg.cas.android.examples.draganddraw.model;


import android.graphics.PointF;

public class Box {
    private final PointF mOrigin;
    private PointF mCurrent;

    public Box(final PointF origin) {
        mOrigin = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(final PointF current) {
        mCurrent = current;
    }

    @Override
    public String toString() {
        return mOrigin.x + " " + mOrigin.y + "|" + mCurrent.x + " " + mCurrent.y;
    }
}
