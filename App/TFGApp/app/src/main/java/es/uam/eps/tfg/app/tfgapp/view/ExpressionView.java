package es.uam.eps.tfg.app.tfgapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.Utils.Utils;
import es.uam.eps.tfg.app.tfgapp.controller.listeners.OnExpressionSelectedListener;
import es.uam.eps.tfg.app.tfgapp.model.drawable.DrawableExpression;
import es.uam.eps.tfg.app.tfgapp.model.drawable.DrawableExpressionList;

/**
 * View for an expression from the CAS
 */
public class ExpressionView extends View {

    private static final String FONT_PATH = "fonts/lmromanslant10-regular-ExpView.otf";

    private final GestureDetector mGestureDetector;
    private OnExpressionSelectedListener mOnExpressionSelectedListener;
    private final DrawableExpression mExp;
    private Expression mSelectedExp = null;
    private int mHeight;
    private int mWidth;
    private final boolean mSelected = false;
    private final Typeface mFont;

    public ExpressionView(final Context context) {
        this(context, null, 0);
    }

    public ExpressionView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mFont = Typeface.createFromAsset(context.getAssets(), FONT_PATH);

        mGestureDetector = new GestureDetector(context, new MyGestureListener());


        mExp = new DrawableExpressionList(mFont, Utils.createUltraLongSampleExpression());
        mExp.setNormalColor(getResources().getColor(R.color.colorExpression));
        mExp.setSelectedColor(getResources().getColor(R.color.colorExpressionSelected));

        Log.d(Utils.LOG_TAG, "Main exp position: " + mExp.x() + " " + mExp.y());
    }

    public ExpressionView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        mWidth = w;
        mHeight = h;

        //FOR DEBUGGING!!!!!!
        if (w / 2 == 952.0)
            Log.d(Utils.LOG_TAG, "Main exp position: " + mExp.x() + " " + mExp.y());
        //FOR DEBUGGING!!!!!!

        mExp.updateCoordinates(w / 2, h / 2);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * @return the used font for the texts
     */
    public Typeface getFont() {
        return mFont;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(Utils.LOG_TAG, "Soltado en posicion: " + event.getX() + "," + event.getY());
            invalidate();
            return true;
        }
        mGestureDetector.onTouchEvent(event);
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mExp.onDraw(canvas);
    }

    /**
     * Sets a listener for doing actions everytime an expression is selected
     *
     * @param onExpressionSelectedListener
     */
    public void setOnExpressionSelectedListener(final OnExpressionSelectedListener onExpressionSelectedListener) {
        mOnExpressionSelectedListener = onExpressionSelectedListener;
    }

    /**
     * GestureListener for perfoming actions whenever the user touches the view
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(final MotionEvent event) {
            Log.d(Utils.LOG_TAG, "onDown");

            mExp.clearSelection();
            final DrawableExpression exp = mExp.select((int) event.getX(), (int) event.getY());

            if (exp != null) {
                Log.d(Utils.LOG_TAG, "Clicked on expression: " + exp.getExpression().symbolicExpression());
                mSelectedExp = exp.getExpression();
            } else {
                Log.d(Utils.LOG_TAG, "Clicked out of the exp");
                mSelectedExp = null;
            }

            mOnExpressionSelectedListener.onExpressionSelected(mSelectedExp);

//            Log.d(Utils.LOG_TAG, "Click en:" + event.getX() + "," + event.getY() + "; circulo en " + mCoords.x + "," + mCoords.y);
//            if (!mCircleRect.contains(event.getX(), event.getY())) {
//                Log.d(Utils.LOG_TAG, "la posicion no esta dentro de " + mCircleRect);
//                mSelected = false;
//            } else {
//                mSelected = true;
//            }
//            if (!mExp.contains((int) event.getX(), (int) event.getY())) {
////                Log.d(Utils.LOG_TAG, "la posicion no esta dentro de " + mCircleRect);
//                mSelected = false;
//            } else {
//                mSelected = false;
//            }
            return true;
        }


        @Override
        public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
            Log.d(Utils.LOG_TAG, "Posicion inicial: " + e1.getX() + "," + e1.getY() + " - Posicion actual: " + e2.getX() + "," + e2.getY());

//            if (mSelected && mExp.isValidPosition(e2.getX(), e2.getY(), new Rect(0, 0, mWidth, mHeight))) {
//                mExp.updateCoordinates((int) e2.getX(), (int) e2.getY());
//            }

            return true;
        }

    }


}
