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

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property Editor for ComponentID. 
 * 
 * @author M. Ranganathan
 * @author martins
 * 
 */
public class ComponentIDPropertyEditor extends TextPropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {
    	
        try {
            String componentString = text;
        	String componentType = componentString.substring(0, componentString.indexOf('['));
        	componentString = componentString.substring(componentType.length()+1,componentString.length()-1);
        	String[] componentStringParts = componentString.split(",");
        	String componentName = componentStringParts[0].substring("name=".length());
        	String componentVendor = componentStringParts[1].substring("vendor=".length());
        	String componentVersion = componentStringParts[2].substring("version=".length());
        	
        	if (componentType.equalsIgnoreCase("LibraryID")) {
                this.setValue(new LibraryID(componentName,componentVendor,componentVersion));
        	
        	} else if (componentType.equalsIgnoreCase("EventTypeID")) {
                this.setValue(new EventTypeID(componentName,componentVendor,componentVersion));
            
        	} else if (componentType.equalsIgnoreCase("ProfileSpecificationID")) {
        		this.setValue(new ProfileSpecificationID(componentName,componentVendor,componentVersion));
            
        	} else if (componentType.equalsIgnoreCase("ResourceAdaptorTypeID")) {
        		this.setValue(new ResourceAdaptorTypeID(componentName,componentVendor,componentVersion));
            
        	} else if (componentType
                    .equalsIgnoreCase("ResourceAdaptorID")) {
        		this.setValue(new ResourceAdaptorID(componentName,componentVendor,componentVersion));
            
        	} else if (componentType.equalsIgnoreCase("SbbID")) {
                this.setValue(new SbbID(componentName,componentVendor,componentVersion));
            
        	} else if ( componentType.equalsIgnoreCase("ServiceID")) {
        		this.setValue(new ServiceID(componentName,componentVendor,componentVersion));
            
        	} else
                throw new IllegalArgumentException("bad component type! "
                        + componentType);            
        } catch (Throwable ex) {
            throw new IllegalArgumentException(ex.getMessage(),ex);
        }
    }

}