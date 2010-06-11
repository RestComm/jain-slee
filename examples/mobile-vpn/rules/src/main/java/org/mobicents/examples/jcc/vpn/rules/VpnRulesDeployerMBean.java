/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.examples.jcc.vpn.rules;

import org.jboss.system.ServiceMBean;

/**
 *
 * @author Oleg Kulikov
 */
public interface VpnRulesDeployerMBean extends ServiceMBean {
    public void setJndiName(String jndiName);
    public String getJndiName();
    
    public void setDeploymentDirectory(String url);
    public String getDeploymentDirectory();
    
    public void setScanPeriod(Integer period);
    public Integer getScanPeriod();
}
