package org.mobicents.slee.resource.sip.wrappers;

import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.sip.SipResourceAdaptor;

/**
 * This class acts as dialog idle timeout timer, when it is run dialog should be
 * considered dead - no requests in and out within this dialog for certain time.
 * 
 * @author B. Baranowski
 * @author eduardomartins
 * 
 */
public class DialogTimeoutTimerTask extends TimerTask {

	private SipResourceAdaptor sipRA;
	private DialogWrapper dialog;
	private static Logger logger=Logger.getLogger(DialogTimeoutTimerTask.class);
	public DialogTimeoutTimerTask(DialogWrapper dialog, SipResourceAdaptor sipResourceAdaptor) {
		this.dialog = dialog;
		this.sipRA = sipResourceAdaptor;
	}

	public void run() {
		try {

			// if there is still a non terminated dialog that is an ra activity ...
			//java.lang.NullPointerException  ---> CAUSED BY!!!!!!!!!!!!!!!!-- && DialogState.TERMINATED!=dialog.getState() 
		    //at org.jboss.mx.loading.RepositoryClassLoader.findClass(RepositoryClassLoader.java:630)
		    //at java.lang.ClassLoader.loadClass(Unknown Source)
		    //at org.jboss.mx.loading.RepositoryClassLoader.loadClassImpl(RepositoryClassLoader.java:474)
		    //at org.jboss.mx.loading.RepositoryClassLoader.loadClass(RepositoryClassLoader.java:415)
		    //at java.lang.ClassLoader.loadClass(Unknown Source)
		    //at java.lang.ClassLoader.loadClassInternal(Unknown Source)
		    //at org.mobicents.slee.resource.sip.wrappers.DialogTimeoutTimerTask.run(DialogTimeoutTimerTask.java:41)
		    //at java.util.TimerThread.mainLoop(Unknown Source)
		    //at java.util.TimerThread.run(Unknown Source)
			//if (dialog != null && sipRA != null	&& DialogState.TERMINATED!=dialog.getState() && sipRA.getActivities().containsKey(dialog.getActivityHandle())) {
			if (dialog != null && sipRA != null	&& sipRA.getActivities().containsKey(dialog.getActivityHandle())) {
				// ... terminate it
				dialog.setHasTimedOut();
				dialog.delete();
			}
			
		}
		catch (Exception e) {
			// don't let exceptions escape, will terminate timer
			e.printStackTrace();
		}
	}

	@Override
	public boolean cancel() {
		dialog = null;
		sipRA = null;
		return super.cancel();
	}
	public String toString()
	{
		return "DialogTimeoutTimerTask["+(super.scheduledExecutionTime()>System.currentTimeMillis())+"]["+dialog+"]";
	}
}
