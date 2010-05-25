package org.mobicents.example.ss7.ussd.jbpm;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

public class USSDPromptHandler implements ActionHandler{
	public static final String _PROMPT_ = "ussd.prompt";
	private String prompt;
	private boolean end = false;
	
	/**
	 * @return the end
	 */
	public boolean isEnd() {
		return end;
	}



	/**
	 * @param end the end to set
	 */
	public void setEnd(boolean end) {
		this.end = end;
	}



	/**
	 * @return the prompt
	 */
	public String getPrompt() {
		return prompt;
	}



	/**
	 * @param prompt the prompt to set
	 */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}



	public void execute(ExecutionContext ctx) throws Exception {
		ctx.setVariable(_PROMPT_, prompt);
		if(end)
		{
			//make transition
			ctx.leaveNode();
		}
	}

}
