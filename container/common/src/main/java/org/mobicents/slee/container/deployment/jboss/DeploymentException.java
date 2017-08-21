package org.mobicents.slee.container.deployment.jboss;

public class DeploymentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DeploymentException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeploymentException(String message) {
		super(message);
	}

	public DeploymentException(Throwable cause) {
		super(cause);
	}

}
