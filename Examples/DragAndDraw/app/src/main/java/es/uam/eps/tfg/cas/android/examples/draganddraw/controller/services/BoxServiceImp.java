package es.uam.eps.tfg.cas.android.examples.draganddraw.controller.services;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.cas.android.examples.draganddraw.model.Box;
import es.uam.eps.tfg.cas.android.examples.draganddraw.utils.Utils;


public class BoxServiceImp implements BoxService {

    List<Box> mBoxList;

    public BoxServiceImp() {
        mBoxList = new ArrayList<>();
    }

    @Override
    public List<Box> getAll() {
        return mBoxList;
    }

    @Override
    public Box getBox(final int index) {
        return mBoxList.get(index);
    }

    @Override
    public boolean add(final Box box) {
        return mBoxList.add(box);
    }

    @Override
    public String dataToString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(mBoxList.size() + "\n");
        for (final Box b : mBoxList) {
            sb.append(b.toString() + "\n");
        }
        return sb.toString();
    }

    @Override
    public void stringToData(final String dataString) {
        mBoxList = new ArrayList<>();
        final String[] lines = dataString.split("\n");
        final int size = Integer.valueOf(lines[0]);
        for (int i = 0; i < size; i++) {
            final int lineIndex = i + 1;
            if (!lines[lineIndex].equals("")) {
                final Box box = createBoxFromString(lines[lineIndex]);
                mBoxList.add(box);
            }
        }
    }

    private Box createBoxFromString(final String line) {
        Log.d(Utils.LOG_TAG, "Line received: " + line);
        final String[] parts = line.split("\\|");

        Log.d(Utils.LOG_TAG, "origin: " + parts[0]);
        Log.d(Utils.LOG_TAG, "current: " + parts[1]);
        final String[] originString = parts[0].split(" ");
        final String[] currentString = parts[1].split(" ");

        final float originX = Float.valueOf(originString[0]);
        final float originY = Float.valueOf(originString[1]);
        final PointF origin = new PointF(originX, originY);

        final float currentX = Float.valueOf(currentString[0]);
        final float currentY = Float.valueOf(currentString[1]);
        final PointF current = new PointF(currentX, currentY);

        final Box box = new Box(origin);
        box.setCurrent(current);

        return box;
    }
}
