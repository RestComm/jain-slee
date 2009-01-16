package javax.slee;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * This class defines an enumerated type that encapsulates the values available for
 * address or numbering plans.  An address plan specifies the format and structure
 * of an associated address string.  A numbering plan typically consists of decimal
 * digits segmented into groups in order to identify specific elements used for
 * identification, routing, and charging capabilities.
 * <p>
 * A singleton instance of each enumerated value is guaranteed (via an implementation
 * of <code>readResolve()</code> - refer {@link java.io.Serializable java.io.Serializable}),
 * so that equality tests using <code>==</code> are always evaluated correctly.  (This
 * equality test is only guaranteed if this class is loaded in the application's boot class
 * path, rather than dynamically loaded at runtime.)
 * @see Address
 */
public final class AddressPlan implements Serializable {
    /**
     * An integer representation of {@link #NOT_PRESENT}.
     */
    public static final int ADDRESS_PLAN_NOT_PRESENT = -1;

    /**
     * An integer representation of {@link #UNDEFINED}.
     */
    public static final int ADDRESS_PLAN_UNDEFINED = 0;

    /**
     * An integer representation of {@link #IP}.
     */
    public static final int ADDRESS_PLAN_IP = 1;

    /**
     * An integer representation of {@link #MULTICAST}.
     */
    public static final int ADDRESS_PLAN_MULTICAST = 2;

    /**
     * An integer representation of {@link #UNICAST}.
     */
    public static final int ADDRESS_PLAN_UNICAST = 3;

    /**
     * An integer representation of {@link #E164}.
     */
    public static final int ADDRESS_PLAN_E164 = 4;

    /**
     * An integer representation of {@link #AESA}.
     */
    public static final int ADDRESS_PLAN_AESA = 5;

    /**
     * An integer representation of {@link #URI}.
     */
    public static final int ADDRESS_PLAN_URI = 6;

    /**
     * An integer representation of {@link #NSAP}.
     */
    public static final int ADDRESS_PLAN_NSAP = 7;

    /**
     * An integer representation of {@link #SMTP}.
     */
    public static final int ADDRESS_PLAN_SMTP = 8;

    /**
     * An integer representation of {@link #X400}.
     */
    public static final int ADDRESS_PLAN_X400 = 10;

    /**
     * An integer representation of {@link #SIP}.
     */
    public static final int ADDRESS_PLAN_SIP = 11;

    /**
     * An integer representation of {@link #E164_MOBILE}.
     */
    public static final int ADDRESS_PLAN_E164_MOBILE = 13;

    /**
     * An integer representation of {@link #H323}.
     */
    public static final int ADDRESS_PLAN_H323 = 14;

    /**
     * An integer representation of {@link #GT}.
     */
    public static final int ADDRESS_PLAN_GT = 15;

    /**
     * An integer representation of {@link #SSN}.
     */
    public static final int ADDRESS_PLAN_SSN = 16;

    /**
     * An integer representation of {@link #SLEE_PROFILE_TABLE}.
     */
    public static final int ADDRESS_PLAN_SLEE_PROFILE_TABLE = 20;

    /**
     * An integer representation of {@link #SLEE_PROFILE}.
     */
    public static final int ADDRESS_PLAN_SLEE_PROFILE = 21;


    /**
     * A string representation of {@link #NOT_PRESENT}.
     */
    public static final String NOT_PRESENT_STRING = "Not Present";

    /**
     * A string representation of {@link #UNDEFINED}.
     */
    public static final String UNDEFINED_STRING = "Undefined";

    /**
     * A string representation of {@link #IP}.
     */
    public static final String IP_STRING = "IP";

    /**
     * A string representation of {@link #MULTICAST}.
     */
    public static final String MULTICAST_STRING = "Multicast";

    /**
     * A string representation of {@link #UNICAST}.
     */
    public static final String UNICAST_STRING = "Unicast";

    /**
     * A string representation of {@link #E164}.
     */
    public static final String E164_STRING = "E.164";

    /**
     * A string representation of {@link #AESA}.
     */
    public static final String AESA_STRING = "AESA";

    /**
     * A string representation of {@link #URI}.
     */
    public static final String URI_STRING = "URI";

