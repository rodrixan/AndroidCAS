package es.uam.eps.tfg.app.tfgapp.model;


import java.util.List;

public interface CASAdapter {
    List<String> getAllExpressions();

    String getExpression(int i);

    String getLeftSideOfEquation(final String exp);

    String getRightSideOfEquation(final String exp);
}
