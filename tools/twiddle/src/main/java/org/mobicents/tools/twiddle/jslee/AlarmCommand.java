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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.management.NotificationSource;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.JMXNameUtility;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * Class which invokes methods on JSLEE AlarmMBean
 * @author baranowb
 *
 */
public class AlarmCommand extends AbstractSleeCommand {

	
	/**
	 * @param name
	 * @param desc
	 */
	public AlarmCommand() {
		super("alarm", "This command performs operations on JSLEE AlarmMBean. ");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#displayHelp()
	 */
	@Override
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		out.println();
		out.println("usage: " + name + " <operation> <arg>*");
		out.println();
		out.println("operation:");
		out.println("    -c, --clear                    Clears alarm which meets criteria. Suboption \"--id\" or \"--nsrc\" must be present. Following suboptions are supported:");
		out.println("            --id                   Specifies ID of alarm to be cleared. This option MUST NOT be used with any other suboptions.");
		out.println("                                   Expects parameter of String type.");
		out.println("            --nsrc                 Specifies Notification source for which alarms will be cleared. It can be used in conjuction with \"--type\".");
		out.println("                                   Expects parameter of NotificationSource type.");
		out.println("            --type                 Type of alarm which will be cleared. It can be used in conjuction with \"--nsrc\".");
		out.println("                                   Expects parameter of String type.");
		out.println("    -l, --list                     Fetches list(id) of active alarms. If \"--nsrc\" option is used, alarm Ids are fetched for this Notification source.");
		out.println("            --nsrc                 Notification source for which alarms Ids are fetched, its not mandatory.");
		out.println("                                   Expects parameter of NotificationSource type.");
		out.println("    -d, --descriptor               Fetches descriptor of alarm, if passed argument is an array of alarm ids, it will return array of descriptors.");
		out.println("                                   Expects parameter of String or array of Strings type.");
		out.println("    -a, --active                   Checks if alarm with matching id is active.");
		out.println("                                   Expects parameter of String type. Passed argument must be id of alarm.");
		out.println();
		out.println("arg:");
		out.println("    NotificationSource:    ProfileTableNotification[table=xxx]");
		out.println("    AlarmId array:         alarmId1;alarmId2");
		out.flush();

	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#getBeanOName()
	 */
	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(JMXNameUtility.SLEE_ALARM);
	}

	protected void processArguments(String[] args) throws CommandException {

		String sopts = ":c:d:a:l"; // "-" is required to allow non option
										// args(I think)!, ":" is for req,
										// argument, lack of it after option
										// means no args.

		LongOpt[] lopts = { 
				new LongOpt("list", LongOpt.NO_ARGUMENT, null, 'l'),
				//Notification source for list
					new LongOpt("nsrc", LongOpt.REQUIRED_ARGUMENT, null, ListAlarmsOperation.nsrc),
				
				new LongOpt("descriptor", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
				new LongOpt("active", LongOpt.REQUIRED_ARGUMENT, null, 'a'), 
				new LongOpt("clear", LongOpt.NO_ARGUMENT, null, 'c'), 
				//Opts for clear
					new LongOpt("nsrc", LongOpt.REQUIRED_ARGUMENT, null, ClearAlarmsOperation.nsrc),
						new LongOpt("type", LongOpt.REQUIRED_ARGUMENT, null, ClearAlarmsOperation.type),
					new LongOpt("id", LongOpt.REQUIRED_ARGUMENT, null, ClearAlarmsOperation.id)
					
			};

		Getopt getopt = new Getopt(null, args, sopts, lopts);
		getopt.setOpterr(false);

		int code;
		while ((code = getopt.getopt()) != -1) {
			switch (code) {
			case ':':
				throw new CommandException("Option requires an argument: " + args[getopt.getOptind()-1]);

			case '?':
				throw new CommandException("Invalid (or ambiguous) option: " + args[getopt.getOptind()-1]);

			case 'c':
				
				super.operation = new ClearAlarmsOperation(super.context,super.log,this);
				super.operation.buildOperation(getopt, args);
				
				break;
			case 'l':
				//operationName = "getAlarms";
				// we need one arg, notification source;
				super.operation = new ListAlarmsOperation(super.context,super.log,this);
				super.operation.buildOperation(getopt, args);
				
				break;
			case 'd':

				// we need one arg, notification source;
				super.operation = new GetDescriptorOperation(super.context,super.log,this);
				super.operation.buildOperation(getopt, args);
				
				break;
			case 'a':
				//operationName = "isActive";
				super.operation = new IsActiveOperation(super.context,super.log,this);
				super.operation.buildOperation(getopt, args);
				break;
			default:
				throw new CommandException("Command: \""+getName()+"\", found unexpected opt: "+args[getopt.getOptind()-1]);

			}
		}
	}
	private class IsActiveOperation extends AbstractOperation
	{

		public IsActiveOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			this.operationName="isActive";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();
			if (optArg.contains(";")) {
				// arrays for ServiceID

				throw new CommandException("Array parameter is not supported by: "+args[opts.getOptind()-1]);

			}
			addArg(optArg, String.class, false);
			
		}
		
	}
	
	
	private class GetDescriptorOperation extends AbstractOperation
	{

