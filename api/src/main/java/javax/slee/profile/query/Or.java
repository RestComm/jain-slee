package javax.slee.profile.query;

/**
 * The <code>Or</code> class is a composite dynamic query expression that
 * successfully matches with a profile if any of the nested query expressions
 * match with the profile.
 */
public final class Or extends CompositeQueryExpression {
    /**
     * Create an <code>Or</code> query expression initially populated with the two
     * specified query expressions.
     * @param expr1 the first query expression to add.
     * @param expr2 the second query expression to add.
     * @throws NullPointerException if either argument is <code>null</code>.
     */
    public Or(QueryExpression expr1, QueryExpression expr2) {
        add(expr1);
        add(expr2);
    }

    /**
     * Create an <code>Or</code> query expression initially populated with the
     * expressions contained in the specified array.
     * @param exprs the query expressions to add.
     * @throws NullPointerException if <code>exprs</code> is <code>null</code> or
     *        contains <code>null</code> elements.
     * @throws IllegalArgumentException if the length of <code>exprs</code> is
     *        less than 2.
     */
    public Or(QueryExpression[] exprs) {
        if (exprs == null) throw new NullPointerException("exprs is null");
        if (exprs.length < 2) throw new IllegalArgumentException("length of exprs must be at least 2");
        for (int i=0; i<exprs.length; i++) add(exprs[i]);
    }

    /**
     * Add the specified query expression to this composite expression.
     * @param expr the query expression to add.
     * @return a reference to <code>this</code>.
     * @throws NullPointerException if <code>expr</code> is <code>null</code>.
     * @throws IllegalArgumentException if adding the query expression to this
     *        composite expression would generate a cyclic expression.
     */
    public Or or(QueryExpression expr) throws NullPointerException, IllegalArgumentException {
        add(expr);
        return this;
    }


    // protected

    // javadoc copied from parent
    protected void toString(StringBuffer buf) {
        QueryExpression[] exprs = getExpressions();
        for (int i=0; i<exprs.length; i++) {
            if (i>0) buf.append(" || ");
            buf.append('(');
            exprs[i].toString(buf);
            buf.append(')');
        }
    }
}
