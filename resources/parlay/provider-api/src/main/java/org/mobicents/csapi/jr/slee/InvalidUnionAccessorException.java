
package org.mobicents.csapi.jr.slee;

/**
 * Thrown when the expected data element has not been set
 */
public class InvalidUnionAccessorException extends org.mobicents.csapi.jr.slee.InvalidArgumentException {

    private Throwable _cause = null;

    public InvalidUnionAccessorException() {
        super(); 
    }

    public InvalidUnionAccessorException(String message) {
        super(message); 
    }

    public InvalidUnionAccessorException(String message, Throwable cause) {
        super(message, cause); 
    }

    public InvalidUnionAccessorException(Throwable cause) {
        _cause = cause; 
    }

} // InvalidUnionAccessorException

