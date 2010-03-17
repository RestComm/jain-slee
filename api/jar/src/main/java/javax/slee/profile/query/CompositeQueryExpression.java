package javax.slee.profile.query;

import java.util.ArrayList;

/**
 * The <code>CompositeQueryExpression</code> class is the base class for all dynamic
 * query expressions that are containers of other query expressions.
 */
public abstract class CompositeQueryExpression extends QueryExpression {
    /**
     * Get the query expressions that have been added to this composite expression.
     * @return the query expressions that have been added to this composite expression.
     */
    public final QueryExpression[] getExpressions() {
        return (QueryExpression[])exprs.toArray(new QueryExpression[exprs.size()]);
    }


    // protected

    /**
     * Add a query expression to this composite expression.
     * @param expr the expression to add.
     * @throws NullPointerException if <code>expr</code> is <code>null</code>.
     * @throws IllegalArgumentException if adding the query expression to this
     *        composite expression would generate a cyclic expression.
     */
    protected final void add(QueryExpression expr) throws NullPointerException, IllegalArgumentException {
        if (expr == null) throw new NullPointerException("expr is null");

        // check for cycles
        if (expr instanceof CompositeQueryExpression) {
            ((CompositeQueryExpression)expr).checkForCycles(this);
        }
        else if (expr instanceof Not) {
            ((Not)expr).checkForCycles(this);
        }

        // no cycles, so add the expression to the list
        exprs.add(expr);
    }


    // package

    /**
     * Check whether the specified expression contains a reference, either direct
     * or indirect, to this expression.
     * @param expr the expression to check and possibly recurse through.
     * @throws IllegalArgumentException if a cyclic expression is detected.
     */
    void checkForCycles(QueryExpression expr) throws IllegalArgumentException {
        // is the expression argument equal to this?
        if (expr == this) throw new IllegalArgumentException("Cyclic expression detected");

        // recurse through all nested expressions that are composite expressions
        for (int i=0; i<exprs.size(); i++) {
            QueryExpression nested = (QueryExpression)exprs.get(i);
            if (nested instanceof CompositeQueryExpression) {
                ((CompositeQueryExpression)nested).checkForCycles(expr);
            }
            else if (nested instanceof Not) {
                ((Not)nested).checkForCycles(expr);
            }
        }
    }


    private final ArrayList exprs = new ArrayList();
}
