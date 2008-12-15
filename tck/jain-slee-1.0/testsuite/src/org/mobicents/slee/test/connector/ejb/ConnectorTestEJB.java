/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Nov 26, 2004
 *
 * ConnectorTestEJB.java
 */
package org.mobicents.slee.test.connector.ejb;

import org.mobicents.slee.connector.adaptor.SleeConnectionImpl;
import org.mobicents.slee.test.connector.TestEvent;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.resource.spi.ManagedConnection;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;
import javax.slee.connection.SleeConnectionFactory;
import org.jboss.logging.Logger;

public class ConnectorTestEJB implements SessionBean {
    private static Logger log = Logger.getLogger(ConnectorTestEJB.class);

    public void ejbCreate() {
    }

    public void ejbActivate() {
    }

    public void ejbRemove() {
    }

    public void ejbPassivate() {
    }

    public void setSessionContext(SessionContext ctx) {
    }
    
    private boolean failed = false;
    
    private void doAssert(boolean b) {
        if (!b)
        {
            failed = true;
            log.debug("Failed assertion");
        }
    }
    
    /*
     * Test1:
     * Test connections
     * 
     */
    
    public boolean test1()
    {
        try {
            failed = false;
       
	        log.debug("Running JCA adaptor test1====================");
	        
	        InitialContext ic = new InitialContext();
	        
	        Context envCtx = (Context)new InitialContext().lookup("java:comp/env");

	        envCtx.bind("foo", "bar");
	        
	        log.debug("Worked!!");
	        
	        
	        SleeConnectionFactory factory =
	            (SleeConnectionFactory)ic.lookup("java:/MobicentsConnectionFactory");
	        
	        log.debug("Looked up sleeconnectionfactory");
	        
	        SleeConnectionImpl conn1 = (SleeConnectionImpl)factory.getConnection();
	        SleeConnectionImpl conn2 = (SleeConnectionImpl)factory.getConnection();
	        
	        log.debug("conn1 is: " + conn1);
	        log.debug("conn2 is: " + conn2);
	        
	        ManagedConnection mc1 = conn1.getMC();
	        ManagedConnection mc2 = conn2.getMC();
	        
	        log.debug("mc1 is: " + mc1);
	        log.debug("mc2 is: " + mc2);
	        
	        
	        
	        conn1.close();
	        conn2.close();
	        
	        log.debug("Closed them");
	        
	        conn1 = (SleeConnectionImpl)factory.getConnection();
	        mc1 = conn1.getMC();
	        conn1.close();
	        
	        conn2 = (SleeConnectionImpl)factory.getConnection();
	        mc2 = conn2.getMC();
	        
	        log.debug("mc1 is: " + mc1);
	        log.debug("mc2 is: " + mc2);
	        
	        
	        
	        conn2.close();
	        
	        
	        return !failed;
        }
        catch(Throwable t) {
            log.error("Caught throwable in running JCA tests", t);
            return false;
        }
    }
    
    
    private boolean testNoHandle() {    
	    try {       	        
	        
	        InitialContext ic = new InitialContext();
	        
	        SleeConnectionFactory factory =
	            (SleeConnectionFactory)ic.lookup("java:/MobicentsConnectionFactory");
	        
	        log.debug("Looked up sleeconnectionfactory");
	        
	        SleeConnection conn1 = factory.getConnection();
	
	        EventTypeID eventTypeOne = conn1.getEventTypeID("org.mobicents.slee.test.connector.TestEventTypeName","mobicents","0.1");	       
	        
	        log.debug("Event type is: " + eventTypeOne);
	        
	        //doAssert(eventTypeOne != null);
	        
	        Object eventOne = new TestEvent("test 1");
	        	        
	        log.debug("Firing first event without activity handle");
	        conn1.fireEvent(eventOne, eventTypeOne, null, null);
	        
	        log.debug("Firing second event without activity handle");
	        conn1.fireEvent(eventOne, eventTypeOne, null, null);
	        
	        conn1.close();
	        
	        return !failed;
	    
	    }
	    catch(Throwable t) {
	        log.error("Caught throwable in running JCA tests", t);
	        
	        return false;
	    }
    }
    
    
    /** Fire two events with no activity handle without a transaction */
    public boolean test2(){
        log.debug("Running test2===============================");
        failed = false;
        
        return testNoHandle();
    }
    
