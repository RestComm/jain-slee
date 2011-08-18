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
package org.mobicents.example.ss7.ussd;

import gov.nist.javax.sip.header.CallID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.CreateException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.mobicents.example.ss7.ussd.jbpm.USSDDecisionHandler;
import org.mobicents.example.ss7.ussd.jbpm.USSDPromptHandler;
import org.mobicents.ussdgateway.ObjectFactory;
import org.mobicents.ussdgateway.USSDRequest;
import org.mobicents.ussdgateway.USSDResponse;

/**
 * This is VERY basic example of USSD usage with jBPM. jBPM flow is ~hardcoded
 * in xml and separated from SBB code. Task of this class are:
 * <ul>
 * <li>Establish session with other peer</li>
 * <li>Feed jBPM process with input</li>
 * <li>Pull jBPM process for ussd prompt - check if process died</li>
 * </ul>
 * 
 * @author baranowb
 * @author amit bhayani
 */
public abstract class SipUSSDSbb implements Sbb {
	private static final String CONTENT_TYPE = "text";
	private static final String CONTENT_SUB_TYPE = "xml";
	private static final String JPDL_FILE = "trainstation.jpdl.xml";
	private static final String PROCESS_NAME="TrainStationDecisions";
	private SbbContext sbbContext;

	// SIP
	private SleeSipProvider provider;

	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private SipActivityContextInterfaceFactory acif;

	// jaxb
	private static final JAXBContext jAXBContext = initJAXBContext();
	private static final ObjectFactory objectFactory = new ObjectFactory();
	// not final, since we want it to be created in way which provides us of
	// notifying slee of excption?
	private static JbpmContext jbpmContext;

	private Tracer logger;

	/** Creates a new instance of CallSbb */
	public SipUSSDSbb() {
	}

