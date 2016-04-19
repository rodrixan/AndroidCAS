package es.uam.eps.tfg.cas.android.examples.testapp.model;


import java.util.List;

public interface CASAdapter {
    List<String> getAllExpressions();

    String getExpression(int i);

    String getLeftSideOfEquation(final String exp);

    String getRightSideOfEquation(final String exp);
}
