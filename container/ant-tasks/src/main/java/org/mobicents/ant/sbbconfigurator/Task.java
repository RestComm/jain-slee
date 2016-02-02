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

/***************************************************
 *                                                 *
 *  Restcomm: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.ant.sbbconfigurator;

import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.BuildException;

/**
 * Ant task to configure an sbb descriptor.
 * @author Eduardo Martins / 2006 PT Inova��o (www.ptinovacao.pt) 
 *
 */
public class Task extends org.apache.tools.ant.Task {
    
	// The method executing the task
    public void execute() throws BuildException {
    	final ClassLoader origCL = Thread.currentThread().getContextClassLoader();    	
    	try {
	    	Thread.currentThread().setContextClassLoader(getClass().getClassLoader());	    	
	    	try {		    			    		
	    		for(int i = 0; i < subTasks.size(); i++) {
	    			SubTask task = (SubTask) subTasks.elementAt(i);	    			
	    			task.run(new File(sbbDescriptor));	
	    		}	    		
	    	} catch (Exception e) {
	    		throw new BuildException(e);
	    	}
	    	
    	} finally {
	    	Thread.currentThread().setContextClassLoader(origCL);
        }    	
    }
        
    public void addSetEnvEntry(SetEnvEntrySubTask task) {
    	subTasks.add(task);
    }
    
    /*
     * here you may add more subtasks that implement
     * the SubTask interface
     */
    
    public void setSbbDescriptor(String sbbDescriptor) {
    	this.sbbDescriptor = sbbDescriptor;
    }
   
    private String sbbDescriptor = "sbb-jar.xml";
    
    private Vector subTasks = new Vector();
}