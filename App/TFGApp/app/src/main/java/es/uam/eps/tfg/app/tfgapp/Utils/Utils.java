package es.uam.eps.tfg.app.tfgapp.Utils;

import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.SingleExpression;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.expressions.types.operations.MULList;
import es.uam.eps.expressions.types.operations.SUMList;

public final class Utils {
    public static final String LOG_TAG = "TFG-APP";

    private Utils() {
    }

    public static ExpressionList<Expression> createLongSampleExpression() {

        final ExpressionList<Expression> sumList = new SUMList<Expression>();

        final ExpressionList<Expression> item1 = new MULList<>();

        item1.add(new SingleExpression("a"));
        item1.add(new SingleExpression("b"));

        final ExpressionList<Expression> item2 = new MULList<>();

        item2.add(new SingleExpression("a"));
        item2.add(new SingleExpression("c"));
        item2.add(new SingleExpression("d"));

        final ExpressionList<Expression> item3 = new MULList<>();

        final ExpressionList<Expression> subItem31 = new SUMList<>();

        item3.add(new SingleExpression("a"));
        subItem31.add(new SingleExpression("e"));
        subItem31.add(new SingleExpression("f"));

        item3.add(subItem31);
        item3.add(new SingleExpression("a"));

        sumList.add(new SingleExpression("i"));
        sumList.add(item1);
        sumList.add(new SingleExpression("h"));
        sumList.add(item2);
        sumList.add(item3);
        sumList.add(new SingleExpression("g"));

        return sumList;
    }

    public static ExpressionList<Expression> createShortSampleExpression() {
        final ExpressionList<Expression> mulList = new MULList<>();

        mulList.add(new SingleExpression("a"));
        mulList.add(new SingleExpression("b"));
        mulList.add(new SingleExpression("c"));
        mulList.add(new SingleExpression("d"));
        mulList.add(new SingleExpression("e"));
        return mulList;
    }

    public static ExpressionList<Expression> createMediumSampleExpression() {
        final ExpressionList<Expression> mulList = new MULList<Expression>();

        mulList.add(new SingleExpression("a"));
        mulList.add(new SingleExpression("b"));
        mulList.add(new SingleExpression("c"));

        final ExpressionList<Expression> sumList = new SUMList<>();

        sumList.add(new SingleExpression("d"));
        sumList.add(new SingleExpression("e"));

        mulList.add(sumList);
        mulList.add(new SingleExpression("f"));

        return mulList;
    }

    public static ExpressionList<Expression> createUltraLongSampleExpression() {

        final ExpressionList<Expression> sumList = new SUMList<Expression>();

        final ExpressionList<Expression> item1 = new MULList<>();

        item1.add(new SingleExpression("a"));
        item1.add(new SingleExpression("b"));

        final ExpressionList<Expression> item2 = new MULList<>();

        item2.add(new SingleExpression("a"));
        item2.add(new SingleExpression("c"));
        item2.add(new SingleExpression("d"));

        final ExpressionList<Expression> item3 = new MULList<>();

        final ExpressionList<Expression> subItem31 = new SUMList<>();

        item3.add(new SingleExpression("a"));
        subItem31.add(new SingleExpression("e"));
        subItem31.add(new SingleExpression("f"));

        item3.add(subItem31);

        sumList.add(new SingleExpression("i"));
        sumList.add(item1);
        sumList.add(new SingleExpression("h"));
        sumList.add(item2);
        sumList.add(item3);
        sumList.add(new SingleExpression("g"));
        sumList.add(new SingleExpression("g"));
        sumList.add(new SingleExpression("g"));
        sumList.add(new SingleExpression("g"));

        return sumList;
    }

}
