package org.mobicents.slee.resource.sip11;

import java.util.logging.Logger;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.message.Request;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.transaction.TransactionManager;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.sip11.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class SipActivityContextInterfaceFactoryImpl implements
		SipActivityContextInterfaceFactory,
		ResourceAdaptorActivityContextInterfaceFactory {

	protected final String jndiName;
	protected String raEntityName;
	protected SleeContainer serviceContainer;
	protected TransactionManager txMgr = null;
	protected ActivityContextFactory activityContextFactory;
	protected SipResourceAdaptor ra=null;
	protected final static Logger logger=Logger.getLogger(SipActivityContextInterfaceFactoryImpl.class.getCanonicalName());
	public SipActivityContextInterfaceFactoryImpl(SleeContainer svcContainer,
			String entityName, SipResourceAdaptor ra, SleeTransactionManager sleeTransactionManager) {
		this.ra=ra;
		this.serviceContainer = svcContainer;
		this.activityContextFactory = svcContainer.getActivityContextFactory();
		this.raEntityName = entityName;
		this.jndiName = "java:slee/resources/" + entityName + "/sipacif";
		this.txMgr=sleeTransactionManager;

	}

	public String getJndiName() {
		// TODO Auto-generated method stub
		return jndiName;
	}

	public ActivityContextInterface getActivityContextInterface(
			ClientTransaction clientTransaction) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		if (clientTransaction == null)
			throw new NullPointerException("sip activity ! huh!!");

		return new ActivityContextInterfaceImpl(this.serviceContainer, this
				.getActivityContextForActivity(clientTransaction, ((ClientTransactionWrapper)clientTransaction).getActivityHandle())
				.getActivityContextId());
	}

	public ActivityContextInterface getActivityContextInterface(
			ServerTransaction serverTransaction) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		if (serverTransaction == null)
			throw new NullPointerException("sip activity ! huh!!");
		return new ActivityContextInterfaceImpl(this.serviceContainer, this
				.getActivityContextForActivity(serverTransaction, ((ServerTransactionWrapper)serverTransaction).getActivityHandle())
				.getActivityContextId());
	}

	public ActivityContextInterface getActivityContextInterface(
			DialogActivity dialog) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		
		if (dialog == null) {
			// lets stick to original message plan...
			throw new NullPointerException("sip activity ! huh!!");
		}
		
		return new ActivityContextInterfaceImpl(this.serviceContainer, this
				.getActivityContextForActivity(dialog, ((DialogWrapper)dialog).getActivityHandle())
				.getActivityContextId());

	}

	/**
	 * Conveniant method to wrap same code for factory methods.
	 * 
	 * @param activity -
	 *            Activity object for which we will return AC, should implement
	 *            one of interfaces: <b>javax.sip.Dialog</b> or
	 *            <b>javax.sip.Transaction</b> .
	 * @return ActovityContext for passed object.
	 * @throws UnrecognizedActivityException 
	 */
	 ActivityContext getActivityContextForActivity(Object activity, SipActivityHandle activityHandle) throws UnrecognizedActivityException {
		
		if (ra.getActivity(activityHandle) == null) {
			throw new UnrecognizedActivityException("Handle: "+activityHandle,activity);	
		}
		else {
			return this.activityContextFactory.getActivityContext(new SleeActivityHandle(raEntityName,activityHandle,serviceContainer));
		}
	 }
}
