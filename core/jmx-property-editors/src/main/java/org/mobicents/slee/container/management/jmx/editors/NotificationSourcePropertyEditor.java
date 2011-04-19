/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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