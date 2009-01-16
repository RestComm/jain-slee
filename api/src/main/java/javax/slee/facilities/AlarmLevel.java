package javax.slee.facilities;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type for the alarm levels supported by the Alarm Facility.
 * The alarm levels match those defined by ITU X.733.
 * <p>
 * A singleton instance of each enumerated value is guaranteed (via an implementation
 * of <code>readResolve()</code> - refer {@link java.io.Serializable java.io.Serializable}),
 * so that equality tests using <code>==</code> are always evaluated correctly.  (This
 * equality test is only guaranteed if this class is loaded in the application's boot class
 * path, rather than dynamically loaded at runtime.)
 * <p>
 * The alarm levels, in order of relative severity, are:
 * <ul>
 *   <li>CLEAR
 *   <li>CRITICAL
 *   <li>MAJOR
 *   <li>WARNING
 *   <li>INDETERMINATE
 *   <li>MINOR
 * </ul>
 * @since SLEE 1.1
 */
public final class AlarmLevel implements Serializable {
    /**
     * An integer representation of {@link #CLEAR}.
     */
    public static final int LEVEL_CLEAR = 0;

    /**
     * An integer representation of {@link #CRITICAL}.
     */
    public static final int LEVEL_CRITICAL = 1;

    /**
     * An integer representation of {@link #MAJOR}.
     */
    public static final int LEVEL_MAJOR = 2;

    /**
     * An integer representation of {@link #WARNING}.
     */
    public static final int LEVEL_WARNING = 3;

    /**
     * An integer representation of {@link #INDETERMINATE}.
     */
    public static final int LEVEL_INDETERMINATE = 4;

    /**
     * An integer representation of {@link #MINOR}.
     */
    public static final int LEVEL_MINOR = 5;


    /**
     * A string representation of {@link #CLEAR}.
     */
    public static final String CLEAR_STRING = "Clear";

    /**
     * A string representation of {@link #CRITICAL}.
     */
    public static final String CRITICAL_STRING = "Critical";

    /**
     * A string representation of {@link #MAJOR}.
     */
    public static final String MAJOR_STRING = "Major";

    /**
     * A string representation of {@link #WARNING}.
     */
    public static final String WARNING_STRING = "Warning";

    /**
     * A string representation of {@link #INDETERMINATE}.
     */
    public static final String INDETERMINATE_STRING = "Indeterminate";

    /**
     * A string representation of {@link #MINOR}.
     */
    public static final String MINOR_STRING = "Minor";


    /**
     * Alarm level used when an error condition has cleared.
     */
    public static final AlarmLevel CLEAR = new AlarmLevel(LEVEL_CLEAR);

    /**
     * Alarm level used for critical error conditions.  The critical severity level
     * indicates that a condition affecting service has occurred and immediate
     * corrective action is required.  For example, a monitored resource has become
     * totally incapacitated and its full capability must be restored.
     */
    public static final AlarmLevel CRITICAL = new AlarmLevel(LEVEL_CRITICAL);

    /**
     * Alarm level used for error conditions of major severity.  The major severity
     * level indicates that a condition affecting service has occurred and urgent
     * corrective action is required.  For example, a monitored resource has experienced
     * severe degradation in its capability and full capability must be restored.
     */
    public static final AlarmLevel MAJOR = new AlarmLevel(LEVEL_MAJOR);

    /**
     * Alarm level used to indicate alarm warning conditions.  The warning severity
     * level indicates the detection of a potential or impending fault that may
     * affect service, before any significant effects have been encountered.  Action
     * should be taken to further diagnose (if necessary) and correct the problem in
     * order to prevent a more serious (eg. service-affecting) fault.
     */
    public static final AlarmLevel WARNING = new AlarmLevel(LEVEL_WARNING);

    /**
     * Alarm level used when the severity of the error condition is unknown.
     */
    public static final AlarmLevel INDETERMINATE = new AlarmLevel(LEVEL_INDETERMINATE);

    /**
     * Alarm level used for error conditions of minor severity.  The minor severity
     * level indicates that a condition has occurred that does not directly affect
     * service, however corrective action should be taken in order to prevent a more
     * serious (eg. service-affecting) fault.  An alarm of minor severity could be
     * reported, for example, when the detected alarm condition is not currently
     * degrading the capability of the monitored resource.
     */
    public static final AlarmLevel MINOR = new AlarmLevel(LEVEL_MINOR);

