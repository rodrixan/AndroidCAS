package es.uam.eps.tfg.cas.android.examples.testapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ExpressionView extends RelativeLayout {

    public ExpressionView(final Context context) {
        super(context);
    }

    public ExpressionView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpressionView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ExpressionView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