    /**
     * A string representation of {@link #NSAP}.
     */
    public static final String NSAP_STRING = "NSAP";

    /**
     * A string representation of {@link #SMTP}.
     */
    public static final String SMTP_STRING = "SMTP";

    /**
     * A string representation of {@link #E164_MOBILE}.
     */
    public static final String E164_MOBILE_STRING = "E.164 Mobile";

    /**
     * A string representation of {@link #X400}.
     */
    public static final String X400_STRING = "X400";

    /**
     * A string representation of {@link #SIP}.
     */
    public static final String SIP_STRING = "SIP";

    /**
     * A string representation of {@link #H323}.
     */
    public static final String H323_STRING = "H323";

    /**
     * A string representation of {@link #GT}.
     */
    public static final String GT_STRING = "GT";

    /**
     * A string representation of {@link #SSN}.
     */
    public static final String SSN_STRING = "SSN";

    /**
     * A string representation of {@link #SLEE_PROFILE_TABLE}.
     */
    public static final String SLEE_PROFILE_TABLE_STRING = "SLEE ProfileTable";

    /**
     * A string representation of {@link #SLEE_PROFILE}.
     */
    public static final String SLEE_PROFILE_STRING = "SLEE Profile";


    /**
     * The NOT_PRESENT value indicates that the address is not present.
     */
    public static final AddressPlan NOT_PRESENT = new AddressPlan(ADDRESS_PLAN_NOT_PRESENT);

     /**
     * The UNDEFINED value indicates that the address is undefined.
     */
    public static final AddressPlan UNDEFINED = new AddressPlan(ADDRESS_PLAN_UNDEFINED);

    /**
     * The IP value indicates that the address is an IP address
     * in dotted notation.
     */
    public static final AddressPlan IP = new AddressPlan(ADDRESS_PLAN_IP);

    /**
     * The MULTICAST value indicates that the address is an IP address in
     * dotted notation that is either an IPv4 class D address or an equivalent IPv6
     * address.
     */
    public static final AddressPlan MULTICAST = new AddressPlan(ADDRESS_PLAN_MULTICAST);

    /**
     * The UNICAST value indicates that the address is an IP address
     * in dotted notation that is not a multicast or broadcast address.
     */
    public static final AddressPlan UNICAST = new AddressPlan(ADDRESS_PLAN_UNICAST);

    /**
     * The E164 value indicates that the address is an international
     * number without the international access code, but including the country
     * code and area code (without the leading zero).
     */
    public static final AddressPlan E164 = new AddressPlan(ADDRESS_PLAN_E164);

    /**
     * The AESA value indicates that the address is an ATM End
     * System Address in binary format (40 bytes).
     */
    public static final AddressPlan AESA = new AddressPlan(ADDRESS_PLAN_AESA);

    /**
     * The URI value indicates that the address is a uniform resource
     * locator as defined in IETF RFC 2396.
     */
    public static final AddressPlan URI = new AddressPlan(ADDRESS_PLAN_URI);

    /**
     * The NSAP value indicates that the address is a binary
     * representation of a Network Service Access Point.
     */
    public static final AddressPlan NSAP = new AddressPlan(ADDRESS_PLAN_NSAP);

    /**
     * The SMTP value indicates that the address is an email address
     * as defined in IETF RFC 822.
     */
    public static final AddressPlan SMTP = new AddressPlan(ADDRESS_PLAN_SMTP);

    /**
     * The E164_MOBILE value indicates that the address is an E.164
     * mobile number.
     */
    public static final AddressPlan E164_MOBILE = new AddressPlan(ADDRESS_PLAN_E164_MOBILE);

    /**
     * The X400 value indicates that the address is an X400 address
     * structured as a set of attribute value pairs separated by semicolons.
     */
    public static final AddressPlan X400 = new AddressPlan(ADDRESS_PLAN_X400);

    /**
     * The SIP value indicates that the address is a Session Initiation
     * Protocol address as specified in IETF RFC 2543.
     */
    public static final AddressPlan SIP = new AddressPlan(ADDRESS_PLAN_SIP);

