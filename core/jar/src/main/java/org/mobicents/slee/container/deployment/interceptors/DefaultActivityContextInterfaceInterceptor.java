/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * DefaultActivityContextInterfaceInterceptor.java
 * 
 * Created on Aug 19, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.slee.ActivityContextInterface;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MActivityContextAttributeAlias;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 * @author F.Moggia fixed activitycontext
 */
public class DefaultActivityContextInterfaceInterceptor implements
        ActivityContextInterfaceInterceptor {
	    
    /**
     * maps aci attr alias names to sbb ac attr names
     */
    private HashMap<String,String> aliases;
    
    private ActivityContextInterface activityContextInterface=null;
    
    private static Logger logger = Logger.getLogger(DefaultActivityContextInterfaceInterceptor.class);
    /**
     * 
     */
    public DefaultActivityContextInterfaceInterceptor
    		(SbbComponent sbbComponent) {
        aliases = new HashMap<String, String>();
        for (MActivityContextAttributeAlias alias : sbbComponent.getDescriptor().getActivityContextAttributeAliases()) {
        	for (String sbbActivityContextAttributeName : alias.getSbbActivityContextAttributeName()) {
        		aliases.put(sbbActivityContextAttributeName, alias.getAttributeAliasName());
        	}
        }
        
        
    }

    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {       
        // It will be better to ask the activity context for the value not the hashmap
        ActivityContext ac = ((ActivityContextInterfaceImpl)this.activityContextInterface).getActivityContext();
        
       
        if (logger.isDebugEnabled()) {
            logger.debug("DefaultActivityContextInterceptor : " + method.getName() );
        }
        
        
        //Handle the accessors of the activity context interface
        String className=proxy.getClass().getName();                
        String accessorType=method.getName().substring(0,3);
        String accessorName=method.getName().substring(3);
        String firstCharLowerCase = accessorName.substring(0,1).toLowerCase();
        String field = firstCharLowerCase.concat(accessorName.substring(1));
        accessorName = field;
        String alias = (String) this.aliases.get(accessorName);
        String key=className.substring(
                ConcreteClassGeneratorUtils.SBB_CONCRETE_CLASS_NAME_PREFIX.length(),
                className.length()-
                ConcreteClassGeneratorUtils.SBB_CONCRETE_CLASS_NAME_SUFFIX.length())+
                "."+accessorName;
        if ( alias != null ) key = alias;
        
        //String key=accessorName;
        if(accessorType.equalsIgnoreCase("get")){
            if(args.length!=0)
                throw new Exception("wrong number of arguments");
            if (logger.isDebugEnabled()) {
                logger.debug("GET key:" + key);
            }
            Object value = ac.getDataAttribute(key);
            if (logger.isDebugEnabled()) {
                logger.debug("GET value: " + value);
            }
            Class returnType = method.getReturnType();
            if ( value == null ) {
                
                if ( returnType.isPrimitive()) {
                    if ( returnType.equals(Integer.TYPE)) {
                        return new Integer(0);
                    } else if ( returnType.equals(Boolean.TYPE)) {
                        return new Boolean("false");
                    } else if ( returnType.equals( Long.TYPE ) ) {
                        return new Long(0);
                    } else if ( returnType.equals ( Double.TYPE )) {
                        return new Double(0);
                    } else if ( returnType.equals(Float.TYPE)) {
                        return new Float(0);
                    }
                }
            }
           
            return value; 
                
        }
        else if(accessorType.equalsIgnoreCase("set")){
            if(args.length!=1)
                throw new Exception("wrong number of arguments");
            ac.setDataAttribute(key,args[0]);
        }
        else
            throw new Exception("wrong accessor method, it's neither a getter nor a setter");
        return null;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ActivityContextInterfaceInterceptor#setData(java.util.Map)
     */
    public void setActivityContextInterface(ActivityContextInterface activityContextInterface) {
        this.activityContextInterface=activityContextInterface;
       // this.data=((ActivityContextInterfaceImpl)activityContextInterface).getActivityContext().getData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ActivityContextInterfaceInterceptor#getData()
     */
    public ActivityContextInterface getActivityContextInterface() {
        return activityContextInterface;
    }

}
