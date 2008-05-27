package org.mobicents.sleetests.sbb.cmp.setSideEffects;

import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;

import java.util.logging.Logger;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class CMPTestChildSbb extends BaseTCKSbb 
									  implements CMPTestChildSbbLocalObject {

	private static Logger log = Logger.getLogger(CMPTestChildSbb.class.getName());
	
	public CMPTestChildSbb() {}
	
	public void onTCKResourceEventX1(TCKResourceEventX event, ActivityContextInterface aci) {
		try {
			log.info("onTCKResourceEventX1 entered.");
			
			setCMP1(41);
			log.info("CMP1: " + getCMP1());
			aci.detach(this.getSbbContext().getSbbLocalObject());
			
			log.info("onTCKResourceEventX1 finished.");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}
	}

	public void methodX1() {
		try {
			log.info("methodX1 entered.");

            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
			boolean callget = ((Boolean) ctx.lookup("callget")).booleanValue();

			if (callget) {
				getCMP2();
			}
			setCMP2(42);
			
			int cmp1 = getCMP1();
			log.info("CMP1: " + cmp1);

			log.info("methodX1 finished.");

			if (cmp1 == 41) {
				setResultPassed("CMP1 == 41");				
			}
			else {
				setResultFailed("CMP1 = " + cmp1);
			}
			
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
	
	public abstract void setCMP1(int value);
	public abstract int  getCMP1();

	public abstract void setCMP2(int value);
	public abstract int  getCMP2();
}
