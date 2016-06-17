package es.uam.eps.tfg.app.tfgapp.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

import es.uam.eps.tfg.algebraicEngine.Operation;

/**
 * Drawable class for an operator
 */
public class DrawableOperator extends DrawableExpression {

    String mExpression;

    public DrawableOperator(final String expression) {
        this(null, expression, DEFAULT_TEXTSIZE);
    }

    public DrawableOperator(final Typeface font, final String expression, final float textSize) {
        this(font, new Point(0, 0), expression, textSize);
    }

    public DrawableOperator(final Typeface font, final Point coordinates, final String expression, final float textSize) {
        super(font, textSize);
        mExpression = expression;
        updateCoordinates(coordinates);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        canvas.drawText(mExpression.toString(), x, y, mPaint);
        drawContainer(canvas);
    }

    private void drawContainer(final Canvas canvas) {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(mRectContainer, paint);
    }

    @Override
    public Operation getExpression() {
        return new Operation(mExpression);
    }

    @Override
    public DrawableExpression getDrawableAtPosition(final int x, final int y, final int[] depth) {
        if (contains(x, y)) {
            return this;
        }
        return null;
    }

    @Override
    public boolean isDrawableOperator() {
        return true;
    }

    @Override
    public boolean isDrawableParenthesis() {

        return false;
    }

    @Override
    public boolean isDrawableSingleExpression() {
        return false;
    }
}