    /**
     * The H323 value indicates that the address is an H.323 address.
     * An H.323 address may be identified by a list of addresses of different
     * types, including URLs.
     */
    public static final AddressPlan H323 = new AddressPlan(ADDRESS_PLAN_H323);

    /**
     * The GT value indicates that the address is a Global Title.
     */
    public static final AddressPlan GT = new AddressPlan(ADDRESS_PLAN_GT);

    /**
     * The SSN value indicates that the address is a Sub System Number.
     */
    public static final AddressPlan SSN = new AddressPlan(ADDRESS_PLAN_SSN);

    /**
     * The SLEE_PROFILE_TABLE value indicates that the address is the
     * name of SLEE profile table.
     */
    public static final AddressPlan SLEE_PROFILE_TABLE = new AddressPlan(ADDRESS_PLAN_SLEE_PROFILE_TABLE);

    /**
     * The SLEE_PROFILE value indicates that the address is the encoded
     * table name and profile name of a SLEE profile.
     */
    public static final AddressPlan SLEE_PROFILE = new AddressPlan(ADDRESS_PLAN_SLEE_PROFILE);

    /**
     * Get an <code>AddressPlan</code> object from an integer value.
     * @param plan the address plan as an integer.
     * @return an <code>AddressPlan</code> object corresponding to <code>plan</code>.
     * @throws IllegalArgumentException if <code>plan</code> is not a valid
     *        address plan value.
     */
    public static AddressPlan fromInt(int plan) throws IllegalArgumentException {
        switch (plan) {
            case ADDRESS_PLAN_NOT_PRESENT: return NOT_PRESENT;
            case ADDRESS_PLAN_UNDEFINED: return UNDEFINED;
            case ADDRESS_PLAN_IP: return IP;
            case ADDRESS_PLAN_MULTICAST: return MULTICAST;
            case ADDRESS_PLAN_UNICAST: return UNICAST;
            case ADDRESS_PLAN_E164: return E164;
            case ADDRESS_PLAN_AESA: return AESA;
            case ADDRESS_PLAN_URI: return URI;
            case ADDRESS_PLAN_NSAP: return NSAP;
            case ADDRESS_PLAN_SMTP: return SMTP;
            case ADDRESS_PLAN_X400: return X400;
            case ADDRESS_PLAN_SIP: return SIP;
            case ADDRESS_PLAN_E164_MOBILE: return E164_MOBILE;
            case ADDRESS_PLAN_H323: return H323;
            case ADDRESS_PLAN_GT: return GT;
            case ADDRESS_PLAN_SSN: return SSN;
            case ADDRESS_PLAN_SLEE_PROFILE_TABLE: return SLEE_PROFILE_TABLE;
            case ADDRESS_PLAN_SLEE_PROFILE: return SLEE_PROFILE;
            default: throw new IllegalArgumentException("Invalid address plan: " + plan);
        }
    }

    /**
     * Get an <code>AddressPlan</code> object from a string value.
     * @param plan the address plan as a string, for example as returned by the {@link #toString()}
     *        method (case insensitive).
     * @return an <code>AddressPlan</code> object corresponding to <code>plan</code>.
     * @throws NullPointerException if <code>plan</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>plan</code> is not a valid
     *        address plan value.
     * @since SLEE 1.1
     */
    public static AddressPlan fromString(String plan) throws NullPointerException, IllegalArgumentException {
        if (plan == null) throw new NullPointerException("plan is null");
        if (plan.equalsIgnoreCase(NOT_PRESENT_STRING)) return NOT_PRESENT;
        if (plan.equalsIgnoreCase(UNDEFINED_STRING)) return UNDEFINED;
        if (plan.equalsIgnoreCase(IP_STRING)) return IP;
        if (plan.equalsIgnoreCase(MULTICAST_STRING)) return MULTICAST;
        if (plan.equalsIgnoreCase(UNICAST_STRING)) return UNICAST;
        if (plan.equalsIgnoreCase(E164_STRING)) return E164;
        if (plan.equalsIgnoreCase(AESA_STRING)) return AESA;
        if (plan.equalsIgnoreCase(URI_STRING)) return URI;
        if (plan.equalsIgnoreCase(NSAP_STRING)) return NSAP;
        if (plan.equalsIgnoreCase(SMTP_STRING)) return SMTP;
        if (plan.equalsIgnoreCase(X400_STRING)) return X400;
        if (plan.equalsIgnoreCase(SIP_STRING)) return SIP;
        if (plan.equalsIgnoreCase(E164_MOBILE_STRING)) return E164_MOBILE;
        if (plan.equalsIgnoreCase(H323_STRING)) return H323;
        if (plan.equalsIgnoreCase(GT_STRING)) return GT;
        if (plan.equalsIgnoreCase(SSN_STRING)) return SSN;
        if (plan.equalsIgnoreCase(SLEE_PROFILE_TABLE_STRING)) return SLEE_PROFILE_TABLE;
        if (plan.equalsIgnoreCase(SLEE_PROFILE_STRING)) return SLEE_PROFILE;
        throw new IllegalArgumentException("Invalid address plan: " + plan);
    }

