package es.uam.eps.tfg.app.tfgapp.model;


import es.uam.eps.expressions.types.interfaces.Expression;

public interface CASAdapter {


    void initCAS(Expression exp);

    Expression getCurrentExpression();

}
