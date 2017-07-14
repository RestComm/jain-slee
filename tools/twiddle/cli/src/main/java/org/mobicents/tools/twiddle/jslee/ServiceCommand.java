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

import java.io.PrintWriter;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.ServiceID;
import javax.slee.management.ServiceState;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * Command for service interaction - activate/deactivate etc.
 * 
 * @author baranowb
 * 
 */
public class ServiceCommand extends AbstractSleeCommand {



	public ServiceCommand() {
		super("service", "This command performs operations on JSLEE ServiceManagementMBean.");

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
		out.println("    -a, --activate                 Activates service('s) with matching ServiceID.");
		out.println("                                   Accepts array argument.");
		out.println("    -d, --deactivate               Deactivates service('s) with matching ServiceID");
		out.println("                                   Accepts array argument.");
		out.println("    -c, --deactivate-and-activate  Deactivates and activates service('s) with matching ServiceID(s)");
		out.println("                                   Supports two sub options(mandatory):");
		out.println("                       --ta        Indicates services to be activated in this operation. Accepts array argument.");
		out.println("                       --td        Indicates services to be deactivated in this operation. Accepts array argument.");
		//out.println("    -u, --usage-mbean              Returns the Object Name of a ServiceUsageMBean object");
		//out.println("                                   Requires ServiceID as argument.");
		out.println("    -i, --services                 Returns list of services in given state");
		out.println("                                   Requires ServiceState as argument.");
		out.println("    -o, --state                    Returns state of service");
		out.println("                                   Requires ServiceID as argument. Does not accept array argument.");
		out.println();
		out.println("arg:");
		out.println("");
		out.println("    ServiceID:             ServiceID[name=xxx,vendor=uuu,version=123.0.00]");
		out.println("    ServiceID Array:       ServiceID[name=xxx,vendor=uuu,version=123.0.00];ServiceID[name=xxx,vendor=uuu,version=123.0.00]");
		out.println("    ServiceState:          [Active|Inactive|Stopping]  ");
		out.println("");
		out.println("Examples: ");
		out.println("");
		out.println("     1. Activate two services:");
		out.println("" + name + " -aServiceID[name=xxx,vendor=uuu,version=123.0.00];ServiceID[name=YYY,vendor=uuu,version=123.0.00]");
		out.println("");
		out.println("     2. Deactivate and activate services:");
		out.println("" + name + " -c --td=ServiceID[name=xxx,vendor=uuu,version=123.0.00];ServiceID[name=YYY,vendor=uuu,version=123.0.00] --taServiceID[name=xxx,vendor=uuu,version=123.0.00];ServiceID[name=YYY,vendor=uuu,version=123.0.00]");
		out.println("");
		out.println("     3. Check which services are inactive:");
		out.println("" + name + " -iInactive");
		
		
		
		out.flush();
	}

	

