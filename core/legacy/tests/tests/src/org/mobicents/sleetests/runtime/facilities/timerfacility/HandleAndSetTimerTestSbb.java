package org.mobicents.sleetests.runtime.facilities.timerfacility;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class HandleAndSetTimerTestSbb extends BaseTCKSbb {
	private static Logger log = Logger.getLogger(HandleAndSetTimerTestSbb.class.getName());
	private NullActivityFactory nullActivityFactory;
	private NullActivityContextInterfaceFactory nullACIFactory;
	private TimerFacility timerFacility;
	public HandleAndSetTimerTestSbb() {}
	
	public void sbbPostCreate() throws CreateException {		
		super.sbbPostCreate();
		setTimerCount(0);
	}
	
	public void setSbbContext(SbbContext arg0) {		
		super.setSbbContext(arg0);
		try {
		Context initCtx = new InitialContext();
		Context env = (Context) initCtx.lookup("java:comp/env");
		nullActivityFactory = (NullActivityFactory) env.lookup("slee/nullactivity/factory");
		nullACIFactory = (NullActivityContextInterfaceFactory) env.lookup("slee/nullactivity/activitycontextinterfacefactory");
		timerFacility = (TimerFacility) env.lookup("slee/facilities/timer");
		}
		catch (Exception e) {
			String msg = "unable to set sbb context, can't proceed. Error msg: " + e.getMessage(); 
			log.info(msg);
			setResult(Boolean.FALSE,msg);
		}
	}
	
	public abstract void setTimerCount(int value);
	public abstract int getTimerCount();
	
	public void onTCKResourceEventX1(TCKResourceEventX event, ActivityContextInterface aci) {
			
		try {
			log.info("HandleAndSetTimerTestSbb Started");			

			NullActivity nullAC = nullActivityFactory.createNullActivity();
			ActivityContextInterface nullACI = nullACIFactory.getActivityContextInterface(nullAC);
			log.info("null ACI created");
			nullACI.attach(getSbbContext().getSbbLocalObject());
			setTimer(nullACI);			
			
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		} 
	}
	
	public void setTimer(ActivityContextInterface aci) {		
		// get actual date
		Date now = new Date();
		// create timer option
		TimerOptions options = new TimerOptions();
        options.setPersistent(true);
        // set timer
       	timerFacility.setTimer(aci, null, now.getTime() + 10000, options);
       	log.info("set 10 secs timer.");
       	
       	setTimerCount(getTimerCount() + 1);
	}
	
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		
		log.info("handling timer event");		
		
		if (getTimerCount() < 2) {
			setTimer(aci);			
		} else {
			String msg = "already set 2 timers, finishing, test suceed"; 
			log.info(msg);
			setResult(Boolean.TRUE,msg);			
		}		
	}
	
	protected void setResult(Boolean testResult, String msg) {		
		HashMap sbbData = new HashMap();
		sbbData.put("result", testResult);
		sbbData.put("message", msg);
		try {
			TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
		}
		catch (Exception e) {
			log.info("unable to send result. Error msg: "+e.getMessage());
		}
	}
}
