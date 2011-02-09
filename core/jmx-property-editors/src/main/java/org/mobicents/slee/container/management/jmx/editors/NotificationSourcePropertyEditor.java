package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.NotificationSource;
import javax.slee.management.ProfileTableNotification;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.SbbNotification;
import javax.slee.management.SubsystemNotification;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property Editor for {@link NotificationSource}. 
 * 
 * @author martins
 * 
 */
public class NotificationSourcePropertyEditor extends TextPropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {
    	    	
        try {
            
        	String nsString = text;
        	String nsType = nsString.substring(0, nsString.indexOf('['));
        	nsString = nsString.substring(nsType.length()+1,nsString.length()-1);
        	
        	if (nsType.equalsIgnoreCase("ProfileTableNotification")) {
        		 this.setValue(parseProfileTableNotification(nsString));
            
        	} else if (nsType.equalsIgnoreCase("RAEntityNotification")) {
        		 this.setValue(parseResourceAdaptorEntityNotification(nsString));
            
        	} else if (nsType.equalsIgnoreCase("SbbNotification")) {
        		 this.setValue(parseSbbNotification(nsString));
            
        	} else if (nsType.equalsIgnoreCase("SubsystemNotification")) {
        		 this.setValue(parseSubsystemNotification(nsString));
            
        	} else
                throw new IllegalArgumentException("bad notification source type! "
                        + nsType);  
        	 
        } catch (Throwable ex) {
            throw new IllegalArgumentException(ex.getMessage(),ex);
        }
    }
    
    private ProfileTableNotification parseProfileTableNotification(String text) {
    	return new ProfileTableNotification(text.substring("table=".length()));
    }
    
    private ResourceAdaptorEntityNotification parseResourceAdaptorEntityNotification(String text) {
    	return new ResourceAdaptorEntityNotification(text.substring("entity=".length()));
    }

    private SbbNotification parseSbbNotification(String text) {
    	
    	int separator = text.indexOf("sbb=SbbID[");
    	
    	String serviceID = text.substring(0, separator-1);
    	serviceID = serviceID.substring("service=".length());
    	ComponentIDPropertyEditor cidPropertyEditor = new ComponentIDPropertyEditor();
    	cidPropertyEditor.setAsText(serviceID);
    	ServiceID service = (ServiceID) cidPropertyEditor.getValue();
    	
    	String sbbID = text.substring(separator);
    	sbbID = sbbID.substring("sbb=".length());
    	cidPropertyEditor.setAsText(sbbID);
    	SbbID sbb = (SbbID) cidPropertyEditor.getValue();
    	
    	return new SbbNotification(service,sbb);
    }
    
    private SubsystemNotification parseSubsystemNotification(String text) {
    	return new SubsystemNotification(text.substring("subsystem=".length()));
    }
    
}