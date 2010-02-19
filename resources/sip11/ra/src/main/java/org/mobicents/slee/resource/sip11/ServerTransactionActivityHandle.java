/**
 * 
 */
package org.mobicents.slee.resource.sip11;

/**
 * @author martins
 *
 */
public class ServerTransactionActivityHandle extends TransactionActivityHandle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final Class<?> TYPE = ServerTransactionActivityHandle.class;
	
	/**
	 * @param branchId
	 * @param method
	 */
	public ServerTransactionActivityHandle(String branchId, String method) {
		super(branchId, method);
	}

}
