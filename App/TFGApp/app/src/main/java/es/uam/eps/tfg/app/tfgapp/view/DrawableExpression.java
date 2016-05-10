package es.uam.eps.tfg.app.tfgapp.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;

/**
 *
 */
public class DrawableExpression extends DrawableElement {

    private static final float DEFAULT_TEXTSIZE = 100f;

    private String mText;


    public DrawableExpression(final Typeface font, final float textSize, final String text) {
        super();
        mPaint.setTypeface(font);
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mText = text;
        updateBounds();
    }

    public DrawableExpression(final Typeface font, final float textSize, final PointF coordinates, final String text) {
        this(font, textSize, text);
        updateCoordinates(coordinates);
    }

    public DrawableExpression(final String text) {
        this(null, text);
    }

    public DrawableExpression(final Typeface font, final String text) {
        this(font, DEFAULT_TEXTSIZE, text);
    }

    public DrawableExpression(final Typeface font, final PointF coordinates, final String text) {
        this(font, DEFAULT_TEXTSIZE, coordinates, text);
    }

    @Override
    public void updateCoordinates(final float x, final float y) {
        this.x = x;
        this.y = y;
        updateBounds(x, y);
    }

    private void updateBounds(final float x, final float y) {
        updateBounds();
        mRectContainer.offset((int) (x - width() / 2), (int) y);
    }

    private void updateBounds() {
        mPaint.getTextBounds(mText, 0, mText.length(), mRectContainer);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        canvas.drawText(mText, x, y, mPaint);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(mRectContainer, paint);
    }

    public void setText(final String text) {
        mText = text;
        updateBounds();
    }

    public void setTextSize(final float size) {
        mPaint.setTextSize(size);
        updateBounds();
    }

    public void setFont(final Typeface font) {
        mPaint.setTypeface(font);
        updateBounds();
    }
}
