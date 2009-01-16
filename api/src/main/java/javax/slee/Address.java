package javax.slee;

import java.io.Serializable;

/**
 * The <code>Address</code> class encapsulates an address that can be used by the SLEE.
 * An address consists of the following two components:
 * <ul>
 *   <li>Address Plan - the plan that defines the address or numbering type and
 *       the format of the address string.
 *   <li>Address String - some combination of alphanumeric characters and symbols
 *       that identify an address within the domain identified by the address plan.
 * </ul>
 * <p>
 * An <code>Address</code> object is immutable.  Once it has been created the values
 * contained by it cannot change.
 * <p>
 * <b>Valid Addresses</b>
 * The valid values for address strings is dependent on the address plan for an address.
 * The following is a list of valid address strings for each address plan:
 * <ul>
 * <li>
 * <b>IP address:</b>
 * <BR>For both Ipv4 and IPv6 the dotted quad/hex notation is used.
 * The address can optionally be followed by a port number separated by a colon.
 * <br>For Example: "127.0.0.1:42"
 *
 * <li>
 * <B>MULTICAST address:</B>
 * <BR>This address is an IP address in dotted notation that is either an Ipv4 class D
 * address or an Ipv6 equivalent address.
 * notation.
 * <br>For example: "224.0.0.0"
 *
 * <li>
 * <B>UNICAST address:</B>
 * <BR>This address is an IP address in dotted notation that is not a multicast or
 * broadcast address.
 * notation.
 * <br>For example: "127.0.0.1"
 *
 * <li>
 * <B>E164 address:</B>
 * <BR>This address is an international number without the international access
 * code, but including the country code and area code (without the leading zero).
 * <br>For example: "442890100100" for a UK based number
 *
 * <li>
 * <B>E164_MOBILE address:</B>
 * <BR>This address is an E.164 mobile number.
 * <br>For example: "44791111111" for a UK based mobile number
 *
 * <li>
 * <B>AESA address:</B>
 * <BR>This address is the ATM End System Address in binary format (40 bytes).
 * <br>For example: 01234567890ABCDEF01234567890ABCDEF01234567
 *
 * <li>
 * <B>URI address:</B>
 * <BR>This address is an uniform resource locator as defined in IETF RFC 2396.
 * <br>For example: "http://jcp.org", "mailto:jainslee@jcp.org"
 *
 * <li>
 * <B>NSAP address:</B>
 * <BR>This address is an binary representation of a Network Service
 * Access Point.
 * <br>For example: 490001AA000400010420
 *
 * <li>
 * <B>SMTP address:</B>
 * <BR>This address is an email address as specified in IETF RFC 822.
 * <br>For example: "jain@sun.com"
 *
 * <li>
 * <B>X400 address:</B>
 * <BR>This address is the X400 address structured as a set of attribute
 * value pairs separated by semicolons.
 * <br>For example: "C=nl;ADMD= ;PRMD=uninet;O=sun;S=Foo;I=S;G=Bar'
 *
 * <li>
 * <B>SIP address:</B>
 * <BR>This address is a Session Initiation Protocol address as specified
 * in IETF RFC 2543.
 * <br>For example: "sip:jain@sun.com"
 *
 * <li>
 * <B>H323 address:</B>
 * <BR>This address is an H.323 address.
 * An H.323 address may be identified by a list of addresses of different
 * types, including URLs.
 * <br>For example: "442890100100,http://www.sun.com,jain@sun.com". This would be an
 * H.323 address which is a combination of an E.164 address, a URL address and an
 * email address.
 *
 * <li>
 * <B>GT address:</B>
 * <BR>This address is a Global Title.
 * The format of a Global Title is <tt>GTI.TT.NP.NA.AI</tt> where:
 * <ul>
 *   <tt>GTI&nbsp;</tt> is the Global Title Indicator<br>
 *   <tt>TT&nbsp;&nbsp;</tt> is the Translation Type<br>
 *   <tt>NP&nbsp;&nbsp;</tt> is the Numbering Plan<br>
 *   <tt>NA&nbsp;&nbsp;</tt> is the Nature of Address Indicator<br>
 *   <tt>AI&nbsp;&nbsp;</tt> is the Address Information
 * </ul>
 * <tt>TT</tt>, <tt>NP</tt>, and <tt>NA</tt> are all optional depending on the Global
 * Title Indicator and standard being used.
 * <br>For example: 1.0.0.0.1234
 *
 * <li>
 * <B>SSN address:</B>
 * <BR>This address is a Sub System Number.
 * <br>For example: "6.255.255.255". The 6 equals the sub system number, that is the 6
 * is equal to the Home Location Register.  The sub system number is seperated by a full
 * stop '<tt>.</tt>' from the Signaling Point Code, which must be in the format of X.X.X
 *
 * <li>
 * <B>SLEE Profile Table address:</B>
 * <BR>This address is the name of a SLEE Profile Table.
 * <br>For example: ServiceOptions
 *
 * <li>
 * <B>SLEE Profile address:</B>
 * <BR>This address is the encoded table name and profile name of a SLEE profile.  The
 * names are separated using a forward slash '<tt>/</tt>' character.
 * <br>For example: ServiceOptions/Gold
 *
 * </ul>
 */
