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
import javax.slee.ComponentID;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceLevel;
import javax.slee.management.NotificationSource;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * Trace command class.
 * 
 * @author baranowb
 * 
 */
@SuppressWarnings("deprecation")
public class TraceCommand extends AbstractSleeCommand {

	public TraceCommand() {
		super("trace", "This command performs operations on JSLEE TraceMBean.");
	}

	/*
	 * (non-Javadoc)
	 * 
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
		out.println("    -a, --tracers-used             Lists tracer names for which Tracer objects have been requested by the");
		out.println("                                   notification source identified by the NotificationSource argument.");
		out.println("                                   Requires notification source as argument.");
		out.println("    -f, --tracers-set              List tracer names for which a trace filter level has been");
		out.println("                                   set for the notification source identified by the NotificationSource parameter.");
		out.println("                                   Requires notification source as argument.");
		out.println("    -s, --set-level                Sets Tracer or Trace level. Depending on options:");
		out.println("                                   --set-level supports following options:");
		out.println("               --cid               Determines SLEE 1.0 component ID. This option excludes \"--nsrc\". It MUST be used in conjunction with --level.");
		out.println("               --nsrc              Determines SLEE 1.1 NotificationSource of tracer affected. This option excludes \"--cid\". It MUST be used in conjunction with --level.");
		out.println("               --name              Determines SLEE 1.1 Tracer name affected. It MUST be used in conjunction wtih --nsrc.");
		out.println("               --level             Determines level of trace (or tracer). It MUST be used with either \"--nsrc\" or \"--cid\"");
		out.println("    -u, --un-set-level             Unsets Tracer level. Depending on options. Option \"--cid\" excludes \"--nsrc\".");
		out.println("               --nsrc              Determines SLEE 1.1 NotificationSource of tracer affected. It MUST be used in conjunction with --name.");
		out.println("               --name              Determines SLEE 1.1 Tracer name affected. It MUST be used in conjunction with --nsrc.");
		out.println("    -g, --get-level                Gets Trace or Tracer level. Depending on options. Option \"--cid\"  excludes \"--nsrc\".");
		out.println("               --cid               Determines SLEE 1.0 component ID. This option excludes \"--nsrc\".");
		out.println("               --nsrc              Determines SLEE 1.1 NotificationSource of tracer affected. It MUST be used in conjunction with --name.");
		out.println("               --name              Determines SLEE 1.1 Tracer name affected. It MUST be used in conjunction wtih --nsrc.");
		out.println("");
		out.println("arg:");
		out.println("");
		out.println("    NotificationSource:    ProfileTableNotification[table=xxx]");
		out.println("    Level             :    [SEVERE|WARNING|INFO|CONFIG|FINE|FINER|FINEST]");
		out.println("");
		
		out.println("Examples: ");
		out.println("");
		out.println("     1. List used tracers:");
		out.println("" + name + " -aProfileTableNotification[table=xxx]");
		out.println("");
		out.println("     2. Set level of tracer:");
		out.println("" + name + " -s --cid=SbbID[name=LocationSbb,vendor=org.mobicents,version=1.2] --level=SEVERE");
		out.println("");
		out.println("     3. Set level of tracer:");
		out.println("" + name + " -s --nsrc=SbbNotification[service=ServiceID[name=SIP Registrar Service,vendor=org.mobicents,version=1.2],sbb=SbbID[name=LocationSbb,vendor=org.mobicents,version=1.2]] --level=SEVERE --name=error.tracer");
		out.flush();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#getBeanOName()
	 */
	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.SLEE_TRACE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.tools.twiddle.AbstractSleeCommand#processArguments
	 * (java.lang.String[])
	 */
	@Override
	protected void processArguments(String[] args) throws CommandException {
		String sopts = ":a:f:sug";

		LongOpt[] lopts = { new LongOpt("tracers-used", LongOpt.REQUIRED_ARGUMENT, null, 'a'),
				new LongOpt("tracers-set", LongOpt.REQUIRED_ARGUMENT, null, 'f'), new LongOpt("set-level", LongOpt.NO_ARGUMENT, null, 's'),
				new LongOpt("cid", LongOpt.REQUIRED_ARGUMENT, null, SetLevelOperation.cid),
				new LongOpt("nsrc", LongOpt.REQUIRED_ARGUMENT, null, SetLevelOperation.nsrc),
				new LongOpt("name", LongOpt.REQUIRED_ARGUMENT, null, SetLevelOperation.name),
				new LongOpt("level", LongOpt.REQUIRED_ARGUMENT, null, SetLevelOperation.level),
				new LongOpt("un-set-level", LongOpt.NO_ARGUMENT, null, 'u'),
				new LongOpt("nsrc", LongOpt.REQUIRED_ARGUMENT, null, UnsetLevelOperation.nsrc),
				new LongOpt("name", LongOpt.REQUIRED_ARGUMENT, null, UnsetLevelOperation.name),
				new LongOpt("get-level", LongOpt.NO_ARGUMENT, null, 'g'),
				new LongOpt("cid", LongOpt.REQUIRED_ARGUMENT, null, GetLevelOperation.cid),
				new LongOpt("nsrc", LongOpt.REQUIRED_ARGUMENT, null, GetLevelOperation.nsrc),
				new LongOpt("name", LongOpt.REQUIRED_ARGUMENT, null, GetLevelOperation.name), };

		Getopt getopt = new Getopt(null, args, sopts, lopts);
		// getopt.setOpterr(false);

		int code;
		while ((code = getopt.getopt()) != -1) {
			switch (code) {
			case ':':
				throw new CommandException("Option requires an argument: " + args[getopt.getOptind() - 1]);

			case '?':
				throw new CommandException("Invalid (or ambiguous) option: " + args[getopt.getOptind() - 1]);

			case 'a':

				super.operation = new GetTracersUsedOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;

			case 'f':

				super.operation = new GetTracersSetOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			case 's':

				super.operation = new SetLevelOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			case 'u':

				super.operation = new UnsetLevelOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			case 'g':

				super.operation = new GetLevelOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}

	}

	private class GetTracersUsedOperation extends AbstractOperation {

		private static final String OPERATION_getTracersUsed = "getTracersUsed";
		
		public GetTracersUsedOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			this.operationName = OPERATION_getTracersUsed;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();
			if (optArg.contains(";")) {
				throw new CommandException("Option does not support array argument: " + args[opts.getOptind() - 1]);

			} else {
				try {
					addArg(optArg, NotificationSource.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse NotificationSource: \"" + optArg + "\"", e);
				}
			}

		}

	}

