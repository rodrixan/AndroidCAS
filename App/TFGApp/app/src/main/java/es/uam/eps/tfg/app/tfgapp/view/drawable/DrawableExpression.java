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
    protected static final float DEFAULT_TEXTSIZE = 100f;
    protected Rect mRectContainer;
    protected int x, y;
    protected int mWidth;
    protected int mHeight;
    protected Paint mPaint;
    private int mNormalColor = Color.BLACK;
    private int mSelectedColor = Color.CYAN;

    protected DrawableExpression(final Typeface font) {
        this(font, DEFAULT_TEXTSIZE);
    }

    protected DrawableExpression(final Typeface font, final float textSize) {
        mRectContainer = new Rect();
        x = 0;
        y = 0;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(font);
    }

    /**
     * Draws the element in a canvas
     *
     * @param canvas view where to draw the element
     */
    public abstract void onDraw(Canvas canvas);

    /**
     * @return the rectangle where the element is contained (its bounds)
     */
    public Rect getContainer() {
        return mRectContainer;
    }

    /**
     * @return point with the element coordinates
     */
    public Point getCoordinates() {
        return new Point(x, y);
    }

    /**
     * Checks if the new position is valid. It doesn't assign the coordinates
     *
     * @param newX           new x position
     * @param newY           new y position
     * @param outerContainer rectangle to find if the coordinates are possible
     * @return true if the element can be placed in the new coordinates, false otherwise
     */
    public boolean isValidPosition(final float newX, final float newY, final Rect outerContainer) {
        if (newX - width() / 2 < outerContainer.left || newY - height() / 2 < outerContainer.top) {
            return false;
        }
        if (newX + width() / 2 > outerContainer.right || newY + height() / 2 > outerContainer.bottom) {
            return false;
        }
        return true;
    }

    /**
     * @return width of the bound rectangle
     */
    public int width() {
        return mWidth;
    }

    /**
     * @return height of the bound rectangle
     */
    public int height() {
        return mHeight;
    }

    public void setNormalColor(final int color) {
        mNormalColor = color;
    }

    public void setSelectedColor(final int color) {
        mSelectedColor = color;
    }

    /**
     * @param outerContainer external bounds
     * @return true if the element is inside the external container, false otherwise
     */
    public boolean isInside(final Rect outerContainer) {
        return outerContainer.contains(mRectContainer);
    }

    /**
     * @return color of the element
     */
    public int getColor() {
        return mPaint.getColor();
    }

    /**
     * @param color color to set for the paint
     */
    protected void setColor(final int color) {
        mPaint.setColor(color);
    }

    /**
     * Checks if a position is inside the element bounds
     *
     * @param x
     * @param y
     * @return true if the element contains the coordinates, false otherwise
     */
    public boolean contains(final int x, final int y) {
        return mRectContainer.contains(x, y);
    }

    /**
     * Same as the method updateCoordinates(x,y)
     *
     * @param newCoord point with new coordinates
     */
    public void updateCoordinates(final Point newCoord) {
        updateCoordinates(newCoord.x, newCoord.y);
    }

    /**
     * Updates both the element position and its bound rectangle
     *
     * @param x
     * @param y
     */
    public void updateCoordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
        mRectContainer = updateBounds();
    }

    /**
     * Update the rectangle that contains the element
     *
     * @return the new bound rectangle
     */
    public Rect updateBounds() {

        final Rect defaultBounds = getDefaultBounds();

        mWidth = defaultBounds.width();
        mHeight = defaultBounds.height();

        return getCenteredBounds(defaultBounds);
    }

    /**
     * @return the minimal rectangle that contains the element,centered at (0,0)
     */
    protected Rect getDefaultBounds() {
        final Rect rect = new Rect();
        final String text = getExpression().toString();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * @return the expression that this element draws
     */
    public abstract Expression getExpression();

    private Rect getCenteredBounds(final Rect defaultBounds) {
        final Rect container = new Rect();
        container.left = x - defaultBounds.width() / 2;
        container.right = container.left + defaultBounds.width();
        container.top = y - defaultBounds.height();
        container.bottom = y;
        return container;
    }

    protected void setHeight(final int height) {
        mHeight = height;
        mRectContainer.top = mRectContainer.bottom - height;
    }

    public float getTextSize() {
        return mPaint.getTextSize();
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

    /**
     * @return true if the element can be considered an operator for an expression, false if not
     */
    public abstract boolean isOperator();

    /**
     * @return true if the element can be considered as a parenthesis for an expression, false if not
     */
    public abstract boolean isParenthesis();

    /**
     * Make the expression in a given position selected, changing its color
     *
     * @param x
     * @param y
     * @return the expression selected, null if no one was found
     */
    public DrawableExpression select(final int x, final int y) {
        final DrawableExpression exp = getDrawableAtPosition(x, y);
        if (exp != null) {
            exp.setColor(mSelectedColor);
        }
        return exp;
    }

    /**
     * Given a position, returns the expression that matches it
     *
     * @param x
     * @param y
     * @return expression that matches the position. Can be single or a list
     */
    protected abstract DrawableExpression getDrawableAtPosition(final int x, final int y);

    public void clearSelection(final int x, final int y) {
        clearSelection();
    }

    public void clearSelection() {
        mPaint.setColor(mNormalColor);
    }
}
