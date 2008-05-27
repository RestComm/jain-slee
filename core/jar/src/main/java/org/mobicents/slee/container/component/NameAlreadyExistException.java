/*
 * Created on Oct 26, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.component;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class NameAlreadyExistException extends Exception {

    public NameAlreadyExistException(String string) {
        super(string);
    }

	public NameAlreadyExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NameAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NameAlreadyExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