public class Address implements Serializable{
    /**
     * Create a new <code>Address</code> using an address plan and address string.
     * @param addressPlan the address plan for this address.
     * @param addressString the address string for this address.
     * @throws NullPointerException if either <code>addressPlan</code> or
     *        <code>addressString</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>addressString</code> is zero-length.
     */
    public Address(AddressPlan addressPlan, String addressString) {
        this(addressPlan, addressString, null, null, null, null);
    }

    /**
     * Create a new <code>Address</code> using an address plan, an address string, an
     * additional optional attributes.
     * @param addressPlan the address plan for this address.
     * @param addressString the address string for this address.
     * @param addressPresentation an optional argument describing whether the address
     *        can be presented to other call parties.
     * @param addressScreening an optional argument describing whether the address has
     *        been screened by a user application.
     * @param subAddressString an optional argument that provides a sub-address string
     *        for protocols that allow sub-addressing.
     * @param addressName an optional argument that allows a more personal name to be
     *        associated with the address.
     * @throws NullPointerException if either <code>addressPlan</code> or
     *        <code>addressString</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>addressString</code> is zero-length.
     */
    public Address(AddressPlan addressPlan, String addressString, AddressPresentation addressPresentation, AddressScreening addressScreening, String subAddressString, String addressName) {
        if (addressPlan == null) throw new NullPointerException("addressPlan is null");
        if (addressString == null) throw new NullPointerException("addressString is null");

        if (addressString.length() == 0)
            throw new IllegalArgumentException("addressString is zero-length");

        this.addressPlan = addressPlan;
        this.addressString = addressString;
        this.addressPresentation = addressPresentation;
        this.addressScreening = addressScreening;
        this.subAddressString = subAddressString;
        this.addressName = addressName;
    }

    /**
     * Get the address plan for the address.
     * @return the address plan.
     */
    public AddressPlan getAddressPlan() {
        return addressPlan;
    }

    /**
     * Get the address string for the address.
     * @return the address string.
     */
    public String getAddressString() {
        return addressString;
    }

    /**
     * Get the optional address presentation attribute for the address.
     * @return the address presentation attribute, or <code>null</code> if no address
     *        presentation attribute was specified.
     */
    public AddressPresentation getAddressPresentation() {
        return addressPresentation;
    }

    /**
     * Get the optional address screening attribute for the address.
     * @return the address screening attribute, or <code>null</code> if no address
     *        screening attribute was specified.
     */
    public AddressScreening getAddressScreening() {
        return addressScreening;
    }

    /**
     * Get the optional sub-address string for the address.
     * @return the sub-address string, or <code>null</code> if no sub-address string
     *        was specified.
     */
    public String getSubAddressString() {
        return subAddressString;
    }

    /**
     * Get the optional address name for the address.
     * @return the address name, or <code>null</code> if no address name was specified.
     */
    public String getAddressName() {
        return addressName;
    }

    /**
     * Compare this address object for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        containing the same address plan and address string as this,
     *        <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Address)) return false;

        Address that = (Address)obj;
        return (this.addressPlan.equals(that.addressPlan))
            && (this.addressString.equals(that.addressString));
    }

    /**
     * Get a hash code value for this address object.
     * @return a hash code value.
     */
    public int hashCode( ) {
        return addressString.hashCode() ^ (addressPlan.toInt() << 15);
    }

    /**
     * Get the textual representation of the address object.  The format of the returned
     * string is: <i>&lt;address-plan&gt;</i>: <i>&lt;address-string&gt;</i>
     * @return the textual representation of the address object.
     */
    public String toString () {
        return (addressPlan.toString() + ": " + addressString);
    }


    private final AddressPlan addressPlan;
    private final String addressString;
    private final AddressPresentation addressPresentation;
    private final AddressScreening addressScreening;
    private final String subAddressString;
    private final String addressName;
}

