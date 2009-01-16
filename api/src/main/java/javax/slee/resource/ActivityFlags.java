package javax.slee.resource;

/**
 * This class defines flags that a Resource Adaptor can use when creating an activity.
 * These flags enable additional contracts between the SLEE and the Resource Adaptor
 * relating to the activity to be used.
 * <p>
 * A Resource Adaptor specifies activity flags when creating an activity using the
 * {@link SleeEndpoint#startActivity(ActivityHandle, Object, int)},
 * {@link SleeEndpoint#startActivitySuspended(ActivityHandle, Object, int)},
 * {@link SleeEndpoint#startActivityTransacted(ActivityHandle, Object, int)} methods.
 * @since SLEE 1.1
 */
public final class ActivityFlags {
    /**
     * Requests no additional behavior from the SLEE.
     */
    public static final int NO_FLAGS = 0x00000000;

    /**
     * Indicate to the SLEE that the SLEE may marshal and unmarshal the activity
     * handle if required to do so.  If this flag is specified the Resource Adaptor must
     * provide a implementation of the {@link Marshaler} interface that is capable
     * of marshaling activities.
     * @see Marshaler#marshalHandle
     * @see Marshaler#unmarshalHandle
     */
    public static final int SLEE_MAY_MARSHAL = 0x00000001;

    /**
     * Request an {@link ResourceAdaptor#activityEnded} callback from
     * the SLEE when the activity has ended.
     */
    public static final int REQUEST_ENDED_CALLBACK = 0x00000002;

    /**
     * Request a {@link ResourceAdaptor#activityUnreferenced} callback
     * from the SLEE when the activity is no longer referenced within the SLEE.
     */
    public static final int REQUEST_ACTIVITY_UNREFERENCED_CALLBACK = 0x00000004;

    /**
     * This bit-mask specifies the activity flag bits reserved for current and
     * future use by the JAIN SLEE specification.  Bits 0-23 are reserved for use
     * by the SLEE specification.
     */
    public static final int STANDARD_FLAGS_MASK = 0x00ffffff;

    /**
     * This bit-mask specifies the activity flag bits reserved for SLEE vendors
     * to represent optional proprietary behavior.  Bits 24-31 are reserved for
     * use by SLEE vendors.
     */
    public static final int VENDOR_FLAGS_MASK = 0xff000000;


    /**
     * Determine whether the specified activity flags request no special behavior.
     * @param flags integer representation of a set of activity flags to test.
     * @return <code>true</code> if the flags define no special behavior,
     *        <code>false</code> otherwise.
     */
    public static boolean hasNoFlags(int flags) {
        return flags == NO_FLAGS;
    }

    /**
     * Determine whether the specified flags request any of the SLEE specification
     * defined special behavior.
     * @param flags integer representation of a set of activity flags to test.
     * @return <code>true</code> if the flags define any SLEE defined special behavior,
     *        <code>false</code> otherwise.
     */
    public static boolean hasStandardFlags(int flags) {
        return (flags & STANDARD_FLAGS_MASK) != NO_FLAGS;
    }

    /**
     * Determine whether the specified flags request any vendor specific behavior.
     * @param flags integer representation of a set of activity flags to test.
     * @return <code>true</code> if the flags define any vendor defined special behavior,
     *        <code>false</code> otherwise.
     */
    public static boolean hasVendorFlags(int flags) {
        return (flags & VENDOR_FLAGS_MASK) != NO_FLAGS;
    }

    /**
     * Test an integer for the presence of one or more flags.
     * @param flags integer representation of a set of activity flags to test.
     * @param flagsToTestFor the flags to test for.  This may include flags defined
     *        by this class and/or vendor specific flags.
     * @return <code>true</code> if the specified flags are set, <code>false</code>
     *        otherwise.
     */
    public static boolean hasFlags(int flags, int flagsToTestFor) {
        return (flags & flagsToTestFor) != NO_FLAGS;
    }

    /**
     * Test for the presence of the {@link #SLEE_MAY_MARSHAL} flag.
     * @param flags integer representation of a set of activity flags to test.
     * @return <code>true</code> if the flags include the <code>SLEE_MAY_MARSHAL</code>
     *        flag, <code>false</code> otherwise.
     */
    public static boolean hasSleeMayMarshal(int flags) {
        return hasFlags(flags, SLEE_MAY_MARSHAL);
    }

