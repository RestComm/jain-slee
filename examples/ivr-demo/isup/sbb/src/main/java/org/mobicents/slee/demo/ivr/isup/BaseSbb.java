package org.mobicents.slee.demo.ivr.isup;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;

import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.slee.resources.ss7.isup.ratype.RAISUPProvider;

/**
 * 
 * @author amit bhayani
 */
public abstract class BaseSbb implements Sbb {

	protected static int CALL_ID_GEN = 1;
	protected static int GEN = 2000;

	public final static String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");
	public final static String ENDPOINT_NAME = "/mobicents/media/IVR/$";

	protected SbbContext sbbContext;

	protected JainMgcpProvider mgcpProvider;
	protected MgcpActivityContextInterfaceFactory mgcpAcif;

	protected Tracer tracer;

	protected RAISUPProvider isupProvider;
	protected ISUPMessageFactory isupMessageFactory;
	protected ISUPParameterFactory isupParameterFactory;
	protected org.mobicents.slee.resources.ss7.isup.ratype.ActivityContextInterfaceFactory acif;

	protected String callIdentifier;

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		tracer = sbbContext.getTracer("Call Controller");
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize ISUP API
			isupProvider = (RAISUPProvider) ctx.lookup("slee/resources/isup/1.0/provider");
			isupMessageFactory = isupProvider.getMessageFactory();
			isupParameterFactory = isupProvider.getParameterFactory();
			acif = (org.mobicents.slee.resources.ss7.isup.ratype.ActivityContextInterfaceFactory) ctx
					.lookup("slee/resources/isup/1.0/acifactory");

			// initialize media api
			mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider");
			mgcpAcif = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory");

		} catch (Exception ne) {
			tracer.severe("Could not set SBB context:", ne);
		}
	}

	public void unsetSbbContext() {
		sbbContext = null;
	}

	protected ActivityContextInterface getConnectionActivity() {
		ActivityContextInterface activities[] = sbbContext.getActivities();
		for (ActivityContextInterface aci : activities) {
			if (aci.getActivity() instanceof MgcpConnectionActivity) {
				return aci;
			}
		}
		return null;
	}

	protected ActivityContextInterface getISUPServerTxActivity() {
		ActivityContextInterface activities[] = sbbContext.getActivities();
		for (ActivityContextInterface aci : activities) {
			if (aci.getActivity() instanceof ISUPServerTransaction) {
				return aci;
			}
		}
		return null;
	}

	public abstract IsupConnection asSbbActivityContextInterface(ActivityContextInterface aci);

	protected IsupConnectionState getState() {
		ActivityContextInterface activity = this.getISUPServerTxActivity();
		return this.asSbbActivityContextInterface(activity).getState();
	}

	protected void setState(IsupConnectionState state) {
		ActivityContextInterface activity = this.getISUPServerTxActivity();
		asSbbActivityContextInterface(activity).setState(state);
	}

	protected String getEndpointID() {
		ActivityContextInterface activity = this.getISUPServerTxActivity();
		return this.asSbbActivityContextInterface(activity).getEndpoint();
	}

	protected void setEndpoint(String endpoint) {
		ActivityContextInterface activity = this.getISUPServerTxActivity();
		asSbbActivityContextInterface(activity).setEndpoint(endpoint);
	}

	protected String getConnectionID() {
		ActivityContextInterface activity = this.getISUPServerTxActivity();
		return this.asSbbActivityContextInterface(activity).getConnectionID();
	}

	protected void setConnectionID(String connectionID) {
		ActivityContextInterface activity = this.getISUPServerTxActivity();
		asSbbActivityContextInterface(activity).setConnectionID(connectionID);
	}

	protected String getCallID() {
		ActivityContextInterface activity = this.getISUPServerTxActivity();
		return this.asSbbActivityContextInterface(activity).getCallID();
	}

	protected void setCallID(String callID) {
		ActivityContextInterface activity = this.getISUPServerTxActivity();
		asSbbActivityContextInterface(activity).setCallID(callID);
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbPostCreate() throws CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbRemove() {
	}

	public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
	}

	public void sbbRolledBack(RolledBackContext arg0) {
	}
	
	
	public InitialEventSelector cicSelect(InitialEventSelector ies) {
		Object event = ies.getEvent();
		if (event instanceof ISUPMessage) {
			ISUPMessage isupMessage = ((ISUPMessage) event);
			long cicCode = isupMessage.getCircuitIdentificationCode().getCIC();
			ies.setCustomName(String.valueOf(cicCode));
		}

		return ies;
	}

}
