package org.mobicents.sleetests.sbb.eventhandlers;

import java.util.HashMap;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.SbbContext;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

/**
 * Declares event handler with signature for custom SBB ACI instead of generic ACI 
 * @author Ivelin Ivanov
 *
 */
public abstract class TestCustomSbbAciHandlerSbb extends BaseTCKSbb {

	private SbbContext sbbContext; 				// This SBB's SbbContext
		
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context;		
	}
	
	public void onTCKResourceEventX1(TCKResourceEventX event,
			CustomSbbAci aci) {
		System.out.println("Event X1 rcvd!");
		try {
			aci.attach(sbbContext.getSbbLocalObject());
			aci.setCustomAttribute(true);
			fireTCKResourceEventX2(event,aci,null);
			System.out.println("Event X2 fired!");
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}		
	}
		
	public void onTCKResourceEventX2(TCKResourceEventX event,
			CustomSbbAci aci) {
		System.out.println("Event X2 rcvd!");
		try {			
			HashMap map = new HashMap();
			if (aci.getCustomAttribute()) {
				map.put("Result", new Boolean(true));
				map.put("Message", "custom Sbb Aci event handler works well.");
			} else {
				map.put("Result", new Boolean(false));
				map.put("Message", "custom Sbb Aci event handler does not work well. The SBB ACI custom attribute value is " + aci.getCustomAttribute() + " which is not the expected value");
			}
			TCKSbbUtils.getResourceInterface().sendSbbMessage(map);							
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}		
	}

    public abstract void fireTCKResourceEventX2(TCKResourceEventX event, ActivityContextInterface aci, Address address);
    
	public abstract CustomSbbAci asSbbActivityContextInterface(
			ActivityContextInterface aci);
    
}

