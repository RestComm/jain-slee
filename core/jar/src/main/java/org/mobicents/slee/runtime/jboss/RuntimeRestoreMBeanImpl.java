/*
 * RuntimeRestoreMBeanImpl.java
 * 
 * Created on Aug 16, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.runtime.jboss;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.SleeManagementMBeanImplMBean;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.ThreadedExecutor;

/**
 * This is the code that runs when a new node is chosen as the cluster leader.
 *  
 */
public class RuntimeRestoreMBeanImpl extends StandardMBean implements
        RuntimeRestoreMBean {
    
   
    private ObjectName objectName;

    private static Logger logger = Logger
            .getLogger(RuntimeRestoreMBeanImpl.class);

    private MBeanServer mbeanServer;

    public RuntimeRestoreMBeanImpl() throws NotCompliantMBeanException {
        super(RuntimeRestoreMBean.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.runtime.jboss.RuntimeRestoreMBean#restoreRuntimeState()
     */
    public void restoreRuntimeState() throws Exception {
        
          logger.info("RuntimeRestoreMBeanImpl.restoreRuntimeState() Restoring runtime state!!");
          Executor exec = new DirectExecutor();
          
          exec.execute( new RuntimeRestoreTask());

    }

    protected ObjectName getObjectName(MBeanServer server, ObjectName name)
            throws MalformedObjectNameException {
        return name == null ? new ObjectName(
                "slee:service=RuntimeRestore,platform=jboss") : name;
    }

    public void setRuntimeRestoreMBean(ObjectName objectName) {
        this.objectName = objectName;
    }

    public ObjectName preRegister(MBeanServer mbs, ObjectName oname)
            throws Exception {
        this.mbeanServer = mbs;
        this.objectName = oname;

        logger.info("RuntimeRestoreMBeanImpl.preRegister(): Preregister called");

        return oname;
    }

    public void startService() {
        this.mbeanServer = MBeanServerFactory.createMBeanServer();
        MBeanProxyExt.create(SleeManagementMBeanImplMBean.class, objectName,
                this.mbeanServer);

    }
    
    public void stopService () {
        logger.info("RuntimeRestoreMBeanImpl.stopService()");
    }
    
    public void saveRuntimeState() throws Exception {
        logger.info("RuntimeRestoreMBeanImpl.saveRuntimeState()");
    }

}

