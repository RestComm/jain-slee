package javax.slee.profile;

import javax.slee.Address;

/**
 * This interface can be used as is or extended for profile specifications that will
 * be used for a Service's Address Profile Table.
 * @deprecated This CMP interface is suitable only for SLEE 1.0 address profile tables, as
 * array types cannot be used in SLEE 1.1 profile queries.  The {@link AddressProfile11CMP}
 * interface can be used instead as a base CMP interface for SLEE 1.1 address profile tables.
 */
public interface AddressProfileCMP {
    /**
     * Get the addresses for this profile.
     * @return the addresses for this profile.
     */
    public Address[] getAddresses();

    /**
     * Set the addresses for this profile.
     * @param addresses the addresses for this profile.
     */
    public void setAddresses(Address[] addresses);

}

