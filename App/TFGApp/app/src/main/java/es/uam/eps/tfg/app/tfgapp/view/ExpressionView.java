package es.uam.eps.tfg.app.tfgapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import es.uam.eps.tfg.app.tfgapp.Utils.Utils;
import es.uam.eps.tfg.app.tfgapp.view.drawable.DrawableExpression;
import es.uam.eps.tfg.app.tfgapp.view.drawable.DrawableExpressionList;

public class ExpressionView extends View {

    private static final String FONT_PATH = "fonts/lmromanslant10-regular-ExpView.otf";

    private final GestureDetector mGestureDetector;
    private final DrawableExpression mExp;
    //    private final Paint mCircle;
//    private final RectF mCircleRect;
//    private final Paint mSquare;
//    private final RectF mSqareRect;
//    private final PointF mCoords;
    private int mHeight;
    private int mWidth;
    private boolean mSelected = false;

    public ExpressionView(final Context context) {
        this(context, null, 0);
    }

    public ExpressionView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Typeface font = Typeface.createFromAsset(context.getAssets(), FONT_PATH);

        mGestureDetector = new GestureDetector(context, new MyGestureListener());
//        mCoords = new PointF(0, 0);
//        mCircleRect = new RectF(mCoords.x - 50, mCoords.y + 50, mCoords.x + 50, mCoords.y - 50);
//        Log.d("APP_TEST", "RectCirculo: " + mCircleRect);
//        mCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mCircle.setColor(getResources().getColor(R.color.colorAccent));
//        mCircle.setStyle(Paint.Style.FILL);
//
//        mSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mSquare.setStyle(Paint.Style.STROKE);
//        mSquare.setColor(getResources().getColor(R.color.colorPrimaryDark));
//        mSquare.setStrokeWidth(5f);
//        mSqareRect = new RectF(100, 100, 200, 200);

        mExp = new DrawableExpressionList(font, Utils.createUltraLongSampleExpression());
        Log.d("APP_TEST", "Main exp position: " + mExp.x() + " " + mExp.y());
    }

    public ExpressionView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        mWidth = w;
        mHeight = h;
        if (w / 2 == 952.0)
            Log.d("APP_TEST", "Main exp position: " + mExp.x() + " " + mExp.y());
        mExp.updateCoordinates(w / 2, h / 2);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d("APP_TEST", "Soltado en posicion: " + event.getX() + "," + event.getY());
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
//        canvas.drawCircle(mCoords.x, mCoords.y, 50f, mCircle);
//        canvas.drawRect(mCircleRect, mSquare);
//        canvas.drawRect(mSqareRect, mSquare);

        mExp.onDraw(canvas);

//        final Paint mPaint = new Paint();
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setTextSize(60);
//        mPaint.setTextAlign(Paint.Align.CENTER);
//        final Typeface font = Typeface.createFromAsset(getContext().getAssets(), FONT_PATH);
//        mPaint.setTypeface(font);
//        final int x = mWidth / 2;
//        final int y = mHeight / 2;
//        final String text = "j";
//        canvas.drawText(text, x, y, mPaint);
//        final Rect rect = new Rect();
//        final Rect container = new Rect();
//        mPaint.getTextBounds(text, 0, text.length(), rect);
//        container.left = x - rect.width() / 2;
//        container.right = x + rect.width() / 2;
//        container.top = y - rect.height();
//        container.bottom = y;
//        mPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(container, mPaint);
//        Log.d("APP_TEST", "Main exp position: " + mExp.x() + " " + mExp.y());
//        Log.d("APP_TEST", "Main exp left/right: " + mExp.left() + " " + mExp.right());

    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(final MotionEvent event) {
            Log.d("APP_TEST", "onDown");
//            Log.d("APP_TEST", "Click en:" + event.getX() + "," + event.getY() + "; circulo en " + mCoords.x + "," + mCoords.y);
//            if (!mCircleRect.contains(event.getX(), event.getY())) {
//                Log.d("APP_TEST", "la posicion no esta dentro de " + mCircleRect);
//                mSelected = false;
//            } else {
//                mSelected = true;
//            }
            if (!mExp.contains((int) event.getX(), (int) event.getY())) {
//                Log.d("APP_TEST", "la posicion no esta dentro de " + mCircleRect);
                mSelected = false;
            } else {
                mSelected = false;
            }
            return true;
        }

        @Override
        public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
            Log.d("APP_TEST", "Posicion inicial: " + e1.getX() + "," + e1.getY() + " - Posicion actual: " + e2.getX() + "," + e2.getY());

            if (mSelected && mExp.isValidPosition(e2.getX(), e2.getY(), new Rect(0, 0, mWidth, mHeight))) {
                mExp.updateCoordinates((int) e2.getX(), (int) e2.getY());
            }

//            if (mSelected && isCircleInside(new float[]{e2.getX(), e2.getY()})) {
//                mCoords.set(e2.getX(), e2.getY());
//                mCircleRect.set(mCoords.x - 50, mCoords.y - 50, mCoords.x + 50, mCoords.y + 50);
//            }
            return true;
        }

        private boolean isCircleInside(final float[] pos) {
            final float x = pos[0];
            final float y = pos[1];
            if (x - 50 < 0 || y - 50 < 0) {
                return false;
            }
            if (x + 50 > mWidth || y + 50 > mHeight) {
                return false;
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(final MotionEvent event) {
            Log.d("APP_TEST", "onDoubleTap");
            return true;
        }

        @Override
        public boolean onFling(final MotionEvent event1, final MotionEvent event2,
                               final float velocityX, final float velocityY) {
            Log.d("APP_TEST", "onFling");
            return true;
        }
    }
}
