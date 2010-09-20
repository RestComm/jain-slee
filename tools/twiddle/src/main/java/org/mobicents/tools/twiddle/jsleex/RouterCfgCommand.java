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

import org.jboss.console.twiddle.command.CommandException;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AccessorOperation;

/**
 * Command to interact with TimerFacilityConfiguration MBean
 * 
 * @author baranowb
 * 
 */
public class RouterCfgCommand extends AbstractSleeCommand {

	public RouterCfgCommand() {
		super("router.cfg", "This command performs operations on Mobicents EventRouterConfiguration MBean.");
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
		out	.println("    -m, --mapper                   Performs operation on executor mapper class name. Instance of this class is used to map activity to executor. Exactly one of following options must be present:");
		out.println("            --get                   Returns name of mapper class. Does not require argument.");
		out.println("            --set                   Sets class of mapper, requires argument which FQN of class to be used for mapping.");
		out	.println("    -t, --threads                  Performs operation on threads used by executor. Exactly one of following options must be present:");
		out.println("            --get                   Returns number of threads used by executor. Does not require argument.");
		out.println("            --set                   Sets number of threads used by executor, requires integer argument which is greater than zero.");
		out	.println("    -x, --stats                    Controls if statistics are enabled(true) or disabled(false). Exactly one of following options must be present:");
		out.println("            --get                   Returns boolean indicating if statistics are being enabled or not. Does not require argument.");
		out.println("            --set                   Sets parameter to enable statistics, requires boolean argument.");	
		out.flush();
	}

	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.MC_EVENT_ROUTER);
	}

	@Override
	protected void processArguments(String[] args) throws CommandException {
		String sopts = ":mtx";
		LongOpt[] lopts = { 
				
				new LongOpt("mapper", LongOpt.NO_ARGUMENT, null, 'm'),
				new LongOpt("threads", LongOpt.NO_ARGUMENT, null, 't'),
				new LongOpt("stats", LongOpt.NO_ARGUMENT, null, 'x'),
					// options
					new LongOpt("set", LongOpt.REQUIRED_ARGUMENT, null, AccessorOperation.set),
					new LongOpt("get", LongOpt.NO_ARGUMENT, null, AccessorOperation.get)

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

				super.operation = new AccessorOperation(super.context, super.log, this,"ExecutorMapperClassName",String.class);
				super.operation.buildOperation(getopt, args);

				break;
			case 't':

				super.operation = new AccessorOperation(super.context, super.log, this,"EventRouterThreads",int.class);
				super.operation.buildOperation(getopt, args);

				break;
			case 'x':

				super.operation = new AccessorOperation(super.context, super.log, this,"CollectStats",boolean.class);
				super.operation.buildOperation(getopt, args);

				break;

			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}

	}
}
