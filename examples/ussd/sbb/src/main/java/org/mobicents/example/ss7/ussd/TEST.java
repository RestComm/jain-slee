package org.mobicents.example.ss7.ussd;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;

public class TEST {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();  
			
			JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext(); 
			ProcessDefinition pd = ProcessDefinition.parseXmlInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("trainstation.jpdl.xml"));
			jbpmContext.deployProcessDefinition(pd);
			ProcessInstance pi = jbpmContext.newProcessInstance("TrainStationDecisions");
			
			pi.signal();
			System.err.println("VARS 1 >"+pi.getContextInstance().getVariables());
			pi.getContextInstance().setVariable("ussd.input", "1");
			pi.signal();
			System.err.println("VARS 2 >"+pi.getContextInstance().getVariables());
			pi.getContextInstance().setVariable("ussd.input", "1");
			pi.signal();
			System.err.println("VARS 2 >"+pi.getContextInstance().getVariables());
			System.err.println(pi.getEnd());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
