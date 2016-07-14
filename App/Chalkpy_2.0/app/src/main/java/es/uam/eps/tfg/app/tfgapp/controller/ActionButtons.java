package es.uam.eps.tfg.app.tfgapp.controller;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;

/**
 * Buttons that perform the CAS allowed actions
 */
public class ActionButtons implements View.OnLongClickListener {

    private static final int[] IDS = {
            R.id.button_exp_associate,
            R.id.button_exp_change_side,
            R.id.button_exp_disassociate,
            R.id.button_exp_move_left,
            R.id.button_exp_move_right,
            R.id.button_exp_operate
    };

    private final ImageButton mChangeSide;
    private final ImageButton mMoveRight;
    private final ImageButton mMoveLeft;
    private final ImageButton mAssociate;
    private final ImageButton mDisassociate;
    private final ImageButton mOperate;
    private final Context mContext;

    public ActionButtons(final View rootView, final Context context) {
        mChangeSide = (ImageButton) rootView.findViewById(R.id.button_exp_change_side);
        mMoveRight = (ImageButton) rootView.findViewById(R.id.button_exp_move_right);
        mMoveLeft = (ImageButton) rootView.findViewById(R.id.button_exp_move_left);
        mAssociate = (ImageButton) rootView.findViewById(R.id.button_exp_associate);
        mDisassociate = (ImageButton) rootView.findViewById(R.id.button_exp_disassociate);
        mOperate = (ImageButton) rootView.findViewById(R.id.button_exp_operate);
        mContext = context;
    }

    public void setListeners(final View.OnClickListener listener) {

        setOnLongClickListener();
        setOnClickListener(listener);

    }

    private void setOnLongClickListener() {
        mChangeSide.setOnLongClickListener(this);
        mMoveRight.setOnLongClickListener(this);
        mMoveLeft.setOnLongClickListener(this);
        mAssociate.setOnLongClickListener(this);
        mDisassociate.setOnLongClickListener(this);
        mOperate.setOnLongClickListener(this);

    }

    private void setOnClickListener(final View.OnClickListener listener) {

        mChangeSide.setOnClickListener(listener);
        mMoveRight.setOnClickListener(listener);
        mMoveLeft.setOnClickListener(listener);
        mAssociate.setOnClickListener(listener);
        mDisassociate.setOnClickListener(listener);
        mOperate.setOnClickListener(listener);

    }

    /**
     * Returns the CAS action associated to a given button
     *
     * @param id viewId of the button
     * @return the associated CAS action
     */
    public CASAdapter.Actions getAction(final int id) {
        switch (id) {
            case R.id.button_exp_change_side:
                return CASAdapter.Actions.CHANGE_SIDE;

            case R.id.button_exp_move_right:
                return CASAdapter.Actions.MOVE_RIGHT;

            case R.id.button_exp_move_left:
                return CASAdapter.Actions.MOVE_LEFT;

            case R.id.button_exp_associate:
                return CASAdapter.Actions.ASSOCIATE;

            case R.id.button_exp_disassociate:
                return CASAdapter.Actions.DISASSOCIATE;

            case R.id.button_exp_operate:
                return CASAdapter.Actions.OPERATE;

            default:
                return null;
        }
    }

    @Override
    public boolean onLongClick(final View view) {
        if (view instanceof ImageButton) {
            final String msg = view.getContentDescription().toString();
            showActionDescription(msg);
            return true;
        }
        return false;
    }

    private void showActionDescription(final String msg) {
        final Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 100);
        toast.show();
    }

    public void enableAll() {
        for (final int id : IDS) {
            enable(id);
        }
    }

    public void enable(final int buttonId) {
        getButton(buttonId).setEnabled(true);
    }

    public ImageButton getButton(final int id) {
        switch (id) {
            case R.id.button_exp_change_side:
                return mChangeSide;

            case R.id.button_exp_move_right:
                return mMoveRight;

            case R.id.button_exp_move_left:
                return mMoveLeft;

            case R.id.button_exp_associate:
                return mAssociate;

            case R.id.button_exp_disassociate:
                return mDisassociate;

            case R.id.button_exp_operate:
                return mOperate;

            default:
                return null;
        }
    }

    public void disableAll() {
        for (final int id : IDS) {
            disable(id);
        }
    }

    public void disable(final int buttonId) {
        getButton(buttonId).setEnabled(false);
    }
}
