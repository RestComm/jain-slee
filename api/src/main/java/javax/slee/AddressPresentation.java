package javax.slee;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type that encapsulates the values available for
 * address presentation to other call parties.  The user application uses an address
 * presentation attribute to determine whether an address is to be presented to end
 * users.
 * <p>
 * A singleton instance of each enumerated value is guaranteed (via an implementation
 * of <code>readResolve()</code> - refer {@link java.io.Serializable java.io.Serializable}),
 * so that equality tests using <code>==</code> are always evaluated correctly.  (This
 * equality test is only guaranteed if this class is loaded in the application's boot class
 * path, rather than dynamically loaded at runtime.)
 * @see Address
 */
public final class AddressPresentation implements Serializable{
    /**
     * An integer representation of {@link #UNDEFINED}.
     */
    public static final int ADDRESS_PRESENTATION_UNDEFINED = 0;

    /**
     * An integer representation of {@link #ALLOWED}.
     */
    public static final int ADDRESS_PRESENTATION_ALLOWED = 1;

    /**
     * An integer representation of {@link #RESTRICTED}.
     */
    public static final int ADDRESS_PRESENTATION_RESTRICTED = 2;

    /**
     * An integer representation of {@link #ADDRESS_NOT_AVAILABLE}.
     */
    public static final int ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE = 3;


    /**
     * A string representation of {@link #UNDEFINED}.
     */
    public static final String UNDEFINED_STRING = "Undefined";

    /**
     * A string representation of {@link #ALLOWED}.
     */
    public static final String ALLOWED_STRING = "Allowed";

    /**
     * A string representation of {@link #RESTRICTED}.
     */
    public static final String RESTRICTED_STRING = "Restricted";

    /**
     * A string representation of {@link #ADDRESS_NOT_AVAILABLE}.
     */
    public static final String ADDRESS_NOT_AVAILABLE_STRING = "Address Not Available";


    /**
     * The UNDEFINED value indicates that the address presentation
     * is undefined.
     */
    public static final AddressPresentation UNDEFINED = new AddressPresentation(ADDRESS_PRESENTATION_UNDEFINED);

    /**
     * The ALLOWED value indicates that the address presentation
     * is allowed.
     */
    public static final AddressPresentation ALLOWED = new AddressPresentation(ADDRESS_PRESENTATION_ALLOWED);

    /**
     * The RESTRICTED value indicates that the address presentation
     * is restricted.
     */
    public static final AddressPresentation RESTRICTED = new AddressPresentation(ADDRESS_PRESENTATION_RESTRICTED);

    /**
     * The ADDRESS_NOT_AVAILABLE value indicates that the address
     * is not available for presentation.
     */
    public static final AddressPresentation ADDRESS_NOT_AVAILABLE = new AddressPresentation(ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE);

    /**
     * Get an <code>AddressPresentation</code> object from an integer value.
     * @param value the address presentation value as an integer.
     * @return an <code>AddressPresentation</code> object corresponding to <code>value</code>.
     * @throws IllegalArgumentException if <code>value</code> is not a valid
     *        address presentation value.
     */
    public static AddressPresentation fromInt(int value) throws IllegalArgumentException {
        switch (value) {
            case ADDRESS_PRESENTATION_UNDEFINED: return UNDEFINED;
            case ADDRESS_PRESENTATION_ALLOWED: return ALLOWED;
            case ADDRESS_PRESENTATION_RESTRICTED: return RESTRICTED;
            case ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE: return ADDRESS_NOT_AVAILABLE;
            default: throw new IllegalArgumentException("Invalid value: " + value);
        }
    }

