package es.uam.eps.tfg.app.tfgapp.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

import es.uam.eps.tfg.algebraicEngine.Operation;
import es.uam.eps.tfg.app.tfgapp.util.CASUtils;

/**
 * Single drawable element (such as numbers or variables)
 */
public class DrawableSingleExpression extends DrawableExpression {

    private Operation mExpression;

    public DrawableSingleExpression(final Operation expression) {
        this(null, expression, DEFAULT_TEXTSIZE);
    }

    public DrawableSingleExpression(final Typeface font, final Operation expression, final float textSize) {
        this(font, new Point(0, 0), expression, textSize);
    }

    public DrawableSingleExpression(final Typeface font, final Point coordinates, final Operation expression, final float textSize) {
        super(font, textSize);
        mExpression = expression;
        updateCoordinates(coordinates);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        canvas.drawText(CASUtils.getSymbolStringExpression(mExpression), x, y, mPaint);
        //drawContainer(canvas);
    }

    private void drawContainer(final Canvas canvas) {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(mRectContainer, paint);
    }

    @Override
    public Operation getExpression() {
        return mExpression;
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

        return false;
    }

    @Override
    public boolean isDrawableParenthesis() {

        return false;
    }

    @Override
    public boolean isDrawableSingleExpression() {
        return true;
    }
}
