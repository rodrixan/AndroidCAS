package es.uam.eps.tfg.app.tfgapp.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;

import es.uam.eps.expressions.types.interfaces.Expression;

/**
 * Each one of the elements that compose the ExpressionView
 */
public abstract class DrawableExpression {
    private static final float DEFAULT_TEXTSIZE = 50f;

    protected Rect mRectContainer;
    protected float x, y;
    protected Paint mPaint;

    public abstract void onDraw(Canvas canvas);

    public abstract Expression getExpression();


    protected DrawableExpression(final Typeface font) {
        mRectContainer = new Rect();
        x = 0;
        y = 0;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DEFAULT_TEXTSIZE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(font);
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

    public void updateCoordinates(final float x, final float y) {
        this.x = x;
        this.y = y;
        updateBounds(x, y);
    }

    protected void updateBounds(final float x, final float y) {
        updateBounds();
        mRectContainer.offset((int) (x - width() / 2), (int) y);
    }

    protected void updateBounds() {
        final String text = getExpression().symbolicExpression();
        mPaint.getTextBounds(text, 0, text.length(), mRectContainer);
    }


    public void setTextSize(final float size) {
        mPaint.setTextSize(size);
        updateBounds();
    }

    public void setFont(final Typeface font) {
        mPaint.setTypeface(font);
        updateBounds();
    }


}
