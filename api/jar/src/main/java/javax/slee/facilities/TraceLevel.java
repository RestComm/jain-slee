package javax.slee.facilities;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type for the trace levels supported by the Trace
 * Facility.  The class is based on the Java Logging API included with J2SE 1.4.
 * <p>
 * A singleton instance of each enumerated value is guaranteed (via an implementation
 * of <code>readResolve()</code> - refer {@link java.io.Serializable java.io.Serializable}),
 * so that equality tests using <code>==</code> are always evaluated correctly.  (This
 * equality test is only guaranteed if this class is loaded in the application's boot class
 * path, rather than dynamically loaded at runtime.)
 * <p>
 * The trace levels in descending order are:
 * <ul>
 *   <li>SEVERE (highest value)
 *   <li>WARNING
 *   <li>INFO
 *   <li>CONFIG
 *   <li>FINE
 *   <li>FINER
 *   <li>FINEST (lowest value)
 * </ul>
 * <p>
 * The trace level OFF is also defined for the purposes of disabling trace message
 * generation at the management level.
 * @since SLEE 1.1
 */
public final class TraceLevel implements Serializable {
    /**
     * An integer representation of {@link #OFF}.
     */
    public static final int LEVEL_OFF = 0;

    /**
     * An integer representation of {@link #SEVERE}.
     */
    public static final int LEVEL_SEVERE = 1;

    /**
     * An integer representation of {@link #WARNING}.
     */
    public static final int LEVEL_WARNING = 2;

    /**
     * An integer representation of {@link #INFO}.
     */
    public static final int LEVEL_INFO = 3;

    /**
     * An integer representation of {@link #CONFIG}.
     */
    public static final int LEVEL_CONFIG = 4;

    /**
     * An integer representation of {@link #FINE}.
     */
    public static final int LEVEL_FINE = 5;

    /**
     * An integer representation of {@link #FINER}.
     */
    public static final int LEVEL_FINER = 6;

    /**
     * An integer representation of {@link #FINEST}.
     */
    public static final int LEVEL_FINEST = 7;


    /**
     * A string representation of {@link #OFF}.
     */
    public static final String OFF_STRING = "Off";

    /**
     * A string representation of {@link #SEVERE}.
     */
    public static final String SEVERE_STRING = "Severe";

    /**
     * A string representation of {@link #WARNING}.
     */
    public static final String WARNING_STRING = "Warning";

    /**
     * A string representation of {@link #INFO}.
     */
    public static final String INFO_STRING = "Info";

    /**
     * A string representation of {@link #CONFIG}.
     */
    public static final String CONFIG_STRING = "Config";

    /**
     * A string representation of {@link #FINE}.
     */
    public static final String FINE_STRING = "Fine";

    /**
     * A string representation of {@link #FINER}.
     */
    public static final String FINER_STRING = "Finer";

    /**
     * A string representation of {@link #FINEST}.
     */
    public static final String FINEST_STRING = "Finest";


    /**
     * Trace level for maximum filtering.  Notifications are not be generated using this level.
     */
    public static final TraceLevel OFF = new TraceLevel(LEVEL_OFF);

    /**
     * Trace level for messages that indicate a severe error has occurred.
     */
    public static final TraceLevel SEVERE = new TraceLevel(LEVEL_SEVERE);

    /**
     * Trace level for messages that indicate warnings.
     */
    public static final TraceLevel WARNING = new TraceLevel(LEVEL_WARNING);

    /**
     * Trace level for general information messages.
     */
    public static final TraceLevel INFO = new TraceLevel(LEVEL_INFO);

    /**
     * Trace level for messages relating to configuration.
     */
    public static final TraceLevel CONFIG = new TraceLevel(LEVEL_CONFIG);

    /**
     * Trace level for general coarse-grained debugging messages.
     */
    public static final TraceLevel FINE = new TraceLevel(LEVEL_FINE);

    /**
     * Trace level for debug messages containing moderate detail.
     */
    public static final TraceLevel FINER = new TraceLevel(LEVEL_FINER);

    /**
     * Trace level for highly detailed or fine-grained messages.  This is the lowest trace level.
     */
    public static final TraceLevel FINEST = new TraceLevel(LEVEL_FINEST);