    /**
     * Get an integer value representation for this <code>AddressPlan</code> object.
     * @return an integer value representation for this <code>AddressPlan</code> object.
     */
    public int toInt() {
        return plan;
    }

    /**
     * Determine if this AddressPlan object represents the NOT_PRESENT address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == NOT_PRESENT)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isNotPresent()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.NOT_PRESENT) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the NOT_PRESENT address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isNotPresent() {
        return plan == ADDRESS_PLAN_NOT_PRESENT;
    }

    /**
     * Determine if this AddressPlan object represents the UNDEFINED address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == UNDEFINED)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isUndefined()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.UNDEFINED) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the UNDEFINED address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isUndefined() {
        return plan == ADDRESS_PLAN_UNDEFINED;
    }

    /**
     * Determine if this AddressPlan object represents the IP address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == IP)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isIP()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.IP) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the IP address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isIP() {
        return plan == ADDRESS_PLAN_IP;
    }

    /**
     * Determine if this AddressPlan object represents the MULTICAST address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == MULTICAST)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isMulticast()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.MULTICAST) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the MULTICAST address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isMulticast() {
        return plan == ADDRESS_PLAN_MULTICAST;
    }

    /**
     * Determine if this AddressPlan object represents the UNICAST address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == UNICAST)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isUnicast()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.UNICAST) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the UNICAST address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isUnicast() {
        return plan == ADDRESS_PLAN_UNICAST;
    }

    /**
     * Determine if this AddressPlan object represents the E164 address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == E164)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isE164()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.E164) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the E164 address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isE164() {
        return plan == ADDRESS_PLAN_E164;
    }

    /**
     * Determine if this AddressPlan object represents the AESA address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == AESA)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isAESA()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.AESA) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the AESA address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isAESA() {
        return plan == ADDRESS_PLAN_AESA;
    }

    /**
     * Determine if this AddressPlan object represents the URI address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == URI)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isURI()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.URI) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the URI address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isURI() {
        return plan == ADDRESS_PLAN_URI;
    }

    /**
     * Determine if this AddressPlan object represents the NSAP address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == NSAP)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isNSAP()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.NSAP) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the NSAP address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isNSAP() {
        return plan == ADDRESS_PLAN_NSAP;
    }

    /**
     * Determine if this AddressPlan object represents the SMTP address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == SMTP)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isSMTP()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.SMTP) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the SMTP address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isSMTP() {
        return plan == ADDRESS_PLAN_SMTP;
    }

    /**
     * Determine if this AddressPlan object represents the X400 address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == X400)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isX400()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.X400) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the X400 address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isX400() {
        return plan == ADDRESS_PLAN_X400;
    }

    /**
     * Determine if this AddressPlan object represents the SIP address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == SIP)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isSIP()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.SIP) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the SIP address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isSIP() {
        return plan == ADDRESS_PLAN_SIP;
    }

    /**
     * Determine if this AddressPlan object represents the E164_MOBILE address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == E164_MOBILE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isE164Mobile()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.E164_MOBILE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the E164_MOBILE address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isE164Mobile() {
        return plan == ADDRESS_PLAN_E164_MOBILE;
    }

    /**
     * Determine if this AddressPlan object represents the H323 address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == H323)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isH323()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.H323) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the H323 address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isH323() {
        return plan == ADDRESS_PLAN_H323;
    }

    /**
     * Determine if this AddressPlan object represents the GT address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == GT)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isGT()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.GT) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the GT address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isGT() {
        return plan == ADDRESS_PLAN_GT;
    }

    /**
     * Determine if this AddressPlan object represents the SSN address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == SSN)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isSSN()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.SSN) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the SSN address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isSSN() {
        return plan == ADDRESS_PLAN_SSN;
    }

    /**
     * Determine if this AddressPlan object represents the SLEE_PROFILE_TABLE address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == SLEE_PROFILE_TABLE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isSleeProfileTable()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.SLEE_PROFILE_TABLE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the SLEE_PROFILE_TABLE address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isSleeProfileTable() {
        return plan == ADDRESS_PLAN_SLEE_PROFILE_TABLE;
    }

    /**
     * Determine if this AddressPlan object represents the SLEE_PROFILE address
     * plan value.
     * <p>
     * This method is effectively equivalent to the conditional test:
     * <code>(this == SLEE_PROFILE)</code>, ie. the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;if (plan.isSleeProfile()) ...</code>
     * <p>
     * is interchangeable with the code:
     * <p>
     * <code>&nbsp;&nbsp;&nbsp;if (plan == AddressPlan.SLEE_PROFILE) ...</code>
     * <p>
     * @return <code>true</code> if this object represents the SLEE_PROFILE address plan
     *       value, <code>false</code> otherwise.
     */
    public boolean isSleeProfile() {
        return plan == ADDRESS_PLAN_SLEE_PROFILE;
    }

