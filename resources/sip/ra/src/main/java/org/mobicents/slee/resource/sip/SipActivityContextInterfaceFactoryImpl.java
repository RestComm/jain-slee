/*
 * Created on Dec 1, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.resource.sip;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ServerTransaction;
import javax.sip.Transaction;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.sip.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip.wrappers.ServerTransactionWrapper;

import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;


/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class SipActivityContextInterfaceFactoryImpl implements
        SipActivityContextInterfaceFactory,
        ResourceAdaptorActivityContextInterfaceFactory {
    private final String jndiName;
    private String raEntityName;
    private SleeContainer serviceContainer;
    
    private ActivityContextFactory activityContextFactory;

    public SipActivityContextInterfaceFactoryImpl(SleeContainer svcContainer, String entityName) {
        this.serviceContainer = svcContainer;
        this.activityContextFactory = svcContainer.getActivityContextFactory();
        this.raEntityName = entityName;
        this.jndiName = "java:slee/resources/" + entityName + "/sipacif";
        
    }

    public String getJndiName() {
        // TODO Auto-generated method stub
        return jndiName;
    }

    public ActivityContextInterface getActivityContextInterface(
            ClientTransaction clientTransaction) throws NullPointerException,
            UnrecognizedActivityException, FactoryException {
        if (clientTransaction == null)
            throw new NullPointerException("sip activity ! huh!!");
        
        ActivityContext AC=this.getActivityContextForActivity(clientTransaction);
        return new ActivityContextInterfaceImpl(this.serviceContainer,AC.getActivityContextId());
    }

    public ActivityContextInterface getActivityContextInterface(
            ServerTransaction serverTransaction) throws NullPointerException,
            UnrecognizedActivityException, FactoryException {
        if (serverTransaction == null)
            throw new NullPointerException("sip activity ! huh!!");
        ActivityContext AC=this.getActivityContextForActivity(serverTransaction);
        return new ActivityContextInterfaceImpl(this.serviceContainer,AC.getActivityContextId());
    }
    public ActivityContextInterface getActivityContextInterface(Dialog dialog) throws NullPointerException, UnrecognizedActivityException, FactoryException {
		if(dialog==null)
		{	
			//lets stick to original message plan...
			throw new NullPointerException("sip activity ! huh!!");
		}
		ActivityContext AC=this.getActivityContextForActivity(dialog);
        return new ActivityContextInterfaceImpl(this.serviceContainer,AC.getActivityContextId());
		
	}
    
    /**
     * Conveniant method to wrap same code for  factory methods.
     * @param activity - Activity object for which we will return AC,  should implement one of interfaces: <b>javax.sip.Dialog</b> or <b>javax.sip.Transaction</b> .
     * @return ActovityContext for passed object.
     */
   private ActivityContext getActivityContextForActivity(Object activity)
   {
	   //SIGNIFICANT performance hit? --> let me know.
//	 Activity Handle for activity
	   SipActivityHandle SAH=null;
	  
	   
	   if(activity instanceof ServerTransactionWrapper)
	   {
		   SAH=((ServerTransactionWrapper)activity).getActivityHandle();
	   }
	   else if(activity instanceof ClientTransactionWrapper)
	   {
		   SAH=((ClientTransactionWrapper)activity).getActivityHandle();
	   }
	   else if(activity instanceof DialogWrapper)
	   {
		   SAH=((DialogWrapper)activity).getActivityHandle();
	   }
	   else
		   throw new ClassCastException(" Activity does not implement javax.sip.Dialog or javax.sip.Transaction!!!");
	   
       // Slee AH which is proxy like class for wrapped activity handle object
       SleeActivityHandle SLAH=new SleeActivityHandle(raEntityName, SAH, serviceContainer);
       // Acitvity context
       // NOTE that SLAH is passed, not an activity object
       ActivityContext AC=this.activityContextFactory.getActivityContext(SLAH);
	return AC;   
   }

	

}