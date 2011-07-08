package org.mobicents.slee.service.tftpserverra.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import net.java.slee.resource.tftp.TransferActivity;
import net.java.slee.resource.tftp.TftpServerActivityContextInterfaceFactory;
import net.java.slee.resource.tftp.events.RequestEvent;

import org.apache.commons.net.tftp.TFTPErrorPacket;
import org.apache.commons.net.tftp.TFTPReadRequestPacket;
import org.apache.commons.net.tftp.TFTPRequestPacket;
import org.apache.commons.net.tftp.TFTPWriteRequestPacket;
import org.jboss.system.server.ServerConfigLocator;

/**
 * Example uses of TFTP server RA.
 * Demonstrated here is the intercept of read and write requests from TFTP clients.
 * The default action is to serve up or store the requested file (through tftp)
 * from a given root directory (in this case the same as used for http).
 * <P>
 * When a special file is requested ("HelloWorld"), the example uses the
 * alternative IOStream interface to read and check the data (in case of a write)
 * or send a special helloworld-string in case of a read request.
 * <P>
 * The read also demoes the ability to suspend the activity, do some other stuff
 * (like querying a database on what data to send) and resume it when the other
 * activity fires (like when a query completes). 
 * 
 * @author Tom Uijldert
 */
public abstract class TftpServerRaExampleSbb implements Sbb {

	private transient Tracer trc;

	private SbbContext	sbbContext;
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory  nullAcif;

	@SuppressWarnings("unused") 		// not used (yet)
	private TftpServerActivityContextInterfaceFactory tftpAcif;

	protected TimerFacility timerFacility;
	protected static TimerOptions timerOptions = new TimerOptions();

	private static File rootDir;

	/**
	 * Read/write in the same directory as http.
	 */
	private static void setRootDir() {
        String basedir = ServerConfigLocator.locate().getServerHomeDir().toString();
        rootDir = new File(basedir + "/deploy/ROOT.war");
	}

	/**
	 * Intercept a TFTP write request and handle it if required, otherwise
	 * just create and write the file.
	 * @param event
	 * @param aci
	 */
	public void onWrite(RequestEvent event, ActivityContextInterface aci) {
		TFTPWriteRequestPacket req = (TFTPWriteRequestPacket) event.getRequest();

		if (isSpecialRequest(req))
		{
			trc.info(" --- Special Write ---");
			doSpecialWrite(req, aci);
		}
		else {
			trc.info(" --- Regular Write ---");
			TransferActivity activity = (TransferActivity) aci.getActivity();
	        try {
	        	activity.receiveFile(new File(rootDir, req.getFilename()));
	        } catch (FileNotFoundException e) {
	        	activity.sendError(TFTPErrorPacket.FILE_NOT_FOUND, e.getMessage());
	            return;
	        } catch (Exception e) {
	        	activity.sendError(TFTPErrorPacket.UNDEFINED, e.getMessage());
	            return;
	        }
		}
	}

	private static final String SPECIAL_CONTENT = "Hello world! (delayed)...";

	private boolean isSpecialRequest(TFTPRequestPacket req) {
		return req.getFilename().equals("HelloWorld");
	}

