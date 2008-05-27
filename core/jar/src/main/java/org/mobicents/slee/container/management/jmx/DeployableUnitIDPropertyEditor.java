/*
 * DeployableUnitIDPropertyEditor.java
 * 
 * Created on Nov 26, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
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

package org.mobicents.slee.container.management.jmx;

import org.mobicents.slee.container.component.DeployableUnitIDImpl;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property editor for deployable unit id.
 *  
 */
public class DeployableUnitIDPropertyEditor extends TextPropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            StringTokenizer st = new StringTokenizer(text, "[", true);
            String name = st.nextToken();
            if (!name.equalsIgnoreCase("DeployableUnitID"))
                throw new IllegalArgumentException(
                        "Expecting DeployableUnitID[number]");
            st.nextToken();
            String idStr = st.nextToken("]");
            int id = Integer.parseInt(idStr);
            this.setValue(new DeployableUnitIDImpl(id));
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException(
                    "Expecting DeployableUnitID[number]",ex);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Expecting DeployableUnitID[number]",ex);
        }
    }

}

