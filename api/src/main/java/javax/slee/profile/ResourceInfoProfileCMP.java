package javax.slee.profile;

/**
 * This interface must be used for profile specifications that will be used for
 * a Service's Resource Info Profile Table.
 * @deprecated As resource adaptor components may use whatever profile
 * specifications they need in SLEE 1.1, this interface and its associated
 * profile specification are no longer required. 
 */
public interface ResourceInfoProfileCMP {
    /**
     * Get the resource information.
     * @return the resource information.
     */
    public String getInfo();

    /**
     * Set the resource information.
     * @param info the resource information.
     */
    public void setInfo(String info);

}

