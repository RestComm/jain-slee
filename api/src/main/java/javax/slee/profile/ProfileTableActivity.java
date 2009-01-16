package javax.slee.profile;

/**
 * This interface is implemented by activity objects on which profile table events
 * propagate.
 */
public interface ProfileTableActivity {
    /**
     * Get the name of the profile table corresponding to this activity.
     * @return the name of the profile table.
     */
    public String getProfileTableName();
}

