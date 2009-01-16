package javax.slee.profile;

import javax.slee.Address;

/**
 * This interface can be used as is or extended for profile specifications that will
 * be used for a Service's Address Profile Table.
 * <p>
 * This CMP interface is suitable for SLEE 1.1 address profile tables.
 * @since SLEE 1.1
 */
public interface AddressProfile11CMP {
    /**
     * Get the address for this profile.
     * @return the address for this profile.
     */
    public Address getAddress();

    /**
     * Set the address for this profile.
     * @param address the address for this profile.
     */
    public void setAddress(Address address);
}
