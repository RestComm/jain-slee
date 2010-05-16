package org.mobicents.ss7.test;

import javax.csapi.cc.jcc.InvalidPartyException;
import javax.csapi.cc.jcc.InvalidStateException;
import javax.csapi.cc.jcc.JccCall;
import javax.csapi.cc.jcc.JccCallEvent;
import javax.csapi.cc.jcc.JccConnection;
import javax.csapi.cc.jcc.JccConnectionEvent;
import javax.csapi.cc.jcc.JccEvent;
import javax.csapi.cc.jcc.MethodNotSupportedException;
import javax.csapi.cc.jcc.PrivilegeViolationException;
import javax.csapi.cc.jcc.ResourceUnavailableException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;


public abstract class Ss7TestSbb implements javax.slee.Sbb {


	public void onCONNECTION_CREATED(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {

		doMsg("CONNECTION_CREATED", event, aci);
		try {
			logger.info("SELECTING ROUTE: "+event.getConnection()+" --> O: "+event.getConnection().getOriginatingAddress()+" D: " +
											 //This is null, since call is not routed yet.
					""+event.getConnection().getDestinationAddress());
			event.getConnection().selectRoute(event.getConnection().getOriginalAddress());
		} catch (MethodNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrivilegeViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPartyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onCONNECTION_AUTHORIZE_CALL_ATTEMPT(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_AUTHORIZE_CALL_ATTEMPT", event, aci);
	}

	public void onCONNECTION_ADDRESS_COLLECT(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_ADDRESS_COLLECT", event, aci);
	}

	public void onCONNECTION_ADDRESS_ANALYZE(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_ADDRESS_ANALYZE", event, aci);
	}

	public void onCONNECTION_CALL_DELIVERY(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_CALL_DELIVERY", event, aci);
	}

	public void onCONNECTION_ALERTING(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_ALERTING", event, aci);
	}

	public void onCONNECTION_CONNECTED(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_CONNECTED", event, aci);
	}

	public void onCONNECTION_DISCONNECTED(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_DISCONNECTED", event, aci);
	}

	public void onCONNECTION_FAILED(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_FAILED", event, aci);
	}

	public void onCONNECTION_MID_CALL(javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {
		doMsg("CONNECTION_MID_CALL", event, aci);
	}

	public void onCALL_CREATED(javax.csapi.cc.jcc.JccCallEvent event, ActivityContextInterface aci) {
		doMsg("CALL_CREATED", event, aci);
	}

	public void onCALL_ACTIVE(javax.csapi.cc.jcc.JccCallEvent event, ActivityContextInterface aci) {
		doMsg("CALL_ACTIVE", event, aci);
	}

	public void onCALL_INVALID(javax.csapi.cc.jcc.JccCallEvent event, ActivityContextInterface aci) {
		doMsg("CALL_INVALID", event, aci);
	}

	public void onCALL_EVENT_TRANSMISSION_ENDED(javax.csapi.cc.jcc.JccCallEvent event, ActivityContextInterface aci) {
		doMsg("CALL_EVENT_TRANSMISSION_ENDED", event, aci);
	}

	public void onCALL_SUPERVISE_START(javax.csapi.cc.jcc.JccCallEvent event, ActivityContextInterface aci) {
		doMsg("CALL_SUPERVISE_START", event, aci);
	}

	public void onCALL_SUPERVISE_END(javax.csapi.cc.jcc.JccCallEvent event, ActivityContextInterface aci) {
		doMsg("CALL_SUPERVISE_END", event, aci);
	}

	public void doMsg(String msg, javax.csapi.cc.jcc.JccCallEvent event, ActivityContextInterface aci) {

		switch (event.getCause()) {
		case JccCallEvent.CALL_SUPERVISE_START:
			logger.info(msg + "---->CALL_SUPERVISE_START "+dumpEvent(event));
			break;

		// Field descriptor #5 I
		case JccCallEvent.CALL_SUPERVISE_END:
			logger.info(msg + "---->CALL_SUPERVISE_START "+dumpEvent(event));
			break;

		// Field descriptor #5 I
		case JccCallEvent.CALL_ACTIVE:
			logger.info(msg + "---->CALL_ACTIVE "+dumpEvent(event));
			break;

		// Field descriptor #5 I
		case JccCallEvent.CALL_INVALID:
			logger.info(msg + "---->CALL_INVALID "+dumpEvent(event));
			break;

		// Field descriptor #5 I
		case JccCallEvent.CALL_EVENT_TRANSMISSION_ENDED:
			logger.info(msg + "---->CALL_EVENT_TRANSMISSION_ENDED "+dumpEvent(event));
			break;

		// Field descriptor #5 I
		case JccCallEvent.CALL_CREATED:
			logger.info(msg + "---->CALL_CREATED "+dumpEvent(event));
			break;
		}
	}

	public void doMsg(String msg, javax.csapi.cc.jcc.JccConnectionEvent event, ActivityContextInterface aci) {

		switch (event.getCause()) {
		case JccEvent.CAUSE_NORMAL:
			logger.info(msg + "---->CAUSE_NORMAL "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_UNKNOWN:
			logger.info(msg + "---->CAUSE_UNKNOWN "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_CALL_CANCELLED:
			logger.info(msg + "---->CAUSE_CALL_CANCELLED "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_DEST_NOT_OBTAINABLE:
			logger.info(msg + "---->CAUSE_DEST_NOT_OBTAINABLE "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_INCOMPATIBLE_DESTINATION:
			logger.info(msg + "---->CAUSE_INCOMPATIBLE_DESTINATION "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_LOCKOUT:
			logger.info(msg + "---->CAUSE_LOCKOUT "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_NEW_CALL:
			logger.info(msg + "---->CAUSE_NEW_CALL "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_RESOURCES_NOT_AVAILABLE:
			logger.info(msg + "---->CALL_CREATED "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_NETWORK_CONGESTION:
			logger.info(msg + "---->CAUSE_NETWORK_CONGESTION "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_NETWORK_NOT_OBTAINABLE:
			logger.info(msg + "---->CAUSE_NETWORK_NOT_OBTAINABLE "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_SNAPSHOT:
			logger.info(msg + "---->CAUSE_SNAPSHOT "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_REDIRECTED:
			logger.info(msg + "---->CAUSE_REDIRECTED "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_MORE_DIGITS_NEEDED:
			logger.info(msg + "---->CAUSE_MORE_DIGITS_NEEDED "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_BUSY:
			logger.info(msg + "---->CAUSE_BUSY "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_NO_ANSWER:
			logger.info(msg + "---->CAUSE_NO_ANSWER "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_CALL_RESTRICTED:
			logger.info(msg + "---->CAUSE_CALL_RESTRICTED "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_GENERAL_FAILURE:
			logger.info(msg + "---->CAUSE_GENERAL_FAILURE "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_TIMER_EXPIRY:
			logger.info(msg + "---->CAUSE_TIMER_EXPIRY "+dumpEvent(event));
			break;

		// Field descriptor #4 I
		case JccEvent.CAUSE_USER_NOT_AVAILABLE:
			logger.info(msg + "---->CAUSE_USER_NOT_AVAILABLE "+dumpEvent(event));
			break;
		}
	}

	private String dumpEvent(JccConnectionEvent event) {
		JccConnection conn = event.getConnection();
	
		String msg = " Event[" + event + "]:\n"+dumpJccConnection(conn)+"\n";

		return msg;
	}
	private String dumpEvent(JccCallEvent event) {
		JccCall conn = event.getCall();
	
		String msg = " Event[" + event + "]:\n"+dumpJccCall(conn)+"\n";

		return msg;
	}
	private String dumpJccConnection(JccConnection conn) {
		if (conn == null) {
			return "Conneciont_NULL";
		}

		String msg = "----------------------\nConnection:\nDAddress[" + conn.getDestinationAddress() + "]" + " \nOAddress[" + conn.getOriginalAddress() + "] \nRAddress["
				+ conn.getOriginalAddress() + "] \nLAddress[" + conn.getLastAddress() + "]\n----------------------";
		
		
		return msg;
	}
	private String dumpJccCall(JccCall call)
	{
		JccConnection[] cons=call.getConnections();
		String msg ="Call Data["+call+"]\n";
		for(JccConnection c: cons)
		{
			msg+=dumpJccConnection(c);
		}
			
			return msg+="\n=============XX=============";
	}
	/**
	 * Initialize the component
	 */
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		this.logger = context.getTracer("Ss7JCCTest");
		try {
			Context myEnv = (Context) new InitialContext().lookup("java:comp/env");

			// Getting SLEE Factility
			timerFacility = (TimerFacility) myEnv.lookup("slee/facilities/timer");
			nullACIFactory = (NullActivityContextInterfaceFactory) myEnv.lookup("slee/nullactivity/activitycontextinterfacefactory");
			nullActivityFactory = (NullActivityFactory) myEnv.lookup("slee/nullactivity/factory");

		} catch (Exception ne) {
			logger.severe("Failed to set sbb context", ne);
		}

	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	// TODO: Implement the lifecycle methods if required
	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	private SbbContext sbbContext; // This SBB's SbbContext

	private TimerFacility timerFacility;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private NullActivityFactory nullActivityFactory;

	private Tracer logger = null;

}
