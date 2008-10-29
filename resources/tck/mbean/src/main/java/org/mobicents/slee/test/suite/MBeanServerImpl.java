/*
 * MBeanServerImpl.java
 * 
 * Created on Jan 17, 2005
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

package org.mobicents.slee.test.suite;

import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.loading.ClassLoaderRepository;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.jmx.adaptor.rmi.RMINotificationListener;

/**
 * This is for TCK support. This class gives remote access to the MBean server.
 * The TCK needs this access. It is just for the TCK.
 * 
 * @author M. Ranganathan
 *  
 */

public class MBeanServerImpl implements MBeanServer {
    private RMIAdaptor mbeanServer;

    private HashMap listenerMap;
    private JUnitSleeTestUtils utils;
    private BulkEventHandlerImpl bulkEventHandler;

    public MBeanServerImpl( JUnitSleeTestUtils utils) throws Exception {

        this.mbeanServer = utils.getRMIAdapter();
        this.bulkEventHandler = utils.getBulkEventHandler();
        this.listenerMap = new HashMap();
        this.utils = utils;

    }

    public ObjectInstance createMBean(String className, ObjectName name)
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException, ReflectionException, MBeanException {

        try {
            return this.mbeanServer.createMBean(className, name);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new MBeanException(e);
        }
    }