    /**
     * Get an <code>AlarmLevel</code> object from an integer value.
     * @param level the level as an integer.
     * @return an <code>AlarmLevel</code> object corresponding to <code>level</code>.
     * @throws IllegalArgumentException if <code>level</code> is not a valid alarm
     *        level value.
     */
    public static AlarmLevel fromInt(int level) throws IllegalArgumentException {
        switch (level) {
            case LEVEL_CLEAR: return CLEAR;
            case LEVEL_CRITICAL: return CRITICAL;
            case LEVEL_MAJOR: return MAJOR;
            case LEVEL_WARNING: return WARNING;
            case LEVEL_INDETERMINATE: return INDETERMINATE;
            case LEVEL_MINOR: return MINOR;
            default: throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    /**
     * Get an <code>AlarmLevel</code> object from a string value.
     * @param level the level as a string, for example as returned by the {@link #toString()}
     *        method (case insensitive).
     * @return an <code>AlarmLevel</code> object corresponding to <code>level</code>.
     * @throws NullPointerException if <code>level</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>level</code> is not a valid alarm
     *        level string.
     */
    public static AlarmLevel fromString(String level) throws NullPointerException, IllegalArgumentException {
        if (level == null) throw new NullPointerException("level is null");
        if (level.equalsIgnoreCase(CLEAR_STRING)) return CLEAR;
        if (level.equalsIgnoreCase(CRITICAL_STRING)) return CRITICAL;
        if (level.equalsIgnoreCase(MAJOR_STRING)) return MAJOR;
        if (level.equalsIgnoreCase(WARNING_STRING)) return WARNING;
        if (level.equalsIgnoreCase(INDETERMINATE_STRING)) return INDETERMINATE;
        if (level.equalsIgnoreCase(MINOR_STRING)) return MINOR;
        throw new IllegalArgumentException("Invalid level: " + level);
    }

    /**
     * Get an integer value representation for this <code>AlarmLevel</code> object.
     * @return an integer value representation for this <code>AlarmLevel</code> object.
     */
    public int toInt() {
        return level;
    }

    /**
     * Determine if this AlarmLevel object represents the CLEAR alarm level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == CLEAR)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (alarmLevel.isClear()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (alarmLevel == AlarmLevel.CLEAR) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the CLEAR alarm level,
     *       <code>false</code> otherwise.
     */
    public boolean isClear() {
        return level == LEVEL_CLEAR;
    }

    /**
     * Determine if this AlarmLevel object represents the CRITICAL alarm level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == CRITICAL)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (alarmLevel.isCritical()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (alarmLevel == AlarmLevel.CRITICAL) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the CRITICAL alarm level,
     *       <code>false</code> otherwise.
     */
    public boolean isCritical() {
        return level == LEVEL_CRITICAL;
    }

    /**
     * Determine if this AlarmLevel object represents the MAJOR alarm level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == MAJOR)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (alarmLevel.isMajor()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (alarmLevel == AlarmLevel.MAJOR) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the MAJOR alarm level,
     *       <code>false</code> otherwise.
     */
    public boolean isMajor() {
        return level == LEVEL_MAJOR;
    }

    /**
     * Determine if this AlarmLevel object represents the WARNING alarm level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == WARNING)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (alarmLevel.isWarning()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (alarmLevel == AlarmLevel.WARNING) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the WARNING alarm level,
     *       <code>false</code> otherwise.
     */
    public boolean isWarning() {
        return level == LEVEL_WARNING;
    }


    /**
     * Determine if this AlarmLevel object represents the INDETERMINATE alarm level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == INDETERMINATE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (alarmLevel.isIndeterminate()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (alarmLevel == AlarmLevel.INDETERMINATE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the INDETERMINATE alarm level,
     *       <code>false</code> otherwise.
     */
    public boolean isIndeterminate() {
        return level == LEVEL_INDETERMINATE;
    }

    /**
     * Determine if this AlarmLevel object represents the MINOR alarm level.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == MINOR)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (alarmLevel.isMinor()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (alarmLevel == AlarmLevel.MINOR) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the MINOR alarm level,
     *       <code>false</code> otherwise.
     */
    public boolean isMinor() {
        return level == LEVEL_MINOR;
    }

    /**
     * Determine if this AlarmLevel object represents a level that is higher than some other
     * AlarmLevel object.  For the purposes of the comparison the following order, from highest
     * to lowest severity, is assumed for alarm levels:
     * <ul>
     *   <li>CLEAR
     *   <li>CRITICAL
     *   <li>MAJOR
     *   <li>WARNING
     *   <li>INDETERMINATE
     *   <li>MINOR
     * </ul>
     * @param other the <code>AlarmLevel</code> object to compare this with.
     * @return <code>true</code> if the level represented by this <code>AlarmLevel</code>
     *        object is considered a higher level than the level represented by the specified
     *        <code>AlarmLevel</code> object, <code>false</code> otherwise.
     * @throws NullPointerException if <code>other</code> is <code>null</code>.
     */
    public boolean isHigherLevel(AlarmLevel other) throws NullPointerException {
        if (other == null) throw new NullPointerException("other is null");

        return this.level < other.level;
    }


    /**
     * Compare this alarm level for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same alarm level as this, <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof AlarmLevel) && ((AlarmLevel)obj).level == level;
    }

    /**
     * Get a hash code value for this alarm level.
     * @return a hash code value.
     */
    public int hashCode() {
        return level;
    }

    /**
     * Get the textual representation of this alarm level object.
     * @return the textual representation of this alarm level object.
     */
    public String toString() {
        switch (level) {
            case LEVEL_CLEAR: return CLEAR_STRING;
            case LEVEL_CRITICAL: return CRITICAL_STRING;
            case LEVEL_MAJOR: return MAJOR_STRING;
            case LEVEL_WARNING: return WARNING_STRING;
            case LEVEL_INDETERMINATE: return INDETERMINATE_STRING;
            case LEVEL_MINOR: return MINOR_STRING;
            default: return "AlarmLevel in Unknown and Invalid State";
        }
    }


    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private AlarmLevel(int level) {
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
