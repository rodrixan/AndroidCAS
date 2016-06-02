package es.uam.eps.tfg.app.tfgapp.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

import es.uam.eps.expressions.types.interfaces.Expression;

/**
 * Each one of the elements that compose the ExpressionView
 */
public abstract class DrawableExpression {
    private static final float DEFAULT_TEXTSIZE = 100f;

    protected Rect mRectContainer;
    protected int x, y;
    protected int mWidth;
    protected int mHeight;
    protected Paint mPaint;

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

    public abstract void onDraw(Canvas canvas);

    public Rect getContainer() {
        return mRectContainer;
    }

    public Point getCoordinates() {
        return new Point(x, y);
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

    public int width() {
        return mWidth;
    }

    public int height() {
        return mHeight;
    }

    public boolean isInside(final Rect outerContainer) {
        return outerContainer.contains(mRectContainer);
    }

    public int getColor() {
        return mPaint.getColor();
    }

    public void setColor(final int color) {
        mPaint.setColor(color);
    }

    public boolean contains(final int x, final int y) {
        return mRectContainer.contains(x, y);
    }

    public void updateCoordinates(final Point newCoord) {
        updateCoordinates(newCoord.x, newCoord.y);
    }

    public void updateCoordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
        mRectContainer = updateBounds();
    }

    protected void setHeight(final int height) {
        mHeight = height;
        mRectContainer.top = mRectContainer.bottom - height;
    }

    public Rect updateBounds() {

        final Rect defaultBounds = getDefaultBounds();

        mWidth = defaultBounds.width();
        mHeight = defaultBounds.height();

        return getCenteredBounds(defaultBounds);
    }

    protected Rect getDefaultBounds() {
        final Rect rect = new Rect();
        //final String text = getExpression().symbolicExpression().replaceAll("\\s", "");
        final String text = getExpression().toString();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    public abstract Expression getExpression();

    private Rect getCenteredBounds(final Rect defaultBounds) {
        final Rect container = new Rect();
        container.left = x - defaultBounds.width() / 2;
        container.right = container.left + defaultBounds.width();
        container.top = y - defaultBounds.height();
        container.bottom = y;
        return container;
    }

    public void setTextSize(final float size) {
        mPaint.setTextSize(size);
        mRectContainer = updateBounds();
    }

    public void setFont(final Typeface font) {
        mPaint.setTypeface(font);
        mRectContainer = updateBounds();
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int left() {
        return mRectContainer.left;
    }

    public int right() {
        return mRectContainer.right;
    }

    public int top() {
        return mRectContainer.top;
    }

    public int bottom() {
        return mRectContainer.bottom;
    }

    public abstract DrawableExpression getDrawableAtPosition(final int x, final int y);

    public abstract boolean isOperator();

    public abstract boolean isParenthesis();

    public void select(final int x, final int y) {
        final DrawableExpression exp = getDrawableAtPosition(x, y);
        exp.setColor(Color.CYAN);
    }

    public void clearSelection() {
        mPaint.setColor(Color.BLACK);
    }
}
