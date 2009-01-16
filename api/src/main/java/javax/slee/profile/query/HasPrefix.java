package javax.slee.profile.query;

/**
 * The <code>HasPrefix</code> class represents a dynamic query expression that
 * checks whether the value of a profile attribute is prefixed by a specified value.
 * <p>
 * This query expression can only be used with profile attributes of type
 * <code>java.lang.String</code>.
 */
public final class HasPrefix extends SimpleQueryExpression {
    /**
     * Create a <code>HasPrefix</code> query expression.  A profile will match
     * the expression criteria if the value specified by <code>attrValue</code>
     * argument is a prefix of the value of the <code>attrName</code> profile
     * attribute, as determined by {@link String#startsWith(String)}.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @throws NullPointerException if either argument is <code>null</code>.
     */
    public HasPrefix(String attrName, String attrValue) {
        super(attrName, attrValue, null);
    }

    /**
     * Create a <code>HasPrefix</code> query expression.  A profile will match
     * the expression criteria if the value specified by <code>attrValue</code>
     * argument is a prefix of the value of the <code>attrName</code> profile
     * attribute, as determined by {@link java.text.Collator#equals(String, String)},
     * where the collator is obtained from the specified <code>QueryCollator</code>.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @param collator the collator to use for the comparison.  May be <code>null</code>.
     * @throws NullPointerException if either <code>attrName</code> or
     *        <code>attrValue</code> is <code>null</code>.
     */
    public HasPrefix(String attrName, String attrValue, QueryCollator collator) {
        super(attrName, attrValue, collator);
    }


    // protected

    // javadoc copied from parent
    protected String getRelation() {
        return "has-prefix";
    }
}
