package es.uam.eps.tfg.app.tfgapp.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;


public class DrawableSingleExpression extends DrawableExpression {


    SingleExpression mExpression;

    protected DrawableSingleExpression(final Typeface font, final SingleExpression expression) {
        this(font, new Point(0, 0), expression);
    }


    public DrawableSingleExpression(final Typeface font, final Point coordinates, final SingleExpression expression) {
        super(font);
        mExpression = expression;
        updateCoordinates(coordinates);
    }

    public DrawableSingleExpression(final SingleExpression expression) {
        this(null, expression);
    }


    @Override
    public void onDraw(final Canvas canvas) {
        //canvas.drawText(mExpression.symbolicExpression(), x, y, mPaint);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(mRectContainer, paint);
    }

    @Override
    public Expression getExpression() {
        return mExpression;
    }
}
