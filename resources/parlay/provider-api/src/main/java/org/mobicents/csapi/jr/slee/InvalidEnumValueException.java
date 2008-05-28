

package org.mobicents.csapi.jr.slee;

/**
 * Thrown when an enum data type is accessed with an invalid request value 
 */
public class InvalidEnumValueException extends org.mobicents.csapi.jr.slee.InvalidArgumentException {

    private Throwable _cause = null;

    public InvalidEnumValueException() {
        super(); 
    }

    public InvalidEnumValueException(String message) {
        super(message); 
    }

    public InvalidEnumValueException(String message, Throwable cause) {
        super(message, cause); 
    }

    public InvalidEnumValueException(Throwable cause) {
        _cause = cause; 
    }

} // InvalidEnumValueException

