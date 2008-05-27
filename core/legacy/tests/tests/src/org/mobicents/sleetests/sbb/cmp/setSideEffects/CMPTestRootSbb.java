package org.mobicents.sleetests.sbb.cmp.setSideEffects;
import java.util.HashMap;

import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
/*
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
*/
import java.util.logging.Logger;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class CMPTestRootSbb extends BaseTCKSbb {
	private static Logger log = Logger.getLogger(CMPTestRootSbb.class.getName());
	
	public CMPTestRootSbb() {}

	public void onTCKResourceEventX1(TCKResourceEventX event, ActivityContextInterface aci) {
		CMPTestChildSbbLocalObject child = null;
		try {
			log.info("CMPTestRootSbb Started");
			log.info("onTCKResourceEventX1 entered.");
			
			child = (CMPTestChildSbbLocalObject) getChildSbb().create();
			log.info("child SBB created");

			setChildCMP(child);
			
			aci.attach(child);
			log.info("child attached to ac of X1");

			log.info("onTCKResourceEventX2 finished.");
		}
		catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}
	}
	
	public void onTCKResourceEventX2(TCKResourceEventX event, ActivityContextInterface aci) {
		try {
			log.info("onTCKResourceEventX2 entered.");
			
			getChildCMP().methodX1();
			
			log.info("onTCKResourceEventX2 finished.");
		}
		catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}
	}
	
    protected void setResultFailed(String msg) throws Exception {
    	log.warning("Failed: " + msg);

    	HashMap sbbData = new HashMap();
    	sbbData.put("result", Boolean.FALSE);
    	sbbData.put("message", msg);
    	TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }
	
    protected void setResultPassed(String msg) throws Exception {
        log.info("Success: " + msg);

        HashMap sbbData = new HashMap();
        sbbData.put("result", Boolean.TRUE);
        sbbData.put("message", msg);
        TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }
	
	public abstract void setChildCMP(CMPTestChildSbbLocalObject value);
	public abstract CMPTestChildSbbLocalObject  getChildCMP();
	
	public abstract ChildRelation getChildSbb();
}
