package org.mobicents.slee.example.loadtest.sip11;

import gov.nist.javax.sip.Utils;

import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.apache.log4j.Logger;

import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

public abstract class SimpleCallSetupTerminatedByServerTestSbb implements
		javax.slee.Sbb {

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SleeSipProvider sipFactoryProvider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private TimerFacility timerFacility;

	private static ContactHeader contactHeader;
	private static Logger logger = Logger
			.getLogger(SimpleCallSetupTerminatedByServerTestChildSbb.class);

	private ContactHeader getContactHeader() throws ParseException {
		if (contactHeader == null) {
			ListeningPoint listeningPoint = sipFactoryProvider
					.getListeningPoint("udp");
			Address address = addressFactory
					.createAddress("Mobicents SIP AS <sip:"
							+ listeningPoint.getIPAddress() + ">");
			((SipURI) address.getURI()).setPort(listeningPoint.getPort());
			contactHeader = headerFactory.createContactHeader(address);
		}
		return contactHeader;
	}

	public abstract ChildRelation getChildRelation();

	public void onInviteEvent(javax.sip.RequestEvent event,
			ActivityContextInterface aci) {
		try {
			// send response
			Response response = messageFactory.createResponse(Response.OK,
					event.getRequest());
			response.setHeader(getContactHeader());
			event.getServerTransaction().sendResponse(response);
			SbbLocalObject sbbLocalObject = this.getSbbContext()
			.getSbbLocalObject();
			aci.detach(sbbLocalObject);
	
			// create bye request
			Request bye = (Request) event.getRequest().clone();
			bye.setMethod(Request.BYE);
			FromHeader fromHeader = (FromHeader) event.getRequest().getHeader(
					FromHeader.NAME);
			ToHeader toHeader = (ToHeader) event.getRequest().getHeader(
					ToHeader.NAME);
			bye.setRequestURI(fromHeader.getAddress().getURI());
			bye.setHeader(headerFactory.createToHeader(fromHeader.getAddress(),
					fromHeader.getTag()));
			bye.setHeader(headerFactory.createFromHeader(toHeader.getAddress(),
					null));
			// change cSeq
			((CSeqHeader) event.getRequest().getHeader(CSeqHeader.NAME))
					.setMethod(Request.BYE);
			// change Via
			ViaHeader viaHeader = ((ViaHeader) bye.getHeader(ViaHeader.NAME));
			SipURI sipURI = (SipURI) event.getRequest().getRequestURI();
			viaHeader.setPort(sipURI.getPort());
			viaHeader.setHost(sipURI.getHost());
			// change Contact
			ContactHeader contactHeader = (ContactHeader) bye
					.getHeader(ContactHeader.NAME);
			contactHeader.setAddress(getContactHeader().getAddress());
			// persist on the child
			((SimpleCallSetupTerminatedByServerTestChildSbbLocalObject) getChildRelation()
					.create()).setBye(bye);
			
			// create a null ac
			NullActivity nullActivity = nullActivityFactory
					.createNullActivity();
			ActivityContextInterface nullActivityContextInterface = nullACIFactory
					.getActivityContextInterface(nullActivity);
			nullActivityContextInterface.attach(sbbLocalObject);
			// and set the timer
			TimerOptions timerOptions = new TimerOptions();
			timerOptions.setPreserveMissed(TimerPreserveMissed.ALL);
			timerFacility.setTimer(nullActivityContextInterface, null, System
					.currentTimeMillis() + 60000, timerOptions);
			
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// detach from null acis, will end implicitly
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		aci.detach(sbbLocalObject);
		try {
			// send bye
			// create client transaction (from bye stored in the child sbb) and
			// atach to it's aci
			ClientTransaction clientTransaction = sipFactoryProvider
					.getNewClientTransaction(((SimpleCallSetupTerminatedByServerTestChildSbbLocalObject) getChildRelation()
							.iterator().next()).getBye());
			sipActivityContextInterfaceFactory.getActivityContextInterface(
					clientTransaction).attach(sbbLocalObject);
			clientTransaction.sendRequest();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void onResponseOkEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		// just detach
		aci.detach(this.getSbbContext().getSbbLocalObject());
	}

	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;

		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			// Getting JAIN SIP Resource Adaptor interfaces
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) ctx
					.lookup("slee/resources/jainsip/1.2/acifactory");
			//sipFactoryProvider = (SipResourceAdaptorSbbInterface)ctx.lookup("slee/resources/jainsip/1.1/provider");
			sipFactoryProvider = (SleeSipProvider) ctx
					.lookup("slee/resources/jainsip/1.2/provider");
			addressFactory = sipFactoryProvider.getAddressFactory();
			headerFactory = sipFactoryProvider.getHeaderFactory();
			messageFactory = sipFactoryProvider.getMessageFactory();

			this.nullACIFactory = (NullActivityContextInterfaceFactory) ctx
					.lookup("slee/nullactivity/activitycontextinterfacefactory");
			this.nullActivityFactory = (NullActivityFactory) ctx
					.lookup("slee/nullactivity/factory");
			this.timerFacility = (TimerFacility) ctx
					.lookup("slee/facilities/timer");

		} catch (NamingException e) {
			logger.warn(e);
		}

	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

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

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	/**
	 * Convenience method to retrieve the SbbContext object stored in setSbbContext.
	 * 
	 * TODO: If your SBB doesn't require the SbbContext object you may remove this 
	 * method, the sbbContext variable and the variable assignment in setSbbContext().
	 *
	 * @return this SBB's SbbContext object
	 */

	protected SbbContext getSbbContext() {
		return sbbContext;
	}

	private SbbContext sbbContext; // This SBB's SbbContext

}