	/* (non-Javadoc)
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#getBeanOName()
	 */
	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.SLEE_SERVICE_MANAGEMENT);
	}

	protected void processArguments(String[] args) throws CommandException {

		//String sopts = ":a:d:u:i:o:c"; // ":" is for req, argument, lack of it
		String sopts = ":a:d:i:o:c";// after option means no args.

		LongOpt[] lopts = { new LongOpt("activate", LongOpt.REQUIRED_ARGUMENT, null, 'a'),
				new LongOpt("deactivate", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
				//new LongOpt("usage-mbean", LongOpt.REQUIRED_ARGUMENT, null, 'u'),
				new LongOpt("services", LongOpt.REQUIRED_ARGUMENT, null, 'i'),
				new LongOpt("state", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
				new LongOpt("deactivate-and-activate", LongOpt.NO_ARGUMENT, null, 'c'),
				// Longopts for deactivateAndActivate - no short ops for those
					new LongOpt("ta", LongOpt.REQUIRED_ARGUMENT, null, DeactivateAndActivateOperation.ta),
					new LongOpt("td", LongOpt.REQUIRED_ARGUMENT, null, DeactivateAndActivateOperation.td) };

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
				// operationName = "activate";
				super.operation = new ActivateOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			case 'd':
				// operationName = "deactivate";
				super.operation = new DeactivateOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			case 'c':
				// operationName = "deactivateAndActivate";
				// this requires some more args than one, so...
				super.operation = new DeactivateAndActivateOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'u':
				// operationName = "getServiceUsageMBean";
				super.operation = new GetServiceUsageMBeanOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			case 'i':
				// operationName = "getServices";
				super.operation = new GetServicesOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'o':
				// operationName = "getState";
				super.operation = new GetStateOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}
	}

	private class ActivateOperation extends AbstractOperation {

		private static final String OPERATION_activate = "activate";
		public ActivateOperation(CommandContext context, Logger log, ServiceCommand serviceCommand) {
			super(context, log, serviceCommand);
			this.operationName = OPERATION_activate;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();

			try {
				if (optArg.contains(";")) {
					// arrays for ServiceID
					addArg(optArg, ServiceID[].class, true);

				} else {
					addArg(optArg, ServiceID.class, true);
				}
			} catch (Exception e) {
				throw new CommandException("Failed to parse ServiceID: \"" + optArg + "\"", e);
			}

		}

	}

	private class DeactivateOperation extends AbstractOperation {

		private static final String OPERATION_deactivate = "deactivate";
		public DeactivateOperation(CommandContext context, Logger log, ServiceCommand serviceCommand) {
			super(context, log, serviceCommand);
			this.operationName = OPERATION_deactivate;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();

			try {
				if (optArg.contains(";")) {
					// arrays for ServiceID
					addArg(optArg, ServiceID[].class, true);

				} else {
					addArg(optArg, ServiceID.class, true);
				}
			} catch (Exception e) {
				throw new CommandException("Failed to parse ServiceID: \"" + optArg + "\"", e);
			}

		}

	}

	private class GetServiceUsageMBeanOperation extends AbstractOperation {

		private static final String OPERATION_getServiceUsageMBean = "getServiceUsageMBean";
		public GetServiceUsageMBeanOperation(CommandContext context, Logger log, ServiceCommand serviceCommand) {
			super(context, log, serviceCommand);
			this.operationName = OPERATION_getServiceUsageMBean;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();

			if (optArg.contains(";")) {
				throw new CommandException("Option does not support array argument.");

			} else {
				try {
					addArg(optArg, ServiceID.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse ServiceID: \"" + optArg + "\"", e);
				}
			}

		}

	}

	private class GetServicesOperation extends AbstractOperation {

		private static final String OPERATION_getServices = "getServices";
		public GetServicesOperation(CommandContext context, Logger log, ServiceCommand serviceCommand) {
			super(context, log, serviceCommand);
			this.operationName = OPERATION_getServices;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();

			if (optArg.contains(";")) {
				throw new CommandException("Option does not support array argument.");

			} else {
				try {
					addArg(optArg, ServiceState.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse ServiceState: \"" + optArg + "\"", e);
				}
			}

		}

	}

	private class GetStateOperation extends AbstractOperation {

		private static final String OPERATION_getState = "getState";
		public GetStateOperation(CommandContext context, Logger log, ServiceCommand serviceCommand) {
			super(context, log, serviceCommand);
			this.operationName = OPERATION_getState;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();

			if (optArg.contains(";")) {
				throw new CommandException("Option does not support array argument.");

			} else {
				try {
					addArg(optArg, ServiceID.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse ServiceID: \"" + optArg + "\"", e);
				}
			}

		}

	}

	private class DeactivateAndActivateOperation extends AbstractOperation {
		// long opts for that
		public static final char ta = 'z';
		public static final char td = 'x';
		private static final String OPERATION_deactivateAndActivate = "deactivateAndActivate";
		private String toActivate;
		private String toDeactivate;

		public DeactivateAndActivateOperation(CommandContext context, Logger log, ServiceCommand serviceCommand) {
			super(context, log, serviceCommand);
			this.operationName = OPERATION_deactivateAndActivate;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// next two opts must contain conf for this.
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] + " --> " + opts.getOptopt());

				case ta:
					toActivate = opts.getOptarg();
					break;
				case td:
					toDeactivate = opts.getOptarg();
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
			}
			if (toActivate == null || toDeactivate == null) {
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requires set of sub options!");
			}

			@SuppressWarnings("rawtypes")
			Class argClass = ServiceID.class;
			if (toActivate.contains(";") || toDeactivate.contains(";")) {
				argClass = ServiceID[].class;
			}
			try {
				addArg(toDeactivate, argClass, true);
			} catch (Exception e) {
				throw new CommandException("Failed to parse service IDs to deactivate: \"" + toDeactivate + "\"", e);
			}

			try {
				addArg(toActivate, argClass, true);
			} catch (Exception e) {
				throw new CommandException("Failed to parse service IDs to activate: \"" + toActivate + "\"", e);
			}

		}

	}
	
}
