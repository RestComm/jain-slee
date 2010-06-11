/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.examples.jcc.vpn.slee;

import java.util.Date;
import javax.slee.ActivityContextInterface;
import org.mobicents.examples.jcc.vpn.rules.Call;

/**
 *
 * @author kulikov
 */
public interface ConnectionActivityContext extends ActivityContextInterface {
    public Call getCall();
    public void setCall(Call call);
    
    public Date getStartTime();
    public void setStartTime(Date time);    
}
