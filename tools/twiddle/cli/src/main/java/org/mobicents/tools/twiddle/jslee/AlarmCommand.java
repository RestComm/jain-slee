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
import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AbstractOperation;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.management.NotificationSource;
import java.io.PrintWriter;

/**
 * Class which invokes methods on JSLEE AlarmMBean
 * @author baranowb
 *
 */
public class AlarmCommand extends AbstractSleeCommand {

	
	/**
	 *
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
		out.println("usage: " + name + " <-operation[[arg] | [--option[=arg]]*]>");
		out.println();
		out.println("operation:");
		out.println("    -c, --clear                    Clears alarm which meets criteria. Option \"--id\" or \"--nsrc\" must be present. Following options are supported:");
		out.println("            --id                   Specifies ID of alarm to be cleared. This option MUST NOT be used with any other options.");
		out.println("                                   Expects parameter of String type.");
		out.println("            --nsrc                 Specifies Notification source for which alarms will be cleared. It can be used in conjunction with \"--type\".");
		out.println("                                   Expects parameter of NotificationSource type.");
		out.println("            --type                 Type of alarm which will be cleared. It can be used in conjunction with \"--nsrc\".");
		out.println("                                   Expects parameter of String type.");
		out.println("    -l, --list                     Fetches list(id) of active alarms. If \"--nsrc\" option is used, alarm Ids are fetched for this Notification source.");
		out.println("            --nsrc                 Notification source for which alarms Ids are fetched, its not mandatory.");
		out.println("                                   Expects parameter of NotificationSource type.");
		out.println("    -d, --descriptor               Fetches descriptor of alarm, if passed argument is an array of alarm ids, it will return array of descriptors.");
		out.println("                                   Expects parameter of String or array of Strings.");
		out.println("    -a, --active                   Checks if alarm with matching id is active.");
		out.println("                                   Expects parameter of String type. Passed argument must be id of alarm.");
		out.println();
		out.println("arg:");
		out.println("");
		out.println("    NotificationSource:    ProfileTableNotification[table=xxx]");
		out.println("    AlarmId array:         alarmId1;alarmId2");
		out.println("");
		out.println("Examples: ");
		out.println("");
		out.println("     1. Check if alarm with specific id is active or not:");
		out.println("" + name + " -a415f719e-3a3d-42b4-acc1-4e84706f031a");
		out.println("" + name + " --active=415f719e-3a3d-42b4-acc1-4e84706f031a");
		out.println("");
		out.println("     2. List ids of active alarms:");
		out.println("" + name + " -l");
		out.println("");
		out.println("     3. List ids of active alarms originating from specific notification source:");
		out.println("" + name + " -l --nsrc=RAEntityNotification[entity=LabRA]");
		out.println("");
		out.println("     4. Clear all alarms of certain type originating from certain notification source:");
		out.println("" + name + " -c --nsrc=RAEntityNotification[entity=LabRA] --type=application.trivial.com.org.net");
		out.flush();

	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#getBeanOName()
	 */
	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.SLEE_ALARM);
	}

	protected void processArguments(String[] args) throws CommandException {

		String sopts = ":cd:a:l"; // "-" is required to allow non option
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
		private static final String OPERATION_isActive = "isActive";
		public IsActiveOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			this.operationName= OPERATION_isActive;
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

		@Override
		protected String prepareResultText() {
			Boolean b = (Boolean) super.operationResult;
			if(b)
			{
				return "Alarm is active.";
			}else
			{
				return "Alarm is inactive.";
			}
			
		}
		
	}
	
	
	private class GetDescriptorOperation extends AbstractOperation
	{


		private static final String OPERATION_getDescriptors = "getDescriptors";

		private static final String OPERATION_getDescriptor = "getDescriptor";
		public GetDescriptorOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//op name not set!
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();
			
			if (optArg.contains(";")) {
				addArg(optArg.split(";"), String[].class, false);
				operationName = OPERATION_getDescriptors;
			} else {
				operationName = OPERATION_getDescriptor;
				addArg(optArg, String.class, false);
			}
			
		}
		//TODO: DISPLAY
		
		
		
	}

	private class ListAlarmsOperation extends AbstractOperation {
		public static final char nsrc = 'z';

		private static final String OPERATION_getAlarms = "getAlarms";
		
		public ListAlarmsOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_getAlarms;
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

		@Override
		protected String prepareResultText() {
			if(super.operationResult == null)
			{
				return "No Alarms present";
			}else
			{
				String[] alarms = (String[]) super.operationResult;
				if(alarms.length == 0)
				{
					return "No Alarms present";
				}else
				{
					return super.prepareResultText();
				}
			}
		}
		

	}
	
	private class ClearAlarmsOperation extends AbstractOperation
	{
		private static final String OPERATION_clearAlarms = "clearAlarms";
		private static final String OPERATION_clearAlarm = "clearAlarm";
		
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
					super.operationName = OPERATION_clearAlarms;
					break;
				case type:
					stringType = opts.getOptarg();
					super.operationName = OPERATION_clearAlarms;
					break;
				case id:
					 stringID = opts.getOptarg();
					 super.operationName = OPERATION_clearAlarm;
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

		@Override
		protected String prepareResultText() {
			if(operationName.equals(OPERATION_clearAlarm))
			{
				Boolean b = (Boolean) super.operationResult;
				if(b)
				{
					return "Alarm has been cleared.";
				}else
				{
					return "Alarm has not been cleared.";
				}
			}else
			{
				//clearAlarms return int number of cleared alarms
				return "Number of Alarms cleared: "+super.operationResult;
			}
		}

		
		
		
		
	}
}