    /**
     * Get a <code>TraceLevel</code> object from an integer value.
     * @param level the level as an integer.
     * @return a <code>TraceLevel</code> object corresponding to <code>level</code>.
     * @throws IllegalArgumentException if <code>level</code> is not a valid trace
     *        level value.
     */
    public static TraceLevel fromInt(int level) throws IllegalArgumentException {
        switch (level) {
            case LEVEL_OFF: return OFF;
            case LEVEL_SEVERE: return SEVERE;
            case LEVEL_WARNING: return WARNING;
            case LEVEL_INFO: return INFO;
            case LEVEL_CONFIG: return CONFIG;
            case LEVEL_FINE: return FINE;
            case LEVEL_FINER: return FINER;
            case LEVEL_FINEST: return FINEST;
            default: throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    /**
     * Get a <code>TraceLevel</code> object from a string value.
     * @param level the level as a string, for example as returned by the {@link #toString()}
     *        method (case insensitive).
     * @return a <code>TraceLevel</code> object corresponding to <code>level</code>.
     * @throws NullPointerException if <code>level</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>level</code> is not a valid trace
     *        level string.
     */
    public static TraceLevel fromString(String level) throws NullPointerException, IllegalArgumentException {
        if (level == null) throw new NullPointerException("level is null");
        if (level.equalsIgnoreCase(OFF_STRING)) return OFF;
        if (level.equalsIgnoreCase(SEVERE_STRING)) return SEVERE;
        if (level.equalsIgnoreCase(WARNING_STRING)) return WARNING;
        if (level.equalsIgnoreCase(INFO_STRING)) return INFO;
        if (level.equalsIgnoreCase(CONFIG_STRING)) return CONFIG;
        if (level.equalsIgnoreCase(FINE_STRING)) return FINE;
        if (level.equalsIgnoreCase(FINER_STRING)) return FINER;
        if (level.equalsIgnoreCase(FINEST_STRING)) return FINEST;
        throw new IllegalArgumentException("Invalid level: " + level);
    }

    /**
     * Get an integer value representation for this <code>TraceLevel</code> object.
     * @return an integer value representation for this <code>TraceLevel</code> object.
     */
    public int toInt() {
        return level;
    }

    /**
     * Determine if this TraceLevel object represents the OFF level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == OFF)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (traceLevel.isOff()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (traceLevel == TraceLevel.OFF) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the OFF level,
     *       <code>false</code> otherwise.
     */
    public boolean isOff() {
        return level == LEVEL_OFF;
    }

    /**
     * Determine if this TraceLevel object represents the SEVERE level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == SEVERE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (traceLevel.isSevere()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (traceLevel == TraceLevel.SEVERE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the SEVERE level,
     *       <code>false</code> otherwise.
     */
    public boolean isSevere() {
        return level == LEVEL_SEVERE;
    }

    /**
     * Determine if this TraceLevel object represents the WARNING level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == WARNING)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (traceLevel.isWarning()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (traceLevel == TraceLevel.WARNING) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the WARNING level,
     *       <code>false</code> otherwise.
     */
    public boolean isWarning() {
        return level == LEVEL_WARNING;
    }

    /**
     * Determine if this TraceLevel object represents the INFO level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == INFO)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (traceLevel.isMinor()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (traceLevel == TraceLevel.INFO) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the INFO level,
     *       <code>false</code> otherwise.
     */
    public boolean isInfo() {
        return level == LEVEL_INFO;
    }

    /**
     * Determine if this TraceLevel object represents the CONFIG level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == CONFIG)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (traceLevel.isConfig()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (traceLevel == TraceLevel.CONFIG) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the CONFIG level,
     *       <code>false</code> otherwise.
     */
    public boolean isConfig() {
        return level == LEVEL_CONFIG;
    }

    /**
     * Determine if this TraceLevel object represents the FINE level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == FINE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (traceLevel.isFine()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (traceLevel == TraceLevel.FINE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the FINE level,
     *       <code>false</code> otherwise.
     */
    public boolean isFine() {
        return level == LEVEL_FINE;
    }

    /**
     * Determine if this TraceLevel object represents the FINER level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == FINER)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (traceLevel.isFiner()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (traceLevel == TraceLevel.FINER) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the FINER level,
     *       <code>false</code> otherwise.
     */
    public boolean isFiner() {
        return level == LEVEL_FINER;
    }

    /**
     * Determine if this TraceLevel object represents the FINEST level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == FINEST)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (traceLevel.isFinest()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (traceLevel == TraceLevel.FINEST) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the FINEST level,
     *       <code>false</code> otherwise.
     */
    public boolean isFinest() {
        return level == LEVEL_FINEST;
    }

    /**
     * Determine if this TraceLevel object represents a level that is higher than some other
     * TraceLevel object.  For the purposes of the comparison OFF is considered a higher level
     * than SEVERE, and FINEST is the lowest level.
     * @param other the <code>TraceLevel</code> object to compare this with.
     * @return <code>true</code> if the level represented by this <code>TraceLevel</code>
     *        object is a higher level than the level represented by the specified
     *        <code>TraceLevel</code> object, <code>false</code> otherwise.
     * @throws NullPointerException if <code>other</code> is <code>null</code>.
     */
    public boolean isHigherLevel(TraceLevel other) throws NullPointerException {
        if (other == null) throw new NullPointerException("other is null");

        return this.level < other.level;
    }

    /**
     * Compare this trace level for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same level as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof TraceLevel) && ((TraceLevel)obj).level == level;
    }

    /**
     * Get a hash code value for this trace level.
     * @return a hash code value.
     */
    public int hashCode() {
        return level;
    }

    /**
     * Get the textual representation of the TraceLevel object.
     * @return the textual representation of the TraceLevel object.
     */
    public String toString() {
        switch (level) {
            case LEVEL_OFF: return OFF_STRING;
            case LEVEL_SEVERE: return SEVERE_STRING;
            case LEVEL_WARNING: return WARNING_STRING;
            case LEVEL_INFO: return INFO_STRING;
            case LEVEL_CONFIG: return CONFIG_STRING;
            case LEVEL_FINE: return FINE_STRING;
            case LEVEL_FINER: return FINER_STRING;
            case LEVEL_FINEST: return FINEST_STRING;
            default: return "TraceLevel in Unknown and Invalid State";
        }
    }


    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private TraceLevel(int level) {
        this.level = level;
    }

    /**
     * Resolve deserialisation references so that the singleton property of each
     * enumerated object is preserved.
     */
    private Object readResolve() throws StreamCorruptedException {
        try {
            return fromInt(level);
        }
        catch (IllegalArgumentException iae) {
            throw new StreamCorruptedException("Invalid internal state found");
        }
    }


    /**
     * The internal state representation of the enumerated type.
     */
    private final int level;
}
