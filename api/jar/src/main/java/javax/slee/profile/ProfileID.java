package javax.slee.profile;

import java.io.Serializable;
import javax.slee.Address;
import javax.slee.AddressPlan;

/**
 * The <code>ProfileID</code> class defines an identifier that can be used to
 * reference profiles.  For example a profile specification's CMP interface
 * may contain attributes of the type <code>ProfileID</code>, allowing profile's of
 * that type to reference other profiles.
 */
public class ProfileID implements Serializable, Cloneable {
    /**
     * Create a <code>ProfileID</code> object using a profile table name and profile name.
     * @param profileTableName the name of the profile table.
     * @param profileName the name of the profile within the profile table.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws IllegalArgumentException if <code>profileTableName</code> includes the '<tt>/</tt>'
     *        character.  This character is not permitted in profile table names.
     */
    public ProfileID(String profileTableName, String profileName) throws NullPointerException, IllegalArgumentException {
        setProfileID(profileTableName, profileName);
    }

    /**
     * Create a <code>ProfileID</code> object from an address.  The address must have the
     * address plan of {@link javax.slee.AddressPlan#SLEE_PROFILE}.
     * @param address the address of the profile.
     * @throws NullPointerException if <code>address</code> is <code>null</code>.
     * @throws IllegalArgumentException if the address plan of the address is not
     *        <code>AddressPlan.SLEE_PROFILE</code>, or the address string does not
     *        contain a profile identifier with the correct encoding:
     *        <tt>&lt;profile-table-name&gt;/&lt;profile-name&gt;</tt>.
     */
    public ProfileID(Address address) throws NullPointerException, IllegalArgumentException {
        if (address == null) throw new NullPointerException("address is null");

        if (!address.getAddressPlan().equals(AddressPlan.SLEE_PROFILE))
            throw new IllegalArgumentException("address must have the SLEE_PROFILE address plan");

        String addressString = address.getAddressString();

        int div = addressString.indexOf('/');
        if (div < 0) throw new IllegalArgumentException("address string does not contain a '/' seperator character");

        this.profileTableName = addressString.substring(0, div);
        this.profileName = addressString.substring(div + 1);
        this.address = address;
    }

    /**
     * Get the name of the profile table referenced by this identifier.
     * @return the profile table name.
     */
    public final String getProfileTableName() {
        return profileTableName;
    }

    /**
     * Get the name of the profile referenced by this identifier.
     * @return the profile name.
     */
    public final String getProfileName() {
        return profileName;
    }

    /**
     * Set the profile table and profile referenced by this profile identifier to new
     * values.
     * @param profileTableName the name of the profile table.
     * @param profileName the name of the profile within the profile table.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws IllegalArgumentException if <code>profileTableName</code> includes the
     *        '<tt>/</tt>' character.  This character is not permitted in profile table names.
     */
    public final void setProfileID(String profileTableName, String profileName) throws NullPointerException, IllegalArgumentException {
        if (profileTableName == null) throw new NullPointerException("profileTableName is null");
        if (profileName == null) throw new NullPointerException("profileName is null");

        if (profileTableName.indexOf('/') >= 0) throw new IllegalArgumentException("profileTableName cannot contain the '/' character");

        this.profileTableName = profileTableName;
        this.profileName = profileName;
        this.address = null;
    }

    /**
     * Get an <code>Address</code> object containing the address form of the profile
     * identified by this profile identifier.
     * @return an <code>Address</code> object containing the address form of the profile
     *        identified by this profile identifier.
     */
    public Address toAddress() {
        if (address == null) {
            address = new Address(AddressPlan.SLEE_PROFILE, profileTableName + '/' + profileName);
        }
        return address;
    }

    /**
     * Create a copy of this profile identifier.
     * @return a copy of this profile identifier.
     * @see Object#clone()
     * @since SLEE 1.1.
     */
    public Object clone(){
        return new ProfileID(profileTableName, profileName);
    }
    
    /**
     * Compare this profile identifier for equality with another object.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class and the
     *        profile table name and profile name are equal.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ProfileID)) return false;

        ProfileID that = (ProfileID)obj;
        return this.profileTableName.equals(that.profileTableName)
            && this.profileName.equals(that.profileName);
    }

    /**
     * Get a hash code value for this profile identifier.  The hash code is the logical
     * OR of the hashcodes of the profile table name and profile name.
     * @return a hash code for this profile identifier.
     */
    public int hashCode() {
        return profileTableName.hashCode() | profileName.hashCode();
    }

    /**
     * Get a string representation for this profile identifier.
     * @return a string representation for this profile identifier.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ProfileID[table=")
            .append(profileTableName)
            .append(",profile=")
            .append(profileName)
            .append("]");
        return buf.toString();
    }


    private String profileTableName;
    private String profileName;

    private transient Address address;
}

