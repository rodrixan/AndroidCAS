package es.uam.eps.tfg.app.tfgapp.model;

import java.util.ArrayList;
import java.util.List;


public class CASImplementation implements CASAdapter {
    List<String> expressions;

    public CASImplementation() {
        expressions = new ArrayList<>();
        expressions.add("(x+3)");
        expressions.add("(5-2)");
        expressions.add("45-3*x=23");
    }


    @Override
    public List<String> getAllExpressions() {
        return expressions;
    }

    @Override
    public String getExpression(final int i) {
        return expressions.get(i);
    }

    @Override
    public String getLeftSideOfEquation(final String exp) {
        return exp.split("=")[0];
    }

    @Override
    public String getRightSideOfEquation(final String exp) {
        return exp.split("=")[1];
    }
}
