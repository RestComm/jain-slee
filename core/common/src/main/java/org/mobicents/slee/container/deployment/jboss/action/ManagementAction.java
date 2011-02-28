package org.mobicents.slee.container.deployment.jboss.action;

/**
 * A management action to be executed by the internal deployer.
 * 
 * @author martins
 * 
 */
public interface ManagementAction {
	
	/**
	 * the type of action
	 * 
	 * @author martins
	 * 
	 */
	public enum Type {
		DEPLOY_MANAGEMENT, RESOURCE_MANAGEMENT, SERVICE_MANAGEMENT
	}

	/**
	 * Executes the action.
	 * 
	 * @throws Exception
	 */
	public void invoke() throws Exception;

	/**
	 * Retrieves the action type.
	 * 
	 * @return
	 */
	public Type getType();

}