	private class GetTracersSetOperation extends AbstractOperation {
		
		private static final String OPERATION_getTracersSet = "getTracersSet";
		
		public GetTracersSetOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			this.operationName = OPERATION_getTracersSet;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();
			if (optArg.contains(";")) {
				throw new CommandException("Option does not support array argument: " + args[opts.getOptind() - 1]);

			} else {
				try {
					addArg(optArg, NotificationSource.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse NotificationSource: \"" + optArg + "\"", e);
				}
			}

		}

	}

	private class SetLevelOperation extends AbstractOperation {

		public static final char cid = 'z';
		public static final char nsrc = 'x';
		public static final char name = 'v';
		public static final char level = 'b';

		private static final String OPERATION_setTraceLevel = "setTraceLevel";
		private String stringCID;

		private String stringNSRC;
		private String stringName; //TODO: allow ansence of name, to set default level? 

		private String stringLevel;

		public SetLevelOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			this.operationName = OPERATION_setTraceLevel;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// tricky
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case cid:
					stringCID = opts.getOptarg();
					break;
				case nsrc:
					stringNSRC = opts.getOptarg();
					break;
				case name:
					stringName = opts.getOptarg();
					break;
				case level:
					stringLevel = opts.getOptarg();
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
				if ((stringCID == null && stringNSRC == null) || (stringNSRC != null && stringCID != null)) {
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", expects either \"--nsrc\" or \"--cid\" to be present");
				}

				if (stringCID != null) {
					// its 1.0
					if (stringLevel == null) {
						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
								+ "\", expects  \"--level\" to be present");
					}
					try {
						addArg(stringCID, ComponentID.class, true);
					} catch (Exception e) {
						throw new CommandException("Failed to parse ComponentID: \"" + stringCID + "\"", e);
					}
					try {
						addArg(stringLevel, Level.class, true);
					} catch (Exception e) {
						throw new CommandException("Failed to parse Level: \"" + stringLevel + "\"", e);
					}
				} else {

					if (stringLevel == null) {
						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
								+ "\", expects  \"--level\" to be present");
					}
					if (stringName == null) {
						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
								+ "\", expects  \"--name\" to be present");
					}

					try {
						addArg(stringNSRC, NotificationSource.class, true);
					} catch (Exception e) {
						throw new CommandException("Failed to parse NotificationSource: \"" + stringNSRC + "\"", e);
					}
					try {
						addArg(stringName, String.class, false);
					} catch (Exception e) {
						throw new CommandException("Failed to parse Name: \"" + stringName + "\"", e);
					}
					try {
						addArg(stringLevel, TraceLevel.class, true);
					} catch (Exception e) {
						throw new CommandException("Failed to parse TraceLevel: \"" + stringLevel + "\"", e);
					}
				}
			}

		}

	}

	private class GetLevelOperation extends AbstractOperation {

		public static final char cid = 'g';
		public static final char nsrc = 'h';
		public static final char name = 'j';
		private static final String OPERATION_getTraceLevel = "getTraceLevel";
		private String stringCID;

		private String stringNSRC;
		private String stringName;

		public GetLevelOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			this.operationName = OPERATION_getTraceLevel;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// tricky
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case cid:
					stringCID = opts.getOptarg();
					break;
				case nsrc:
					stringNSRC = opts.getOptarg();
					break;
				case name:
					stringName = opts.getOptarg();
					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
				if ((stringCID == null && stringNSRC == null) || (stringNSRC != null && stringCID != null)) {
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", expects either \"--nsrc\" or \"--cid\" to be present");
				}

				if (stringCID != null) {
					// its 1.0

					try {
						addArg(stringCID, ComponentID.class, true);
					} catch (Exception e) {
						throw new CommandException("Failed to parse ComponentID: \"" + stringCID + "\"", e);
					}

				} else {

					if (stringName == null) {
						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
								+ "\", expects  \"--name\" to be present");
					}

					try {
						addArg(stringNSRC, NotificationSource.class, true);
					} catch (Exception e) {
						throw new CommandException("Failed to parse NotificationSource: \"" + stringNSRC + "\"", e);
					}
					try {
						addArg(stringName, String.class, false);
					} catch (Exception e) {
						throw new CommandException("Failed to parse Name: \"" + stringName + "\"", e);
					}

				}
			}

		}

	}

	private class UnsetLevelOperation extends AbstractOperation {

		public static final char nsrc = 'o';
		public static final char name = 'p';

		private String stringNSRC;
		private String stringName;
		private static final String OPERATION_unsetTraceLevel = "unsetTraceLevel";
		public UnsetLevelOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			this.operationName = OPERATION_unsetTraceLevel;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// tricky
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case nsrc:
					stringNSRC = opts.getOptarg();
					break;
				case name:
					stringName = opts.getOptarg();
					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
				if (stringNSRC == null) {
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", expects \"--nsrc\" to be present");
				}
				if (stringName == null) {
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", expects \"--name\" to be present");
				}

				try {
					addArg(stringNSRC, NotificationSource.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse NotificationSource: \"" + stringNSRC + "\"", e);
				}
				try {
					addArg(stringName, String.class, false);
				} catch (Exception e) {
					throw new CommandException("Failed to parse Name: \"" + stringName + "\"", e);
				}

			}

		}

	}
}
