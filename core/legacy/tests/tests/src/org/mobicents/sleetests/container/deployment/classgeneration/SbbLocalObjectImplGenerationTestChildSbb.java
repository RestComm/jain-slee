package org.mobicents.sleetests.container.deployment.classgeneration;

import java.util.HashMap;
import java.util.logging.Logger;

import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class SbbLocalObjectImplGenerationTestChildSbb extends BaseTCKSbb implements SbbLocalObjectImplGenerationTestChildSbbLocalObject {

	private static Logger log = Logger.getLogger(SbbLocalObjectImplGenerationTestChildSbb.class.getName());
	
	public void method1() {
		HashMap sbbData = new HashMap();
		sbbData.put("result", Boolean.TRUE);
		sbbData.put("message", "method1() invoked");
		try {
			TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
		}
		catch (Exception e) {
			log.severe("failed to send test result");
			e.printStackTrace();
		}
	}
	
	public void method2() {
		// TODO Auto-generated method stub
		
	}
}
