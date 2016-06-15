package es.uam.eps.tfg.app.tfgapp.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.interfaces.Operator;

/**
 * Single drawable element (such as numbers or variables)
 */
public class DrawableSingleExpression extends DrawableExpression {

    SingleExpression mExpression;

    public DrawableSingleExpression(final SingleExpression expression) {
        this(null, expression, DEFAULT_TEXTSIZE);
    }

    public DrawableSingleExpression(final Typeface font, final SingleExpression expression, final float textSize) {
        this(font, new Point(0, 0), expression, textSize);
    }

    public DrawableSingleExpression(final Typeface font, final Point coordinates, final SingleExpression expression, final float textSize) {
        super(font, textSize);
        mExpression = expression;
        updateCoordinates(coordinates);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        canvas.drawText(mExpression.symbolicExpression(), x, y, mPaint);
        //drawContainer(canvas);
    }

    private void drawContainer(final Canvas canvas) {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(mRectContainer, paint);
    }

    @Override
    public Expression getExpression() {
        return mExpression;
    }

    @Override
    public DrawableExpression getDrawableAtPosition(final int x, final int y, int[] depth) {
        if (contains(x, y)) {
            return this;
        }
        return null;
    }

    @Override
    public boolean isDrawableOperator() {
        for (final Operator op : Operator.values()) {
            if (op.toString().equals(mExpression.toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isDrawableParenthesis() {
        if (mExpression.toString().equals("(") || mExpression.toString().equals(")")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDrawableSingleExpression() {
        return true;
    }
}
