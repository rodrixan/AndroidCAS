package es.uam.eps.tfg.cas.android.examples.draganddraw.controller.services;


import java.util.List;

import es.uam.eps.tfg.cas.android.examples.draganddraw.model.Box;

public interface BoxService {
    List<Box> getAll();

    Box getBox(int index);

    boolean add(Box box);

    String dataToString();

    void stringToData(String dataString);
}
