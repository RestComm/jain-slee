package org.mobicents.slee.resource.sip;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;

import org.mobicents.slee.container.component.ComponentKey;

// TO REMOVE?

public class SipToSLEEUtility {

	//protected static Logger log = Logger.getLogger(SipResourceAdaptor.class
	//		.getCanonicalName());

	public static void displayMessage(Logger log,String nameOfModule, String message,
			ComponentKey key, Level l) {
		if (log.isLoggable(l)) {
			log.log(l, "\n================================\n" + nameOfModule
					+ " " + message + ":\n" + "\nEvent:\nName:" + key.getName()
					+ "\nVendor: " + key.getVendor() + "\nVersion: "
					+ key.getVersion() + "\n================================");
		}

	}

	public static void displayDeliveryMessage(Logger log,String module, int eventID,
			ComponentKey key, Object activityID, Level l) {
		if (log.isLoggable(l)) {
			log.log(l, "\n================================\n" + module
					+ " delivering event:\nEventID: " + eventID
					+ "\nEvent desc:" + key + "\nActivityID: " + activityID
					+ "\n================================");
		}

	}

	public static void displayMessage(Logger log,String header, String module,
			Object toDump, Level l) {
		if (log.isLoggable(l)) {
			log.log(l, "\n================================\nMODULE:" + module
					+ "\n================================\n" + header
					+ "\n================================\n" + toDump
					+ "\n================================\n");
		}
	}

	// SLEE<-> SIP Constants

	public static final String EVENT_PREFIX_1_2 = "javax.sip.dialog.";

	public static final String EVENT_PREFIX_1_1 = "javax.sip.message.";

	public static final String VENDOR_1_2 = "net.java";

	public static final String VERSION_1_2 = "1.2";

	public static final String VENDOR_1_1 = "javax.sip";

	public static final String VERSION_1_1 = "1.1";

	public static final String RESPONSE_MIDIX = "Response";
	public static final String REQUEST_MIDIX = "Request";

	public static String doMessage(Throwable t) {
		StringBuffer sb = new StringBuffer();
		int tick = 0;
		Throwable e = t;
		do {
			StackTraceElement[] trace = e.getStackTrace();
			if (tick++ == 0)
				sb.append(e.getClass().getCanonicalName() + ":"
						+ e.getLocalizedMessage() + "\n");
			else
				sb.append("Caused by: " + e.getClass().getCanonicalName() + ":"
						+ e.getLocalizedMessage() + "\n");

			for (StackTraceElement ste : trace)
				sb.append("\t" + ste + "\n");

			e = e.getCause();
		} while (e != null);

		return sb.toString();

	}

	// For now event key generation is simple
	public static ComponentKey generateEventKey(RequestEvent req,
			boolean inDialog) {
		String vendor = null;
		String prefix = null;
		String version = null;
		if (inDialog) {
			vendor = VENDOR_1_2;
			version = VERSION_1_2;
			prefix = EVENT_PREFIX_1_2 + REQUEST_MIDIX + ".";
		} else {
			vendor = VENDOR_1_1;
			version = VERSION_1_1;
			prefix = EVENT_PREFIX_1_1 + REQUEST_MIDIX + ".";
		}

		return new ComponentKey(prefix + req.getRequest().getMethod(), vendor,
				version);
	}

	public static ComponentKey generateEventKey(ResponseEvent resp,
			boolean inDialog) {
		String vendor = null;
		String prefix = null;
		String version = null;
		if (inDialog) {
			vendor = VENDOR_1_2;
			version = VERSION_1_2;
			prefix = EVENT_PREFIX_1_1 + RESPONSE_MIDIX + ".";
		} else {
			vendor = VENDOR_1_1;
			version = VERSION_1_1;
			prefix = EVENT_PREFIX_1_1 + RESPONSE_MIDIX + ".";
		}
		ComponentKey eventKey = null;
		int statusCode = resp.getResponse().getStatusCode();
		if (statusCode == 100) {
			//System.out.println("--->100");
			eventKey = new ComponentKey(prefix + "TRYING", vendor, version);
		} else if (100 < statusCode && statusCode < 200) {
			//System.out.println("---> INF "+statusCode);
			eventKey = new ComponentKey(prefix + "INFORMATIONAL", vendor,
					version);
		} else if (statusCode < 300) {
			//System.out.println("---> SUCC "+statusCode);
			eventKey = new ComponentKey(prefix + "SUCCESS", vendor, version);
		} else if (statusCode < 400) {
			//System.out.println("---> REDIRECT "+statusCode);
			eventKey = new ComponentKey(prefix + "REDIRECT", vendor, version);
		} else if (statusCode < 500) {
			//System.out.println("---> CE "+statusCode);
			eventKey = new ComponentKey(prefix + "CLIENT_ERROR", vendor,
					version);
		} else if (statusCode < 600) {
			//System.out.println("---> SE "+statusCode);
			eventKey = new ComponentKey(prefix + "SERVER_ERROR", vendor,
					version);
		} else {
			//System.out.println("---> GF "+statusCode);
			eventKey = new ComponentKey(prefix + "GLOBAL_FAILURE", vendor,
					version);
		}

		return eventKey;
	}

	public static ComponentKey generateEventKey(Object event, boolean inDialog) {
		return null;
	}

	public static Set<ComponentKey> getEventKeys() {
		return null;
	}

}
