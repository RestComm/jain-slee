package org.mobicents.sleetests.container.deployment.classgeneration;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class SbbLocalObjectImplGenerationTestSbb extends BaseTCKSbb {

public abstract ChildRelation getChildRelation();
	
	private static Logger log = Logger.getLogger(SbbLocalObjectImplGenerationTestSbb.class.getName());

	public void onTCKResourceEventX1(TCKResourceEventX event, ActivityContextInterface aci) {
		try {
			((SbbLocalObjectImplGenerationTestChildSbbLocalObject)getChildRelation().create()).method1();
		} catch (Exception e) {
			HashMap sbbData = new HashMap();
			sbbData.put("result", Boolean.FALSE);
			sbbData.put("message", "method1() invocation result in exception "+e.getMessage());
			try {
				TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
			}
			catch (Exception f) {
				f.printStackTrace();
				log.severe("failed to send test result");
			}
		}
	}		
}
