package javax.slee.facilities;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type for the alarm and trace levels supported by
 * the Alarm Facility and Trace Facility.  The class is based on the Java Logging API
 * included with J2SE 1.4.
 * <p>
 * A singleton instance of each enumerated value is guaranteed (via an implementation
 * of <code>readResolve()</code> - refer {@link java.io.Serializable java.io.Serializable}),
 * so that equality tests using <code>==</code> are always evaluated correctly.  (This
 * equality test is only guaranteed if this class is loaded in the application's boot class
 * path, rather than dynamically loaded at runtime.)
 * <p>
 * The levels in descending order are:
 * <ul>
 *   <li>SEVERE (highest value)
 *   <li>WARNING
 *   <li>INFO
 *   <li>CONFIG
 *   <li>FINE
 *   <li>FINER
 *   <li>FINEST (lowest value)
 * </ul>
 * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
 */
public final class Level implements Serializable {
    /**
     * An integer representation of {@link #OFF}.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final int LEVEL_OFF = 0;

    /**
     * An integer representation of {@link #SEVERE}.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final int LEVEL_SEVERE = 1;

    /**
     * An integer representation of {@link #WARNING}.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final int LEVEL_WARNING = 2;

    /**
     * An integer representation of {@link #INFO}.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final int LEVEL_INFO = 3;

    /**
     * An integer representation of {@link #CONFIG}.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final int LEVEL_CONFIG = 4;

    /**
     * An integer representation of {@link #FINE}.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final int LEVEL_FINE = 5;

    /**
     * An integer representation of {@link #FINER}.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final int LEVEL_FINER = 6;

    /**
     * An integer representation of {@link #FINEST}.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final int LEVEL_FINEST = 7;

    /**
     * Level for maximum filtering.  Notifications cannot be generated using this level.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final Level OFF = new Level(LEVEL_OFF);

    /**
     * Level for messages indicating a severe failure.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final Level SEVERE = new Level(LEVEL_SEVERE);

    /**
     * Level for messages indicating warning conditions.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final Level WARNING = new Level(LEVEL_WARNING);

    /**
     * Level for information messages.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final Level INFO = new Level(LEVEL_INFO);

    /**
     * Level for configuration messages.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final Level CONFIG = new Level(LEVEL_CONFIG);

    /**
     * Level for basic debug messages.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final Level FINE = new Level(LEVEL_FINE);

    /**
     * Level for moderately detailed messages.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final Level FINER = new Level(LEVEL_FINER);

    /**
     * Level for highly detailed messages.  This is the lowest level.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static final Level FINEST = new Level(LEVEL_FINEST);


    /**
     * Get an <code>Level</code> object from an integer value.
     * @param level the level as an integer.
     * @return an <code>Level</code> object corresponding to <code>level</code>.
     * @throws IllegalArgumentException if <code>level</code> is not a valid
     *        level value.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public static Level fromInt(int level) throws IllegalArgumentException {
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
     * Get an integer value representation for this <code>Level</code> object.
     * @return an integer value representation for this <code>Level</code> object.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public int toInt() {
        return level;
    }

    /**
     * Determine if this Level object represents the OFF level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == OFF)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (level.isOff()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (level == Level.OFF) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the OFF level,
     *       <code>false</code> otherwise.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isOff() {
        return level == LEVEL_OFF;
    }

    /**
     * Determine if this Level object represents the SEVERE level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == SEVERE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (level.isSevere()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (level == Level.SEVERE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the SEVERE level,
     *       <code>false</code> otherwise.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isSevere() {
        return level == LEVEL_SEVERE;
    }

    /**
     * Determine if this Level object represents the WARNING level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == WARNING)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (level.isWarning()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (level == Level.WARNING) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the WARNING level,
     *       <code>false</code> otherwise.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isWarning() {
        return level == LEVEL_WARNING;
    }

    /**
     * Determine if this Level object represents the INFO level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == INFO)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (level.isMinor()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (level == Level.INFO) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the INFO level,
     *       <code>false</code> otherwise.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isMinor() {
        return level == LEVEL_INFO;
    }

    /**
     * Determine if this Level object represents the CONFIG level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == CONFIG)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (level.isConfig()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (level == Level.CONFIG) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the CONFIG level,
     *       <code>false</code> otherwise.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isConfig() {
        return level == LEVEL_CONFIG;
    }

    /**
     * Determine if this Level object represents the FINE level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == FINE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (level.isFine()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (level == Level.FINE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the FINE level,
     *       <code>false</code> otherwise.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isFine() {
        return level == LEVEL_FINE;
    }

    /**
     * Determine if this Level object represents the FINER level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == FINER)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (level.isFiner()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (level == Level.FINER) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the FINER level,
     *       <code>false</code> otherwise.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isFiner() {
        return level == LEVEL_FINER;
    }

    /**
     * Determine if this Level object represents the FINEST level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == FINEST)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (level.isFinest()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (level == Level.FINEST) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the FINEST level,
     *       <code>false</code> otherwise.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isFinest() {
        return level == LEVEL_FINEST;
    }

    /**
     * Determine if this Level object represents a level that is higher or more severe
     * that some other Level object.  For the purposes of the comparison OFF
     * is considered a higher level than SEVERE.
     * @param other the <code>Level</code> object to compare this with.
     * @return <code>true</code> if the level represented by this <code>Level</code>
     *        object is a higher level than the level represented by the specified
     *        <code>Level</code> object, <code>false</code> otherwise.
     * @throws NullPointerException if <code>other</code> is <code>null</code>.
     * @deprecated Trace and alarm levels now defined in {@link TraceLevel} and {@link AlarmLevel}.
     */
    public boolean isHigherLevel(Level other) throws NullPointerException {
        if (other == null) throw new NullPointerException("other is null");

        return this.level < other.level;
    }

    /**
     * Compare this level for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same level as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof Level) && ((Level)obj).level == level;
    }

    /**
     * Get a hash code value for this level.
     * @return a hash code value.
     */
    public int hashCode() {
        return level;
    }

    /**
     * Get the textual representation of the Level object.
     * @return the textual representation of the Level object.
     */
    public String toString() {
        switch (level) {
            case LEVEL_OFF: return "Off";
            case LEVEL_SEVERE: return "Severe";
            case LEVEL_WARNING: return "Warning";
            case LEVEL_INFO: return "Info";
            case LEVEL_CONFIG: return "Config";
            case LEVEL_FINE: return "Fine";
            case LEVEL_FINER: return "Finer";
            case LEVEL_FINEST: return "Finest";
            default: return "Level in Unknown and Invalid State";
        }
    }


    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private Level(int level) {
        this.level = level;
    }

    /**
     * Resolve deserialisation references so that the singleton property of each
     * enumerated object is preserved.
     */
    private Object readResolve() throws StreamCorruptedException {
        if (level == LEVEL_OFF) return OFF;
        if (level == LEVEL_SEVERE) return SEVERE;
        if (level == LEVEL_WARNING) return WARNING;
        if (level == LEVEL_INFO) return INFO;
        if (level == LEVEL_CONFIG) return CONFIG;
        if (level == LEVEL_FINE) return FINE;
        if (level == LEVEL_FINER) return FINER;
        if (level == LEVEL_FINEST) return FINEST;
        throw new StreamCorruptedException("Invalid internal state found");
    }


    /**
     * The internal state representation of the enumerated type.
     */
    private final int level;
}
