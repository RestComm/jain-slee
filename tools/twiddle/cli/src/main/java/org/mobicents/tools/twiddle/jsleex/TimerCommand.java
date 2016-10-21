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
public class TimerCommand extends AbstractSleeCommand {

	public TimerCommand() {
		super("timer", "This command performs operations on Mobicents TimerFacilityConfiguration MBean.");
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
		out.println("    -t, --threads                   Performs operation on number of threads in timer facility. Exactly one of following options must be present:");
		out.println("            --get                   Returns number of currently used threads. Does not require argument.");
		out.println("            --set                   Sets number of threads, requires argument which is integer greater than zero.");
		out.flush();
	}

	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.MC_TIMER_FACILITY);
	}

	@Override
	protected void processArguments(String[] args) throws CommandException {
		String sopts = ":t";
		LongOpt[] lopts = { new LongOpt("list", LongOpt.NO_ARGUMENT, null, 'l'),
				// options
				new LongOpt("set", LongOpt.REQUIRED_ARGUMENT, null, ThreadsOperation.set),
				new LongOpt("get", LongOpt.NO_ARGUMENT, null, ThreadsOperation.get)

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

			case 't':

				super.operation = new ThreadsOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;

			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}

	}

	private class ThreadsOperation extends AbstractOperation {
		public static final char set = 's';
		public static final char get = 'g';
		private static final String OPERATION_setTimerThreads = "setTimerThreads";
		private static final String OPERATION_getTimerThreads = "getTimerThreads";
		public ThreadsOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
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

					super.operationName = OPERATION_setTimerThreads;
					optArg = opts.getOptarg();
					try {
						addArg(Integer.parseInt(optArg), int.class, false);
					} catch (Exception e) {
						throw new CommandException("Failed to parse Integer: \"" + optArg + "\"", e);
					}

					break;
				case get:

					super.operationName = OPERATION_getTimerThreads;

					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}
			if (super.operationName == null) {
				throw new CommandException("Command: \"" + sleeCommand.getName() + "\", expects either \"--set\" or \"--get\"!");
			}
		}

	}
}
