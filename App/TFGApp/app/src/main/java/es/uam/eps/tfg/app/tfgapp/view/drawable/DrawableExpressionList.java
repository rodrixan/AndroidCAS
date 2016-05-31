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

    protected DrawableExpressionList(final Typeface font, final ExpressionList<Expression> expList) {
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
            final List<DrawableExpression> drawExp = getDrawableFromExpression(exp);

            if (drawExp.size() != 0) {
                mDrawableExpList.addAll(drawExp);
            }
            mDrawableExpList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(op.toString())));
        }
        //delete the last operator
        mDrawableExpList.remove(mDrawableExpList.size() - 1);
    }

    private List<DrawableExpression> getDrawableFromExpression(final Expression exp) {
        final List<DrawableExpression> drawExpList = new ArrayList<>();

        if (exp instanceof ExpressionList) {

            final DrawableExpressionList drawableExpressionList = new DrawableExpressionList(mPaint.getTypeface(), (ExpressionList) exp);
            final Operator op = ((ExpressionList) exp).getOperator();

            drawExpList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression("(")));

            for (final Expression e : (ExpressionList<Expression>) exp) {
                final List<DrawableExpression> subExpression = getDrawableFromExpression(e);
                drawExpList.addAll(subExpression);
                drawExpList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(op.toString())));

            }
            //replace the last operator occurrence
            drawExpList.set(drawExpList.size() - 1, new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(")")));
            drawableExpressionList.mDrawableExpList = drawExpList;
            return Arrays.asList(new DrawableExpression[]{drawableExpressionList});

        } else if (exp instanceof SingleExpression) {
            drawExpList.add(new DrawableSingleExpression(mPaint.getTypeface(), (SingleExpression) exp));
        }
        return drawExpList;
    }

    @Override
    public void updateCoordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
        updateBounds(x, y);
        updateSubExpressionsCoordinates();
    }

    private void updateSubExpressionsCoordinates() {
        final int nextY = (int) y;
        int leftBound = mRectContainer.left;
        for (final DrawableExpression exp : mDrawableExpList) {
            final int expWidth = exp.width();
            final float nextX = leftBound + expWidth / 2;
            exp.updateCoordinates((int) nextX, nextY);
            exp.mRectContainer.left = leftBound;
            exp.mRectContainer.right = leftBound + expWidth;
            leftBound = exp.mRectContainer.right;
        }
    }

    private void updateBounds(final int x, final int y) {
        updateBounds();
        mRectContainer.offset(x - width() / 2, y);

    }

    private void updateBounds() {
        final String text = getExpression().symbolicExpression().replaceAll("\\s", "");
        mPaint.getTextBounds(text, 0, text.length(), mRectContainer);
    }

}
