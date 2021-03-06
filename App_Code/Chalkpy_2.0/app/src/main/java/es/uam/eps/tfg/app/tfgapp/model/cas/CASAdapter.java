package es.uam.eps.tfg.app.tfgapp.model.cas;

import java.util.List;
import java.util.UUID;

import es.uam.eps.tfg.algebraicEngine.Operation;
import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.exception.NotApplicableReductionException;

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
     * Gets an operation given an id
     *
     * @param id id of the operation
     * @return the operation whose id is the given one
     */
    Operation getOperationById(UUID id);

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
     * Given an expression, it returns the operator symbol for the parent expression as a string
     *
     * @param exp expression for knowing its parent operator symbol
     * @return the string representation of the operator, null if the expression's parent is not an operation
     */
    String getParentStringOperatorSymbol(Operation exp);

    /**
     * Given an expression, returns the operator symbol for the grandparent expression as a string
     *
     * @param exp expression for knowing its grandparent symbol expression
     * @return string representation of the symbol
     */
    String getGrandParentStringOperatorSymbol(Operation exp);

    /**
     * Given an expression, returns its infix representation
     *
     * @param exp operation to get the infix representation
     * @return infix notation of the expression.
     */
    String getSymbolStringExpression(Operation exp);

    /**
     * Commutes an element one position to the right or left
     *
     * @param elementToCommute element to commute
     * @param leftOrRight      commute to left or right
     * @return new expression with the element commuted
     * @throws NotApplicableReductionException
     */
    Operation commutativeProperty(Operation elementToCommute, Actions leftOrRight) throws NotApplicableReductionException;

    /**
     * Associate the elements between two limits
     *
     * @param startElem  starting element to associate
     * @param endElement ending element to associate
     * @return new expression with associated elements
     * @throws NotApplicableReductionException
     */
    Operation associativeProperty(Operation startElem, Operation endElement) throws NotApplicableReductionException;

    /**
     * Dissociates a previous associated subexpression
     *
     * @param elementToDissociate expression to dissociate
     * @return new expression with the dissociated element
     * @throws NotApplicableReductionException
     */
    Operation dissociativeProperty(Operation elementToDissociate) throws NotApplicableReductionException;


    Operation operate(Operation selection) throws NotApplicableReductionException;

    Operation commonFactor(List<Operation> commonElems) throws NotApplicableReductionException;

    Operation changeSide(Operation selection) throws NotApplicableReductionException;

    Operation distribute(Operation elemToDistribute, Operation sumOperation) throws NotApplicableReductionException;


    boolean isOnDistributiveForm(final Operation singleElem, final Operation sumOperation);

    /**
     * @return list of sample expressions as strings
     */
    List<String> getSampleExpressions();

    /**
     * Creates an Operation from its string representation
     *
     * @param CASExpression string in the CAS representation form
     * @return
     */
    Operation createOperationFromString(String CASExpression);

    /**
     * Available actions to perform
     */
    enum Actions {
        CHANGE_SIDE(R.string.action_change_side), MOVE_LEFT(R.string.action_move_left),
        MOVE_RIGHT(R.string.action_move_right),
        ASSOCIATE(R.string.action_associate), DISASSOCIATE(R.string.action_disassociate),
        OPERATE(R.string.action_operate), DISTRIBUTE(R.string.action_distribute),
        COMMON_FACTOR(R.string.action_common_factor);

        private final int mStringCode;

        Actions(final int stringCode) {
            mStringCode = stringCode;
        }

        public int getStringCode() {
            return mStringCode;
        }
    }

}
