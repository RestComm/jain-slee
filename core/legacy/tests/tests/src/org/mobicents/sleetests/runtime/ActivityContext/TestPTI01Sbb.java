package org.mobicents.sleetests.runtime.ActivityContext;

import java.util.HashMap;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.SbbContext;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;

/**
 * Tests if attaching an sbb entity to the AC where the event was received doesn't cause the sbbEntity to receive it again. 
 * @author Eduardo Martins
 *
 */
public abstract class TestPTI01Sbb extends BaseTCKSbb {

	private SbbContext sbbContext; 				// This SBB's SbbContext
		
	public void setSbbContext(SbbContext context) { 
		this.sbbContext = context;		
	}
	
	// CMP field
    public abstract void setValue(boolean val);
    public abstract boolean getValue();
	
	public void onTCKResourceEventX1(TCKResourceEventX event,
			ActivityContextInterface aci) {
		System.out.println("Event X1 rcvd!");
		try {
			if (getValue() == false) {
				setValue(true);				
				aci.attach(sbbContext.getSbbLocalObject());
				fireTCKResourceEventX2(event,aci,null);
				System.out.println("Event X2 fired!");
			}
			else {				
				HashMap map = new HashMap();
				map.put("Result", new Boolean(false));
				map.put("Message", "Sbb Entity received the event X1 two times.");
				TCKSbbUtils.getResourceInterface().sendSbbMessage(map);				
			}
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}		
	}
		
	public void onTCKResourceEventX2(TCKResourceEventX event,
			ActivityContextInterface aci) {
		System.out.println("Event X2 rcvd!");
		try {			
				HashMap map = new HashMap();
				map.put("Result", new Boolean(true));
				map.put("Message", "Sbb Entity doesn't received the event X1 two times.");
				TCKSbbUtils.getResourceInterface().sendSbbMessage(map);							
		} catch (Exception e) {
			TCKSbbUtils.handleException(e);
		}		
	}

    public abstract void fireTCKResourceEventX2(TCKResourceEventX event, ActivityContextInterface aci, Address address);
}
