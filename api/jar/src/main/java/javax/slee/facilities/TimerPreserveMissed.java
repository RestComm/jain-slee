package javax.slee.facilities;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type for the late-timer preservation options
 * suppored by the {@link TimerFacility Timer Facility}. A singleton instance of 
 * each enumerated value is guaranteed (via an implementation of <code>readResolve()</code>
 * - refer {@link java.io.Serializable java.io.Serializable}), so that equality tests
 * using <code>==</code> are always evaluated correctly.  (This equality test is only
 * guaranteed if this class is loaded in the application's boot class path, rather
 * than dynamically loaded at runtime.)
 * <p>
 * The late-timer preservation options are:
 * <ul>
 *   <li>NONE - timer events for a late timer are not generated.
 *   <li>ALL - timer events for a late timer are always generated.
 *   <li>LAST - only one timer event is generated for a late timer, that event
 *       corresponding to the most recent firing of the timer.
 * </ul>
 */
public final class TimerPreserveMissed implements Serializable {
    /**
     * An integer representation of {@link #NONE}.
     */
    public static final int PRESERVE_NONE = 0;

    /**
     * An integer representation of {@link #ALL}.
     */
    public static final int PRESERVE_ALL = 1;

    /**
     * An integer representation of {@link #LAST}.
     */
    public static final int PRESERVE_LAST = 2;


    /**
     * A string representation of {@link #NONE}.
     */
    public static final String NONE_STRING = "None";

    /**
     * A string representation of {@link #ALL}.
     */
    public static final String ALL_STRING = "All";

    /**
     * A string representation of {@link #LAST}.
     */
    public static final String LAST_STRING = "Last";


    /**
     * Preserve-missed value for the NONE option.
     */
    public static final TimerPreserveMissed NONE = new TimerPreserveMissed(PRESERVE_NONE);

    /**
     * Preserve-missed value for the ALL option.
     */
    public static final TimerPreserveMissed ALL = new TimerPreserveMissed(PRESERVE_ALL);

    /**
     * Preserve-missed value for the LAST option.
     */
    public static final TimerPreserveMissed LAST = new TimerPreserveMissed(PRESERVE_LAST);


    /**
     * Get a <code>TimerPreserveMissed</code> object from an integer value.
     * @param option the preserve-missed option as an integer.
     * @return a <code>TimerPreserveMissed</code> object corresponding to <code>option</code>.
     * @throws IllegalArgumentException if <code>option</code> is not a valid
     *        preserve-missed option value.
     */
    public static TimerPreserveMissed fromInt(int option) throws IllegalArgumentException {
        switch (option) {
            case PRESERVE_NONE: return NONE;
            case PRESERVE_ALL: return ALL;
            case PRESERVE_LAST: return LAST;
            default: throw new IllegalArgumentException("Invalid preserve-missed value: " + option);
        }
    }

    /**
     * Get a <code>TimerPreserveMissed</code> object from a string value.
     * @param option the preserve-missed option as a string, for example as returned
              by the {@link #toString()} method (case insensitive).
     * @return a <code>TimerPreserveMissed</code> object corresponding to <code>option</code>.
     * @throws NullPointerException if <code>option</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>option</code> is not a valid
     *        preserve-missed option value.
     * @since SLEE 1.1
     */
    public static TimerPreserveMissed fromString(String option) throws NullPointerException, IllegalArgumentException {
        if (option == null) throw new NullPointerException("option is null");
        if (option.equalsIgnoreCase(NONE_STRING)) return NONE;
        if (option.equalsIgnoreCase(ALL_STRING)) return ALL;
        if (option.equalsIgnoreCase(LAST_STRING)) return LAST;
        throw new IllegalArgumentException("Invalid preserve-missed value: " + option);
    }

    /**
     * Get an integer value representation for this <code>TimerPreserveMissed</code> object.
     * @return an integer value representation for this <code>TimerPreserveMissed</code> object.
     */
    public int toInt() {
        return option;
    }

    /**
     * Determine if this TimerPreserveMissed object represents the NONE option.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == NONE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (timerPreserveMissed.isNone()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (timerPreserveMissed == TimerPreserveMissed.NONE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the NONE option,
     *       <code>false</code> otherwise.
     */
    public boolean isNone() {
        return option == PRESERVE_NONE;
    }

    /**
     * Determine if this TimerPreserveMissed object represents the ALL option.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == ALL)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (timerPreserveMissed.isAll()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (timerPreserveMissed == TimerPreserveMissed.ALL) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the ALL option,
     *       <code>false</code> otherwise.
     */
    public boolean isAll() {
        return option == PRESERVE_ALL;
    }

    /**
     * Determine if this TimerPreserveMissed object represents the LAST option.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == LAST)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (timerPreserveMissed.isLast()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (timerPreserveMissed == TimerPreserveMissed.LAST) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the LAST option,
     *       <code>false</code> otherwise.
     */
    public boolean isLast() {
        return option == PRESERVE_LAST;
    }

    /**
     * Compare this preserve-missed option object for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same preserve-missed option as this, <code>false</code>
     *        otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof TimerPreserveMissed) && ((TimerPreserveMissed)obj).option == option;
    }

    /**
     * Get a hash code value for this preserve-missed option object.
     * @return a hash code value.
     */
    public int hashCode() {
        return option;
    }

    /**
     * Get the textual representation of the TimerPreserveMissed object.
     * @return the textual representation of the TimerPreserveMissed object.
     */
    public String toString() {
        switch (option) {
            case PRESERVE_NONE: return NONE_STRING;
            case PRESERVE_ALL: return ALL_STRING;
            case PRESERVE_LAST: return LAST_STRING;
            default: return "TimerPreserveMissed in Unknown and Invalid State";
        }
    }


    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private TimerPreserveMissed(int option) {
        this.option = option;
    }

    /**
     * Resolve deserialisation references so that the singleton property of each
     * enumerated object is preserved.
     */
    private Object readResolve() throws StreamCorruptedException {
        try {
            return fromInt(option);
        }
        catch (IllegalArgumentException iae) {
            throw new StreamCorruptedException("Invalid internal state found");
        }
    }


    /**
     * The internal state representation of the enumerated type.
     */
    private final int option;
}
