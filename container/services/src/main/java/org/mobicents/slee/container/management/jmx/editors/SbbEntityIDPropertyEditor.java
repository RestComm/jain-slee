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

import javax.slee.ServiceID;

//import org.jboss.util.propertyeditor.TextPropertyEditorSupport;
//import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.runtime.sbbentity.NonRootSbbEntityID;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntityID;

/**
 * Property Editor for SBBEntityID. 
 * @author baranowb
 * 
 */
public class SbbEntityIDPropertyEditor extends TextPropertyEditorSupport {
	private ComponentIDPropertyEditor componentIDPropertyEditor = new ComponentIDPropertyEditor();
    public void setAsText(String text) throws IllegalArgumentException {
    	//text
    	//RootID: /ServiceID[name=xxx,vendor=yyy,version=1.0]/rootConvergence
    	//non root id: /ServiceID[name=xxx,vendor=yyy,version=1.0]/rootConvergence/relationX/childY
        try {
              String[] idParts = text.split("/");
              if(idParts.length<3)
              {
            	  throw new IllegalArgumentException("Text does not represent valid ID, parts '"+idParts.length+"', value '"+text+"'");
              }
              
              componentIDPropertyEditor.setAsText(idParts[1]);
              ServiceID sid = (ServiceID) componentIDPropertyEditor.getValue();
              RootSbbEntityID rsid = new RootSbbEntityID(sid, idParts[2]);
              if(idParts.length > 3)
              {
            	  int i = 3;
            	  SbbEntityID parent = rsid;
            	  while(i<idParts.length) {
                	 parent = new NonRootSbbEntityID(parent, idParts[i], idParts[i+1]);
                	 i+=2;
            	  }
            	  setValue(parent);
              }else
              {
            	  setValue(rsid); 
              }
              
        } catch (Throwable ex) {
            throw new IllegalArgumentException(ex.getMessage(),ex);
        }
    }

}