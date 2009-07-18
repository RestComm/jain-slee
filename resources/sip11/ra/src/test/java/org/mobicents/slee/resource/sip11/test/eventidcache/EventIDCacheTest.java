package org.mobicents.slee.resource.sip11.test.eventidcache;

import javax.sip.message.Request;
import javax.sip.message.Response;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.resource.sip11.EventIDCache;

public class EventIDCacheTest {

	private EventLookupFacility eventLookupFacility = new EventLookupFacility();
	private EventIDCache eventIDCache = new EventIDCache();
	
	private final String vendor = "net.java.slee";
	private final String version = "1.2";
	
	private DummyEventType eventType = null;
	
	@Test
	public void testClientTransactionEvents() {
	
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Response.TRYING", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.TRYING)), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Response.PROVISIONAL", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.RINGING)), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Response.SUCCESS", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.OK)), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Response.REDIRECT", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.MOVED_TEMPORARILY)), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Response.CLIENT_ERROR", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.NOT_FOUND)), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Response.SERVER_ERROR", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.SERVER_INTERNAL_ERROR)), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Response.GLOBAL_FAILURE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.SESSION_NOT_ACCEPTABLE)), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Timeout.TRANSACTION", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getTransactionTimeoutEventId(eventLookupFacility,false), eventType);
	}
	
	@Test
	public void testServerTransactionEvents() {
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.INVITE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.INVITE), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.ACK", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.ACK), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.CANCEL", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.CANCEL), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.BYE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.BYE), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.OPTIONS", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.OPTIONS), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.REGISTER", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.REGISTER), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.INFO", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.INFO), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.PRACK", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.PRACK), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.UPDATE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.UPDATE), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.MESSAGE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.MESSAGE), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.SUBSCRIBE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.SUBSCRIBE), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.NOTIFY", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.NOTIFY), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.REFER", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.REFER), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.PUBLISH", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.PUBLISH), false), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.SIP_EXTENSION", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility,new RequestMessage("MARTINS"), false), eventType);
	}
	
	@Test
	public void testDialogEvents() {
	
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.INVITE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.INVITE), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.ACK", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.ACK), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.BYE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.BYE), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.message.Request.CANCEL", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.CANCEL), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.OPTIONS", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.OPTIONS), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.REGISTER", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.REGISTER), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.INFO", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.INFO), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.PRACK", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.PRACK), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.UPDATE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.UPDATE), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.MESSAGE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.MESSAGE), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.SUBSCRIBE", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.SUBSCRIBE), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.NOTIFY", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.NOTIFY), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.REFER", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.REFER), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.PUBLISH", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.PUBLISH), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Dialog.SIP_EXTENSION", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility,new RequestMessage("MARTINS"), true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Timeout.TRANSACTION", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getTransactionTimeoutEventId(eventLookupFacility,true), eventType);
		
		eventType = new DummyEventType();
		eventLookupFacility.putEventID("javax.sip.Timeout.Dialog", vendor, version, eventType);
		Assert.assertEquals(eventIDCache.getDialogTimeoutEventId(eventLookupFacility), eventType);
	}
}
