
package org.mobicents.csapi.jr.slee;

/**
 * Thrown when an invalid argument is set
 */
public class InvalidArgumentException extends java.lang.Exception {

    private Throwable _cause = null;

    public InvalidArgumentException() {
        super(); 
    }

    public InvalidArgumentException(String message) {
        super(message); 
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message); 
        _cause = cause;
    }

    public InvalidArgumentException(Throwable cause) {
        _cause = cause; 
    }

    public Throwable getCause() {
        return _cause;
    } 

} // InvalidArgumentException

