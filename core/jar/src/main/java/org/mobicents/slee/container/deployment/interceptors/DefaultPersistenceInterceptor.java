/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * DefaultPersistenceInterceptor.java
 * 
 * Created on Jul 26, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.Method;

import org.jboss.logging.Logger;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/**
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 *  
 */
public class DefaultPersistenceInterceptor implements PersistenceInterceptor {
    SbbEntity sbbEntity = null;

    Object persistentSbbStateObject = null;

    Class persistentClass = null;

    private static Logger logger = Logger
            .getLogger(DefaultPersistenceInterceptor.class);

    /**
     *  
     */
    public DefaultPersistenceInterceptor() {
        super();
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        Object returnObject = null;

        Class returnType = method.getReturnType();

        String accessorType = method.getName().substring(0, 3);
        String accessorName = method.getName().substring(3);
        char newChar;

        char firstChar = accessorName.charAt(0);

        if (Character.isLowerCase(firstChar)) {
            newChar = Character.toUpperCase(firstChar);
        } else
            newChar = Character.toLowerCase(firstChar);

        StringBuilder sbuf = new StringBuilder(accessorName);
        sbuf.setCharAt(0, newChar);
        accessorName = sbuf.toString();
        if (logger.isDebugEnabled()) {
            logger.debug("sbbEntity:" + sbbEntity);
            logger.debug("sbbEntity.getSbbDescriptor():"
                    + sbbEntity.getSbbComponent());
            logger.debug("sbbEntity.getSbbDescriptor()).getCMPFields():"
                    + sbbEntity.getSbbComponent().getDescriptor().getSbbClasses().getSbbAbstractClass().getCmpFields());
        }

        if (accessorType.equalsIgnoreCase("get")) {
            //logger.debug("get called");
            try {
                returnObject = sbbEntity.getCMPField(accessorName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(" error accessing cmp field ", e);

            }

        } else if (accessorType.equalsIgnoreCase("set")) {
            //logger.debug("set called");
            sbbEntity.setCMPField(accessorName, args[0]);
        } else
            throw new Exception(
                    "wrong accessor method, it's neither a getter nor a setter");
        if (returnObject == null) {
            if (returnType.isPrimitive()) {
                if (returnType.equals(Integer.TYPE)) {
                    return new Integer(0);
                } else if (returnType.equals(Boolean.TYPE)) {
                    return new Boolean("false");
                } else if (returnType.equals(Long.TYPE)) {
                    return new Long(0);
                } else if (returnType.equals(Double.TYPE)) {
                    return new Double(0);
                } else if (returnType.equals(Float.TYPE)) {
                    return new Float(0);
                }
            }
        }
        return returnObject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#getSbbEntity()
     */
    public SbbEntity getSbbEntity() {
        // TODO Auto-generated method stub
        return sbbEntity;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#setSbbEntity(gov.nist.slee.runtime.SbbEntity)
     */
    public void setSbbEntity(SbbEntity sbbEntity) {
        if (logger.isDebugEnabled()) {
            logger.debug("In setSbbEntity::" + sbbEntity);
        }
        this.sbbEntity = (SbbEntity) sbbEntity;
    }

}