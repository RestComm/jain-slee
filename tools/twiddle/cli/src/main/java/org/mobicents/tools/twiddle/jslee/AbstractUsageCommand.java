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
package org.mobicents.tools.twiddle.jslee;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AbstractOperation;

import javax.management.*;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import java.io.PrintWriter;

/**
 * One bulky command to interact with usage MBeans. 
 * 
 * @author baranowb
 * 
 */
public abstract class AbstractUsageCommand extends AbstractSleeCommand {

	/**
	 * @param commandName
	 * @param desc
	 */
	public AbstractUsageCommand(String commandName, String desc) {
		super(commandName,desc);

		try {
			this.PROFILE_PROVISIONING_MBEAN = new ObjectName(Utils.SLEE_PROFILE_PROVISIONING); // damn
			// annoying.
			this.RESOURCE_MANAGEMENT_MBEAN = new ObjectName(Utils.SLEE_RESOURCE_MANAGEMENT); // damn
			// annoying.
			this.SERVICE_MANAGEMENT_MBEAN = new ObjectName(Utils.SLEE_SERVICE_MANAGEMENT); // damn
			// annoying.
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.provisioningMBeanName = getProvisioningMBeanName();
		this.getOperationName = getUsageMGMTMBeanOperation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.tools.twiddle.AbstractSleeCommand#displayHelp()
	 */
	@Override
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		
		// out.println("usage: " + name +
		// " <-tprofileTableName> [-pprofileName] <operation> <arg>*");
		addHeaderDescription(out);
		out.println("'SetID' refers to SetName or SbbID and SetName.");
		out.println();
		out.println("operation:");
		out.println("    -l, --list                     Lists certain information about parameters sets. Requires one of options to be present:");
		out.println("           --sets                  Instructs command to list declared parameter sets. Does not require argument.");
		out.println("           --parameters            Instructs command to list parameters of parameter set. Does not require argument.");
		//TODO: make 'list' also list notification mngr conf for resource set.
		out.println("    -g, --get                      Fetches value of certain parameter in set. Does not take argument.");
		out.println("                                   Requires '--name' option to be present. Following options are supported: ");
		out.println("           --name                  Specifies name of parameter in a set for get operation. Requires parameter name as argument. This option is mandatory.");
		out.println("           --rst                   If present, indicates that 'get' operation should reset parameter value. Does not require argument.");
		out.println("    -r, --reset                    Resets assets in 'Usage' realm. Does not take argument. If 'SetID' is specified, reset command resets specific set, otherwise it acts on default one.");
		out.println("                                   If it is not present, reset command performs operation on default set. Following option is supported:");
		out.println("           --all                   Resets ALL parameters for 'ResourceName', ignores 'SetID'.");
		out.println("    -c, --create                   Creates usage parameter set for given 'SetID'. Does not require argument.");
		out.println("    -d, --delete                   Deletes usage parameter set with given 'SetID'. Does not require argument.");
		out.println("    -n, --notify                   This operation either turn on/off notifications for parameter or queries about state of notifications.");
		out.println("                                   Does not take parameter, supports following options:");
		out.println("           --name                  Specifies name of parameter. Requires parameter name as argument. It is mandatory.");
		out.println("           --value                 Specifies value of parameter. Requires boolean argument.");
		out.println("           --is                    Request information about notification(if its enabled). Does not require argument.");
		
		//out.println("    -i, --is-notify                Checks if notification is on for certain parameter in set. Following options are supported:");
		//out.println("           --name                  Specifies name of parameter. Requires parameter name as argument. It is mandatory.");
		out.println("");
		out.println("Examples: ");
		addExamples(out);	
		out.flush();
	}

	
	
	
	protected abstract void addHeaderDescription(PrintWriter out);
	protected abstract void addExamples(PrintWriter out);




	// //////////////////////
	// Command essentials //
	// //////////////////////

	protected static final String GET_MANAGER_NAME_METHOD = "getUsageNotificationManagerMBean";
	protected static final String GET_MANAGER_NAME_SBB_METHOD = "getSbbUsageNotificationManagerMBean";
	
	//those are required to get bean on which we should invoke op.
	protected ObjectName PROFILE_PROVISIONING_MBEAN;
	protected final static String PROFILE_GET_METHOD = "getProfileTableUsageMBean";
	protected ObjectName RESOURCE_MANAGEMENT_MBEAN;
	protected final static String RESOURCE_GET_METHOD = "getResourceUsageMBean";
	protected ObjectName SERVICE_MANAGEMENT_MBEAN;
	protected final static String SERVICE_GET_METHOD = "getServiceUsageMBean";
	//used not only in operation
	protected final static String GET_SPECIFIC_BEAN_METHOD = "getUsageMBean";
	protected final static String GET_SPECIFIC_SBB_BEAN_METHOD = "getSbbUsageMBean";


	//name of provisioning mbean.
	protected ObjectName provisioningMBeanName;
	// get op from provisioning mbean.
	protected String getOperationName;
	// usage mgmt bean
	protected ObjectName usageMgmtMBeanName;
	// usage manager bean - notification
	protected ObjectName usageSetManagerMBeanName;
	// object name of 
	protected ObjectName specificObjectName;
	// holds string resource name - ProfileTable, RA Entity, ServiceID
	protected String resourceName;
	// holds name, if default usage, it will be null.
	protected String usageSetName;

	//part for ServiceUsage params.
	protected ServiceID serviceID;
	
	protected SbbID sbbID;
	
	protected ComponentIDPropertyEditor editor = new ComponentIDPropertyEditor();
	
	protected final static String PREFIX_SERVICEID = "ServiceID";
	//protected final static String PREFIX_SBBID = "SbbID";
	// //////////////////////
	//    Set essentials   //
	// //////////////////////

	// list of fields, so we know what type they are?

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.tools.twiddle.AbstractSleeCommand#getBeanOName()
	 */
	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		// tricky, its kind of dynamic :)
		// this depends on op
		return specificObjectName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.tools.twiddle.AbstractSleeCommand#processArguments(java
	 * .lang.String[])
	 */
	@Override
	protected void processArguments(String[] args) throws CommandException {

		//String sopts = "-:lgrcdni";
		String sopts = "-:lgrcdn";

		LongOpt[] lopts = {

				new LongOpt("noprefix", LongOpt.NO_ARGUMENT, null, 0x1000),
				// operration part
				new LongOpt("list", LongOpt.NO_ARGUMENT, null, 'l'),
					new LongOpt("sets", LongOpt.NO_ARGUMENT, null, ListOperation.sets),
					new LongOpt("parameters", LongOpt.NO_ARGUMENT, null, ListOperation.parameters),
				new LongOpt("get", LongOpt.NO_ARGUMENT, null, 'g'),
					new LongOpt("name", LongOpt.REQUIRED_ARGUMENT, null, GetOperation.name),
					new LongOpt("rst", LongOpt.NO_ARGUMENT, null, GetOperation.rst),
				new LongOpt("reset", LongOpt.NO_ARGUMENT, null, 'r'),
					new LongOpt("all", LongOpt.NO_ARGUMENT, null, ResetOperation.all),	
				new LongOpt("create", LongOpt.NO_ARGUMENT, null, 'c'),
				new LongOpt("delete", LongOpt.NO_ARGUMENT, null, 'd'),
				
				//mgmt
				new LongOpt("notify", LongOpt.NO_ARGUMENT, null, 'n'),
					new LongOpt("value", LongOpt.REQUIRED_ARGUMENT, null, NotifyOperation.value),
					new LongOpt("is-notify", LongOpt.NO_ARGUMENT, null, NotifyOperation.is),
		};

		Getopt getopt = new Getopt(null, args, sopts, lopts);
		getopt.setOpterr(false);
		int nonOptArgIndex = 0;
		int code;
		while ((code = getopt.getopt()) != -1) {
			switch (code) {
			case ':':
				throw new CommandException("Option requires an argument: " + args[getopt.getOptind() - 1]);

			case '?':
				throw new CommandException("Invalid (or ambiguous) option: " + args[getopt.getOptind() - 1]);

			case 0x1000:
				break;
			case 1:
				// non opt args, table and profile name(maybe)
				switch (nonOptArgIndex) {
				case 0:
					resourceName = getopt.getOptarg();
					if(resourceName.startsWith(PREFIX_SERVICEID))
					{
						
						try{
							editor.setAsText(resourceName);
						}catch(Exception e)
						{
							throw new CommandException("Command: \"" + getName() + "\" failed to parse ServiceID.",e);
						}
						this.serviceID = (ServiceID) editor.getValue();
						
						this.resourceName = null;
					}
					nonOptArgIndex++;
					break;
				case 1:
					if(this.serviceID!=null)
					{
						
						try{
							this.editor.setAsText(getopt.getOptarg());
						}catch(Exception e)
						{
							throw new CommandException("Command: \"" + getName() + "\" failed to parse SbbID.",e);
						}
						this.sbbID = (SbbID) this.editor.getValue();
					}else
					{
						usageSetName = getopt.getOptarg();
					}
					nonOptArgIndex++;
					break;
				case 2:
					if(this.serviceID == null)
					{
						throw new CommandException("Command: \"" + getName() + "\" expects at most two non opt arguments!");
					}
					usageSetName = getopt.getOptarg();
					nonOptArgIndex++;
					break;
				default:
					throw new CommandException("Command: \"" + getName() + "\" expects at most three non opt arguments!");
				}
				break;

			case 'l':
				// list
				super.operation = new ListOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'g':
				// get
				super.operation = new GetOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'r':
				// reset
				super.operation = new ResetOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
		
			case 'c':
				// create
				super.operation = new CreateOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;	
			case 'd':
				// delete
				super.operation = new DeleteOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;	
			case 'n':
				// notify mngr
				super.operation = new NotifyOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;	
//			case 'i':
//				// check notify
//				super.operation = new IsNotifyOperation(super.context, super.log, this);
//				prepareCommand();
//				super.operation.buildOperation(getopt, args);
//				break;	
				
				
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}
	}

	protected void prepareCommand() throws CommandException {
		// get bean name and bean info.
		
		if(this.resourceName == null && this.serviceID == null)
		{
			throw new CommandException("Command: \"" + getName() + "\", expects 'ResourceName'!");
		}
		//in case of reset SbbID may be null.
		if(this.serviceID!=null && this.sbbID == null)
		{
			throw new CommandException("Command: \"" + getName() + "\", expects atleast 'SbbID' in 'SetID'!");
		}
		String[] sig = null;
		Object[] parms = null;
		
		if(this.serviceID == null)
		{
			sig = new String[]{"java.lang.String"};
			parms = new Object[]{resourceName};
		}else
		{
			sig = new String[]{"javax.slee.ServiceID"};
			parms = new Object[]{this.serviceID};
		}
		MBeanServerConnection server = super.context.getServer();
		//get usage mgmt bean.
		try {
			this.usageMgmtMBeanName = (ObjectName) server.invoke(this.provisioningMBeanName, this.getOperationName, parms, sig);
		} catch (Exception e) {
			throw new CommandException("Command: \"" + getName()+"\" failed to obtain usage management bean name. Resource does not exist in container?", e);
		} 
	}
	
	protected ObjectName getSpecificUsageMBeanOName() throws CommandException
	{
		String[] sig = null;
		Object[] parms = null;
		if (this.serviceID == null) {
			if (usageSetName != null) {
				sig = new String[] { "java.lang.String" };
				parms = new Object[] { usageSetName };
			}else
			{
				sig = new String[] { };
				parms = new Object[] {  };
			}
		} else {
			if (usageSetName != null) {
				sig = new String[] { "javax.slee.SbbID","java.lang.String" };
				parms = new Object[] { this.sbbID,usageSetName };
			}else
			{
				sig = new String[] { "javax.slee.SbbID" };
				parms = new Object[] { this.sbbID };
			}
		}
		MBeanServerConnection server = super.context.getServer();
		//get usage mgmt bean.
		try {
			if(serviceID != null)
			{
				this.specificObjectName =  (ObjectName) server.invoke(this.usageMgmtMBeanName, GET_SPECIFIC_SBB_BEAN_METHOD, parms, sig);
			}else
			{
				this.specificObjectName =  (ObjectName) server.invoke(this.usageMgmtMBeanName, GET_SPECIFIC_BEAN_METHOD, parms, sig);
			}
			return this.specificObjectName;
		} catch (Exception e) {
			throw new CommandException("Command: \"" + getName()+"\" failed to obtain usage management bean name. Resource does not exist in container?", e);
		} 
	}
	protected ObjectName getSpecificUsageNotificationMBeanOName() throws CommandException
	{
		String[] sig = null;
		Object[] parms = null;
		if(this.serviceID == null)
		{
			sig = new String[]{};
			parms = new Object[]{};
		}else
		{
			sig = new String[] { "javax.slee.SbbID" };
			parms = new Object[] { this.sbbID };
		}
		MBeanServerConnection server = super.context.getServer();
		//get usage mgmt bean.
		try {
			if(serviceID!=null)
			{
				this.specificObjectName =  (ObjectName) server.invoke(this.usageMgmtMBeanName, GET_MANAGER_NAME_SBB_METHOD, parms, sig);	
			}else
			{
				this.specificObjectName =  (ObjectName) server.invoke(this.usageMgmtMBeanName, GET_MANAGER_NAME_METHOD, parms, sig);
			}
			return this.specificObjectName;
		} catch (Exception e) {
			throw new CommandException("Command: \"" + getName()+"\" failed to obtain usage management bean name. Resource does not exist in container?", e);
		} 
	}
	public abstract ObjectName getProvisioningMBeanName();
	public abstract String getUsageMGMTMBeanOperation();
	/**
	 * Operation to perform list like actions. It does following:
	 * <ul>
	 * <li>list parameters sets</li>
	 * <li>list parameters in parameter set</li>
	 * <li></li>
	 * </ul>
	 * 
	 * @author baranowb
	 * 
	 */
	protected class ListOperation extends AbstractOperation
	{
		public static final char sets = 'v';
		public static final char parameters = 'b';
		private final static String OPERTION_getUsageParameterSets = "getUsageParameterSets";
		public ListOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);

			// default
			super.operationName = OPERTION_getUsageParameterSets;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			
			//add default value:
			specificObjectName = usageMgmtMBeanName;
			//check opts.
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] + " --> " + opts.getOptopt());

				case sets:
					super.operationName = OPERTION_getUsageParameterSets;
					
					break;
				case parameters:
					//this can be empty.
					if(serviceID!=null)
					{
						super.operationName = GET_SPECIFIC_SBB_BEAN_METHOD;
					}else
					{
						super.operationName = GET_SPECIFIC_BEAN_METHOD;
					}
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
			}
		}

		/* (non-Javadoc)
		 * @see org.mobicents.tools.twiddle.op.AbstractOperation#invoke()
		 */
		@Override
		public void invoke() throws CommandException {

			if(super.operationName.equals("getUsageParameterSets"))
			{
				if(serviceID!=null)
				{
					addArg(sbbID, "javax.slee.SbbID", false);
				}
				super.invoke();
			}else
			{
				try {
					String getMethod = super.operationName;

					
					MBeanServerConnection conn = context.getServer();
					
					Object[] parms = null;
					String[] sig = null;
					if(serviceID==null)
					{
						parms = new Object[]{};
						sig = new String[]{};
					}else
					{
						parms = new Object[]{sbbID};
						sig = new String[]{"javax.slee.SbbID"};
						
					}
					ObjectName on = (ObjectName) conn.invoke(usageMgmtMBeanName, getMethod, parms, sig);
					super.operationResult = conn.getMBeanInfo(on); 
					displayResult();
				
				} catch (Exception e) {
					//add handle error here?
					throw new CommandException("Failed to invoke \"" + this.operationName + "\" due to: ", e);
				}	
			}
		}

		/* (non-Javadoc)
		 * @see org.mobicents.tools.twiddle.op.AbstractOperation#displayResult()
		 */
		@Override
		public void displayResult() {
			if (!context.isQuiet()) {
				if (operationResult instanceof MBeanInfo) {

					PrintWriter out = context.getWriter();
					// dont look at operationResult, we want info

					MBeanOperationInfo[] infos = ((MBeanInfo) super.operationResult).getOperations();
					for (MBeanOperationInfo info : infos) {
						if (!info.getName().startsWith("get")) {
							continue;
						}
						out.println();
						// out.println("Class: "+info.getClass());
						out.println("Desc   : " + info.getDescription());
						out.println("Name   : " + info.getName().replaceFirst("get", ""));
						out.println("Sample : " + info.getReturnType().equals("javax.slee.usage.SampleStatistics"));

					}

					// render results to out
					out.flush();
				} else {
					super.displayResult();
				}
			}
		}

	}

	protected class GetOperation extends AbstractOperation
	{
		public static final char rst = 'x';
		public static final char name = 'z';
		private boolean reset = false;
	
		public GetOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "get";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] + " --> " + opts.getOptopt());

				case rst:
					reset = true;
					break;
				case name:
					//TODO: check upper case?
					String opt = opts.getOptarg();
					if(!Character.isUpperCase(opt.charAt(0)))
					{
						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
								+ "\", cannot proceed. Parameter name must start with upper case: " + opt);
					}
					super.operationName += opt;
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
			}
			if(super.operationName.equals( "get")) //empty, no name?
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires atleast '--name' option.");
			}
			addArg(new Boolean(reset), boolean.class, false);
			
			//now we have to get proper bean name
			getSpecificUsageMBeanOName();

		}
		
	}
	
	protected class ResetOperation extends AbstractOperation {
		public static final char all = 'a';
		
		private final static String OPERATION_resetAllUsageParameters = "resetAllUsageParameters";
		public boolean allIndication = false;
		
		public ResetOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_resetAllUsageParameters;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] + " --> " + opts.getOptopt());

				case all: //if all we need to act on mgmt bean
					
					specificObjectName = usageMgmtMBeanName;
					allIndication = true;
					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
				
			}

			if(!allIndication)
			{
				//we need specific bean name
				getSpecificUsageMBeanOName();
			}else
			{
				if(serviceID!=null && sbbID!=null)
				{
					addArg(sbbID, "javax.slee.SbbID", false);
				}else
				{
					//ServiceID wide reset.
				}
			}
			
		}
	}
	protected class CreateOperation extends AbstractOperation
	{
		private final static String OPERATION_createUsageParameterSet = "createUsageParameterSet";
		public CreateOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_createUsageParameterSet;
			
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			//there is no more to that atm?
			if(usageSetName == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires 'SetID' to be present");
			}
			if(serviceID!=null)
			{
				if(sbbID == null)
				{
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", requires SbbID in 'SetID' to be present");
				}
				addArg(sbbID, "javax.slee.SbbID", false);
			}
			addArg(usageSetName, String.class, false);
			specificObjectName = usageMgmtMBeanName;
		}
		
	}
	protected class DeleteOperation extends AbstractOperation
	{
		private final static String OPERATION_removeUsageParameterSet = "removeUsageParameterSet";
		public DeleteOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_removeUsageParameterSet;
			
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			if(usageSetName == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires 'SetID' to be present");
			}
			if(serviceID!=null)
			{
				if(sbbID == null)
				{
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", requires SbbID in 'SetID' to be present");
				}
				addArg(sbbID, "javax.slee.SbbID", false);
			}
			addArg(usageSetName, String.class, false);
			specificObjectName = usageMgmtMBeanName;
		}
		
	}
	
	//mgr part :)
	
	protected class NotifyOperation extends AbstractOperation
	{
		public static final char value = 'o';
		public static final char is = 'i';
		private Boolean booleanValue;
		private String parameterName;
		private boolean isIndicated = false;
		
		public NotifyOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] + " --> " + opts.getOptopt());

				case value:
					this.booleanValue = Boolean.parseBoolean(opts.getOptarg());
					break;
				case GetOperation.name:
					String opt = opts.getOptarg();//this is name of param
					if(!Character.isUpperCase(opt.charAt(0)))
					{
						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
								+ "\", cannot proceed. Parameter name must start with upper case: " + opt);
					}
					parameterName = opt;
					
					break;
				case is:
					isIndicated = true;
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
			}
			
			if(parameterName == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires '--name' option to be present.");
			}
			if( (booleanValue != null && isIndicated) ||(booleanValue == null && !isIndicated))
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires either '--is' or '--value'.");
			}
			
			if(isIndicated)
			{
				super.operationName = "get"+parameterName+"NotificationsEnabled";
			}else
			{
				super.operationName = "set"+parameterName+"NotificationsEnabled";
				addArg(booleanValue, boolean.class, false);
			}
			

			
			
			//now we need mgmt bean
			getSpecificUsageNotificationMBeanOName();
			
			
		}
		
	}
	
//	protected class IsNotifyOperation extends AbstractOperation
//	{
//
//		public IsNotifyOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
//			super(context, log, sleeCommand);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public void buildOperation(Getopt opts, String[] args) throws CommandException {
//			int code;
//			while ((code = opts.getopt()) != -1) {
//				switch (code) {
//				case ':':
//					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);
//
//				case '?':
//					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] + " --> " + opts.getOptopt());
//
//				case GetOperation.name:
//					String opt = opts.getOptarg();//this is name of param
//					if(!Character.isUpperCase(opt.charAt(0)))
//					{
//						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
//								+ "\", cannot proceed. Parameter name must start with upper case: " + opt);
//					}
//					super.operationName = "get"+opt+"NotificationsEnabled";
//					
//					break;
//				default:
//					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
//							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);
//
//				}
//			}
//			
//			if(super.operationName == null)
//			{
//				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
//						+ "\", requires '--name' option to be present.");
//
//			}
//			//now we need mgmt bean
//			getSpecificUsageNotificationMBeanOName();
//			
//		}
//		
//	}
}
