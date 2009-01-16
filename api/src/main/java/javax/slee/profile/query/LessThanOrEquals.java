package javax.slee.profile.query;

/**
 * The <code>LessThanOrEquals</code> class represents a dynamic query expression
 * that checks whether the value of a profile attribute is less than or equal to a
 * specified value.
 * <p>
 * This query expression can only be used with profile attributes whose class
 * implements the {@link Comparable} interface.
 */
public final class LessThanOrEquals extends OrderedQueryExpression {
    /**
     * Create a <code>LessThanOrEquals</code> query expression.  A profile will match
     * the expression criteria if the value of the <code>attrName</code> attribute is
     * less than or equal to <code>attrValue</code>, as determined by
     * {@link Comparable#compareTo(Object)}.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws IllegalArgumentException if the class of <code>attrValue</code> does
     *        not implement the <code>java.lang.Comparable</code> interface.
     */
    public LessThanOrEquals(String attrName, Object attrValue) throws NullPointerException {
        super(attrName, attrValue, null);
    }

    /**
     * Create a <code>LessThanOrEquals</code> query expression.  A profile will match
     * the expression criteria if the value of the <code>attrName</code> attribute is
     * less than or equal to <code>attrValue</code>, as determined by
     * {@link java.text.Collator#compare(String, String)}, where the collator is obtained
     * from the specified <code>QueryCollator</code>.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @param collator the collator to use for the comparison.  May be <code>null</code>.
     * @throws NullPointerException if either <code>attrName</code> or
     *        <code>attrValue</code> is <code>null</code>.
     */
    public LessThanOrEquals(String attrName, String attrValue, QueryCollator collator) throws NullPointerException {
        super(attrName, attrValue, collator);
    }


    // protected

    // javadoc copied from parent
    protected String getRelation() {
        return "<=";
    }
}
