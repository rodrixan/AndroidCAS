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

import es.uam.eps.tfg.algebraicEngine.Operation;
import es.uam.eps.tfg.app.tfgapp.util.CASUtils;

/**
 * List of drawable elements (such as operations)
 */
public class DrawableExpressionList extends DrawableExpression {

    private final Operation mExpression;
    private List<DrawableExpression> mDrawableExpList;

    public DrawableExpressionList(final Typeface font, final Point coordinates, final Operation exp, final float textSize) {
        super(font, textSize);
        mExpression = exp;
        createDrawableList();
        updateCoordinates(coordinates);
    }

    public DrawableExpressionList(final Typeface font, final Operation exp, final float textSize) {
        this(font, new Point(0, 0), exp, textSize);
    }

    public DrawableExpressionList(final Typeface font, final Operation exp) {
        this(font, new Point(0, 0), exp, DEFAULT_TEXTSIZE);
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
        final String op = CASUtils.getStringOperatorSymbol(mExpression);
        for (final Operation exp : mExpression.getArgs()) {
            final List<DrawableExpression> drawExp = getDrawableExpressionFromExpression(exp);
            if (drawExp.size() != 0) {
                mDrawableExpList.addAll(drawExp);
            }
            mDrawableExpList.add(new DrawableOperator(mPaint.getTypeface(), op, mPaint.getTextSize()));
        }
        //delete the last operator
        if (mDrawableExpList.size() > 0) {
            mDrawableExpList.remove(mDrawableExpList.size() - 1);
        }
    }

    private List<DrawableExpression> getDrawableExpressionFromExpression(final Operation exp) {

        if (CASUtils.isMathematicalOperation(exp)) {
            return expressionAsDrawableExpressionList(exp);

        } else {
            final DrawableSingleExpression singleExp = new DrawableSingleExpression(mPaint.getTypeface(), exp, mPaint.getTextSize());
            return Arrays.asList(new DrawableExpression[]{singleExp});
        }
    }

    private List<DrawableExpression> expressionAsDrawableExpressionList(final Operation exp) {
        final List<DrawableExpression> returnList = new ArrayList<>();

        final DrawableExpressionList drawableExpressionList = new DrawableExpressionList(mPaint.getTypeface(), exp, mPaint.getTextSize());

        final String op = CASUtils.getStringOperatorSymbol(exp);

        returnList.add(new DrawableParenthesis(mPaint.getTypeface(), "(", mPaint.getTextSize()));

        if (CASUtils.isInverseOperation(exp) || CASUtils.isMinusOperation(exp)) {
            returnList.add(new DrawableOperator(mPaint.getTypeface(), op, mPaint.getTextSize()));
        }
        for (final Operation e : exp.getArgs()) {
            final List<DrawableExpression> subExpression = getDrawableExpressionFromExpression(e);
            returnList.addAll(subExpression);
            returnList.add(new DrawableOperator(mPaint.getTypeface(), op, mPaint.getTextSize()));
        }
        //replace the last operator occurrence
        returnList.set(returnList.size() - 1, new DrawableParenthesis(mPaint.getTypeface(), ")", mPaint.getTextSize()));

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
        int leftBound = mRectContainer.left;
        for (final DrawableExpression exp : mDrawableExpList) {
            final int expWidth = exp.width();
            final float nextX = leftBound + expWidth / 2;
            exp.updateCoordinates((int) nextX, nextY);
            exp.setHeight(mHeight);//all items with the same height
            leftBound = exp.mRectContainer.right;
        }
    }

    @Override
    protected Rect getDefaultBounds() {
        final Rect rect = new Rect();
        //used for height, width will be changed to better size
        final String text = CASUtils.getInfixExpressionOf(getExpression());
        mPaint.getTextBounds(text, 0, text.length(), rect);
        rect.right = rect.left + width();
        rect.top = rect.bottom - height();
        return rect;
    }

    @Override
    public Operation getExpression() {
        return mExpression;
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
    public int height() {
        int height = 0;
        for (final DrawableExpression exp : mDrawableExpList) {
            final int expHeight = exp.height();
            if (height < expHeight) {
                height = expHeight;
            }
        }
        mHeight = height;
        mRectContainer.top = mRectContainer.bottom - height;
        return height;
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
        return false;
    }

    @Override
    public void clearSelection() {
        for (final DrawableExpression exp : mDrawableExpList) {
            exp.setNormalColor(mNormalColor);
        }
    }

    @Override
    public void clearSelection(final int x, final int y) {
        final int[] depth = {0};
        final DrawableExpression selected = getDrawableAtPosition(x, y, depth);
        selected.setNormalColor(mNormalColor);
        if (selected != null) {
            selected.clearSelection();
        }
    }

    @Override
    public DrawableExpression getDrawableAtPosition(final int x, final int y, final int[] depth) {
        for (final DrawableExpression exp : mDrawableExpList) {
            if (exp.contains(x, y)) {

                if (exp.isDrawableOperator()) {
                    if (depth[0] > 0) {
                        depth[0] -= 1;
                    }
                    return this;
                } else if (exp.isDrawableSingleExpression()) {
                    return exp;
                } else if (CASUtils.isMinusOperation(exp.getExpression())) {
                    return exp;
                }
                depth[0] += 1;
                return getDrawableSubExpressionAt(x, y, exp, depth);
            }
        }
        return null;
    }

    private DrawableExpression getDrawableSubExpressionAt(final int x, final int y, final DrawableExpression exp, final int[] depth) {

        final DrawableExpression selected = exp.getDrawableAtPosition(x, y, depth);
        if (selected == null) {
            return null;
        }

        if (selected.isDrawableOperator() || selected.isDrawableParenthesis()) {
            depth[0] -= 1;
            return exp;
        } else {

            return selected;
        }
    }

    @Override
    public void setColor(final int color) {
        for (final DrawableExpression exp : mDrawableExpList) {
            exp.setColor(color);
        }
    }
}
