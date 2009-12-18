package org.mobicents.slee.example.xcapclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.StartActivityException;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.commons.httpclient.HttpException;
import org.mobicents.slee.resource.xcapclient.AsyncActivity;
import org.mobicents.slee.resource.xcapclient.ResponseEvent;
import org.mobicents.slee.resource.xcapclient.XCAPClientActivityContextInterfaceFactory;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptorSbbInterface;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.client.appusage.resourcelists.jaxb.EntryType;
import org.openxdm.xcap.client.appusage.resourcelists.jaxb.ListType;
import org.openxdm.xcap.client.appusage.resourcelists.jaxb.ObjectFactory;
import org.openxdm.xcap.client.appusage.resourcelists.key.ResourceListsUserElementUriKey;
import org.openxdm.xcap.common.key.UserDocumentUriKey;
import org.openxdm.xcap.common.key.UserElementUriKey;
import org.openxdm.xcap.common.resource.ElementResource;
import org.openxdm.xcap.common.uri.ElementSelector;
import org.openxdm.xcap.common.uri.ElementSelectorStep;
import org.openxdm.xcap.common.uri.ElementSelectorStepByAttr;
import org.openxdm.xcap.common.xml.XMLValidator;

public abstract class XCAPClientExampleSbb implements javax.slee.Sbb {

	private SbbContext sbbContext = null; 	
	
	/**
	 * static jaxb context for example pojos
	 */
	private static JAXBContext jAXBContext = initJAXBContext();
	private static JAXBContext initJAXBContext() {
		try {
			return JAXBContext.newInstance("org.openxdm.xcap.client.appusage.resourcelists.jaxb");
		} catch (JAXBException e) {
			log.severe("unable to init jaxb context",e);
			return null;
		}
	}
	
	private Context myEnv = null;
	private XCAPClientResourceAdaptorSbbInterface ra = null;
	private XCAPClientActivityContextInterfaceFactory acif = null;
	
