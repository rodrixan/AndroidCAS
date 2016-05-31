package es.uam.eps.tfg.app.tfgapp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;


public class DrawableExpressionList extends DrawableExpression {

    private final ExpressionList<Expression> mExpressionList;
    private List<DrawableExpression> mDrawableExpList;

    protected DrawableExpressionList(final Typeface font, final ExpressionList<Expression> expList) {
        this(font, new PointF(0, 0), expList);
    }

    public DrawableExpressionList(final Typeface font, final PointF coordinates, final ExpressionList<Expression> expression) {
        super(font);
        mExpressionList = expression;
        updateCoordinates(coordinates);
        updateBounds();
        Log.d("APP_TEST", "Left: " + mRectContainer.left);
        Log.d("APP_TEST", "Right: " + mRectContainer.right);
        Log.d("APP_TEST", "Width: " + width());

        createDrawableList();


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
        for (final DrawableExpression element : mDrawableExpList) {
            element.onDraw(canvas);
        }
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(mRectContainer, paint);
    }

    private void createDrawableList() {
        mDrawableExpList = new ArrayList<>();
        final int nElements = mExpressionList.size();
        float leftPos = mRectContainer.left;
        Log.d("APP_TEST", "Initial leftPos: " + leftPos);

        for (final Expression exp : mExpressionList) {
            final DrawableExpression drawExp = getDrawableFromExpression(exp);
            // if it's not assigned, don't add it
            if (drawExp != null) {
                leftPos = updateElementCoordinates(drawExp, leftPos);
                mDrawableExpList.add(drawExp);
            }
        }
    }

    private DrawableExpression getDrawableFromExpression(final Expression exp) {
        DrawableExpression drawExp = null;

        if (exp instanceof ExpressionList) {
            drawExp = new DrawableExpressionList(mPaint.getTypeface(), (ExpressionList) exp);
        } else if (exp instanceof SingleExpression) {
            drawExp = new DrawableSingleExpression(mPaint.getTypeface(), (SingleExpression) exp);
        }
        return drawExp;
    }

    private float updateElementCoordinates(final DrawableExpression drawExp, final float leftPos) {

        drawExp.updateCoordinates(leftPos + drawExp.width() / 2, this.y);
        Log.d("APP_TEST", "Updating element position: (MAIN " + this.x + " " + this.y + "), " + drawExp.x + " " + drawExp.y + " left pos: " + leftPos);
        return leftPos + drawExp.width();
    }

}
