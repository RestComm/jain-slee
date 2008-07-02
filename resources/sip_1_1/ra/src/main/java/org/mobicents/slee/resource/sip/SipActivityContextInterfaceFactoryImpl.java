package org.mobicents.slee.resource.sip;

import java.util.logging.Logger;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ServerTransaction;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.sip.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.WrapperSuperInterface;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;

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

		ActivityContext AC = this
				.getActivityContextForActivity(clientTransaction);
		return new ActivityContextInterfaceImpl(this.serviceContainer, AC
				.getActivityContextId());
	}

	public ActivityContextInterface getActivityContextInterface(
			ServerTransaction serverTransaction) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		if (serverTransaction == null)
			throw new NullPointerException("sip activity ! huh!!");
		ActivityContext AC = this
				.getActivityContextForActivity(serverTransaction);
		return new ActivityContextInterfaceImpl(this.serviceContainer, AC
				.getActivityContextId());
	}

	public ActivityContextInterface getActivityContextInterface(
			DialogActivity dialog) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
		if (dialog == null) {
			// lets stick to original message plan...
			throw new NullPointerException("sip activity ! huh!!");
		}
		ActivityContext AC = this.getActivityContextForActivity(dialog);
		return new ActivityContextInterfaceImpl(this.serviceContainer, AC
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
	 */
	 ActivityContext getActivityContextForActivity(Object activity) {
		// SIGNIFICANT performance hit? --> let me know.
		// Activity Handle for activity
		SipActivityHandle SAH = null;

		if (activity instanceof ServerTransactionWrapper) {
			SAH = ((ServerTransactionWrapper) activity).getActivityHandle();
		} else if (activity instanceof ClientTransactionWrapper) {
			SAH = ((ClientTransactionWrapper) activity).getActivityHandle();
		} else if (activity instanceof DialogWrapper) {
			SAH = ((DialogWrapper) activity).getActivityHandle();
		} else
			throw new ClassCastException(
					" Activity does not implement javax.sip.Dialog or javax.sip.Transaction!!!");
		
		
		
		
		//ra.addActivity(SAH,(WrapperSuperInterface) activity);
			
		SleeActivityHandle SLAH = new SleeActivityHandle(raEntityName, SAH,
				serviceContainer);
		// Acitvity context
		// NOTE that SLAH is passed, not an activity object
		ActivityContext AC = this.activityContextFactory
				.getActivityContext(SLAH);
		
		

		

		// Slee AH which is proxy like class for wrapped activity handle object
		
		return AC;

	}
}
