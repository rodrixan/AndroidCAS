package es.uam.eps.tfg.app.tfgapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.Utils.Utils;
import es.uam.eps.tfg.app.tfgapp.controller.listeners.OnExpressionActionListener;
import es.uam.eps.tfg.app.tfgapp.controller.listeners.OnExpressionUpdateListener;
import es.uam.eps.tfg.app.tfgapp.view.drawable.DrawableExpression;
import es.uam.eps.tfg.app.tfgapp.view.drawable.DrawableExpressionList;

/**
 * View for an expression from the CAS
 */
public class ExpressionView extends View implements OnExpressionUpdateListener {



    private final GestureDetector mGestureDetector;
    private final boolean mSelected = false;
    private final Typeface mFont;
    private DrawableExpression mExp;
    private OnExpressionActionListener mOnExpressionActionListener;
    private Expression mSelectedExp = null;

    public ExpressionView(final Context context) {
        this(context, null, 0);
    }

    public ExpressionView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mFont = Typeface.createFromAsset(context.getAssets(), Utils.FONT_PATH);

        mGestureDetector = new GestureDetector(context, new MyGestureListener());

        mExp = null;

        //Log.d(Utils.LOG_TAG, "Main exp position: " + mExp.x() + " " + mExp.y());
    }

    public ExpressionView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {

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
            Log.d(Utils.LOG_TAG, "Dropped at: " + event.getX() + "," + event.getY());
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
     * @param onExpressionActionListener
     */
    public void setOnExpressionActionListener(final OnExpressionActionListener onExpressionActionListener) {
        mOnExpressionActionListener = onExpressionActionListener;
    }

    @Override
    public void onExpressionUpdated(final Expression exp) {
        Point coord = null;
        if (mExp != null) {
            coord = mExp.getCoordinates();
        } else {
            coord = new Point(0, 0);
        }
        final int textSize = getResources().getDimensionPixelSize(R.dimen.exp_text_size);
        mExp = new DrawableExpressionList(mFont, coord, (ExpressionList) exp, textSize);

        mExp.setNormalColor(getResources().getColor(R.color.colorExpression));
        mExp.setSelectedColor(getResources().getColor(R.color.colorExpressionSelected));
    }

    private Expression getSelectedExp(final int x, final int y) {
        final DrawableExpression exp = mExp.select(x, y);
        if (exp != null) {
            Log.d(Utils.LOG_TAG, "Clicked on expression: " + exp.getExpression().symbolicExpression());
            return exp.getExpression();
        } else {
            Log.d(Utils.LOG_TAG, "Clicked out of the exp");
            return null;
        }
    }

    /**
     * GestureListener for perfoming actions whenever the user touches the view
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(final MotionEvent event) {
            Log.d(Utils.LOG_TAG, "onDown");

            mExp.clearSelection();

            mSelectedExp = getSelectedExp((int) event.getX(), (int) event.getY());

            mOnExpressionActionListener.onExpressionSelected(mSelectedExp);

            return true;
        }

        @Override
        public void onLongPress(final MotionEvent e) {
            Log.d(Utils.LOG_TAG, "onLongPress");

        }
    }

}
