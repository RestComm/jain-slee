package org.mobicents.slee.container.deployment.jboss;

import javax.management.ObjectName;

import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.SubDeployerExtMBean;
import org.jboss.mx.util.ObjectNameFactory;

/**
 * Simple MBean for obtaining current deployer status which will show what's
 * deployed and what's on the waiting list for deployment and undeployment.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
public interface SLEESubDeployerMBean extends SubDeployerExtMBean {

  // The default ObjectName
  public static final ObjectName OBJECT_NAME =
     ObjectNameFactory.create("jboss.system:service=SLEESubDeployer");
  
  String showStatus() throws DeploymentException;

  public void setWaitTimeBetweenOperations(long waitTime);

  public long getWaitTimeBetweenOperations();  
}