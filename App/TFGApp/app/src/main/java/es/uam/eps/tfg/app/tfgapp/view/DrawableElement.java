package es.uam.eps.tfg.app.tfgapp.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Each one of the elements that compose the ExpressionView
 */
public abstract class DrawableElement {
    protected Rect mRectContainer;
    protected float x, y;
    protected Paint mPaint;

    public abstract void onDraw(Canvas canvas);

    protected DrawableElement() {
        mRectContainer = new Rect();
        x = 0;
        y = 0;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
    }

    public Rect getContainer() {
        return mRectContainer;
    }

    public PointF getCoordinates() {
        return new PointF(x, y);
    }

    public void updateCoordinates(final PointF newCoord) {
        updateCoordinates(newCoord.x, newCoord.y);
    }

    public void updateCoordinates(final float x, final float y) {
        this.x = x;
        this.y = y;
        mRectContainer.set((int) (x - width() / 2), (int) (y - height() / 2), (int) (x + width() / 2), (int) (y + height() / 2));
    }

    public float width() {
        return Math.abs(mRectContainer.right - mRectContainer.left);
    }

    public float height() {
        return Math.abs(mRectContainer.bottom - mRectContainer.top);
    }

    public boolean isValidPosition(final float newX, final float newY, final Rect outerContainer) {
        if (newX - width() / 2 < outerContainer.left || newY - height() / 2 < outerContainer.top) {
            return false;
        }
        if (newX + width() / 2 > outerContainer.right || newY + height() / 2 > outerContainer.bottom) {
            return false;
        }
        return true;
    }

    public boolean isInside(final Rect outerContainer) {
        return outerContainer.contains(mRectContainer);
    }

    public void setColor(final int color) {
        mPaint.setColor(color);
    }

    public int getColor() {
        return mPaint.getColor();
    }

    public boolean contains(final float x, final float y) {
        return mRectContainer.contains((int) x, (int) y);
    }

}
