package org.mobicents.example.ss7.ussd.jbpm;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;

public class USSDDecisionHandler implements DecisionHandler {

	private static final Logger logger = Logger.getLogger(USSDDecisionHandler.class);
	
	public static final String _INPUT_ = "ussd.input";
	public static final String _START_ = "welcome";
	public static final String _END_ = "end";
	public String decide(ExecutionContext ctx) throws Exception {
		//get what we got from user
		String input = (String) ctx.getVariable(_INPUT_);
		Map transitions = ctx.getNode().getLeavingTransitionsMap();

		if(transitions.get(input) == null)
		{
			return _END_;
		}else
		{
			return ""+input;
		}
				
		
	}

}