    /**
     * Get an <code>AddressPresentation</code> object from a string value.
     * @param value the address presentation as a string, for example as returned by the
              {@link #toString()} method (case insensitive).
     * @return an <code>AddressPresentation</code> object corresponding to <code>value</code>.
     * @throws NullPointerException if <code>value</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>value</code> is not a valid
     *        address presentation value.
     * @since SLEE 1.1
     */
    public static AddressPresentation fromString(String value) throws NullPointerException, IllegalArgumentException {
        if (value == null) throw new NullPointerException("value is null");
        if (value.equalsIgnoreCase(UNDEFINED_STRING)) return UNDEFINED;
        if (value.equalsIgnoreCase(ALLOWED_STRING)) return ALLOWED;
        if (value.equalsIgnoreCase(RESTRICTED_STRING)) return RESTRICTED;
        if (value.equalsIgnoreCase(ADDRESS_NOT_AVAILABLE_STRING)) return ADDRESS_NOT_AVAILABLE;
        throw new IllegalArgumentException("Invalid value: " + value);
    }

    /**
     * Get an integer value representation for this <code>AddressPresentation</code> object.
     * @return an integer value representation for this <code>AddressPresentation</code> object.
     */
    public int toInt() {
        return value;
    }

    /**
     * Determine if this AddressPresentation object represents the UNDEFINED address
     * presentation value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == UNDEFINED)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (presentation.isUndefined()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (presentation == AddressPresentation.UNDEFINED) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the UNDEFINED address presentation
     *       value, <code>false</code> otherwise.
     */
    public boolean isUndefined() {
        return value == ADDRESS_PRESENTATION_UNDEFINED;
    }

    /**
     * Determine if this AddressPresentation object represents the ALLOWED address
     * presentation value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == ALLOWED)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (presentation.isAllowed()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (presentation == AddressPresentation.ALLOWED) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the ALLOWED address presentation
     *       value, <code>false</code> otherwise.
     */
    public boolean isAllowed() {
        return value == ADDRESS_PRESENTATION_ALLOWED;
    }

    /**
     * Determine if this AddressPresentation object represents the RESTRICTED address
     * presentation value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == RESTRICTED)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (presentation.isRestricted()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (presentation == AddressPresentation.RESTRICTED) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the RESTRICTED address presentation
     *       value, <code>false</code> otherwise.
     */
    public boolean isRestricted() {
        return value == ADDRESS_PRESENTATION_RESTRICTED;
    }

    /**
     * Determine if this AddressPresentation object represents the ADDRESS_NOT_AVAILABLE address
     * presentation value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == ADDRESS_NOT_AVAILABLE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (presentation.isAddressNotAvailable()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (presentation == AddressPresentation.ADDRESS_NOT_AVAILABLE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the ADDRESS_NOT_AVAILABLE address presentation
     *       value, <code>false</code> otherwise.
     */
    public boolean isAddressNotAvailable() {
        return value == ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE ;
    }

    /**
     * Compare this address presentation for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same address presentation value as this,
     *        <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof AddressPresentation)
            && ((AddressPresentation)obj).value == value;
    }

    /**
     * Get a hash code value for this address presentation.
     * @return a hash code value.
     */
    public int hashCode() {
        return value;
    }

    /**
     * Get the textual representation of the address presentation object.
     * @return the textual representation of the address presentation object.
     */
    public String toString() {
        switch (value) {
            case ADDRESS_PRESENTATION_UNDEFINED: return UNDEFINED_STRING;
            case ADDRESS_PRESENTATION_ALLOWED: return ALLOWED_STRING;
            case ADDRESS_PRESENTATION_RESTRICTED: return RESTRICTED_STRING;
            case ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE: return ADDRESS_NOT_AVAILABLE_STRING;
            default: return "AddressPresentation in Unknown and Invalid State";
        }
    }

    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private AddressPresentation(int value) {
        this.value = value;
    }

    /**
     * Resolve deserialisation references so that the singleton property of each
     * enumerated object is preserved.
     */
    private Object readResolve() throws StreamCorruptedException {
        try {
            return fromInt(value);
        }
        catch (IllegalArgumentException iae) {
            throw new StreamCorruptedException("Invalid internal state found");
        }
    }


    private final int value;
}

