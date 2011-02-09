/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
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
 * @author Eduardo Martins / 2006 PT Inovação (www.ptinovacao.pt) 
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