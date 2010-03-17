package javax.slee.management;

import java.io.Serializable;

/**
 * The <code>DeployableUnitID</code> class encapsulate the identity of deployable
 * units installed in the SLEE.
 */
public final class DeployableUnitID implements Comparable, Serializable {
    /**
     * Create a new deployable unit identifier.
     * @param url the URL where the deployable unit was installed from.
     * @throws NullPointerException if <code>url</code> is <code>null</code>.
     */
    public DeployableUnitID(String url) {
        if (url == null) throw new NullPointerException("url is null");
        this.url = url;
    }

    /**
     * Get the URL that the deployable unit was installed from.
     * @return the URL that the deployable unit was installed from.
     */
    public final String getURL() { return url; }

    /**
     * Compare this deployable unit identifier for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a deployable unit identifier
     *        with the same URL as this, <code>false</code> otherwise.
     * @see Object#equals(Object)
     */
    public final boolean equals(Object obj) {
        return (obj == this) || (obj instanceof DeployableUnitID && compareTo(obj) == 0);
    }

    /**
     * Get a hash code value for this deployable unit identifier.
     * @return a hash code value for this identifier.
     * @see Object#hashCode()
     */
    public final int hashCode() {
        return url.hashCode();
    }

    /**
     * Get a string representation for this deployable unit identifier.
     * @return a string representation for this identifier.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("DeployableUnitID[url=").append(url).append(']');
        return buf.toString();
    }

    /**
     * Compare this deployable unit identifier with the specified object for order.
     * Returns a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * <p>
     * Deployable unit ordering is determined by the <code>java.lang.String</code>
     * ordering of the url attributes of <code>this</code> and <code>obj</code>.
     * @param obj the object to compare this with.
     * @return a negative integer, zero, or a positive integer if this deployable
     *        unit identifier is considered less than, equal to, or greater than
     *        the specified object.
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Object obj) {
        return url.compareTo(((DeployableUnitID)obj).url);
    }

    private final String url;
}

