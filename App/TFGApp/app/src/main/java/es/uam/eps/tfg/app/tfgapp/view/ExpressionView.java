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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.algebraicEngine.Operation;
import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.controller.listeners.OnExpressionActionListener;
import es.uam.eps.tfg.app.tfgapp.controller.listeners.OnExpressionUpdateListener;
import es.uam.eps.tfg.app.tfgapp.util.PreferenceUtils;
import es.uam.eps.tfg.app.tfgapp.util.Utils;
import es.uam.eps.tfg.app.tfgapp.view.drawable.DrawableExpression;
import es.uam.eps.tfg.app.tfgapp.view.drawable.DrawableExpressionList;

/**
 * View for an expression from the CAS
 */
public class ExpressionView extends View implements OnExpressionUpdateListener {

    private final GestureDetector mGestureDetector;
    private final Typeface mFont;
    private DrawableExpression mExp;
    private OnExpressionActionListener mOnExpressionActionListener;
    private int mSelectedDepth;
    private int mCurrentTextSize;

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
        mCurrentTextSize = getResources().getDimensionPixelSize(R.dimen.exp_text_size);
        mExp.setTextSize(mCurrentTextSize);

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
            invalidate();
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
     * Sets a listener for doing actions every time an expression is selected
     *
     * @param onExpressionActionListener
     */
    public void setOnExpressionActionListener(final OnExpressionActionListener onExpressionActionListener) {
        mOnExpressionActionListener = onExpressionActionListener;
    }

    @Override
    public void onExpressionUpdated(final Operation exp) {
        Point coord = null;
        if (mExp != null) {
            coord = mExp.getCoordinates();
        } else {
            coord = new Point(0, 0);
        }
        final int textSize = getResources().getDimensionPixelSize(R.dimen.exp_text_size);
        mExp = new DrawableExpressionList(mFont, coord, exp, textSize);

        final int normalColor = PreferenceUtils.getExpressionColor(getContext());
        final int selectedColor = PreferenceUtils.getExpressionHighlightColor(getContext());
        mExp.setNormalColor(normalColor);
        mExp.setSelectedColor(selectedColor);
        invalidate();
    }

    /**
     * Returns the expression located at given coordinates and the depth of it
     *
     * @param x     x coordinate
     * @param y     y coordinate
     * @param depth depth of the expression returned in position 0. must be allowed by the external caller
     * @return expression located at the coordinates, null if no one was found
     */
    private Operation getSelectedExp(final int x, final int y, final int[] depth) {

        final DrawableExpression exp = mExp.select(x, y, depth);

        if (exp != null) {
            Log.d(Utils.LOG_TAG, "Clicked on expression: " + exp.getExpression().toString() + " DEPTH: " + depth[0]);

            return exp.getExpression();
        } else {
            Log.d(Utils.LOG_TAG, "Clicked out of the exp");
            return null;
        }
    }

    /**
     * GestureListener for performing actions whenever the user touches the view
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private boolean mMultiSelection = false;
        private List<Operation> mSelectedExpressions = new ArrayList<>();

        @Override
        public boolean onSingleTapUp(final MotionEvent e) {

            if (!mMultiSelection) {

                selectSingleExpression((int) e.getX(), (int) e.getY());

            } else {
                if (selectMultipleExpression((int) e.getX(), (int) e.getY(), false)) {
                    mOnExpressionActionListener.onMultipleExpressionSelected(mSelectedExpressions);
                }
            }

            return true;
        }

        /**
         * Performs a multiple selection. It will add the consequent selections to the internal list
         *
         * @param x
         * @param y
         * @param first if its the first time invoked
         * @return true if success, false if failure
         */
        private boolean selectMultipleExpression(final int x, final int y, final boolean first) {
            Log.d(Utils.LOG_TAG, "multi-selection expression: ");

            final int[] depth = {0};
            final Operation selection = getSelectedExp(x, y, depth);
            if (first) {
                mSelectedDepth = depth[0];
            }
            if (selection != null) {
                if (depth[0] != mSelectedDepth) {
                    cancelSelection();
                    Toast.makeText(getContext(), R.string.popup_multiple_selection_depth_error, Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (!mSelectedExpressions.contains(selection)) {
                    Log.d(Utils.LOG_TAG, "Added expression: " + selection.toString());

                    mSelectedExpressions.add(selection);

                } else {
                    Log.d(Utils.LOG_TAG, "Already contained: " + selection.toString());

                    mExp.clearSelection(x, y);

                    mSelectedExpressions.remove(selection);
                }
                Log.d(Utils.LOG_TAG, "Current multiple selection: " + mSelectedExpressions.toString());
                return true;
            } else {
                cancelSelection();
                return false;
            }
        }

        /**
         * Performs a single selection
         *
         * @param x
         * @param y
         */
        private void selectSingleExpression(final int x, final int y) {
            Log.d(Utils.LOG_TAG, "Single selection");
            final int[] depth = {0};
            mExp.clearSelection();
            final Operation selection = getSelectedExp(x, y, depth);
            if (selection != null) {
                Log.d(Utils.LOG_TAG, "Selected Exp: " + selection.toString());
                mOnExpressionActionListener.onSingleExpressionSelected(selection);
            } else {
                Log.d(Utils.LOG_TAG, "Selected Exp: none");
                cancelSelection();
            }
        }

        /**
         * Cancels the current selection
         */
        private void cancelSelection() {
            Log.d(Utils.LOG_TAG, "Selection cancelled");
            mMultiSelection = false;
            mExp.clearSelection();
            mSelectedExpressions = null;
            mOnExpressionActionListener.onCancelledSelectedExpression();
        }

        @Override
        public void onLongPress(final MotionEvent e) {

            Log.d(Utils.LOG_TAG, "Multiple selection started");
            final int x = (int) e.getX();
            final int y = (int) e.getY();
            mMultiSelection = true;
            mSelectedExpressions = new ArrayList<>();
            mExp.clearSelection();

            if (selectMultipleExpression(x, y, true)) {
                Toast.makeText(getContext(), R.string.popup_multiple_selection_enabled, Toast.LENGTH_SHORT).show();
                mOnExpressionActionListener.onMultipleExpressionSelected(mSelectedExpressions);
            }
        }

    }

}
