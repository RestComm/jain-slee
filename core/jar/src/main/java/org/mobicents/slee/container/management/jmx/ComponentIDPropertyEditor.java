/*
 * Created on Oct 27, 2004
 *
 *
 *  Author: M. Ranganathan
 * 
 * The software was developed by employees of the National Institute of
 * Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 15 Untied States Code Section 105, works of NIST
 * employees are not subject to copyright protection in the United States
 * and are considered to be in the public domain.  As a result, a formal
 * license is not needed to use the software.
 *
 * The software is provided by NIST as a service and is expressly
 * provided "AS IS."  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 * OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 * AND DATA ACCURACY.  NIST does not warrant or make any representations
 * regarding the use of the software or the results thereof, including but
 * not limited to the correctness, accuracy, reliability or usefulness of
 * the software.
 * 
 */
package org.mobicents.slee.container.management.jmx;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.mobicents.slee.container.component.ComponentIDImpl;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.container.component.ProfileSpecificationIDImpl;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;

import javax.slee.ComponentID;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property Editor for ComponentID. 
 * 
 * @author M. Ranganathan
 * 
 */
public class ComponentIDPropertyEditor extends TextPropertyEditorSupport {

    public void setAsText(String key) throws IllegalArgumentException {
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(key, "[",
                    true);
            ComponentIDImpl value;
            ComponentKey ckey;
            String componentType = stringTokenizer.nextToken();

            stringTokenizer.nextToken();
            String ckeyStr = stringTokenizer.nextToken("]");
            if (ckeyStr.equals(""))
                throw new IllegalArgumentException("bad component ID ");
            ckey = new ComponentKey(ckeyStr);
            if (componentType.equalsIgnoreCase(ComponentIDImpl.SBB_ID)) {
                value = new SbbIDImpl(ckey);
            } else if (componentType
                    .equalsIgnoreCase(ComponentIDImpl.RESOURCE_ADAPTOR_TYPE_ID)) {
                value = new ResourceAdaptorTypeIDImpl(ckey);
            } else if (componentType
                    .equalsIgnoreCase(ComponentIDImpl.RESOURCE_ADAPTOR_ID)) {
                value = new ResourceAdaptorIDImpl(ckey);
            } else if (componentType
                    .equalsIgnoreCase(ComponentIDImpl.PROFILE_SPECIFICATION_ID)) {
                value = new ProfileSpecificationIDImpl(ckey);
            } else if (componentType.equalsIgnoreCase(ComponentIDImpl.SERVICE_ID)) {
                value = new ServiceIDImpl(ckey);
            } else if ( componentType.equalsIgnoreCase(ComponentIDImpl.EVENT_TYPE_ID)) {
                value = new EventTypeIDImpl( ckey);
            } else
                throw new IllegalArgumentException("bad component type! "
                        + componentType);
            super.setValue(value);
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    
    public String getAsText() {
        return this.getValue().toString();
    }

}