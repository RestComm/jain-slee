/*
 * RaConfigTestSbb.java
 *
 * Created on 14 Декабрь 2006 г., 18:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.sleetests.container.ratypeUninstall;

import java.util.HashMap;

import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.sbbutils.BaseTCKSbb;
import com.opencloud.sleetck.lib.sbbutils.TCKSbbUtils;
/**
 *
 * @author Oleg Kulikov
 */
public abstract class RaTypeUninstallTestSbb extends BaseTCKSbb {
    
    /** Creates a new instance of RaConfigTestSbb */
    public RaTypeUninstallTestSbb() {
    }
    
    // TODO: Implement the lifecycle methods if required
    public void sbbCreate() throws javax.slee.CreateException {
    }
    
    public void sbbPostCreate() throws javax.slee.CreateException {
    }
    
    public void sbbActivate() {
    }
    
    public void sbbPassivate() {
    }
    
    public void sbbRemove() {
    }
    
    public void sbbLoad() {
    }
    
    public void sbbStore() {
    }
    
    public void sbbExceptionThrown(Exception exception, Object event,
            ActivityContextInterface activity) {
    }
    
    public void sbbRolledBack(RolledBackContext context) {
    }
    
    public void onTCKResourceEventX1(TCKResourceEventX event,
            ActivityContextInterface aci) {
        System.out.println("ON EventX: " + event);
        try {
            setResultPassed("OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    protected void setResultFailed(String msg) throws Exception {
        
    	HashMap sbbData = new HashMap();
    	sbbData.put("result", Boolean.FALSE);
    	sbbData.put("message", msg);
    	TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }
	
    protected void setResultPassed(String msg) throws Exception {

        HashMap sbbData = new HashMap();
        sbbData.put("result", Boolean.TRUE);
        sbbData.put("message", msg);
        TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData);
    }
    
}
