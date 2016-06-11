package es.uam.eps.tfg.app.tfgapp.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.interfaces.Operator;

/**
 * List of drawable elements (such as operations)
 */
public class DrawableExpressionList extends DrawableExpression {

    private final ExpressionList<Expression> mExpressionList;
    private List<DrawableExpression> mDrawableExpList;


    public DrawableExpressionList(final Typeface font, final Point coordinates, final ExpressionList<Expression> exp, final float textSize) {
        super(font, textSize);
        mExpressionList = exp;
        createDrawableList();
        updateCoordinates(coordinates);
    }

    public DrawableExpressionList(final Typeface font, final ExpressionList<Expression> exp, final float textSize) {
        this(font, new Point(0, 0), exp, textSize);
    }

    public DrawableExpressionList(final Typeface font, final ExpressionList<Expression> exp) {
        this(font, new Point(0, 0), exp, DEFAULT_TEXTSIZE);
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
        //drawExternalContainers(canvas);
    }

    private void drawExternalContainers(final Canvas canvas) {
        for (final DrawableExpression element : mDrawableExpList) {

            if (element instanceof DrawableExpressionList) {
                drawContainer(canvas, element, Color.RED);
            }
        }
        drawContainer(canvas, this, Color.BLUE);
    }

    private void drawContainer(final Canvas canvas, final DrawableExpression element, final int color) {
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        canvas.drawRect(element.mRectContainer, paint);
    }


    private void createDrawableList() {
        mDrawableExpList = new ArrayList<>();
        final Operator op = mExpressionList.getOperator();
        for (final Expression exp : mExpressionList) {
            final List<DrawableExpression> drawExp = getDrawableExpressionFromExpression(exp);
            if (drawExp.size() != 0) {
                mDrawableExpList.addAll(drawExp);
            }
            mDrawableExpList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(op.toString()), mPaint.getTextSize()));
        }
        //delete the last operator
        mDrawableExpList.remove(mDrawableExpList.size() - 1);
    }

    private List<DrawableExpression> getDrawableExpressionFromExpression(final Expression exp) {

        if (exp instanceof ExpressionList) {
            return expressionListAsDrawableExpressionList((ExpressionList<Expression>) exp);

        } else if (exp instanceof SingleExpression) {
            return Arrays.asList(new DrawableExpression[]{new DrawableSingleExpression(mPaint.getTypeface(), (SingleExpression) exp, mPaint.getTextSize())});
        }
        return null;
    }

    private List<DrawableExpression> expressionListAsDrawableExpressionList(final ExpressionList<Expression> exp) {
        final List<DrawableExpression> returnList = new ArrayList<>();

        final DrawableExpressionList drawableExpressionList = new DrawableExpressionList(mPaint.getTypeface(), exp, mPaint.getTextSize());

        final Operator op = exp.getOperator();

        returnList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression("("), mPaint.getTextSize()));

        for (final Expression e : exp) {
            final List<DrawableExpression> subExpression = getDrawableExpressionFromExpression(e);
            returnList.addAll(subExpression);
            returnList.add(new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(op.toString()), mPaint.getTextSize()));
        }
        //replace the last operator occurrence
        returnList.set(returnList.size() - 1, new DrawableSingleExpression(mPaint.getTypeface(), new SingleExpression(")"), mPaint.getTextSize()));

        drawableExpressionList.setDrawableExpList(returnList);

        return Arrays.asList(new DrawableExpression[]{drawableExpressionList});
    }

    private void setDrawableExpList(final List<DrawableExpression> list) {
        mDrawableExpList = list;
    }

    @Override
    public void updateCoordinates(final int x, final int y) {
        super.updateCoordinates(x, y);
        updateSubExpressionsCoordinates();
    }

    private void updateSubExpressionsCoordinates() {
        final int nextY = y;
        final int parentHeight = mHeight;
        int leftBound = mRectContainer.left;
        for (final DrawableExpression exp : mDrawableExpList) {
            final int expWidth = exp.width();
            final float nextX = leftBound + expWidth / 2;
            exp.updateCoordinates((int) nextX, nextY);
            exp.setHeight(parentHeight);//all items with the same height
            leftBound = exp.mRectContainer.right;
        }
    }

    @Override
    public int width() {
        int width = 0;
        for (final DrawableExpression exp : mDrawableExpList) {
            width += exp.width();
        }
        mWidth = width;
        mRectContainer.right = mRectContainer.left + width;
        return width;

    }

    @Override
    protected Rect getDefaultBounds() {
        final Rect rect = new Rect();
        //used for height, width will be changed to better size
        final String text = getExpression().toString();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        rect.right = rect.left + width();
        return rect;
    }

    @Override
    public DrawableExpression getDrawableAtPosition(final int x, final int y) {
        for (final DrawableExpression exp : mDrawableExpList) {
            if (exp.contains(x, y)) {
                if (exp.isOperator()) {
                    return this;
                }
                return getDrawableSubExpressionAt(x, y, exp);
            }
        }
        return null;
    }

    private DrawableExpression getDrawableSubExpressionAt(final int x, final int y, final DrawableExpression exp) {
        final DrawableExpression selected = exp.getDrawableAtPosition(x, y);
        if (selected == null) {
            return null;
        }
        if (selected.isOperator() || selected.isParenthesis()) {
            return exp;
        } else {
            return selected;
        }
    }

    @Override
    public boolean isOperator() {
        return false;
    }

    @Override
    public boolean isParenthesis() {
        return false;
    }

    @Override
    public void clearSelection() {
        for (final DrawableExpression exp : mDrawableExpList) {
            exp.clearSelection();
        }
    }

    @Override
    public void setColor(final int color) {
        for (final DrawableExpression exp : mDrawableExpList) {
            exp.setColor(color);
        }
    }
}