	// ////////////////////////
	// SLEE events handlers //
	// ////////////////////////
	/**
	 * Deploy process definition when the service is activated by
	 * SLEE
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStartServiceEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {
		ServiceActivity sa = (ServiceActivity) aci.getActivity();
		if (sa.getService().equals(this.sbbContext.getService())) {
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
			
			jbpmContext = jbpmConfiguration.createJbpmContext();
			
			ProcessDefinition pd = ProcessDefinition.parseXmlInputStream(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(JPDL_FILE));
			jbpmContext.deployProcessDefinition(pd);
		}
	}

	/**
	 * Handler to clear JBPM.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onActivityEndEvent(ActivityEndEvent event, ActivityContextInterface aci) {
		if (jbpmContext != null && aci.getActivity() instanceof ServiceActivity) {
			// just in case
			ServiceActivity sa = (ServiceActivity) aci.getActivity();
			if (sa.getService().equals(this.sbbContext.getService())) {
				jbpmContext.close();
				jbpmContext = null;
			}
		}
	}

	// initial
	public void onInviteEvent(RequestEvent event, ActivityContextInterface ac) {
		// its initial request
		try {
			DialogActivity da = (DialogActivity) this.provider.getNewDialog(event.getServerTransaction());
			da.terminateOnBye(true);
			ActivityContextInterface daACI = this.acif.getActivityContextInterface(da);
			daACI.attach(this.sbbContext.getSbbLocalObject());
			ProcessInstance pi = jbpmContext.newProcessInstance(PROCESS_NAME);
			this.setProcessInstance(pi);
			
			//set original invoke id, so we know how to term
			USSDRequest extracted = extractUssdRequest(event);
			if (extracted == null) {
				// error has been handled
				return;
			}
			
			this.setInvokeId(extracted.getInvokeId());
			
		} catch (SipException e) {

			e.printStackTrace();
			handleError(event.getServerTransaction(), Response.BAD_REQUEST, e);
			return;
		}

		processUssdRequest(event);
		if (isSessionDead()) {
			// in this case, send bye over dialog

			sendBye();

		}
	}

	// intermediate
	public void onInfoEvent(RequestEvent event, ActivityContextInterface ac) {
		//info will contain USSDResponse ;[
		
		
		processUssdRequest(event);
		
		if (isSessionDead()) {
			// in this case, send bye over dialog

			sendBye();

		}
	
	}

	// final
	public void onByeEvent(RequestEvent event, ActivityContextInterface ac) {
		// something should be here?
		sendResponse(null, event.getServerTransaction());
	}

	public void onCancelEvent(CancelRequestEvent event, ActivityContextInterface ac) {
		this.provider.acceptCancel(event, false);
	}

	// success
	public void onSuccessEvent(ResponseEvent event, ActivityContextInterface ac) {
		// nothing,
	}

	// ///////////////////
	// Private methods //
	// ///////////////////
	private void handleError(ServerTransaction tx, int statusCode, Exception ee) {
		// send back error

		try {
			Response response = this.messageFactory.createResponse(statusCode, tx.getRequest());

			if (response.getHeader(MaxForwardsHeader.NAME) == null) {
				response.addHeader(this.headerFactory.createMaxForwardsHeader(69));
			}
			String msg = ee.getMessage();
			ContentTypeHeader cth = this.headerFactory.createContentTypeHeader("text", "plain");
			response.setContent(msg, cth);
			tx.sendResponse(response);
		} catch (ParseException pe) {
			// TODO Auto-generated catch block
			pe.printStackTrace();
		} catch (SipException se) {
			// TODO Auto-generated catch block
			se.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// FIXME: clear dialog?
	}

	private void sendResponse(String ussdResponse, ServerTransaction stx) {

		try {
			Response okresponse = this.messageFactory.createResponse(Response.OK, stx.getRequest());
			DialogActivity da = getDialog();

			if (ussdResponse != null) {
				ContentTypeHeader cth = this.headerFactory.createContentTypeHeader(CONTENT_TYPE, CONTENT_SUB_TYPE);
				okresponse.setContent(ussdResponse, cth);
			}
			okresponse.addHeader(this.getLocalContact());
			String tag = null;
			if (da.getLocalTag() == null) {
				tag = System.currentTimeMillis() + "";
			} else {
				tag = da.getLocalTag();
			}
			ToHeader to = (ToHeader) okresponse.getHeader(ToHeader.NAME);
			if (to.getTag() == null) {
				to.setTag(tag);
			}
			stx.sendResponse(okresponse);

			// FIXME: clear dialog?
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendBye() {
		DialogActivity da = getDialog();
		if (da == null) {
			logger.severe("Dialog activity is not present!");
			return;
		}

		try {
			Request request = da.createRequest(Request.BYE);
			ClientTransaction ctx = this.provider.getNewClientTransaction(request);

			da.sendRequest(ctx);
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	private void processUssd(RequestEvent event) {
//		// now lets get USSD
//		USSDRequest extracted = extractUssd(event);
//		if (extracted == null) {
//			// error has been handled
//			return;
//		}
//		String drooled = processUssd(extracted);
//
//		// send ok
//		if (drooled != null) {
//			sendResponse(drooled, event.getServerTransaction());
//			if (isSessionDead()) {
//				// in this case, send bye over dialog
//
//				sendBye();
//
//			}
//		}
//	}

	private DialogActivity getDialog() {
		ActivityContextInterface[] acis = this.sbbContext.getActivities();
		for (ActivityContextInterface aci : acis) {
			if (aci.getActivity() instanceof DialogActivity) {
				return (DialogActivity) aci.getActivity();
			}
		}

		return null;
	}

	// FIXME: add this as cmp?

	private ContactHeader getLocalContact() {
		ContactHeader ch = this.headerFactory.createContactHeader();
		SipURI localURI = this.provider.getLocalSipURI("udp");
		Address addr = this.addressFactory.createAddress(localURI);
		ch.setAddress(addr);
		return ch;

	}

	// //////////////
	// USSD Stuff //
	// //////////////
	// FIXME: once XSD is done, switch to JAXB or something
	private USSDRequest extractUssdRequest(RequestEvent event) {
		Request sipRequest = event.getRequest();
		ContentTypeHeader cth = (ContentTypeHeader) event.getRequest().getHeader(ContentTypeHeader.NAME);
		if (cth == null) {
			// FIXME: break
			return null;
		} else {
			if (!cth.getContentType().equals(CONTENT_TYPE) || !cth.getContentSubType().equals(CONTENT_SUB_TYPE)
					|| sipRequest.getContent() == null) {
				// FIXME: break,
				return null;
			}
		}
		try {
			Unmarshaller uMarshaller = jAXBContext.createUnmarshaller();
			ByteArrayInputStream bis = new ByteArrayInputStream(sipRequest.getRawContent());
			JAXBElement<USSDRequest> data = (JAXBElement<USSDRequest>) uMarshaller.unmarshal(bis);
			return data.getValue();
		} catch (JAXBException e) {
			// FIXME: tear down

			e.printStackTrace();
		}

		return null;
	}
	
	private USSDResponse extractUssdResponse(RequestEvent event) {
		Request sipRequest = event.getRequest();
		ContentTypeHeader cth = (ContentTypeHeader) event.getRequest().getHeader(ContentTypeHeader.NAME);
		if (cth == null) {
			// FIXME: break
			return null;
		} else {
			if (!cth.getContentType().equals(CONTENT_TYPE) || !cth.getContentSubType().equals(CONTENT_SUB_TYPE)
					|| sipRequest.getContent() == null) {
				// FIXME: break,
				return null;
			}
		}
		try {
			Unmarshaller uMarshaller = jAXBContext.createUnmarshaller();
			ByteArrayInputStream bis = new ByteArrayInputStream(sipRequest.getRawContent());
			JAXBElement<USSDResponse> data = (JAXBElement<USSDResponse>) uMarshaller.unmarshal(bis);
			return data.getValue();
		} catch (JAXBException e) {
			// FIXME: tear down

			e.printStackTrace();
		}

		return null;
	}

	private void processUssdRequest(RequestEvent event)
	{
		USSDRequest extracted  =extractUssdRequest(event);
		ProcessInstance pi = this.getProcessInstance();
		pi.getContextInstance().setVariable(USSDDecisionHandler._INPUT_, extracted.getUssdString());
		pi.signal();
		String data = (String) pi.getContextInstance().getVariable(USSDPromptHandler._PROMPT_);
		if(isSessionDead())
		{
		USSDResponse response = this.objectFactory.createUSSDResponse();
		response.setInvokeId(getInvokeId()); //use invoke id from original one
		response.setUssdCoding(extracted.getUssdCoding());
		response.setUssdString(data);

		try {
			Marshaller marshaller = jAXBContext.createMarshaller();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			JAXBElement<USSDResponse> res = this.objectFactory.createResponse(response);
			marshaller.marshal(res, bos);
			sendResponse(new String(bos.toByteArray()), event.getServerTransaction());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	
		}else
		{
			//its damn SS7 logic....
			USSDRequest response = this.objectFactory.createUSSDRequest(); // yeah, we name it 'response' cause it in fact it is response.... pffff
			response.setUssdCoding(extracted.getUssdCoding());
			response.setUssdString(data);
			response.setInvokeId(extracted.getInvokeId()+1); //not a best way, it's <-127,128> so it flips, but for example this will work. 
			response.setDialogId(extracted.getDialogId());
			
			try {
				Marshaller marshaller = jAXBContext.createMarshaller();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				JAXBElement<USSDRequest> res = this.objectFactory.createRequest(response);
				marshaller.marshal(res, bos);
				sendResponse(new String(bos.toByteArray()), event.getServerTransaction());
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			
		}
	
	}
//	private void processUssdResponse(RequestEvent event)
//	{
//		USSDResponse extracted  = extractUssdResponse(event);
//		ProcessInstance pi = this.getProcessInstance();
//		pi.getContextInstance().setVariable(USSDDecisionHandler._INPUT_, extracted.getUssdString());
//		pi.signal();
//		String data = (String) pi.getContextInstance().getVariable(USSDPromptHandler._PROMPT_);
//		if(isSessionDead())
//		{
//		USSDResponse response = this.objectFactory.createUSSDResponse();
//		response.setInvokeId(getInvokeId()); //use invoke id from original one
//		response.setUssdCoding(extracted.getUssdCoding());
//		response.setUssdString(data);
//
//		try {
//			Marshaller marshaller = jAXBContext.createMarshaller();
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			JAXBElement<USSDResponse> res = this.objectFactory.createResponse(response);
//			marshaller.marshal(res, bos);
//			sendResponse(new String(bos.toByteArray()), event.getServerTransaction());
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}
//	
//		}else
//		{
//			//its damn SS7 logic....
//			USSDRequest response = this.objectFactory.createUSSDRequest(); // yeah, we name it 'response' cause it in fact is response.... pffff
//			response.setUssdCoding(extracted.getUssdCoding());
//			response.setUssdString(data);
//			response.setInvokeId(extracted.getInvokeId()+1); //not a best way, it's <-127,128> so it flips, but for example this will work. 
//			response.setDialogId(extracted.getDialogId());
//			
//			try {
//				Marshaller marshaller = jAXBContext.createMarshaller();
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				JAXBElement<USSDRequest> res = this.objectFactory.createRequest(response);
//				marshaller.marshal(res, bos);
//				sendResponse(new String(bos.toByteArray()), event.getServerTransaction());
//			} catch (JAXBException e) {
//				e.printStackTrace();
//			}
//			
//		}
//
//	}
	

	private boolean isSessionDead() {
		ProcessInstance pi = this.getProcessInstance();
		if(pi == null || pi.getEnd()!=null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static JAXBContext initJAXBContext() {
		try {
			return JAXBContext.newInstance("org.mobicents.ussdgateway");
		} catch (JAXBException e) {
			// logger.severe("unable to init jaxb context",e);
			e.printStackTrace();
			return null;
		}
	}

	// ////////
	// CMPS //
	// ////////
	public abstract void setProcessInstance(ProcessInstance pi);

	public abstract ProcessInstance getProcessInstance();
	
	public abstract void setInvokeId(int pi);

	public abstract int getInvokeId();

	/**
	 * Generate a custom convergence name so that events with the same call
	 * identifier will go to the same root SBB entity.
	 */
	public InitialEventSelector callIDSelect(InitialEventSelector ies) {
		Object event = ies.getEvent();
		String callId = null;
		if (event instanceof ResponseEvent) {
			ies.setInitialEvent(false);
			return ies;
		} else if (event instanceof RequestEvent) {
			// If request event, the convergence name to callId
			Request request = ((RequestEvent) event).getRequest();
			CallID callIdHeader = (CallID) request.getHeader(CallID.NAME);
			callId = callIdHeader.getCallId();
			// Set the convergence name
			if (logger.isFineEnabled()) {
				logger.fine("Setting convergence name to: " + callId);
			}
			ies.setCustomName(callId);
			return ies;
		} else {
			ies.setInitialEvent(false);
			return ies;
		}

	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.logger = sbbContext.getTracer(SipUSSDSbb.class.getSimpleName());

		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");

			addressFactory = provider.getAddressFactory();
			headerFactory = provider.getHeaderFactory();
			messageFactory = provider.getMessageFactory();
			acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
		this.logger = null;
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
}
