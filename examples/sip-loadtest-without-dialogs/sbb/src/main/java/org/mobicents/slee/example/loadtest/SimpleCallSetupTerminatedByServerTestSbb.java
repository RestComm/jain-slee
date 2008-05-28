package org.mobicents.slee.example.loadtest;

import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
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
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipResourceAdaptorSbbInterface;

public abstract class SimpleCallSetupTerminatedByServerTestSbb implements javax.slee.Sbb {

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SipResourceAdaptorSbbInterface sipFactoryProvider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private NullActivityFactory nullActivityFactory;
    private NullActivityContextInterfaceFactory nullACIFactory;
    private TimerFacility timerFacility;
	
    public abstract ChildRelation getChildRelation();
    
	public void onInviteEvent(javax.sip.RequestEvent event, ActivityContextInterface aci) {
        try {
        	// send response
			((ServerTransaction)aci.getActivity()).sendResponse(this.createResponse(event.getRequest(), Response.OK));
			
			// create bye request
            Request bye = (Request) event.getRequest().clone();
			bye.setMethod(Request.BYE);
			FromHeader fromHeader = (FromHeader)event.getRequest().getHeader(FromHeader.NAME);
			ToHeader toHeader = (ToHeader)event.getRequest().getHeader(ToHeader.NAME);
            bye.setRequestURI(fromHeader.getAddress().getURI());
            bye.setHeader(headerFactory.createToHeader(fromHeader.getAddress(),fromHeader.getTag()));
            bye.setHeader(headerFactory.createFromHeader(toHeader.getAddress(),"12345"));
            // change cSeq
            ((CSeqHeader) event.getRequest().getHeader(CSeqHeader.NAME)).setMethod(Request.BYE);
            // change Via
            ViaHeader viaHeader = ((ViaHeader) bye.getHeader(ViaHeader.NAME));
            SipURI sipURI = (SipURI)event.getRequest().getRequestURI();
            viaHeader.setPort(sipURI.getPort());
            viaHeader.setHost(sipURI.getHost());
            // change Contact
            ContactHeader contactHeader = (ContactHeader) bye.getHeader(ContactHeader.NAME);
            contactHeader.setAddress(toHeader.getAddress());
            // persist on the child
            ((SimpleCallSetupTerminatedByServerTestChildSbbLocalObject)getChildRelation().create()).setBye(bye);
            // set timer
            NullActivity nullActivity = nullActivityFactory.createNullActivity();
            ActivityContextInterface nullActivityContextInterface = nullACIFactory.getActivityContextInterface(nullActivity);
            nullActivityContextInterface.attach(this.getSbbContext().getSbbLocalObject());
            TimerOptions timerOptions = new TimerOptions();
            timerOptions.setPreserveMissed(TimerPreserveMissed.ALL);
            timerFacility.setTimer(nullActivityContextInterface, null, System.currentTimeMillis()+60000, timerOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Response createResponse(final Request request, int responseType) throws ParseException {
		final SipURI sipUri = (SipURI) request.getRequestURI();
		final SipURI sipAddress = addressFactory.createSipURI(sipUri.getUser(),
				sipUri.getHost());
		Response response = messageFactory
				.createResponse(responseType, request);
		response.addHeader(headerFactory
				.createContactHeader(addressFactory
				.createAddress(sipAddress)));
		return response;
	}
	
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		try {
		// create client transaction (from bye stored in the child sbb) and atach to it's aci
		ClientTransaction clientTransaction = sipFactoryProvider.getSipProvider().getNewClientTransaction(((SimpleCallSetupTerminatedByServerTestChildSbbLocalObject)getChildRelation().iterator().next()).getBye());
		sipActivityContextInterfaceFactory.getActivityContextInterface(clientTransaction).attach(this.getSbbContext().getSbbLocalObject());
		// detach from timer aci, will end implicitly
		aci.detach(this.getSbbContext().getSbbLocalObject());
		// send bye
		clientTransaction.sendRequest();
		// sleep a bit
		/*try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onResponseOkEvent(ResponseEvent event,ActivityContextInterface aci) {
		//System.out.println("ok rcvd");
		// just detach
		aci.detach(this.getSbbContext().getSbbLocalObject());
	}

	
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context; 

		try {
			Context ctx = (Context)new InitialContext().lookup("java:comp/env");
			// Getting JAIN SIP Resource Adaptor interfaces
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory)ctx.lookup("slee/resources/jainsip/1.2/acifactory");
	        //sipFactoryProvider = (SipResourceAdaptorSbbInterface)ctx.lookup("slee/resources/jainsip/1.1/provider");
			sipFactoryProvider = (SipResourceAdaptorSbbInterface)ctx.lookup("slee/resources/jainsip/1.2/provider");
	        addressFactory = sipFactoryProvider.getAddressFactory();
	        headerFactory = sipFactoryProvider.getHeaderFactory();
	        messageFactory = sipFactoryProvider.getMessageFactory();
	     
            this.nullACIFactory = (NullActivityContextInterfaceFactory) ctx.lookup("slee/nullactivity/activitycontextinterfacefactory");
            this.nullActivityFactory = (NullActivityFactory) ctx.lookup("slee/nullactivity/factory");
            this.timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
	        
	        
		} catch (NamingException e) {
			e.printStackTrace();
		}
	
	
	}
    public void unsetSbbContext() { this.sbbContext = null; }
    
    public void sbbCreate() throws javax.slee.CreateException {}
    public void sbbPostCreate() throws javax.slee.CreateException {}
    public void sbbActivate() {}
    public void sbbPassivate() {}
    public void sbbRemove() {}
    public void sbbLoad() {}
    public void sbbStore() {}
    public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {}
    public void sbbRolledBack(RolledBackContext context) {}
	

	
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
