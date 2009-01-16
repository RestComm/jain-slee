package javax.slee.profile.query;

/**
 * The <code>OrderedQueryExpression</code> class is the base class for all dynamic
 * query expressions that perform direct binary operator comparisons on profile
 * attributes values where the type of the profile attribute type exhibits a notion
 * of total order.
 */
public abstract class OrderedQueryExpression extends SimpleQueryExpression {
    /**
     * Create an <code>OrderedQueryExpression</code> for the attribute with the
     * specified name and value.  An optional query collator may also be specified
     * if the type of the attribute being compared is <code>java.lang.String</code>.
     * @param attrName the name of the profile attribute.
     * @param attrValue the value of the attribute to compare with.
     * @param collator the collator to use for the comparison.  May be <code>null</code>.
     * @throws NullPointerException if either <code>attrName</code> or <code>attrValue</code>
     *        is <code>null</code>.
     * @throws IllegalArgumentException if the class of <code>attrValue</code> does
     *        not implement the <code>java.lang.Comparable</code> interface.
     */
    protected OrderedQueryExpression(String attrName, Object attrValue, QueryCollator collator) throws NullPointerException {
        super(attrName, attrValue, collator);

        if (!(attrValue instanceof Comparable))
            throw new IllegalArgumentException("Attribute value class " + attrValue.getClass().getName()
                + " does not implement the java.lang.Comparable interface");
    }
}
