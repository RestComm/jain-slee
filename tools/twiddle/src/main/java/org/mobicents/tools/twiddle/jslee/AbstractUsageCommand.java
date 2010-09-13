/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.tools.twiddle.jslee;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.PrintWriter;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.JMXNameUtility;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * One bulky command to interact with usage MBeans. 
 * 
 * @author baranowb
 * 
 */
public abstract class AbstractUsageCommand extends AbstractSleeCommand {

	/**
	 * @param name
	 * @param desc
	 */
	public AbstractUsageCommand(String commandName, String desc) {
		super(commandName,desc);

		try {
			this.PROFILE_PROVISIONING_MBEAN = new ObjectName(JMXNameUtility.SLEE_PROFILE_PROVISIONING); // damn
			// annoying.
			this.RESOURCE_MANAGEMENT_MBEAN = new ObjectName(JMXNameUtility.SLEE_RESOURCE_MANAGEMENT); // damn
			// annoying.
			this.SERVICE_MANAGEMENT_MBEAN = new ObjectName(JMXNameUtility.SLEE_SERVICE_MANAGEMENT); // damn
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
		out.println();
		// out.println("usage: " + name +
		// " <-tprofileTableName> [-pprofileName] <operation> <arg>*");
		out.println("usage: " + name + " <ResourceName> [SetName] <operation> <arg>*");
		out.println("ResourceName - determines resource in container - ProfileTableName, RAEntityName, ServiceID");//TODO: add good description, this command is quite complicated!
		out.println("SetName - identifies set, in case of profiles and RA it is simply name of usage set. In case of");
		out.println("          Services, it has form of <SBBID> [SetName] - where SetName is simple string name.");
		out.println();
		out.println("operation:");
		out.println("    -l, --list                     Lists certain information about parameters sets. Requires one of suboptions to be present:");
		out.println("           --sets                  Instructs command to list declared parameter sets. Does not require argument.");
		out.println("           --parameters            Instructs command to list parameters of parameter set. Does not require argument.");
		//TODO: make 'list' also list notification mngr conf for resource set.
		out.println("    -g, --get                      Fetches value of certain parameter in set. Does not take argument.");
		out.println("                                   Requires '--name' suboption to be present. Following suboptions are supported: ");
		out.println("           --name                  Specifies name of parameter in a set for get operation. Requiers parameter name as argument. This option is mandatory.");
		out.println("           --rst                   If present, indicates that 'get' operation should reset parameter value. Does not require argument.");
		out.println("    -r, --reset                    Resets assets in 'Usage' realm. Does not take argument. If 'SetName' is not specified, reset command resets specific set.");
		out.println("                                   If it is not present, reset command performs operation on default set. Following suboption is spported:");
		out.println("           --all                   Resets ALL parameters for 'ResourceName', ignores 'SetName'.");
		out.println("    -c, --create                   Creates usage parameter set for given 'SetName'. Does not require argument.");
		out.println("    -d, --delete                   Deletes usage parameter set with given 'SetName'. Does not require argument.");
		out.println("    -n, --notify                   Turns on or off notification per usage parameter. Does not take parameter, requiers suboptions to specify name and value:");
		out.println("           --name                  Specifies name of parameter. Requiers parameter name as argument.");
		out.println("           --value                 Specifies value of parameter. Requiers boolean argument.");
		out.println("    -i, --is-notify                Checks if notification is on for certain parameter in set. Following suboptions are supported:");
		out.println("           --name                  Specifies name of parameter. Requiers parameter name as argument. It is mandatory.");
		out.flush();
	}

	
	
	
	// //////////////////////
	// Command essentials //
	// //////////////////////

	protected static final String GET_MANAGER_NAME_METHOD = "getUsageNotificationManagerMBean";
	
	//those are required to get bean on which we should invoke op.
	protected ObjectName PROFILE_PROVISIONING_MBEAN;
	protected final static String PROFILE_GET_METHOD = "getProfileTableUsageMBean";
	protected ObjectName RESOURCE_MANAGEMENT_MBEAN;
	protected final static String RESOURCE_GET_METHOD = "getResourceUsageMBean";
	protected ObjectName SERVICE_MANAGEMENT_MBEAN;
	protected final static String SERVICE_GET_METHOD = "getServiceUsageMBean";
	
	protected final static String LIST_SETS_METHOD = "getUsageParameterSets";
	protected final static String GET_SPECIFIC_BEAN_METHOD = "getUsageMBean";
	protected final static String RESET_ALL_PARAMETERS_METHOD = "resetAllUsageParameters";
	protected final static String CREATE_METHOD = "createUsageParameterSet";
	protected final static String REMOVE_METHOD = "removeUsageParameterSet";
	
	
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

		String sopts = "-:lgrcdni";

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
				new LongOpt("is-notify", LongOpt.REQUIRED_ARGUMENT, null, 'i'),
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
					nonOptArgIndex++;
					break;
				case 1:
					usageSetName = getopt.getOptarg();
					nonOptArgIndex++;
					break;
				default:
					throw new CommandException("Command: \"" + getName() + "\" expects at most two non opt arguments!");
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
			case 'i':
				// check notify
				super.operation = new IsNotifyOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;	
				
				
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}
	}

	protected void prepareCommand() throws CommandException {
		// get bean name and bean info.
		
		if(resourceName == null)
		{
			throw new CommandException("Command: \"" + getName() + "\", expects 'ResourceName'!");
		}
		String[] sig = new String[]{"java.lang.String"};
		Object[] parms = new Object[]{resourceName};
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
		String[] sig = new String[]{};
		Object[] parms = new Object[]{};
		if(usageSetName!=null)
		{
			sig = new String[]{"java.lang.String"};
			parms = new Object[]{usageSetName};
		}
		MBeanServerConnection server = super.context.getServer();
		//get usage mgmt bean.
		try {
			this.specificObjectName =  (ObjectName) server.invoke(this.usageMgmtMBeanName, GET_SPECIFIC_BEAN_METHOD, parms, sig);
			return this.specificObjectName;
		} catch (Exception e) {
			throw new CommandException("Command: \"" + getName()+"\" failed to obtain usage management bean name. Resource does not exist in container?", e);
		} 
	}
	protected ObjectName getSpecificUsageNotificationMBeanOName() throws CommandException
	{
		String[] sig = new String[]{};
		Object[] parms = new Object[]{};
		
		MBeanServerConnection server = super.context.getServer();
		//get usage mgmt bean.
		try {
			this.specificObjectName =  (ObjectName) server.invoke(this.usageMgmtMBeanName, GET_MANAGER_NAME_METHOD, parms, sig);
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

		public ListOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);

			// default
			super.operationName = LIST_SETS_METHOD;
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
					super.operationName = LIST_SETS_METHOD;
					
					break;
				case parameters:
					//this can be empty.
					super.operationName = GET_SPECIFIC_BEAN_METHOD;
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
				
				super.invoke();
			}else
			{
				try {
					String getMethod = super.operationName;

					
					MBeanServerConnection conn = context.getServer();
					Object[] parms = new Object[]{};
					String[] sig = new String[]{};
					
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
						+ "\", requires atleast '--name' suboption.");
			}
			addArg(new Boolean(reset), boolean.class, false);
			
			//now we have to get proper bean name
			getSpecificUsageMBeanOName();

		}
		
	}
	
	protected class ResetOperation extends AbstractOperation {
		public static final char all = 'a';
		public boolean allIndication = false;
		public ResetOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = RESET_ALL_PARAMETERS_METHOD;
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

				case all:
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
			}
			
		}
	}
	protected class CreateOperation extends AbstractOperation
	{

		public CreateOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = CREATE_METHOD;
			
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			//there is no more to that atm?
			if(usageSetName == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires 'SetName' to be present");
			}
			addArg(usageSetName, String.class, false);
			specificObjectName = usageMgmtMBeanName;
		}
		
	}
	protected class DeleteOperation extends AbstractOperation
	{

		public DeleteOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = REMOVE_METHOD;
			
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			if(usageSetName == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires 'SetName' to be present");
			}
			addArg(usageSetName, String.class, false);
			specificObjectName = usageMgmtMBeanName;
		}
		
	}
	
	//mgr part :)
	
	protected class NotifyOperation extends AbstractOperation
	{
		public static final char value = 'o';

		private Boolean booleanValue;
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
					super.operationName = "set"+opt+"NotificationsEnabled";
					
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
			}
			
			if(super.operationName == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires '--name' suboption to be present.");

			}

			if(this.booleanValue == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires '--value' suboption to be present.");

			}
			addArg(booleanValue, boolean.class, false);
			//now we need mgmt bean
			getSpecificUsageNotificationMBeanOName();
			try {
				PrintWriter out = context.getWriter();
				MBeanInfo i = super.context.getServer().getMBeanInfo(specificObjectName);
				MBeanAttributeInfo[] infos = i.getAttributes();
				for(MBeanAttributeInfo info:infos)
				{				
					
					out.println();
					//out.println("Class: "+info.getClass());
					out.println("Desc : "+info.getDescription());
					out.println("Name : "+info.getName());
					out.println("Type : "+info.getType());
					out.println("r/w  : "+info.isReadable()+"/"+info.isWritable());
					out.println("isIs : "+info.isIs()); // LOL :)
					
				}
				// render results to out
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	protected class IsNotifyOperation extends AbstractOperation
	{

		public IsNotifyOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			// TODO Auto-generated constructor stub
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

				case GetOperation.name:
					String opt = opts.getOptarg();//this is name of param
					if(!Character.isUpperCase(opt.charAt(0)))
					{
						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
								+ "\", cannot proceed. Parameter name must start with upper case: " + opt);
					}
					super.operationName = "get"+opt+"NotificationsEnabled";
					
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
			}
			
			if(super.operationName == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires '--name' suboption to be present.");

			}
			//now we need mgmt bean
			getSpecificUsageNotificationMBeanOName();
			
		}
		
	}
}
