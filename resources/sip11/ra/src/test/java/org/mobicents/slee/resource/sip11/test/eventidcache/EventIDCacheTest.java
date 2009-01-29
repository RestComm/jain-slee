package org.mobicents.slee.resource.sip11.test.eventidcache;

import javax.sip.message.Request;
import javax.sip.message.Response;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.resource.sip11.EventIDCache;

public class EventIDCacheTest {

	private EventLookupFacility eventLookupFacility = new EventLookupFacility();
	private EventIDCache eventIDCache = new EventIDCache();
	private int counter = 0;
	
	private final String vendor = "net.java.slee";
	private final String version = "1.2";
	
	@Test
	public void testClientTransactionEvents() {
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Response.TRYING", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.TRYING)), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Response.PROVISIONAL", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.RINGING)), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Response.SUCCESS", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.OK)), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Response.REDIRECT", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.MOVED_TEMPORARILY)), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Response.CLIENT_ERROR", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.NOT_FOUND)), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Response.SERVER_ERROR", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.SERVER_INTERNAL_ERROR)), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Response.GLOBAL_FAILURE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new ResponseMessage(Response.SESSION_NOT_ACCEPTABLE)), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Timeout.TRANSACTION", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getTransactionTimeoutEventId(eventLookupFacility,false), counter);
	}
	
	@Test
	public void testServerTransactionEvents() {
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.INVITE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.INVITE), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.ACK", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.ACK), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.CANCEL", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.CANCEL), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.BYE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.BYE), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.OPTIONS", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.OPTIONS), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.REGISTER", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.REGISTER), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.INFO", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.INFO), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.PRACK", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.PRACK), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.UPDATE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.UPDATE), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.MESSAGE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.MESSAGE), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.SUBSCRIBE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.SUBSCRIBE), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.NOTIFY", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.NOTIFY), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.REFER", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.REFER), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.PUBLISH", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.PUBLISH), false), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.SIP_EXTENSION", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility,new RequestMessage("MARTINS"), false), counter);
	}
	
	@Test
	public void testDialogEvents() {
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.INVITE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.INVITE), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.ACK", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.ACK), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.BYE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.BYE), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.message.Request.CANCEL", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.CANCEL), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.OPTIONS", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.OPTIONS), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.REGISTER", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.REGISTER), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.INFO", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.INFO), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.PRACK", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.PRACK), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.UPDATE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.UPDATE), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.MESSAGE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.MESSAGE), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.SUBSCRIBE", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.SUBSCRIBE), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.NOTIFY", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.NOTIFY), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.REFER", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.REFER), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.PUBLISH", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility, new RequestMessage(Request.PUBLISH), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Dialog.SIP_EXTENSION", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getEventId(eventLookupFacility,new RequestMessage("MARTINS"), true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Timeout.TRANSACTION", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getTransactionTimeoutEventId(eventLookupFacility,true), counter);
		counter++;
		eventLookupFacility.putEventID("javax.sip.Timeout.Dialog", vendor, version, counter);
		Assert.assertEquals(eventIDCache.getDialogTimeoutEventId(eventLookupFacility), counter);
	}
}
