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
public class DeployerCommand extends AbstractSleeCommand {

	public DeployerCommand() {
		super("deployer", "This command performs operations on Mobicents DeploymentManager MBean.");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		out.println();
		out.println("usage: " + name + " <-operation>");
		out.println();
		out.println("operation:");
		out.println("    -s, --status                   Retrieves status of deployment. Does not require argument.");
		out.flush();
	}

	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.MC_DEPLOYER);
	}

	@Override
	protected void processArguments(String[] args) throws CommandException {
		String sopts = ":s";
		LongOpt[] lopts = { new LongOpt("status", LongOpt.NO_ARGUMENT, null, 's')

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

			case 's':

				super.operation = new StatusOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;

			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}

	}

	private class StatusOperation extends AbstractOperation {
		//TODO: format it, it uses XML, which is fine for web, mainly strip <p>,</p>,<br>,<strong>,</string>
		public StatusOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "showStatus";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {

		}
	}
}
