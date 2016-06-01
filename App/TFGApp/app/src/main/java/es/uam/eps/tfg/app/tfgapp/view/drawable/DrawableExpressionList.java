package es.uam.eps.tfg.app.tfgapp.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.interfaces.Operator;

public class DrawableExpressionList extends DrawableExpression {

    private final ExpressionList<Expression> mExpressionList;
    private List<DrawableExpression> mDrawableExpList;

    public DrawableExpressionList(final Typeface font, final ExpressionList<Expression> expList) {
        this(font, new Point(0, 0), expList);
    }

    public DrawableExpressionList(final Typeface font, final Point coordinates, final ExpressionList<Expression> expression) {
        super(font);
        mExpressionList = expression;
        createDrawableList();
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
        //canvas.drawText(mExpressionList.symbolicExpression().replaceAll("\\s", ""), x, y, mPaint);
        for (final DrawableExpression element : mDrawableExpList) {

            element.onDraw(canvas);
            if (element instanceof DrawableExpressionList) {
                final Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.RED);
                canvas.drawRect(element.mRectContainer, paint);
            }
        }
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        canvas.drawRect(mRectContainer, paint);
    }

    public void createDrawableList() {
        mDrawableExpList = new ArrayList<>();
        final Operator op = mExpressionList.getOperator();
        for (final Expression exp : mExpressionList) {
            final List<DrawableExpression> drawExp = getDrawableExpressionFromExpression(exp);
            if (drawExp.size() != 0) {
                mDrawableExpList.addAll(drawExp);
            }
            mDrawableExpList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(op.toString())));
        }
        //delete the last operator
        mDrawableExpList.remove(mDrawableExpList.size() - 1);
    }

    private List<DrawableExpression> getDrawableExpressionFromExpression(final Expression exp) {

        if (exp instanceof ExpressionList) {
            return expressionListAsDrawableExpressionList((ExpressionList<Expression>) exp);

        } else if (exp instanceof SingleExpression) {
            return Arrays.asList(new DrawableExpression[]{new DrawableSingleExpression(mPaint.getTypeface(), (SingleExpression) exp)});
        }
        return null;
    }

    private List<DrawableExpression> expressionListAsDrawableExpressionList(final ExpressionList<Expression> exp) {
        final List<DrawableExpression> returnList = new ArrayList<>();

        final DrawableExpressionList drawableExpressionList = new DrawableExpressionList(mPaint.getTypeface(), exp);

        final Operator op = exp.getOperator();

        returnList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression("(")));

        for (final Expression e : exp) {
            final List<DrawableExpression> subExpression = getDrawableExpressionFromExpression(e);
            returnList.addAll(subExpression);
            returnList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(op.toString())));
        }
        //replace the last operator occurrence
        returnList.set(returnList.size() - 1, new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(")")));

        drawableExpressionList.setDrawableExpList(returnList);

        return Arrays.asList(new DrawableExpression[]{drawableExpressionList});
    }

    private void setDrawableExpList(final List<DrawableExpression> list) {
        mDrawableExpList = list;
    }

    @Override
    public void updateCoordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
        mRectContainer = updateBounds();
        updateSubExpressionsCoordinates();
    }

    private void updateSubExpressionsCoordinates() {
        final int nextY = y;
        int leftBound = mRectContainer.left;
        for (final DrawableExpression exp : mDrawableExpList) {
            final int expWidth = exp.width();
            final float nextX = leftBound + expWidth / 2;
            exp.updateCoordinates((int) nextX, nextY);
            leftBound = exp.mRectContainer.right;
        }
    }

}
