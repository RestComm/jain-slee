/*
 *
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.examples.ss7.isup;

import java.io.IOException;
import java.util.Arrays;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.CreateException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.StartActivityException;
import javax.slee.serviceactivity.ServiceActivity;

import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ISUPParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;
import org.mobicents.slee.resources.ss7.isup.ratype.ActivityContextInterfaceFactory;
import org.mobicents.slee.resources.ss7.isup.ratype.RAISUPProvider;

/**
 * 
 * @author baranowb
 */
public abstract class ISUPSbb implements Sbb {

	private SbbContext sbbContext;

	private Tracer logger;
	// ////////////////
	// Some RA vars //
	// ////////////////
	private RAISUPProvider provider;

	private ActivityContextInterfaceFactory acif;

	// ///////////////////
	// SLEE facilities //
	// ///////////////////
	private TimerFacility timerFacility;

	private NullActivityContextInterfaceFactory nullACIFactory;

	private NullActivityFactory nullActivityFactory;

	/** Creates a new instance of CallSbb */
	public ISUPSbb() {
	}

	// ////////////////////////
	// SLEE events handlers //
	// ////////////////////////
	/**
	 * Deploy process definition when the service is activated by SLEE
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStartServiceEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {
		ServiceActivity sa = (ServiceActivity) aci.getActivity();
		if (sa.getService().equals(this.sbbContext.getService())) {
			// lets GO
			if(!this.provider.isTransportUp())
			{
				startTimer();
				return;
			}else
			{
				resetCircuits();
				return;
			}
			

		}
	}

	/**
	 * 
	 * @param event
	 * @param aci
	 */
	public void onActivityEndEvent(ActivityEndEvent event, ActivityContextInterface aci) {

	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {

	}

	public void onResetCircuitEvent(ResetCircuitMessage event, ActivityContextInterface aci) {
		
		logger.info("Received RST: "+event.getCircuitIdentificationCode().getCIC());
		
	}
	public void onCircuitGroupResetEvent(CircuitGroupResetMessage event, ActivityContextInterface aci) {
		logger.info("Received CGR: "+event.getCircuitIdentificationCode().getCIC());
		logger.info("Received CGR: "+event.getRangeAndStatus().getRange());
		logger.info("Received CGR: "+Arrays.toString(event.getRangeAndStatus().getStatus()));
		//send ACK;
		
		
		
	}
	/////////
	// IES //
	/////////
	public InitialEventSelector isupSelectMessage(InitialEventSelector ies)
	{
		Object msg = ies.getEvent();
		Object activity = ies.getActivity();
		if(activity instanceof ServiceActivity)
		{
			ServiceActivity sa = (ServiceActivity) activity;
			if (sa.getService().equals(this.sbbContext.getService())) {
				// lets GO
				ies.setCustomName("ISUP");
				ies.setInitialEvent(true);
				return ies;

			}
		}else
		{
			if(msg instanceof ISUPMessage)
			{
				ISUPMessage isp = (ISUPMessage) msg;
				switch(isp.getMessageType().getCode())
				{
				case ResetCircuitMessage.MESSAGE_CODE:
				case CircuitGroupResetMessage.MESSAGE_CODE:
					ies.setCustomName("ISUP");
					ies.setInitialEvent(true);
					return ies;
				default:
						logger.info("Received unexpected message: "+msg);
						
				}
			}
		}
		ies.setInitialEvent(false);
		return ies;
	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.logger = sbbContext.getTracer("ISUPSbb");

		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			this.provider = (RAISUPProvider) ctx.lookup("slee/resources/isup/1.0/provider");

			this.acif = (ActivityContextInterfaceFactory) ctx.lookup("slee/resources/isup/1.0/acifactory");
			final Context myEnv = (Context) new InitialContext();
			// slee facilities
			this.timerFacility = (TimerFacility) myEnv.lookup(TimerFacility.JNDI_NAME);
			this.nullACIFactory = (NullActivityContextInterfaceFactory) myEnv.lookup(NullActivityContextInterfaceFactory.JNDI_NAME);
			this.nullActivityFactory = (NullActivityFactory) myEnv.lookup(NullActivityFactory.JNDI_NAME);
		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
		this.logger = null;
		this.timerFacility = null;
		this.nullACIFactory = null;
		this.nullActivityFactory = null;
		this.provider = null;
		this.acif = null;
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

	public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
	}

	public void sbbRolledBack(RolledBackContext rolledBackContext) {
	}

	// ////////////////////////
	// Some private methods //
	// ////////////////////////
	private void startTimer() {
		final ActivityContextInterface timerACI = this.nullACIFactory.getActivityContextInterface(this.nullActivityFactory
				.createNullActivity());
		timerACI.attach(sbbContext.getSbbLocalObject());
		// set the timer on the null AC, because the one from this event
		// will end as soon as we send back the 200 ok
		this.timerFacility.setTimer(timerACI, null, System.currentTimeMillis() + (20 * 1000), new TimerOptions());
	}
	
	private void resetCircuits() {
		ActivityContextInterface circuitACI = null;
		ISUPClientTransaction ctx = null;

		ISUPMessageFactory msgFactory = this.provider.getMessageFactory();
		ISUPParameterFactory prmFactory = this.provider.getParameterFactory();

		for (int i = 1; i <= 10; i++) {
			ResetCircuitMessage rst = null;

			rst = msgFactory.createRSC();
			CircuitIdentificationCode cic = prmFactory.createCircuitIdentificationCode();
			cic.setCIC(i);
			rst.setCircuitIdentificationCode(cic);
			try {
				ctx = this.provider.createClientTransaction(rst);
				circuitACI = this.acif.getActivityContextInterface(ctx);
				circuitACI.attach(this.sbbContext.getSbbLocalObject());
				ctx.sendRequest();
			} catch (ActivityAlreadyExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SLEEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransactionAlredyExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (StartActivityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParameterRangeInvalidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//now group rest :)
		
		CircuitGroupResetMessage cgrm = msgFactory.createGRS();
		CircuitIdentificationCode cic = prmFactory.createCircuitIdentificationCode();
		cic.setCIC(1);
		cgrm.setCircuitIdentificationCode(cic);
		RangeAndStatus ras = prmFactory.createRangeAndStatus();
		ras.setRange((byte) 10);
		byte[] status = new byte[10];
		
	}

}
