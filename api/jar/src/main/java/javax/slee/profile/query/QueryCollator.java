package javax.slee.profile.query;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

/**
 * The <code>QueryCollator</code> class describes a collator that can be
 * used in dynamic profile queries in a Java serializable form.
 */
public final class QueryCollator implements Serializable {
    /**
     * Create a collator identified by locale only.  The collator uses the
     * default strength and decomposition for the locale.
     * @param locale the locale for which the collator should be obtained.
     * @throws NullPointerException if <code>locale</code> is <code>null</code>.
     */
    public QueryCollator(Locale locale) {
        this(locale, false, 0, false, 0);
    }

    /**
     * Create a collator for a given locale using the specified strength.
     * The collator uses the default decomposition for the locale.
     * @param locale the locale for which the collator should be obtained.
     * @param strength the strength of collator comparisions.  This must
     *        be one of {@link Collator#PRIMARY}, {@link Collator#SECONDARY},
     *        {@link Collator#TERTIARY}, or {@link Collator#IDENTICAL}.
     * @throws NullPointerException if <code>locale</code> is <code>null</code>.
     */
    public QueryCollator(Locale locale, int strength) {
        this(locale, true, strength, false, 0);
    }

    /**
     * Create a collator for a given locale using the specified strength
     * and decomposition.
     * @param locale the locale for which the collator should be obtained.
     * @param strength the strength of collator comparisions.  This must
     *        be one of {@link Collator#PRIMARY}, {@link Collator#SECONDARY},
     *        {@link Collator#TERTIARY}, or {@link Collator#IDENTICAL}.
     * @param decomposition the decomposition mode to use in the collator.
     *        This must be one of {@link Collator#NO_DECOMPOSITION},
     *        {@link Collator#CANONICAL_DECOMPOSITION}, or
     *        {@link Collator#FULL_DECOMPOSITION}.
     * @throws NullPointerException if <code>locale</code> is <code>null</code>.
     */
    public QueryCollator(Locale locale, int strength, int decomposition) {
        this(locale, true, strength, true, decomposition);
    }

    /**
     * Get the locale for this collator.
     * @return the locale for this collator.
     */
    public Locale getLocale() { return locale; }

    /**
     * Determine whether a collator strength has been defined for this collator.
     * @return <code>true<code> if a collator strength has been defined for this
     *        collator, <code>false</code> otherwise.
     */
    public boolean hasStrength() { return hasStrength; }

    /**
     * Get the collator strength specified for this collator.  The return value
     * only has meaning if <code>{@link #hasStrength()} == true</code>.
     * @return the collator strength specified for this collator.
     */
    public int getStrength() { return strength; }

    /**
     * Determine whether a decomposition mode has been defined for this collator.
     * @return <code>true<code> if a decomposition mode has been defined for this
     *        collator, <code>false</code> otherwise.
     */
    public boolean hasDecomposition() { return hasDecomposition; }

    /**
     * Get the decomposition mode specified for this collator.  The return value
     * only has meaning if <code>{@link #hasDecomposition()} == true</code>.
     * @return the decomposition mode specified for this collator.
     */
    public int getDecomposition() { return decomposition; }

    /**
     * Get the {@link Collator java.text.Collator} described by this
     * <code>QueryCollator</code> object.
     * @return the <code>Collator</code> described by this object.
     */
    public Collator getCollator() {
        if (collator == null) {
            collator = Collator.getInstance(locale);
            if (hasStrength) collator.setStrength(strength);
            if (hasDecomposition) collator.setDecomposition(decomposition);
        }
        return collator;
    }

    /**
     * Compare this query collator for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is a <code>QueryCollator</code>
     *        with the same locale, strength, and decomposition properties as
     *        <code>this</code>, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof QueryCollator)) return false;

        QueryCollator that = (QueryCollator)obj;
        return this.locale.equals(that.locale)
            && this.hasStrength == that.hasStrength
            && this.strength == that.strength
            && this.hasDecomposition == that.hasDecomposition
            && this.decomposition == that.decomposition;
    }

    /**
     * Get a hash code value for this query collator.
     * @return as hash code.  The hash code is equal to the hash code of
     *        the collator's locale.
     */
    public int hashCode() {
        return locale.hashCode();
    }

    /**
     * Get a string representation for this query collator.
     * @return a string representation for this query collator.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(locale);
        if (hasStrength) buf.append(":strength=").append(getStrengthString());
        if (hasDecomposition) buf.append(":decomposition=").append(getDecompositionString());
        return buf.toString();
    }


    // private

    private QueryCollator(Locale locale, boolean hasStrength, int strength, boolean hasDecomposition, int decomposition) {
        if (locale == null) throw new NullPointerException("locale is null");
        this.locale = locale;
        this.hasStrength = hasStrength;
        this.strength = strength;
        this.hasDecomposition = hasDecomposition;
        this.decomposition = decomposition;
    }

    private String getStrengthString() {
        switch (strength) {
            case Collator.PRIMARY: return "primary";
            case Collator.SECONDARY: return "secondary";
            case Collator.TERTIARY: return "tertiary";
            case Collator.IDENTICAL: return "identical";
            default: return "unknown";
        }
    }

    private String getDecompositionString() {
        switch (decomposition) {
            case Collator.NO_DECOMPOSITION: return "none";
            case Collator.CANONICAL_DECOMPOSITION: return "canonical";
            case Collator.FULL_DECOMPOSITION: return "full";
            default: return "unknown";
        }
    }


    private final Locale locale;
    private final boolean hasStrength;
    private final int strength;
    private final boolean hasDecomposition;
    private final int decomposition;
    private transient Collator collator;
}