    /**
     * Compare this address plan for equality with another.
     * @param obj the object to compare this with.
     * @return <code>true</code> if <code>obj</code> is an instance of this class
     *        representing the same address plan value as this,
     *        <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return (obj instanceof AddressPlan) && ((AddressPlan)obj).plan == plan;
    }

    /**
     * Get a hash code value for this address plan.
     * @return a hash code value.
     */
    public int hashCode() {
        return plan;
    }

    /**
     * Get the string representation of the address plan object.
     * @return the string representation of the address plan object.
     */
    public String toString() {
        switch (plan) {
            case ADDRESS_PLAN_NOT_PRESENT: return NOT_PRESENT_STRING;
            case ADDRESS_PLAN_UNDEFINED: return UNDEFINED_STRING;
            case ADDRESS_PLAN_IP: return IP_STRING;
            case ADDRESS_PLAN_MULTICAST: return MULTICAST_STRING;
            case ADDRESS_PLAN_UNICAST: return UNICAST_STRING;
            case ADDRESS_PLAN_E164: return E164_STRING;
            case ADDRESS_PLAN_AESA: return AESA_STRING;
            case ADDRESS_PLAN_URI: return URI_STRING;
            case ADDRESS_PLAN_NSAP: return NSAP_STRING;
            case ADDRESS_PLAN_SMTP: return SMTP_STRING;
            case ADDRESS_PLAN_X400: return X400_STRING;
            case ADDRESS_PLAN_SIP: return SIP_STRING;
            case ADDRESS_PLAN_E164_MOBILE: return E164_MOBILE_STRING;
            case ADDRESS_PLAN_H323: return H323_STRING;
            case ADDRESS_PLAN_GT: return GT_STRING;
            case ADDRESS_PLAN_SSN: return SSN_STRING;
            case ADDRESS_PLAN_SLEE_PROFILE_TABLE: return SLEE_PROFILE_TABLE_STRING;
            case ADDRESS_PLAN_SLEE_PROFILE: return SLEE_PROFILE_STRING;
            default: return "AddressPlan in Unknown and Invalid State";
        }
    }

    /**
     * Private constructor to prevent unauthorized object creation.
     */
    private AddressPlan(int plan) {
        this.plan = plan;
    }

    /**
     * Resolve deserialisation references so that the singleton property of each
     * enumerated object is preserved.
     */
    private Object readResolve() throws StreamCorruptedException {
        try {
            return fromInt(plan);
        }
        catch (IllegalArgumentException iae) {
            throw new StreamCorruptedException("Invalid internal state found");
        }
    }


    private final int plan;
}
