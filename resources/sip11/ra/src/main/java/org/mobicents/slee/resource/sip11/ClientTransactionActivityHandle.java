/**
 * 
 */
package org.mobicents.slee.resource.sip11;

/**
 * @author martins
 *
 */
public class ClientTransactionActivityHandle extends TransactionActivityHandle {

	public static final Class<?> TYPE = ClientTransactionActivityHandle.class;

	/**
	 * @param branchId
	 * @param method
	 */
	public ClientTransactionActivityHandle(String branchId, String method) {
		super(branchId, method);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isReplicated() {
		return false;
	}
}
