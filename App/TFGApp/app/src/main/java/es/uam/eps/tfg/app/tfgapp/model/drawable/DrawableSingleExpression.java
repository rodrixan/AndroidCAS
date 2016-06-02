package es.uam.eps.tfg.app.tfgapp.model.drawable;

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
        this(null, expression);
    }

    public DrawableSingleExpression(final Typeface font, final SingleExpression expression) {
        this(font, new Point(0, 0), expression);
    }

    public DrawableSingleExpression(final Typeface font, final Point coordinates, final SingleExpression expression) {
        super(font);
        mExpression = expression;
        updateCoordinates(coordinates);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        canvas.drawText(mExpression.symbolicExpression(), x, y, mPaint);
        drawContainer(canvas);
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
    public DrawableExpression getDrawableAtPosition(final int x, final int y) {
        if (contains(x, y)) {
            return this;
        }
        return null;
    }

    @Override
    public boolean isOperator() {
        for (final Operator op : Operator.values()) {
            if (op.toString().equals(mExpression.toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isParenthesis() {
        if (mExpression.toString().equals("(") || mExpression.toString().equals(")")) {
            return true;
        }
        return false;
    }
}
