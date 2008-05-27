package org.mobicents.sleetests.sbb.events;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class RootChildBlockingTestChildSbb extends BaseTCKSbb 
													implements RootChildBlockingTestChildSbbLocalObject {

	private static Logger log = Logger.getLogger(RootChildBlockingTestChildSbb.class.getName());
	
	public RootChildBlockingTestChildSbb() {}
	
	public void onTCKResourceEventX1(TCKResourceEventX event, ActivityContextInterface aci) {
		try {
			log.info("onTCKResourceEventX1, fire NotifyEvent");
			
			fireNotifyEvent(new NotifyEvent(), getNullAciRootCMP(), null);

			log.info("onTCKResourceEventX1 ended");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}
	}

	public void onTCKResourceEventX2(TCKResourceEventX event, ActivityContextInterface aci) {
		try {
			log.info("onTCKResourceEventX2, fire NotifyEvent");

			aci.detach(this.getSbbContext().getSbbLocalObject());
			log.info("onTCKResourceEventX2, going to sleep for 50 ms");
			
			Thread.sleep(50);
			
			setResultPassed("2nd Event successfully processed");
			log.info("onTCKResourceEventX2 ended");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}
	}

    protected void setResultPassed(String msg) throws Exception {
        log.info("Success: " + msg);

        HashMap sbbData = new HashMap();
        sbbData.put("result", Boolean.TRUE);
        sbbData.put("message", msg);
        TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }
    
    protected void setResultFailed(String msg) throws Exception {
    	log.warning("Failed: " + msg);

    	HashMap sbbData = new HashMap();
    	sbbData.put("result", Boolean.FALSE);
    	sbbData.put("message", msg);
    	TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }
	
	public void setNullAciRootCmpFromRemote(ActivityContextInterface aci) {
		setNullAciRootCMP(aci);
	}
	
	// 'nullAciRootCMP' CMP field setter
	public abstract void setNullAciRootCMP(ActivityContextInterface value);
	// 'nullAciRootCMP' CMP field getter
	public abstract ActivityContextInterface getNullAciRootCMP();
	
	public abstract void fireNotifyEvent(NotifyEvent event, ActivityContextInterface aci, Address address);
}
