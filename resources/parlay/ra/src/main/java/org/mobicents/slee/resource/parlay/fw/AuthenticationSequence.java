package org.mobicents.slee.resource.parlay.fw;


/**
 * Defines allowable authentication sequences.
 * 
 *  JDK 1.5 replace with enum
 */
public class AuthenticationSequence {

    /**
     * One way authentication (not normally used but here for legacy support)
     */
    public static final AuthenticationSequence ONE_WAY = new AuthenticationSequence("ONE_WAY");

    /**
     * Standard untrusted parlay authentication sequence. Requires off line
     * provisioning e.g. for RSA keys and certificates.
     */
    public static final AuthenticationSequence TWO_WAY = new AuthenticationSequence("TWO_WAY");

    /**
     * For trusted clients. Only provisioning required is framework is aware of
     * client domain ID.
     */
    public static final AuthenticationSequence TRUSTED = new AuthenticationSequence("TRUSTED");

    /**
     * Indicates the authentication sequence selected.
     */
    private String type = null;

    /**
     * Private constructor.
     */
    private AuthenticationSequence(String type) {
        this.type = type;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && obj.getClass().equals(AuthenticationSequence.class)) {
            AuthenticationSequence rhs = (AuthenticationSequence) obj;
            if (rhs.type.equals(this.type)) {
                result = true;
            }
        }
        return result;
        
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return type.hashCode();
    }

    /**
     * Utility method to construct an instance of this class from a string in a
     * properties file.
     * 
     * @param sequence
     *            A string identifying the authentication sequence.
     * 
     * @return an instance of this class
     */
    public static AuthenticationSequence getAuthenticationSequence(
            String sequence) {
        AuthenticationSequence result = null;
        if (sequence.equals("ONE_WAY")) {
            result = ONE_WAY;
        } else if (sequence.equals("TWO_WAY")) {
            result = TWO_WAY;
        } else if (sequence.equals("TRUSTED")) {
            result = TRUSTED;
        }
        return result;
    }
    
    public String getType() {
        return type;
    }

}