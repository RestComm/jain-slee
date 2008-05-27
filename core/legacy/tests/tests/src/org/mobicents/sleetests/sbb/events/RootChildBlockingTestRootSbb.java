package org.mobicents.sleetests.sbb.events;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

public abstract class RootChildBlockingTestRootSbb extends BaseTCKSbb {
	private static Logger log = Logger.getLogger(RootChildBlockingTestRootSbb.class.getName());
	
	public RootChildBlockingTestRootSbb() {}

	public void onTCKResourceEventX1(TCKResourceEventX event, ActivityContextInterface aci) {
		RootChildBlockingTestChildSbbLocalObject child = null;
		
		try {
			log.info("RootChildBlockingTestRootSbb Started");

			child = (RootChildBlockingTestChildSbbLocalObject) getChildSbb().create();
			log.info("child SBB created");

			Context initCtx = new InitialContext();
			Context env = (Context) initCtx.lookup("java:comp/env");
			NullActivityFactory nullActivityFactory = (NullActivityFactory) env.lookup("slee/nullactivity/factory");
			NullActivity nullAC = nullActivityFactory.createNullActivity();
			
			NullActivityContextInterfaceFactory  nullACIFactory = 
				(NullActivityContextInterfaceFactory) env.lookup("slee/nullactivity/activitycontextinterfacefactory");			
		    ActivityContextInterface nullACI = nullACIFactory.getActivityContextInterface(nullAC);
			log.info("null ACI created");
			
		    setNullAciChildCMP(nullACI);
			log.info("null ACI stored in root's CMP");

		    child.setNullAciRootCmpFromRemote(nullACI);
			log.info("null ACI stored in child's CMP");
			
			nullACI.attach(child);
			log.info("child attached to null ACI");
			
			nullACI.attach(this.getSbbContext().getSbbLocalObject());
			log.info("root attached to null ACI");
			
			aci.attach(child);
			log.info("child attached to ac of X1");
			
			aci.detach(this.getSbbContext().getSbbLocalObject());
			log.info("root detached from ac of X1");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		} 
	}
	
	/**
	 * Nofication expected from Child SBB on NullACI created by Root SBB
	 * @param event
	 * @param aci
	 */
	public void onNotifyEvent(NotifyEvent event, ActivityContextInterface aci) {
		try {
			log.info("onNotify received, waiting 25 ms");
			
			Thread.sleep(25);
			
			log.info("onNotify returned from sleep");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}
	}
	
	// 'nullAciChildCMP' CMP field setter
	public abstract void setNullAciChildCMP(ActivityContextInterface value);
	// 'nullAciChildCMP' CMP field getter
	public abstract ActivityContextInterface getNullAciChildCMP();

	public abstract ChildRelation getChildSbb();
}
