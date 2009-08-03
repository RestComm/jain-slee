package org.mobicents.slee.resource;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.StartActivityException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class SleeEndpointStartActivityNotTransactedExecutor {
					
	private final SleeContainer sleeContainer;
	
	private final SleeEndpointImpl sleeEndpoint;
		
	public SleeEndpointStartActivityNotTransactedExecutor(SleeContainer sleeContainer,SleeEndpointImpl sleeEndpoint) {
		this.sleeContainer = sleeContainer;
		this.sleeEndpoint = sleeEndpoint;
	}
	
	void execute(final ActivityHandle handle, final int activityFlags) throws StartActivityException,
	SLEEException {
		
		final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
		
		final Runnable runnable = new Runnable() {
			
			public void run() {
				boolean rollback = true;
				try {
					txManager.begin();
					sleeEndpoint._startActivity(handle, activityFlags);	            	
			        rollback = false;            	
				} catch (NotSupportedException e) {
					throw new SLEEException(e.getMessage(),e);
				} catch (SystemException e) {
					throw new SLEEException(e.getMessage(),e);
				}
				finally {		        		
					try {
						if (rollback) {
							txManager.rollback();
						}
						else {
							txManager.commit();
						}
					} catch (Throwable e) {
						throw new SLEEException(e.getMessage(),e);
					}
		        }		
			}
		};
		
		Transaction tx = null;
		try {
			tx = txManager.getTransaction();
			if (tx != null) {
				txManager.suspend();
			}
			runnable.run();
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}
		finally {
			if (tx != null) {
				try {
					txManager.resume(tx);
				}
				catch (Throwable e) {
					throw new SLEEException(e.getMessage(),e);
				}
			}
		}
	}
	
}