    /** Fire two events with no activity handle in a transaction */
    public boolean test3(){
        log.debug("Running test3======================================");
        failed = false;
        
        return testNoHandle();
    }
    
    private boolean testWithHandle() {    
	    try {       	        
	        
	        InitialContext ic = new InitialContext();
	        
	        SleeConnectionFactory factory =
	            (SleeConnectionFactory)ic.lookup("java:/MobicentsConnectionFactory");
	        
	        log.debug("Looked up sleeconnectionfactory");
	        
	        SleeConnection conn1 = factory.getConnection();
	
	        EventTypeID eventTypeOne = conn1.getEventTypeID("org.mobicents.slee.test.connector.TestEventTypeName","mobicents","0.1");	       
	        
	        log.debug("Event type is: " + eventTypeOne);
	        
	        //doAssert(eventTypeOne != null);
	        
	        Object eventOne = new TestEvent("test 1");
	        
	        ExternalActivityHandle handle = conn1.createActivityHandle();
	        log.debug("Created activityhandle: " + handle);
	        	        
	        log.debug("Firing first event with activity handle");
	        conn1.fireEvent(eventOne, eventTypeOne, handle, null);
	        
	        log.debug("Firing second event with activity handle");
	        conn1.fireEvent(eventOne, eventTypeOne, handle, null);
	        
	        conn1.close();
	        
	        return !failed;
	    
	    }
	    catch(Throwable t) {
	        log.error("Caught throwable in running JCA tests", t);
	        
	        return false;
	    }
    }    
    
    /** Fire two events with an activity handle without a transaction */
    public boolean test4(){        
        log.debug("Running test4===================================");
        failed = false;
        
        return testWithHandle();
     
    }
   
    /** Fire two events with an activity handle in a transaction */
    public boolean test5() {
        log.debug("Running test5=======================================");
        failed = false;
        
        return testWithHandle();
    }
    
    private boolean testWith2Handles() {
        try {       	        
	        
	        InitialContext ic = new InitialContext();
	        
	        SleeConnectionFactory factory =
	            (SleeConnectionFactory)ic.lookup("java:/MobicentsConnectionFactory");
	        
	        log.debug("Looked up sleeconnectionfactory");
	        
	        SleeConnection conn1 = factory.getConnection();
	
	        EventTypeID eventTypeOne = conn1.getEventTypeID("org.mobicents.slee.test.connector.TestEventTypeName","mobicents","0.1");	       
	        
	        log.debug("Event type is: " + eventTypeOne);
	        
	        //doAssert(eventTypeOne != null);
	        
	        Object eventOne = new TestEvent("test 1");
	        
	        ExternalActivityHandle handle1 = conn1.createActivityHandle();
	        log.debug("Created activityhandle: " + handle1);
	        	        
	        log.debug("Firing first event without activity handle");
	        conn1.fireEvent(eventOne, eventTypeOne, handle1, null);
	        
	        ExternalActivityHandle handle2 = conn1.createActivityHandle();
	        log.debug("Created activityhandle: " + handle2);
	        
	        log.debug("Firing second event without activity handle");
	        conn1.fireEvent(eventOne, eventTypeOne, handle2, null);
	        
	        conn1.close();
	        
	        return !failed;
	    
	    }
	    catch(Throwable t) {
	        log.error("Caught throwable in running JCA tests", t);
	        
	        return false;
	    }
    }
    
    /** Fire events on two different activity handles in a transaction */
    public boolean test6() {    
	    log.debug("Running test6=======================");
	    failed = false;
	    return testWith2Handles();
    }   
    
    /** Fire events on two different activity handles without a transaction */
    public boolean test7() {    
	    log.debug("Running test7=======================");
	    failed = false;
	    return testWith2Handles();
    } 
    
}