		public GetDescriptorOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//op name not set!
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();
			
			if (optArg.contains(";")) {
				addArg(optArg.split(";"), String[].class, false);
				operationName = "getDescriptors";
			} else {
				operationName = "getDescriptor";
				addArg(optArg, String.class, false);
			}
			
		}
		
	}

	private class ListAlarmsOperation extends AbstractOperation {
		public static final char nsrc = 'z';

		public ListAlarmsOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getAlarms";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code = opts.getopt();

			switch (code) {
			case ':':
				throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

			case '?':
				throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);
			case -1:
				// this means its mbean attribute
				break;
			case nsrc:
				String optArg = opts.getOptarg();

				if (optArg.contains(";")) {
					// arrays for ServiceID

					throw new CommandException("Array parameter is not supported by: "+args[opts.getOptind()-1]);

				}
				try{
					addArg(optArg, NotificationSource.class, true);
				}catch(Exception e)
				{
					throw new CommandException("Failed to parse NotificationSource: \""+optArg+"\"",e);
				}
				break;
			default:
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);
			}

		}

	}
	
	private class ClearAlarmsOperation extends AbstractOperation
	{
		public static final char nsrc = 'v';
		public static final char type = 'b';
		public static final char id = 'x';
		
		
		private String stringID;
		
		private String stringNsrc;
		private String stringType;
		
		public ClearAlarmsOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//name not set
			
	
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			//tricky

			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind()-1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind()-1]+" --> "+opts.getOptopt());
				
				case nsrc:
					
					stringNsrc = opts.getOptarg();
					super.operationName = "clearAlarms";
					break;
				case type:
					stringType = opts.getOptarg();
					super.operationName = "clearAlarms";
					break;
				case id:
					 stringID = opts.getOptarg();
					 super.operationName = "clearAlarm";
					break;
				default:
					throw new CommandException("Operation \""+this.operationName+"\" for command: \""+sleeCommand.getName()+"\", found unexpected opt: "+args[opts.getOptind()-1]);

				}
			}
			
			
			if( (stringID == null && stringNsrc == null) ||  (stringID != null && stringNsrc != null) )
			{
				throw new CommandException("Operation \""+this.operationName+"\" for command: \""+sleeCommand.getName()+"\", expects either \"--nsrc\" or \"--id\" to be present");
			}
			
			if(stringID!=null)
			{
				
				if (stringID.contains(";")) {
					// arrays for ServiceID

					throw new CommandException("Array parameter is not supported by: "+args[opts.getOptind()-1]);

				}
			
			addArg(stringID, String.class, false);
				
			}else
			{
				//its alarms
				if (stringNsrc.contains(";")) {
					// arrays for ServiceID

					throw new CommandException("Array parameter is not supported by: "+args[opts.getOptind()-1]);

				}
				try{
					addArg(stringNsrc, NotificationSource.class, true);
				}catch(Exception e)
				{
					throw new CommandException("Failed to parse NotificationSource: \""+stringNsrc+"\"",e);
				}
				
				//type?
				if(stringType!=null)
				{
					if (stringType.contains(";")) {
						// arrays for ServiceID

						throw new CommandException("Array parameter is not supported by: "+args[opts.getOptind()-1]);

					}
				}
				addArg(stringType, String.class, false);
			}
			
			
		}
		
	}
}