    public ObjectInstance createMBean(String className, ObjectName name,
            Object params[], String signature[])
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException, ReflectionException, MBeanException {
        try {
            return this.mbeanServer.createMBean(className, name, params,
                    signature);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ObjectInstance createMBean(String className, ObjectName name,
            ObjectName loaderName, Object params[], String signature[])
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException, InstanceNotFoundException,
            ReflectionException, MBeanException {
        try {
            return this.mbeanServer.createMBean(className, name, loaderName,
                    params, signature);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void unregisterMBean(ObjectName name)
            throws InstanceNotFoundException, MBeanRegistrationException {
        try {
            this.mbeanServer.unregisterMBean(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public ObjectInstance getObjectInstance(ObjectName name)
            throws InstanceNotFoundException {
        try {
            return this.mbeanServer.getObjectInstance(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Set queryMBeans(ObjectName name, QueryExp query) {
        try {
            return this.mbeanServer.queryMBeans(name, query);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Set queryNames(ObjectName name, QueryExp query) {
        try {
            return this.mbeanServer.queryNames(name, query);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public boolean isRegistered(ObjectName name) {
        try {
            return this.mbeanServer.isRegistered(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Integer getMBeanCount() {
        try {
            return this.mbeanServer.getMBeanCount();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Object getAttribute(ObjectName name, String attribute)
            throws AttributeNotFoundException, InstanceNotFoundException,
            MBeanException, ReflectionException {
        try {
            return this.mbeanServer.getAttribute(name, attribute);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public AttributeList getAttributes(ObjectName name, String[] attributes)
            throws InstanceNotFoundException, ReflectionException {
        try {
            return this.mbeanServer.getAttributes(name, attributes);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public void setAttribute(ObjectName name, Attribute attribute)
            throws InstanceNotFoundException, AttributeNotFoundException,
            InvalidAttributeValueException, MBeanException, ReflectionException {
        try {
            this.mbeanServer.setAttribute(name, attribute);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public AttributeList setAttributes(ObjectName name, AttributeList attributes)
            throws InstanceNotFoundException, ReflectionException {
        try {
            return this.mbeanServer.setAttributes(name, attributes);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public Object invoke(ObjectName name, String operationName,
            Object params[], String signature[])
            throws InstanceNotFoundException, MBeanException,
            ReflectionException {
        try {
            return this.mbeanServer.invoke(name, operationName, params,
                    signature);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public String getDefaultDomain() {
        try {
            return this.mbeanServer.getDefaultDomain();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public void addNotificationListener(ObjectName name,
            NotificationListener listener, NotificationFilter filter,
            Object handback) throws InstanceNotFoundException {
        try {
            System.out.println("addNotificationListener " + listener.getClass());
           // new Exception().printStackTrace();
            RMINotificationListener rmiListener = 
                new RMINotificationListenerImpl(listener);
            this.mbeanServer.addNotificationListener(name, rmiListener, filter,
                    handback);
            this.listenerMap.put(listener, rmiListener);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
 
    public void addNotificationListener(ObjectName name, ObjectName listener,
            NotificationFilter filter, Object handback)
            throws InstanceNotFoundException {
        try {

            
            this.mbeanServer.addNotificationListener(name, listener, filter,
                    handback);

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public void removeNotificationListener(ObjectName name,
            NotificationListener listener) throws InstanceNotFoundException,
            ListenerNotFoundException {

        try {
            RMINotificationListener rmiListener = (RMINotificationListener) this.listenerMap
                    .get(listener);
            if (rmiListener == null)
                throw new ListenerNotFoundException("no listener in my map! ");
            this.mbeanServer.removeNotificationListener(name, rmiListener);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

    }

    public void removeNotificationListener(ObjectName name, ObjectName listener)
            throws InstanceNotFoundException, ListenerNotFoundException {
        try {
            this.mbeanServer.removeNotificationListener(name, listener);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public MBeanInfo getMBeanInfo(ObjectName name)
            throws InstanceNotFoundException, IntrospectionException,
            ReflectionException {
        try {
            return this.mbeanServer.getMBeanInfo(name);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public boolean isInstanceOf(ObjectName name, String className)
            throws InstanceNotFoundException {
        try {
            return this.mbeanServer.isInstanceOf(name, className);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#createMBean(java.lang.String,
     *      javax.management.ObjectName, javax.management.ObjectName)
     */
    public ObjectInstance createMBean(String arg0, ObjectName arg1,
            ObjectName arg2) throws ReflectionException,
            InstanceAlreadyExistsException, MBeanRegistrationException,
            MBeanException, NotCompliantMBeanException,
            InstanceNotFoundException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#registerMBean(java.lang.Object,
     *      javax.management.ObjectName)
     */
    public ObjectInstance registerMBean(Object arg0, ObjectName arg1)
            throws InstanceAlreadyExistsException, MBeanRegistrationException,
            NotCompliantMBeanException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }


    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getDomains()
     */
    public String[] getDomains() {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.ObjectName, javax.management.NotificationFilter,
     *      java.lang.Object)
     */
    public void removeNotificationListener(ObjectName arg0, ObjectName arg1,
            NotificationFilter arg2, Object arg3)
            throws InstanceNotFoundException, ListenerNotFoundException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#removeNotificationListener(javax.management.ObjectName,
     *      javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public void removeNotificationListener(ObjectName arg0,
            NotificationListener arg1, NotificationFilter arg2, Object arg3)
            throws InstanceNotFoundException, ListenerNotFoundException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String)
     */
    public Object instantiate(String arg0) throws ReflectionException,
            MBeanException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      javax.management.ObjectName)
     */
    public Object instantiate(String arg0, ObjectName arg1)
            throws ReflectionException, MBeanException,
            InstanceNotFoundException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      java.lang.Object[], java.lang.String[])
     */
    public Object instantiate(String arg0, Object[] arg1, String[] arg2)
            throws ReflectionException, MBeanException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#instantiate(java.lang.String,
     *      javax.management.ObjectName, java.lang.Object[], java.lang.String[])
     */
    public Object instantiate(String arg0, ObjectName arg1, Object[] arg2,
            String[] arg3) throws ReflectionException, MBeanException,
            InstanceNotFoundException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize(javax.management.ObjectName,
     *      byte[])
     */
    public ObjectInputStream deserialize(ObjectName arg0, byte[] arg1)
            throws InstanceNotFoundException, OperationsException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize(java.lang.String, byte[])
     */
    public ObjectInputStream deserialize(String arg0, byte[] arg1)
            throws OperationsException, ReflectionException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#deserialize(java.lang.String,
     *      javax.management.ObjectName, byte[])
     */
    public ObjectInputStream deserialize(String arg0, ObjectName arg1,
            byte[] arg2) throws InstanceNotFoundException, OperationsException,
            ReflectionException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoaderFor(javax.management.ObjectName)
     */
    public ClassLoader getClassLoaderFor(ObjectName arg0)
            throws InstanceNotFoundException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoader(javax.management.ObjectName)
     */
    public ClassLoader getClassLoader(ObjectName arg0)
            throws InstanceNotFoundException {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.MBeanServer#getClassLoaderRepository()
     */
    public ClassLoaderRepository getClassLoaderRepository() {
        throw new RuntimeException("Not Implemented"); //TODO: implement this
    }

}

