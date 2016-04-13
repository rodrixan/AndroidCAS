package es.uam.eps.tfg.cas.android.examples.draganddraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import es.uam.eps.tfg.cas.android.examples.draganddraw.controller.services.BoxService;
import es.uam.eps.tfg.cas.android.examples.draganddraw.controller.services.BoxServiceImp;
import es.uam.eps.tfg.cas.android.examples.draganddraw.model.Box;
import es.uam.eps.tfg.cas.android.examples.draganddraw.utils.Utils;

public class BoxDrawingView extends View {

    private static final String SAVED_PARENT_STATE = "parent state";
    private static final String SAVED_BOXES_STATE = "boxes state";

    private Box mCurrentBox;
    private final BoxService mBoxService = new BoxServiceImp();
    private final Paint mBoxPaint;
    //private Paint mBgdPaint;


    //creating in code
    public BoxDrawingView(final Context context) {
        this(context, null);
    }

    //inflating from xml
    public BoxDrawingView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        //paint box
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        //paint background
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();

        bundle.putParcelable(SAVED_PARENT_STATE, super.onSaveInstanceState());
        bundle.putString(SAVED_BOXES_STATE, mBoxService.dataToString());

        Log.d(Utils.LOG_TAG, "Saving boxes:[" + mBoxService.dataToString() + "]");

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        Log.d(Utils.LOG_TAG, "Restoring boxes:[" + ((Bundle) state).getString(SAVED_BOXES_STATE) + "]");
        mBoxService.stringToData(((Bundle) state).getString(SAVED_BOXES_STATE));
        super.onRestoreInstanceState(((Bundle) state).getParcelable(SAVED_PARENT_STATE));
        invalidate();
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";

                mCurrentBox = new Box(current);
                mBoxService.add(mCurrentBox);

                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
            default:
                break;
        }
        Log.i(Utils.LOG_TAG, action + " at x= " + current.x + ", y= " + current.y);

        return true;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        for (final Box box : mBoxService.getAll()) {
            final float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            final float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            final float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            final float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }
}