    /**
     * actually do the special thing that needs to be done.
     * In this case, use the inputstream to capture received data and generate an
     * error if it doesn't have pre-defined content.
     * 
     * @param packet
     * @param aci
     */
    private void doSpecialWrite(TFTPWriteRequestPacket packet, ActivityContextInterface aci) {
		TransferActivity activity = (TransferActivity) aci.getActivity();
        try {
	        byte[] buffer = new byte[128];

	        InputStream is = activity.getInputStream();
			int bytesRead = is.read(buffer);
			String content = new String(buffer, 0, bytesRead); 
			if (!content.equals(SPECIAL_CONTENT)) {
				trc.severe("--- Special write: wrong content '"+content+"' ---");
				activity.sendError(TFTPErrorPacket.UNDEFINED, "wrong content");
			}else
			{
				trc.info("--- Special write: content approved ---");
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Intercept a TFTP read request and handle it if required, otherwise
	 * just read/send the file.
	 * @param event
	 * @param aci
	 */
	public void onRead(RequestEvent event, ActivityContextInterface aci) {
		TFTPReadRequestPacket req = (TFTPReadRequestPacket) event.getRequest();

		if (isSpecialRequest(req))
		{
			trc.info(" --- Special Read ---");
			doSpecialRead();
		}
		else {
			trc.info(" --- Regular Read ---");
			TransferActivity activity = (TransferActivity) aci.getActivity();
	        try {
	    		activity.sendFile(new File(rootDir, req.getFilename()));
	        } catch (FileNotFoundException e) {
	        	activity.sendError(TFTPErrorPacket.FILE_NOT_FOUND, e.getMessage());
	            return;
	        } catch (Exception e) {
	        	activity.sendError(TFTPErrorPacket.UNDEFINED, e.getMessage());
	            return;
	        }
		}
	}

	/**
	 * Start another activity triggered by this read request.
	 * 
	 * @param ctxt
	 */
	private void doSpecialRead() {
		startSomeRelatedActivity();
	}

    /**
     * For example purposes this just starts a timer on a null activity, delaying
     * the read request-handling with about 2 seconds.
     */
    private void startSomeRelatedActivity() {
	    NullActivity nullActivity = nullActivityFactory.createNullActivity();
	    ActivityContextInterface aci = null;
	    try {
	        aci = nullAcif.getActivityContextInterface(nullActivity);
	        aci.attach(sbbContext.getSbbLocalObject());
	        // actually, just wait for 2 seconds.
            timerFacility.setTimer(aci, null,
                    System.currentTimeMillis() + 2000, timerOptions);
        } catch (Exception e) {
            trc.severe("startRelatedActivity(): Failed to start timer", e);
        }
    }

	/**
	 * The firing of the related event (timeout here actually - check the xml)
	 * that will trigger the pending read request handling.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onSomeOtherRelatedEvent(TimerEvent event, ActivityContextInterface aci) {

		try {
			trc.info(" --- Special Read: delayed action ---");
			OutputStream os = getRequestActivity().getOutputStream();
			os.write(SPECIAL_CONTENT.getBytes());
            os.flush(); os.close();
		} catch (Exception e) {
            trc.severe("Error resuming TFTP READ request", e);
		}
    }

	private TransferActivity getRequestActivity() {
		for (ActivityContextInterface aci : sbbContext.getActivities())
			if (aci.getActivity() instanceof TransferActivity)
				return (TransferActivity) aci.getActivity();
		return null;
	}

	//----- SBB lifetime methods
	public void setSbbContext(SbbContext context) {
		sbbContext = context;
		trc = context.getTracer(this.getClass().getSimpleName());
		try {
			Context name = (Context) new InitialContext().lookup("java:comp/env");

			nullActivityFactory = (NullActivityFactory) name.lookup("slee/nullactivity/factory");
			nullAcif = (NullActivityContextInterfaceFactory)
						name.lookup("slee/nullactivity/activitycontextinterfacefactory");
			timerFacility = (TimerFacility) name.lookup("slee/facilities/timer");

			tftpAcif = (TftpServerActivityContextInterfaceFactory)
						name.lookup("slee/resources/mobicents/tftpserver/acifactory");
		} catch (NamingException e) {
			trc.severe(e.getMessage());
		}
		setRootDir();
	}

	public void unsetSbbContext() { }

	public void sbbActivate() { }

	public void sbbCreate() throws CreateException { }

	public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) { }

	public void sbbLoad() { }

	public void sbbPassivate() { }

	public void sbbPostCreate() throws CreateException { }

	public void sbbRemove() { }

	public void sbbRolledBack(RolledBackContext arg0) { }

	public void sbbStore() { }
}
