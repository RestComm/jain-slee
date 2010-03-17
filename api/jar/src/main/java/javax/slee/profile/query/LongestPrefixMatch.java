package javax.slee.profile.query;

/**
 * The <code>LongestPrefixMatch</code> class represents a dynamic query expression that
 * determines if a profile attribute value is the longest prefix match against a
 * specified value.
 * <p>
 * This query expression can only be used with profile attributes of type
 * <code>java.lang.String</code>.
 * <p>
 * As an example of how this query expression can be used, consider a profile
 * specification that contains a <code>String</code> attribute named "prefix".
 * A number of profiles exist in a profile table as shown below:
 * <br>
 * <table align="center" border="1">
 *   <tr><th align="center">Profile name</th><th align="center">Value of <code>prefix</code> attribute</th></tr>
 *   <tr><td align="center">A</td><td align="center">1</td>
 *   <tr><td align="center">B</td><td align="center">12</td>
 *   <tr><td align="center">C</td><td align="center">123</td>
 *   <tr><td align="center">D</td><td align="center">1234</td>
 *   <tr><td align="center">E</td><td align="center">124</td>
 * </table>
 * <br>
 * The table below indicates which profiles would match using the longest prefix rule
 * given various attribute values to test:
 * <br>
 * <table align="center" border="1">
 *   <tr><th align="center">Test value</th><th align="center">Matching profile name</th></tr>
 *   <tr><td align="center">1653333</td><td align="center">A</td>
 *   <tr><td align="center">1256999</td><td align="center">B</td>
 *   <tr><td align="center">1238764</td><td align="center">C</td>
 *   <tr><td align="center">1234567</td><td align="center">D</td>
 *   <tr><td align="center">1247123</td><td align="center">E</td>
 *   <tr><td align="center">2987654</td><td align="center"><i>none</i></td>
 * </table>
 */
public final class LongestPrefixMatch extends SimpleQueryExpression {
    /**
     * Create a <code>LongestPrefixMatch</code> query expression.  A profile will match
     * the expression criteria if the value of the <code>attrName</code> attribute
     * is the longest prefix match for the value specified by <code>attrValue</code>,
     * as determined by {@link String#startsWith(String)}.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @throws NullPointerException if either argument is <code>null</code>.
     */
    public LongestPrefixMatch(String attrName, String attrValue) {
        super(attrName, attrValue, null);
    }

    /**
     * Create a <code>LongestPrefixMatch</code> query expression.  A profile will match
     * the expression criteria if the value of the <code>attrName</code> attribute
     * is the longest prefix match for the value specified by <code>attrValue</code>,
     * as determined by {@link java.text.Collator#equals(String, String)}, where the
     * collator is obtained from the specified <code>QueryCollator</code>.
     * @param attrName the name of the profile attribute to compare.
     * @param attrValue the value of the attribute to compare with.
     * @param collator the collator to use for the comparison.  May be <code>null</code>.
     * @throws NullPointerException if either <code>attrName</code> or
     *        <code>attrValue</code> is <code>null</code>.
     */
    public LongestPrefixMatch(String attrName, String attrValue, QueryCollator collator) {
        super(attrName, attrValue, collator);
    }


    // protected

    // javadoc copied from parent
    protected String getRelation() {
        return "is-longest-prefix-of";
    }
}
