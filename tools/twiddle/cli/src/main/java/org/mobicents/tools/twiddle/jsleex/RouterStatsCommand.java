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

import java.io.PrintWriter;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.EventTypeID;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * Command to interact with TimerFacilityConfiguration MBean
 * 
 * @author baranowb
 * 
 */
public class RouterStatsCommand extends AbstractSleeCommand {

	public RouterStatsCommand() {
		super("router.stats", "This command performs operations on Mobicents EventRouterStatistics MBean.");
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
		out	.println("   -m, --mapped-activities     Returns number of mapped activities. Optionally it can take positive integer argument.");
		out	.println("                               In case argument is present it indicates executor for which value should be returned.");
		//public int getActivitiesMapped() throws ManagementException;
		//public int getActivitiesMapped(int executor) throws ManagementException;
		out.println("    -a, --avg-time              Returns avarage time spent in routing tasks. Without any option this returns avg time for all executors and events. ");
		out.println("                                Options may be used in any conjunctions: ");
		out.println("           --eventTypeId        Specifies EventTypeID for which avg time should be retrieved. Requires EventTypeID argument.");
		out.println("           --executor           Specifies executor for which avg time should be retrieved. Requires positive integer argument.");
		//public long getAverageEventRoutingTime() throws ManagementException;
		//public long getAverageEventRoutingTime(EventTypeID eventTypeID) throws ManagementException;
		//public long getAverageEventRoutingTime(int executor) throws ManagementException;
		//public long getAverageEventRoutingTime(int executor, EventTypeID eventTypeID) throws ManagementException;
		out.println("    -r, --events-routed         Returns number of events routed. It requires \"--eventTypeId\" option to be present. ");
	    out.println("                                Following options are supported: ");
		out.println("           --eventTypeId        Specifies EventTypeID for which avg time should be retrieved. Requires EventTypeID argument.");
		out.println("           --executor           Specifies executor for which avg time should be retrieved. Requires positive integer argument.");
		//public long getEventsRouted(EventTypeID eventTypeID) throws ManagementException;
		//public long getEventsRouted(int executor, EventTypeID eventTypeID) throws ManagementException;
		out.println("    -e, --executed-tasks        Returns number of executed tasks. Optionally it can take positive integer argument.");
		out.println("                                In case argument is present it indicates executor for which value should be returned.");
		//public long getExecutedTasks() throws ManagementException;
		//public long getExecutedTasks(int executor) throws ManagementException;
		out.println("    -i, --idle-time             Returns idle time for particular executor. Requires positive integer argument, which indicates executor.");
		//public long getIdleTime(int executor) throws ManagementException;
		out.println("    -c, --misc-tasks            Returns number of misc tasks(not routing) executed within container. Optionally it can take positive integer argument.");
		out.println("                                In case argument is present it indicates executor for which value should be returned.");
		//public long getMiscTasksExecuted() throws ManagementException;	
		//public long getMiscTasksExecuted(int executor) throws ManagementException;
		out.println("    -t, --executing-time        Returns total time spent in executor. Requires positive integer argument, which indicates executor.");
		//public long getExecutingTime(int executor) throws ManagementException;
		out.println("    -x, --misc-executing-time   Returns total time spent in executor(misc tasks). Requires positive integer argument, which indicates executor.");
		//public long getMiscTasksExecutingTime(int executor) throws ManagementException;
	    out.println("    -o, --routing-time          Returns total time spent on routing event type in particular executor. Requires both option to be present.");
		out.println("                                Following options are supported: ");
		out.println("          --eventTypeId         Specifies EventTypeID for which avg time should be retrieved. Requires EventTypeID argument.");
		out.println("          --executor            Specifies executor for which avg time should be retrieved. Requires positive integer argument.");
		//public long getRoutingTime(int executor, EventTypeID eventTypeID) throws ManagementException;
		//public String printAllStats() throws ManagementException;
		out.println("    -p, print-all               Prints all statistics. Does not require argument.");
		out.println("Examples: ");
		out.println("");
		out.println("     1. Get number of milliseconds spent on routing certain event type:");
		out.println("" + name + " -r --eventTypeId=EventTypeID[name=javax.sip.message.Request.OPTIONS,vendor=net.java.slee,version=1.2]");
		out.println("");
		out.println("     2. Get number of milliseconds spent on routing certain event type in certain executor:");
		out.println("" + name + "  -r --eventTypeId=EventTypeID[name=javax.sip.message.Request.OPTIONS,vendor=net.java.slee,version=1.2] --executor=2");
		
		

		
		out.flush();
	}

	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.MC_EVENT_ROUTER_STATS);
	}

	@Override
	protected void processArguments(String[] args) throws CommandException {
		String sopts = ":m::are::i:c::t:x:op";
		LongOpt[] lopts = { 
				
				new LongOpt("mapped-activities", LongOpt.OPTIONAL_ARGUMENT, null, 'm'),
				new LongOpt("avg-time", LongOpt.NO_ARGUMENT, null, 'a'),
					// options
					new LongOpt("eventTypeId", LongOpt.REQUIRED_ARGUMENT, null, AvgTimeOperation.eventTypeId),
					new LongOpt("executor", LongOpt.REQUIRED_ARGUMENT, null, AvgTimeOperation.executor),
				new LongOpt("events-routed", LongOpt.NO_ARGUMENT, null, 'r'),
				new LongOpt("executed-tasks", LongOpt.OPTIONAL_ARGUMENT, null, 'e'),
				new LongOpt("idle-time", LongOpt.REQUIRED_ARGUMENT, null, 'i'),
				new LongOpt("misc-tasks", LongOpt.OPTIONAL_ARGUMENT, null, 'c'),
				new LongOpt("executing-time", LongOpt.REQUIRED_ARGUMENT, null, 't'),
				new LongOpt("misc-executing-time", LongOpt.REQUIRED_ARGUMENT, null, 'x'),
				new LongOpt("routing-time", LongOpt.NO_ARGUMENT, null, 'o'),
				new LongOpt("print-all", LongOpt.NO_ARGUMENT, null, 'p')
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

			case 'm':

				super.operation = new MappedActivitiesOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;

			case 'a':

				super.operation = new AvgTimeOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
				
			case 'r':

				super.operation = new EventsRoutedOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'e':

				super.operation = new ExecutedTasksOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
			case 'i':

				super.operation = new IdleTimeOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
				
			case 'c':

				super.operation = new MiscTasksOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 't':

				super.operation = new ExecutingTimeOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'x':

				super.operation = new MiscTimeOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'o':

				super.operation = new RoutingTimeOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'p':

				super.operation = new PrintAllOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;	
				
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}

	}

	private class MappedActivitiesOperation extends AbstractOperation {

		public MappedActivitiesOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getActivitiesMapped";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// it may have arg
			String optArg = opts.getOptarg();
			if (optArg != null) {
				try {
					addArg(Integer.parseInt(optArg), int.class, false);
				} catch (Exception e) {
					throw new CommandException("Failed to parse Integer: \"" + optArg + "\"", e);
				}
			}
		}

	}

	private class AvgTimeOperation extends AbstractOperation {
		public AvgTimeOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getAverageEventRoutingTime";
		}

		public static final char eventTypeId = 'z';
		public static final char executor = 'q';

		private String stringEventTypeId;
		private String stringExecutor;

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;

			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case eventTypeId:
					if (stringEventTypeId != null) {
						throw new CommandException("Event Type can be specified only once.");
					}
					stringEventTypeId = opts.getOptarg();

					break;
				case executor:
					if (stringExecutor != null) {
						throw new CommandException("Executor can be specified only once.");
					}
					stringExecutor = opts.getOptarg();

					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}

			if (stringExecutor != null) {
				// add arg
				try {
					addArg(Integer.parseInt(stringExecutor), int.class, false);
				} catch (Exception e) {
					throw new CommandException("Failed to parse Integer: \"" + stringExecutor + "\"", e);
				}
			}

			if (stringEventTypeId != null) {
				// add arg
				try {
					addArg(stringEventTypeId, EventTypeID.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse EventTypeID: \"" + stringEventTypeId + "\"", e);
				}
			}

		}
	}

	private class EventsRoutedOperation extends AbstractOperation {

		private String stringEventTypeId;
		private String stringExecutor;

		public EventsRoutedOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getEventsRouted";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;

			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case AvgTimeOperation.eventTypeId:
					if (stringEventTypeId != null) {
						throw new CommandException("Event Type can be specified only once.");
					}
					stringEventTypeId = opts.getOptarg();

					break;
				case AvgTimeOperation.executor:
					if (stringExecutor != null) {
						throw new CommandException("Executor can be specified only once.");
					}
					stringExecutor = opts.getOptarg();

					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}

			if (stringEventTypeId == null) {
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires \"--eventTypeId\"");
			}

			if (stringExecutor != null) {
				// add arg
				try {
					addArg(Integer.parseInt(stringExecutor), int.class, false);
				} catch (Exception e) {
					throw new CommandException("Failed to parse Integer: \"" + stringExecutor + "\"", e);
				}
			}

			if (stringEventTypeId != null) {
				// add arg
				try {
					addArg(stringEventTypeId, EventTypeID.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse EventTypeID: \"" + stringEventTypeId + "\"", e);
				}
			}

		}

	}

	private class ExecutedTasksOperation extends AbstractOperation {

		public ExecutedTasksOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getExecutedTasks";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// it may have arg
			String optArg = opts.getOptarg();
			if (optArg != null) {
				try {
					addArg(Integer.parseInt(optArg), int.class, false);
				} catch (Exception e) {
					throw new CommandException("Failed to parse Integer: \"" + optArg + "\"", e);
				}
			}
		}

	}

	private class IdleTimeOperation extends AbstractOperation {

		public IdleTimeOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getIdleTime";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// it may have arg
			String optArg = opts.getOptarg();

			try {
				addArg(Integer.parseInt(optArg), int.class, false);
			} catch (Exception e) {
				throw new CommandException("Failed to parse Integer: \"" + optArg + "\"", e);
			}

		}

	}

	private class MiscTasksOperation extends AbstractOperation {
		public MiscTasksOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getMiscTasksExecuted";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// it may have arg
			String optArg = opts.getOptarg();
			if (optArg != null) {
				try {
					addArg(Integer.parseInt(optArg), int.class, false);
				} catch (Exception e) {
					throw new CommandException("Failed to parse Integer: \"" + optArg + "\"", e);
				}
			}
		}
	}

	private class ExecutingTimeOperation extends AbstractOperation {

		public ExecutingTimeOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getExecutingTime";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// it may have arg
			String optArg = opts.getOptarg();

			try {
				addArg(Integer.parseInt(optArg), int.class, false);
			} catch (Exception e) {
				throw new CommandException("Failed to parse Integer: \"" + optArg + "\"", e);
			}

		}

	}

	private class MiscTimeOperation extends AbstractOperation {

		public MiscTimeOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getMiscTasksExecutingTime";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// it may have arg
			String optArg = opts.getOptarg();

			try {
				addArg(Integer.parseInt(optArg), int.class, false);
			} catch (Exception e) {
				throw new CommandException("Failed to parse Integer: \"" + optArg + "\"", e);
			}

		}

	}

	private class RoutingTimeOperation extends AbstractOperation {
		private String stringEventTypeId;
		private String stringExecutor;

		public RoutingTimeOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getRoutingTime";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;

			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case AvgTimeOperation.eventTypeId:
					if (stringEventTypeId != null) {
						throw new CommandException("Event Type can be specified only once.");
					}
					stringEventTypeId = opts.getOptarg();

					break;
				case AvgTimeOperation.executor:
					if (stringExecutor != null) {
						throw new CommandException("Executor can be specified only once.");
					}
					stringExecutor = opts.getOptarg();

					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}

			if (stringEventTypeId == null) {
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires \"--eventTypeId\"");
			}
			if (stringExecutor == null) {
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires \"--executor\"");
			}

			// add arg
			try {
				addArg(Integer.parseInt(stringExecutor), int.class, false);
			} catch (Exception e) {
				throw new CommandException("Failed to parse Integer: \"" + stringExecutor + "\"", e);
			}

			// add arg
			try {
				addArg(stringEventTypeId, EventTypeID.class, true);
			} catch (Exception e) {
				throw new CommandException("Failed to parse EventTypeID: \"" + stringEventTypeId + "\"", e);
			}

		}

	}
	
	private class PrintAllOperation extends AbstractOperation
	{
		public PrintAllOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "printAllStats";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			

		}
	}
}
