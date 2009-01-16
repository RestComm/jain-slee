package javax.slee.profile.query;

/**
 * The <code>Not</code> class is a composite dynamic query expression that
 * inverts the match result of its nested query expression.
 */
public final class Not extends QueryExpression {
    /**
     * Create a <code>Not</code> query expression.
     * @param expr the query expression who's match result will be inverted
     *        by this <code>Not</code> expression.
     * @throws NullPointerException if <code>expr</code> is <code>null</code>.
     */
    public Not(QueryExpression expr) {
        if (expr == null) throw new NullPointerException("expr is null");
        this.expr = expr;
    }

    /**
     * Get the query expression who's match result will be inverted by this <code>Not</code>
     * query expression.
     * @return the nested query expression.
     */
    public QueryExpression getExpression() { return expr; }


    // protected

    // javadoc copied from parent
    protected void toString(StringBuffer buf) {
        buf.append("!(");
        expr.toString(buf);
        buf.append(')');
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

        // recurse through nested expression if it's a composite expression
        QueryExpression nested = this.expr;
        if (nested instanceof CompositeQueryExpression) {
            ((CompositeQueryExpression)nested).checkForCycles(expr);
        }
        else if (nested instanceof Not) {
            ((Not)nested).checkForCycles(expr);
        }
    }


    private final QueryExpression expr;
}
