package javax.slee.profile.query;

/**
 * The <code>RangeMatch</code> class represents a dynamic query expression that checks
 * whether the value of a profile attribute lies within a specified range.  A profile
 * matches the expression if the profile attribute value is greater than or equal to
 * the lower bound value and less than or equal to the upper bound value.
 * <p>
 * This query expression can only be used with profile attributes whose class
 * implements the {@link Comparable} interface.
 */
public final class RangeMatch extends QueryExpression {
    /**
     * Create a <code>RangeMatch</code> query expression.  A profile will match the
     * expression criteria if the value of the <code>attrName</code> attribute is
     * greater than or equal to <code>fromValue</code> and less than or equal to
     * <code>toValue</code>, as determined by {@link Comparable#compareTo(Object)}.
     * @param attrName the name of the profile attribute to compare.
     * @param fromValue the lower bound of the range.
     * @param toValue the upper bound of the range.
     * @throws NullPointerException if any argument is <code>null</code>.
     * @throws IllegalArgumentException if the class of <code>fromValue</code> or
     *        <code>toValue</code> does not implement the <code>java.lang.Comparable</code>
     *        interface.
     */
    public RangeMatch(String attrName, Object fromValue, Object toValue) {
        if (attrName == null) throw new NullPointerException("attrName is null");
        if (fromValue == null) throw new NullPointerException("fromValue is null");
        if (toValue == null) throw new NullPointerException("toValue is null");

        // implement in terms of other expressions
        lowerBound = new GreaterThanOrEquals(attrName, fromValue);
        upperBound = new LessThanOrEquals(attrName, toValue);
    }

    /**
     * Create a <code>RangeMatch</code> query expression.  A profile will match the
     * expression criteria if the value of the <code>attrName</code> attribute is
     * greater than or equal to <code>fromValue</code> and less than or equal to
     * <code>toValue</code>, as determined by {@link java.text.Collator#compare(String, String)},
     * where the collator is obtained from the specified <code>QueryCollator</code>.
     * @param attrName the name of the profile attribute to compare.
     * @param fromValue the lower bound of the range.
     * @param toValue the upper bound of the range.
     * @param collator the collator to use for the comparison.  May be <code>null</code>.
     * @throws NullPointerException if <code>attrName</code>, <code>fromValue</code>,
     *        or <code>toValue</code> is <code>null</code>.
     * @throws IllegalArgumentException if the class of <code>fromValue</code> or
     *        <code>toValue</code> does not implement the <code>java.lang.Comparable</code>
     *        interface.
     */
    public RangeMatch(String attrName, String fromValue, String toValue, QueryCollator collator) {
        if (fromValue == null) throw new NullPointerException("fromValue is null");
        if (toValue == null) throw new NullPointerException("toValue is null");

        // implement in terms of other expressions
        lowerBound = new GreaterThanOrEquals(attrName, fromValue, collator);
        upperBound = new LessThanOrEquals(attrName, toValue, collator);
    }

    /**
     * Get the name of the profile attribute used by this query expression.
     * @return the name of the profile attribute.
     */
    public String getAttributeName() {
        return lowerBound.getAttributeName();
    }

    /**
     * Get the lower-bound value the profile attribute will be compared to.
     * @return the lower-bound value the profile attribute will be compared to.
     */
    public Object getFromValue() {
        return lowerBound.getAttributeValue();
    }

    /**
     * Get the upper-bound value the profile attribute will be compared to.
     * @return the upper-bound value the profile attribute will be compared to.
     */
    public Object getToValue() {
        return upperBound.getAttributeValue();
    }

    /**
     * Get the query collator used by this query expression.
     * @return the query collator, or <code>null</code> if one has not been
     *       specified for this query expression.
     */
    public QueryCollator getCollator() { return lowerBound.getCollator(); }


    // protected

    // javadoc copied from parent
    protected void toString(StringBuffer buf) {
        buf.append(lowerBound.getAttributeValue()).append(" <= ").
            append(getAttributeName()).append(" <= ").append(upperBound.getAttributeValue());
    }


    private final GreaterThanOrEquals lowerBound;
    private final LessThanOrEquals upperBound;
}
