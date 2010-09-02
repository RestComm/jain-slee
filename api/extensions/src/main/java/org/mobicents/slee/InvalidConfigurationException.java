package org.mobicents.slee;

/**
 * This exception can be thrown if the configuration properties supplied by the Administrator are not
 * validated by a JAIN SLEE component. 
 */
public class InvalidConfigurationException extends Exception {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = -8461652778978593342L;

	/**
     * Creates an <code>InvalidConfigurationException</code> with a detail message.
     * @param message the detail message.
     */
    public InvalidConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates an <code>InvalidConfigurationException</code> with a detail message
     * and cause.
     * @param message the detail message.
     * @param cause the reason this exception was thrown.
     */
    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
