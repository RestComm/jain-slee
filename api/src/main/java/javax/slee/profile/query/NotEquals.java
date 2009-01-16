package javax.slee.profile.query;

/**
 * The <code>NotEquals</code> class represents a dynamic query expression that checks for
 * for inequality between a profile attribute and a specified value.
 */
public final class NotEquals extends SimpleQueryExpression {
    /**
     * Create a <code>NotEquals</code> query expression.  A profile will match the
     * expression criteria if the value of the <code>attrName</code> attribute
     * is not equal to <code>attrValue</code>, as determined by {@link Object#equals(Object)}.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @throws NullPointerException if either argument is <code>null</code>.
     */
    public NotEquals(String attrName, Object attrValue) throws NullPointerException {
        super(attrName, attrValue, null);
    }

    /**
     * Create a <code>NotEquals</code> query expression.  A profile will match the
     * expression criteria if the value of the <code>attrName</code> attribute
     * is not equal to <code>attrValue</code>, as determined by {@link java.text.Collator#equals(String, String)},
     * where the collator is obtained from the specified <code>QueryCollator</code>.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @param collator the collator to use for the comparison.  May be <code>null</code>.
     * @throws NullPointerException if either <code>attrName</code> or
     *        <code>attrValue</code> is <code>null</code>.
     */
    public NotEquals(String attrName, String attrValue, QueryCollator collator) throws NullPointerException {
        super(attrName, attrValue, collator);
    }


    // protected

    // javadoc copied from parent
    protected String getRelation() {
        return "!=";
    }
}