	private String userName = "sip:bob@example.com";
	private String documentName = "index";
			
	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context;
		if (log == null) {
			log = context.getTracer(getClass().getSimpleName());
		}		 
		try {
			myEnv = (Context) new InitialContext().lookup("java:comp/env");           
			ra = (XCAPClientResourceAdaptorSbbInterface) myEnv.lookup("slee/resources/xcapclient/1.0/sbbrainterface");
			acif = (XCAPClientActivityContextInterfaceFactory) myEnv.lookup("slee/resources/xcapclient/1.0/acif");  
		}
		catch (NamingException e) {
			log.severe("unable to set sbb context",e);
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
			
	protected SbbContext getSbbContext() {
		return sbbContext;
	}	
	
	/**
	 * 
	 * @param event
	 * @param aci
	 */
	public void onServiceStartedEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {		           
		   						
		log.info("service started...");
		
		try {
			syncTest();
			asyncTest();
		}
		catch (Exception f) {
			log.severe("sync test failed...",f);
		}
        						
	}	
	
	public void syncTest() throws HttpException, IOException, JAXBException {
					
		// create uri		
		UserDocumentUriKey docKey = new UserDocumentUriKey("resource-lists",userName,documentName);
		
		// the doc to put
		String initialDocument =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<resource-lists xmlns=\"urn:ietf:params:xml:ns:resource-lists\">" +
				"<list name=\"friends\"/>" +
			"</resource-lists>";			
		
		String element = "<entry uri=\"sip:alice@example.com\" xmlns=\"urn:ietf:params:xml:ns:resource-lists\"/>";
		
		String finalDocument =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<resource-lists xmlns=\"urn:ietf:params:xml:ns:resource-lists\">" +
				"<list name=\"friends\">" +
				element +
				"</list>" +
			"</resource-lists>";	
		
		// put the document and get sync response
		Response response = ra.put(docKey,"application/resource-lists+xml",initialDocument,null);
		
		// check put response
		if (response != null) {
			if(response.getCode() == 200 || response.getCode() == 201) {
				log.info("document created in xcap server...");
			} else {
				log.severe("bad response from xcap server: "+response.toString());
			}
		} else {
			log.severe("unable to create document in xcap server...");
		}
					
		// let's create an uri selecting an element
		LinkedList<ElementSelectorStep> elementSelectorSteps = new LinkedList<ElementSelectorStep>();
		ElementSelectorStep step1 = new ElementSelectorStep("resource-lists");
		ElementSelectorStep step2 = new ElementSelectorStepByAttr("list","name","friends");
		ElementSelectorStep step3 = new ElementSelectorStepByAttr("entry","uri","sip:alice@example.com");
		elementSelectorSteps.add(step1);
		elementSelectorSteps.addLast(step2);
		elementSelectorSteps.addLast(step3);
		UserElementUriKey elemKey = new UserElementUriKey("resource-lists",userName,documentName,new ElementSelector(elementSelectorSteps),null);		
		
		// put the element and get sync response
		response = ra.put(elemKey,ElementResource.MIMETYPE,element,null);
		
		// check put response
		if (response != null) {
			if(response.getCode() == 201) {
				log.info("element created in xcap server...");
			} else {
				log.severe("bad response from xcap server: "+response.toString());
			}
		} else {
			log.severe("unable to create element in xcap server...");
		}
				
		// get the document and check content is ok
		response = ra.get(docKey,null);
		
		// check get response		
		if (response != null) {
			if(response.getCode() == 200 && XMLValidator.weaklyEquals((String)response.getContent(),finalDocument)) {
				log.info("document retreived in xcap server and content is the expected...");
				log.info("sync test suceed :)");
			} else {
				log.severe("bad response from xcap server: "+response.toString());
			}
		} else {
			log.severe("unable to retreive document in xcap server...");
		}	
							
	}
	
	public void asyncTest() throws ActivityAlreadyExistsException, NullPointerException, UnrecognizedActivityException, TransactionRequiredLocalException, TransactionRolledbackLocalException, HttpException, SLEEException, IllegalStateException, JAXBException, IOException, StartActivityException {
		
		// now we will use marshalling and unmarshalling too
						
		// let's create a list containing  someone
		ObjectFactory of = new ObjectFactory();
		ListType listType = of.createListType();
		listType.setName("enemies");
		EntryType entry = of.createEntryType();
		entry.setUri("sip:winniethepooh@disney.com");
		listType.getListOrExternalOrEntry().add(entry);
		
		// create the key selecting the new element
		LinkedList<ElementSelectorStep> elementSelectorSteps = new LinkedList<ElementSelectorStep>();
		ElementSelectorStep step1 = new ElementSelectorStep("resource-lists");
		ElementSelectorStep step2 = new ElementSelectorStepByAttr("list","name","enemies");
		elementSelectorSteps.add(step1);
		elementSelectorSteps.addLast(step2);
		ResourceListsUserElementUriKey key = new ResourceListsUserElementUriKey(userName,documentName,new ElementSelector(elementSelectorSteps),null);		
		
		// marshall content to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jAXBContext.createMarshaller().marshal(listType, baos);
		
		// lets put the element using the sync interface
		Response response = ra.put(key,ElementResource.MIMETYPE,baos.toByteArray(),null);
		// check put response
		if (response != null) {
			if(response.getCode() == 201) {
				log.info("list element created in xcap server...");
			} else {
				log.severe("bad response from xcap server: "+response.toString());
			}
		} else {
			log.severe("unable to create list element in xcap server...");
		}
		
		// now lets get it using the async interface
		
		// get a async request activity from the xcap client ra
		AsyncActivity activity = ra.createActivity();
		
		// attach this sbb entity to the activity's related aci 
		ActivityContextInterface aci = acif.getActivityContextInterface(activity);
		aci.attach(sbbContext.getSbbLocalObject());
		
		// send request
		activity.get(key,null);
		
		// the response will be asyncronous
	}
	
	/*
	 * ResponseEvent handler
	 */
	public void onResponseEvent(ResponseEvent event, ActivityContextInterface aci) {
		
		log.info("onResponseEvent(event="+event+",aci="+aci+")");
		
		// check put response
		Response response = event.getResponse();
		if (response != null) {
			if(response.getCode() == 200) {
				log.info("list element retreived from xcap server...");
				// let's unmarshall content 
				StringReader stringReader = new StringReader(response.getContent());
				ListType listType = null;
				try {
					listType = (ListType) jAXBContext.createUnmarshaller().unmarshal(stringReader);
				}
				catch (Exception e) {
					log.severe("unable to unmarshall response content",e);
				}
				stringReader.close();
				if(listType != null && listType.getName().equals("enemies")) {
					// check if it's winnie inside
					List<?> list = listType.getListOrExternalOrEntry();		
					if(list.size() == 1) {
						EntryType entry = (EntryType)((JAXBElement<?>)list.get(0)).getValue();
						if(entry.getUri().equals("sip:winniethepooh@disney.com")) {
							log.info("async test suceed :)");
						}
						else {
							log.severe("list element retreived is not the expected one");
						}
					}
					else {
						log.severe("list element retreived is not the expected one");
					}
				}				
				else {
					log.severe("list element retreived is not the expected one");
				}
			} else {
				log.severe("bad response from xcap server: "+response.toString());
			}
		} else {
			log.severe("unable to create list element in xcap server...");
		}
		
		try {
			// delete the document
			ra.delete(new UserDocumentUriKey("resource-lists",userName,documentName),null);	
		}
		catch (Exception e) {
			log.severe("failed to delete document",e);
		}
						
		// cleanup, end the activity
		AsyncActivity activity = (AsyncActivity)aci.getActivity();
		if (activity != null) {
			activity.endActivity();
		}
		
	}	
		
	private static Tracer log;
	
}