    /**
     * Test for the presence of the {@link #REQUEST_ENDED_CALLBACK} flag.
     * @param flags integer representation of a set of activity flags to test.
     * @return <code>true</code> if the flags include the <code>REQUEST_ENDED_CALLBACK</code>
     *        flag, <code>false</code> otherwise.
     */
    public static boolean hasRequestEndedCallback(int flags) {
        return hasFlags(flags, REQUEST_ENDED_CALLBACK);
    }

    /**
     * Test for the presence of the {@link #REQUEST_ACTIVITY_UNREFERENCED_CALLBACK} flag.
     * @param flags integer representation of a set of activity flags to test.
     * @return <code>true</code> if the flags include the
     *        <code>REQUEST_ACTIVITY_UNREFERENCED_CALLBACK</code> flag, <code>false</code>
     *        otherwise.
     */
    public static boolean hasRequestSleeActivityGCCallback(int flags) {
        return hasFlags(flags, REQUEST_ACTIVITY_UNREFERENCED_CALLBACK);
    }

    /**
     * Add the {@link #SLEE_MAY_MARSHAL} flag to an existing integer representation of
     * activity flags.  The new flag is bitwise OR'ed onto the existing flags.  This
     * method has no effect if the <code>SLEE_MAY_MARSHAL</code> flag is already present
     * in the existing flags.
     * @param currentFlags the existing integer representation of activity flags.
     * @return an integer equivalent to the input activity flags with the
     *        <code>SLEE_MAY_MARSHAL</code> flag set.
     */
    public static int setSleeMayMarshal(int currentFlags) {
        return currentFlags | SLEE_MAY_MARSHAL;
    }

    /**
     * Add the {@link #REQUEST_ENDED_CALLBACK} flag to an existing integer representation
     * of activity flags.  The new flag is bitwise OR'ed onto the existing flags.  This
     * method has no effect if the <code>REQUEST_ENDED_CALLBACK</code> flag is already
     * present in the existing flags.
     * @param currentFlags the existing integer representation of activity flags.
     * @return an integer equivalent to the input activity flags with the
     *        <code>REQUEST_ENDED_CALLBACK</code> flag set.
     */
    public static int setRequestEndedCallback(int currentFlags) {
        return currentFlags | REQUEST_ENDED_CALLBACK;
    }

    /**
     * Add the {@link #REQUEST_ACTIVITY_UNREFERENCED_CALLBACK} flag to an existing integer
     * representation of activity flags.  The new flag is bitwise OR'ed onto the existing
     * flags.  This method has no effect if the <code>REQUEST_ACTIVITY_UNREFERENCED_CALLBACK</code>
     * flag is already present in the existing flags.
     * @param currentFlags the existing integer representation of activity flags.
     * @return an integer equivalent to the input activity flags with the
     *        <code>REQUEST_ACTIVITY_UNREFERENCED_CALLBACK</code> flag set.
     */
    public static int setRequestSleeActivityGCCallback(int currentFlags) {
        return currentFlags | REQUEST_ACTIVITY_UNREFERENCED_CALLBACK;
    }

    /**
     * Get a string representation of an integer containing activity flags.
     * @param flags integer representation of a set of activity flags.
     * @return a string representation containing all the SLEE specification defined
     *        activity flags and a hex representation of any vendor defined flags.
     */
    public static String toString(int flags) {
        if (hasNoFlags(flags)) return "NO_FLAGS";

        StringBuffer buf = new StringBuffer(64);

        if (hasFlags(flags, SLEE_MAY_MARSHAL))
            buf.append("SLEE_MAY_MARSHAL");

        if( hasFlags(flags, REQUEST_ENDED_CALLBACK)) {
            if (buf.length() > 0) buf.append('|');
            buf.append("REQUEST_ENDED_CALLBACK");
        }

        if( hasFlags(flags, REQUEST_ACTIVITY_UNREFERENCED_CALLBACK)) {
            if (buf.length() > 0) buf.append('|');
            buf.append("REQUEST_ACTIVITY_UNREFERENCED_CALLBACK");
        }

        if (hasFlags(flags, VENDOR_FLAGS_MASK)) {
            if (buf.length() > 0) buf.append('|');
            buf.append("VENDOR_FLAGS == 0x").append(Integer.toHexString(flags & VENDOR_FLAGS_MASK));
        }

        return buf.toString();
    }


    /**
     * Private constructor to prevent object creation.
     */
    private ActivityFlags() {}
}
