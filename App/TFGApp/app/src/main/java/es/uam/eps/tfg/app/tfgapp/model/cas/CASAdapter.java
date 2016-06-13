package es.uam.eps.tfg.app.tfgapp.model.cas;

import java.util.List;

import es.uam.eps.expressions.types.interfaces.Expression;

public interface CASAdapter {
    void initCAS(Expression exp);

    Expression getCurrentExpression();

    Expression commuteProperty(Expression mainExp, int iniPos, int finalPos);

    Expression associativeProperty(Expression mainExp, int from, int to);

    Expression dissociativeProperty(Expression mainExp, int elemPos);

    Expression operate(Expression mainExp, int elemPos);

    Expression operate(Expression mainExp, int elemPos1, int elemPos2);

    Expression operate(Expression mainExp, int... elemPos);

    Expression dropElement(Expression mainExp, int elemPos);

    Expression moveMember(Expression equation, int elemPos);

    List<Expression> getSampleExpressions();

    enum Actions {SELECT_SINGLE, SELECT_MULTIPLE, CHANGE_SIDE, MOVE_LEFT, MOVE_RIGHT, DELETE, ASSOCIATE, DISASSOCIATE, OPERATE}

}
