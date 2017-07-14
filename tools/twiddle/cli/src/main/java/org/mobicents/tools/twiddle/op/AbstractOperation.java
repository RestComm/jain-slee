/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.mobicents.tools.twiddle.op;

import java.beans.PropertyEditor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.Attribute;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.jboss.util.propertyeditor.PropertyEditors;
import org.mobicents.tools.twiddle.AbstractSleeCommand;

import gnu.getopt.Getopt;

/**
 * Class which handles different aspects of building jmx call. 
 * @author baranowb
 * 
 */
public abstract class AbstractOperation {
	//editors
	public static final String CID_SEPARATOR = ";";
	
	protected Object operationResult;
	protected AbstractSleeCommand sleeCommand;
	protected String operationName;
	// string rep of parameter and prop editors for those.
	protected ArrayList<Object> opArguments = new ArrayList<Object>();
	protected ArrayList<String> opSignature = new ArrayList<String>();

	// context and logger from Command, its not a good way, but....
	protected CommandContext context;
	protected Logger log;

	protected AbstractOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
		super();
		this.context = context;
		this.log = log;
		this.sleeCommand = sleeCommand;
	}

	/**
	 * @return the operationName
	 */
	public String getOperationName() {
		return operationName;
	}

	/**
	 * @return the opArguments
	 */
	public ArrayList<Object> getOpArguments() {
		return opArguments;
	}

	/**
	 * @return the opSignature
	 */
	public ArrayList<String> getOpSignature() {
		return opSignature;
	}

	/**
	 * Each operation should create this method. Its specific to command.
	 * Ususally it will just to opts.getOptarg(); but in case of more
	 * complicated ops, it will do more.
	 * 
	 * @param opts
	 * @param args
	 */
	public abstract void buildOperation(Getopt opts,String[] args) throws CommandException;

	/**
	 * Method to display result of operation.
	 */
	public void displayResult() {
		//default impl of display;
		if (!context.isQuiet()) {
			// Translate the result to text
			String resultText = prepareResultText();

			// render results to out
			PrintWriter out = context.getWriter();
			out.println(resultText);
			out.flush();
		}
	}
	
	
	protected String prepareResultText() {
		String resultText = null;

		if (operationResult != null) {
				
				try {
					if(operationResult instanceof Collection)
					{
						//convert to array?
						Collection c = (Collection) operationResult;
						Object[] arrayO = c.toArray();
						operationResult = arrayO;
					}
					
					//here we may have array as result, dont want that, we will handle how its displayed.
					if(operationResult.getClass().isArray())
					{
						Object[] resultArray = (Object[]) operationResult;
						Class memberClass =resultArray.getClass().getComponentType(); // get type of array stored classes.
						PropertyEditor editor = PropertyEditors.getEditor(memberClass);
						resultText = unfoldArray("", resultArray,editor);
					}else
					{
						PropertyEditor editor = PropertyEditors.getEditor(operationResult.getClass());
						editor.setValue(operationResult);
						resultText = editor.getAsText();
					}
					
				} catch (RuntimeException e) {
					// No property editor found or some conversion problem
					//TODO: add something more sophisticated here
					log.debug("No editor found: ",e);
					//fallback op
					if(operationResult.getClass().isArray())
					{
			
						//TODO: this is not readable, but result can be passed into another invocation
						Object[] arrayResult = (Object[]) operationResult;
						resultText = unfoldArray("", arrayResult, null);
					}else
					{
						resultText = operationResult.toString();
					}
					
				}
		
				log.debug("Converted result: " + resultText);
			
		} else {
			resultText = "'success'";
		}
		return resultText;
	}

	/**
	 * Default implementation.
	 * @param prefix
	 * @param array
	 * @return
	 */
	protected String unfoldArray(String prefix, Object[] array, PropertyEditor editor)
	{
		StringBuffer sb = new StringBuffer("\n");
		for (int index=0;index<array.length;index++)
		{
			if(editor!=null)
			{
				editor.setValue(array[index]);
				sb.append(editor.getAsText());
			}
			else
			{
				sb.append(array[index].toString());
			}
			 
                if (index < array.length-1) {
                	//sb.append(CID_SEPARATOR);
                	sb.append("\n");
                }
		}
		
		return  sb.toString();
	}
	protected void addArg(Object arg, Class<? extends Object> argClass, boolean usPE) throws CommandException {
		if (usPE) {
			PropertyEditor pe = PropertyEditors.getEditor(argClass);
			if (pe == null) {
				throw new CommandException("There is no property editor for: " + argClass);
			}

			pe.setAsText((String) arg);
			opArguments.add(pe.getValue());
		} else {
			opArguments.add(arg);
		}
		opSignature.add(argClass.getName());

	}
	protected void addArg(Object arg, String argClass, boolean usPE) throws CommandException {
		if (usPE) {
			try{
				PropertyEditor pe = PropertyEditors.getEditor(argClass);
				if (pe == null) {
					throw new CommandException("There is no property editor for: " + argClass);
				}
				if(arg!=null)
				{
					pe.setAsText((String) arg);
					opArguments.add(pe.getValue());
				}else
				{
					opArguments.add(null);
				}
			}catch(ClassNotFoundException cnfe)
			{
				throw new CommandException("Failed to locate class.",cnfe);
			}
		} else {
			opArguments.add(arg);
		}
		opSignature.add(argClass);

	}
	public void invoke() throws CommandException {
		try {
			ObjectName on = sleeCommand.getBeanOName();
		
			MBeanServerConnection conn = context.getServer();
			Object[] parms = getOpArguments().toArray();
			String[] sig = new String[getOpSignature().size()];
			sig = getOpSignature().toArray(sig);
			operationResult = conn.invoke(on, this.operationName, parms, sig);
			displayResult();
		} catch (Exception e) {
			//add handle error here?
			throw new CommandException("Failed to invoke \"" + this.operationName + "\" due to: ", e);
		}
	}

}
