package javax.slee.profile.query;

/**
 * The <code>SimpleQueryExpression</code> class is the base class for all dynamic
 * query expressions that perform binary operator comparisons on profile attribute
 * values.
 */
public abstract class SimpleQueryExpression extends QueryExpression {
    /**
     * Create a <code>SimpleQueryExpression</code> for the attribute with the
     * specified name.  An optional query collator may also be specified if the
     * type of the attribute being compared is <code>java.lang.String</code>.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @param collator the collator to use for the expression, or <code>null</code>
     *        if no collator is specified.
     * @throws NullPointerException if either <code>attrName</code> or
     *        <code>attrValue</code> is <code>null</code>.
     */
    protected SimpleQueryExpression(String attrName, Object attrValue, QueryCollator collator) {
        if (attrName == null) throw new NullPointerException("attrName is null");
        if (attrValue == null) throw new NullPointerException("attrValue is null");
        this.attrName = attrName;
        this.attrValue = attrValue;
        this.collator = collator;
    }

    /**
     * Get the name of the profile attribute used by this query expression.
     * @return the name of the profile attribute.
     */
    public final String getAttributeName() { return attrName; }

    /**
     * Get the value the profile attribute will be compared to.
     * @return the value the profile attribute will be compared to.
     */
    public final Object getAttributeValue() { return attrValue; }

    /**
     * Get the query collator used by this query expression.
     * @return the query collator, or <code>null</code> if one has not been
     *       specified for this query expression.
     */
    public final QueryCollator getCollator() { return collator; }


    // protected

    // javadoc copied from parent
    protected final void toString(StringBuffer buf) {
        buf.append(getAttributeName()).append(' ').append(getRelation()).append(' ').append(getAttributeValue());
        QueryCollator collator = getCollator();
        if (collator != null) buf.append(" [").append(collator).append("]");
    }

    /**
     * Get the symbol or other name for this expression relation to be used in the
     * <code>toString</code> output.
     * @return the symbol or other name for this expression relation.
     */
    protected abstract String getRelation();


    private final String attrName;
    private final Object attrValue;
    private final QueryCollator collator;
}
