package javax.slee.profile.query;

import java.io.Serializable;

/**
 * The <code>QueryExpression</code> class is the base class for all dynamic query
 * expressions.
 */
public abstract class QueryExpression implements Serializable {
    /**
     * Get a string representation for this query expression.
     * @return a string representation for this query expression.
     */
    public final String toString() {
        StringBuffer buf = new StringBuffer();
        toString(buf);
        return buf.toString();
    }


    // protected

    /**
     * Get a string representation for this query expression.
     * @param buf a string buffer the string representation should be appended to.
     */
    protected abstract void toString(StringBuffer buf);
}
