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
package org.mobicents.tools.twiddle.jsleex;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.beans.PropertyEditor;
import java.io.PrintWriter;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * Command to interact with ActivityManagement MBean
 * 
 * @author baranowb
 * 
 */
public class ActivityCommand extends AbstractSleeCommand {

	public ActivityCommand() {
		super("activity", "This command performs operations on Restcomm ActivityManagement MBean.");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		out.println();
		out.println("usage: " + name + " <-operation[[arg] | [--option[=arg]]*]>");
		out.println();
		out.println("operation:");
		out.println("    -c, --count                     Retrieves current count of activity contexts. Does not require argument.");
		out.println("    -q, --query                     Performs liveliness query if no option is specified.");
		out.println("                                    Optionally it may be followed by one of options, which alter action:");
		out.println("    		--set                    Sets interval between queries. Requires argument of type long, interval is set in minutes.");
		out.println("    		--get                    Gets interval. Does not require argument.");
		out.println("    -i, --idle                      Performs operation on activity context maximal idle time. Requires one of following options:");
		out.println("    		--set                    Sets idle time. Requires argument of long type. Idle time is set in minutes.");
		out.println("    		--get                    Gets idle time. Does not require argument.");
		//out.println("    -d, --details                   Retrieves details of underlying activity context. Requires ActivityContextHandle as argument.");
		out.println("    -l, --list                      Depending on option lists specific information. It must be followed by one of options:");
		out.println("    		--factories              Lists activity context factories. Does not require argument.");
		out.println("    		--contexts               Lists contexts present in container. It takes optional boolean argument. If argument is set to true information about context is detailed.");
		out.println("    		--id-by-activity-type    List IDs of activity context based on activity class name. Requires FQN class name of activity as argument.");
		out.println("    		--id-by-ra-entity        List IDs of activity context based on RA entity name. Requires entity name as argument.");
		out.println("    		--id-by-sbb-id           List IDs of activity context based on SBB ID. Requires SBB ID as argument.");
		//out.println("    		--id-by-sbbe-id          List IDs of activity context based on SBB entity ID. Requires SBB entity ID as argument.");
		//out.println("    -e, --end                       Ends explicitly activity. Requires ActivityContextHandle as argument.");
		//out.println("arg:");
		//out.println("");
		//out.println("");
		out.println("Examples: ");
		out.println("");
		out.println("     1. Perform livelines query:");
		out.println("" + name + " -q");
		out.println("");
		out.println("     2. Set automatic livelines query delay to 1,5 minute:");
		out.println("" + name + " -q --set=90");
		out.println("");
		out.println("     3. List activity contexts, wit all information, attachments, names, variables, ...:");
		out.println("" + name + " -l --contexts=true");
		out.flush();
	}

	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.MC_ACTIVITY_MANAGEMENT);
	}

	@Override
	protected void processArguments(String[] args) throws CommandException {
		//String sopts = ":cqid:le:";
		String sopts = ":cqil";
		LongOpt[] lopts = { 
				new LongOpt("count", LongOpt.NO_ARGUMENT, null, 'c'),
				new LongOpt("query", LongOpt.NO_ARGUMENT, null, 'q'),
					// options
					new LongOpt("set", LongOpt.REQUIRED_ARGUMENT, null, QueryOperation.set),
					new LongOpt("get", LongOpt.NO_ARGUMENT, null, QueryOperation.get),
				new LongOpt("idle", LongOpt.NO_ARGUMENT, null, 'i'),
					// options
					new LongOpt("set", LongOpt.REQUIRED_ARGUMENT, null, QueryOperation.set),
					new LongOpt("get", LongOpt.NO_ARGUMENT, null, QueryOperation.get),
				//new LongOpt("details", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
				new LongOpt("list", LongOpt.NO_ARGUMENT, null, 'l'),
					// options
					new LongOpt("factories", LongOpt.NO_ARGUMENT, null, ListOperation.factories),
					new LongOpt("contexts", LongOpt.OPTIONAL_ARGUMENT, null, ListOperation.contexts),
					new LongOpt("id-by-activity-type", LongOpt.REQUIRED_ARGUMENT, null, ListOperation.activityType),
					new LongOpt("id--by-ra-entity", LongOpt.REQUIRED_ARGUMENT, null, ListOperation.raEntity),
					new LongOpt("id-by-sbb-id", LongOpt.REQUIRED_ARGUMENT, null, ListOperation.sbbID),
					//new LongOpt("id-by-sbbe-id", LongOpt.REQUIRED_ARGUMENT, null, ListOperation.sbbEID),
				//new LongOpt("end", LongOpt.REQUIRED_ARGUMENT, null, 'e'),

		};

		Getopt getopt = new Getopt(null, args, sopts, lopts);
		getopt.setOpterr(false);

		int code;
		while ((code = getopt.getopt()) != -1) {
			switch (code) {
			case ':':
				throw new CommandException("Option requires an argument: " + args[getopt.getOptind() - 1]);

			case '?':
				throw new CommandException("Invalid (or ambiguous) option: " + args[getopt.getOptind() - 1]);

			case 'c':

				super.operation = new CountOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'q':

				super.operation = new QueryOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'i':

				super.operation = new IdleOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'd':

				super.operation = new DetailsOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'l':

				super.operation = new ListOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'e':

				super.operation = new EndOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}

	}
	
	private class CountOperation extends AbstractOperation
	{
		private static final String OPERATION_getActivityContextCount = "getActivityContextCount";
		protected CountOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_getActivityContextCount;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			//no args
		}
		
	}

	private class QueryOperation extends AbstractOperation {
		public static final char set = 's';
		public static final char get = 'g';

		private static final String OPERATION_setTimeBetweenLivenessQueries = "setTimeBetweenLivenessQueries";
		private static final String OPERATION_getTimeBetweenLivenessQueries = "getTimeBetweenLivenessQueries";
		private static final String OPERATION_queryActivityContextLiveness = "queryActivityContextLiveness";
		
		public QueryOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			// op name is set depending on options
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {

			int code;
			String optArg;
			while ((code = opts.getopt()) != -1) {
				if (super.operationName != null) {
					throw new CommandException("Command: \"" + sleeCommand.getName() + "\", expects either \"--set\" or \"--get\"!");
				}
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case set:

					super.operationName = OPERATION_setTimeBetweenLivenessQueries;
					optArg = opts.getOptarg();

					try {
						addArg(Long.parseLong(optArg), long.class, false);
					} catch (Exception e) {
						throw new CommandException("Failed to parse Long: \"" + optArg + "\"", e);
					}

					break;
				case get:

					super.operationName = OPERATION_getTimeBetweenLivenessQueries;

					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
				if (super.operationName == null) {
					throw new CommandException("Command: \"" + sleeCommand.getName() + "\", expects either \"--set\" or \"--get\"!");
				}

			}
			
			//set in case no opt
			if(super.operationName == null)
			{
				super.operationName = OPERATION_queryActivityContextLiveness;
			}

		}

	}
	private class IdleOperation extends AbstractOperation {
		
		private static final String OPERATION_setActivityContextMaxIdleTime = "setActivityContextMaxIdleTime";
		private static final String OPERATION_getActivityContextMaxIdleTime = "getActivityContextMaxIdleTime";
		public IdleOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			// op name is set depending on options
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {

			int code;
			String optArg;
			while ((code = opts.getopt()) != -1) {
				if (super.operationName != null) {
					throw new CommandException("Command: \"" + sleeCommand.getName() + "\", expects either \"--set\" or \"--get\"!");
				}
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case QueryOperation.set:

					super.operationName = OPERATION_setActivityContextMaxIdleTime;
					optArg = opts.getOptarg();
					try {
						addArg(Long.parseLong(optArg), long.class, false);
					} catch (Exception e) {
						throw new CommandException("Failed to parse Long: \"" + optArg + "\"", e);
					}

					break;
				case QueryOperation.get:

					super.operationName = OPERATION_getActivityContextMaxIdleTime;

					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
				if (super.operationName == null) {
					throw new CommandException("Command: \"" + sleeCommand.getName() + "\", expects either \"--set\" or \"--get\"!");
				}

			}

		}

	}
	private class DetailsOperation extends AbstractOperation {
		
		private static final String OPERATION_retrieveActivityContextDetails = "retrieveActivityContextDetails";
		public DetailsOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_retrieveActivityContextDetails;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			//TODO add that damn thing. ACH parse.

		}

	}
	
	
	
	
	private class ListOperation extends AbstractOperation {
		public static final char sbbEID = 'z';
		public static final char sbbID = 'x';
		public static final char raEntity = 'v';
		public static final char activityType = 'b';
		public static final char contexts = 'n';
		public static final char factories = 'm';

		private static final String OPERATION_retrieveActivityContextIDBySbbEntityID = "retrieveActivityContextIDBySbbEntityID";
		private static final String OPERATION_retrieveActivityContextIDBySbbID = "retrieveActivityContextIDBySbbID";
		private static final String OPERATION_retrieveActivityContextIDByResourceAdaptorEntityName = "retrieveActivityContextIDByResourceAdaptorEntityName";
		private static final String OPERATION_retrieveActivityContextIDByActivityType = "retrieveActivityContextIDByActivityType";
		private static final String OPERATION_listActivityContexts = "listActivityContexts";
		private static final String OPERATION_listActivityContextsFactories = "listActivityContextsFactories";

		
		

		public ListOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			// op name is set depending on options
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {

			int code;
			String optArg;
			while ((code = opts.getopt()) != -1) {
				if (super.operationName != null) {
					throw new CommandException("Command: \"" + sleeCommand.getName() + "\", expects  option!");
				}
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

//				case sbbEID:
//					super.operationName = OPERATION_retrieveActivityContextIDBySbbEntityID;
//					optArg = opts.getOptarg();		
//					addArg(optArg, String.class, false);
//										break;
				case sbbID:
					super.operationName = OPERATION_retrieveActivityContextIDBySbbID;
					optArg = opts.getOptarg();
				
					addArg(optArg, String.class, false);
					
					
					break;
				case raEntity:
					super.operationName = OPERATION_retrieveActivityContextIDByResourceAdaptorEntityName;
					optArg = opts.getOptarg();
					
					addArg(optArg, String.class, false);
				
					
					break;
				case activityType:
					super.operationName = OPERATION_retrieveActivityContextIDByActivityType;
					optArg = opts.getOptarg();
					
					addArg(optArg, String.class, false);
					
					
					break;				
					
				case contexts:
					super.operationName = OPERATION_listActivityContexts;
					optArg = opts.getOptarg();
					
					if(optArg == null)
					{
						optArg = "False";
					}
					
					addArg(Boolean.valueOf(optArg), boolean.class, false);
					break;
				case factories:
					super.operationName = OPERATION_listActivityContextsFactories;
					break;					

				
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
				if (super.operationName == null) {
					throw new CommandException("Command: \"" + sleeCommand.getName() + "\", expects  option!");
				}

			}

		}
		public final static int AC_ID=0;
	   
	    public final static int ACTIVITY_CLASS=1;
	   
	    public final static int LAST_ACCESS_TIME=2;

	    public final static int RA=3;
	   
	    public final static int SBB_ATTACHMENTS=4;
	   
	    public final static int NAMES_BOUND_TO=5;
	  
	    public final static int TIMERS_ATTACHED=6;
	    
	    public final static int DATA_PROPERTIES=7;
		/* (non-Javadoc)
		 * @see org.mobicents.tools.twiddle.op.AbstractOperation#unfoldArray(java.lang.String, java.lang.Object[])
		 */
		@Override
		protected String unfoldArray(String prefix, Object[] array, PropertyEditor editor) {
			
			if(super.operationName.equals(OPERATION_listActivityContexts))
			{
				//array of arrays of len 8 :)
				StringBuffer sb = new StringBuffer();
				
				for(Object o:array)
				{
					Object[] rep = (Object[]) o;
					
					sb.append("ACH               : ").append(rep[AC_ID]).append("\n");
					sb.append("Class             : ").append(rep[ACTIVITY_CLASS]).append("\n");
					sb.append("Last access time  : ").append(rep[LAST_ACCESS_TIME]).append("\n");
					sb.append("Resource Adaptor  : ").append(rep[RA]).append("\n");
					if(rep[SBB_ATTACHMENTS].getClass().isArray())
					{
						sb.append("SBB attached      : ").append(super.unfoldArray("     ", (Object[])rep[SBB_ATTACHMENTS],null)).append("\n");
						sb.append("Names             : ").append(super.unfoldArray("     ", (Object[])rep[NAMES_BOUND_TO],null)).append("\n");
						sb.append("Timers Attached   : ").append(super.unfoldArray("     ", (Object[])rep[TIMERS_ATTACHED],null)).append("\n");
						sb.append("Data properties   : ").append(super.unfoldArray("     ", (Object[])rep[DATA_PROPERTIES],null)).append("\n");
					}else
					{
						sb.append("SBB attached      : ").append(rep[SBB_ATTACHMENTS]).append("\n");
						sb.append("Names             : ").append(rep[NAMES_BOUND_TO]).append("\n");
						sb.append("Timers Attached   : ").append(rep[TIMERS_ATTACHED]).append("\n");
						sb.append("Data properties   : ").append(rep[DATA_PROPERTIES]).append("\n");
					}
					sb.append("\n");
				}
				return sb.toString();
			}else
			{
				return super.unfoldArray(prefix, array,editor);
			}
			
		
		}

	}
	
	private class EndOperation extends AbstractOperation {
		
		private static final String OPERATION_endActivity = "endActivity";
		public EndOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_endActivity;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			//TODO: add ACH parse
			//String optArg = opts.getOptarg();
			//addArg(optArg, String.class, false);

		}

	}
}
