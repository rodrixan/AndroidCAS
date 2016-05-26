package es.uam.eps.tfg.app.tfgapp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;

import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.interfaces.Expression;

/**
 * Created by Rodri on 26/05/2016.
 */
public class DrawableExpressionList extends DrawableExpression {

    private final ExpressionList<Expression> mExpressionList;

    protected DrawableExpressionList(final Typeface font, final ExpressionList<Expression> expList) {
        super(font);
        mExpressionList = expList;
        updateBounds();
    }

    public DrawableExpressionList(final Typeface font, final PointF coordinates, final ExpressionList<Expression> expression) {
        this(font, expression);
        updateCoordinates(coordinates);
    }

    public DrawableExpressionList(final ExpressionList<Expression> expression) {
        this(null, expression);
    }


    @Override
    public Expression getExpression() {
        return mExpressionList;
    }


    @Override
    public void onDraw(final Canvas canvas) {
        canvas.drawText(mExpressionList.symbolicExpression(), x, y, mPaint);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(mRectContainer, paint);
    }

}
