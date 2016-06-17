package es.uam.eps.tfg.app.tfgapp.model.cas;

import java.util.List;

import es.uam.eps.tfg.algebraicEngine.Operation;

/**
 * Union point between the controller and the CAS
 */
public interface CASAdapter {

    /**
     * Initializes the CAS with a new expression. All changes in the last one will be lost.
     *
     * @param exp new expression to be consider the main one
     */
    void initCAS(String exp);

    /**
     * @return the current expression
     */
    Operation getCurrentExpression();

    /**
     * Given an expression, it returns the operator symbol as a string
     *
     * @param exp expression for knowing its operator symbol
     * @return the string representation of the operator, null if the expression is not an operation
     */
    String getStringOperatorSymbol(Operation exp);

    /**
     * Given an expression, it returns the operator symbol fo the parent expression as a string
     *
     * @param exp expression for knowing its parent operator symbol
     * @return the string representation of the operator, null if the expression's parent is not an operation
     */
    String getParentStringOperatorSymbol(Operation exp);

    /**
     * Given an expression, returns its infix representation
     *
     * @param exp operation to get the infix representation
     * @return infix notation of the expression.
     */
    String getSymbolicExpression(Operation exp);


    /**
     * TODO: implement
     */
    Operation commuteProperty(Operation elementToCommute, Actions leftOrRight);

    Operation associativeProperty(Operation startElem, Operation endElement);

    Operation dissociativeProperty(Operation elementToDissociate);

    /**
     * TODO implement
     */

//
//    Expression operate(Expression mainExp, int elemPos);
//
//    Expression operate(Expression mainExp, int elemPos1, int elemPos2);
//
//    Expression operate(Expression mainExp, int... elemPos);
//
//    Expression dropElement(Expression mainExp, int elemPos);
//
//    Expression moveMember(Expression equation, int elemPos);
//

    List<String> getSampleExpressions();

    /**
     * Available actions to perform
     */
    enum Actions {
        SELECT_SINGLE, SELECT_MULTIPLE, CHANGE_SIDE, MOVE_LEFT, MOVE_RIGHT, DELETE, ASSOCIATE, DISASSOCIATE, OPERATE
    }

}
