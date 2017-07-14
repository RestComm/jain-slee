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

	private final static String _TO_SEPARATE="Deployable Units Waiting For Uninstall";
	
	public DeployerCommand() {
		super("deployer", "This command performs operations on Restcomm DeploymentManager MBean.");
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
		private static final String OPERATION_showStatus = "showStatus";
		//format it, it uses XML, which is fine for web, mainly strip <p>,</p>,<br>,<strong>,</strong>
		public StatusOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_showStatus;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {

		}

		@Override
		public void displayResult() {
			//strip <p>,</p>,<br>,<strong>,</strong>
			String opResult = (String) super.operationResult;
			opResult = opResult.replaceAll("<p>", "");
			opResult = opResult.replaceAll("</p>", "");
			opResult = opResult.replaceAll("<strong>", "");
			opResult = opResult.replaceAll("</strong>", "");
			opResult = opResult.replaceAll("<br>", "\n");
			opResult = opResult.replace(_TO_SEPARATE, "\n"+_TO_SEPARATE);
			super.operationResult = opResult;
			
			//and display
			super.displayResult();
		}
		
		
	}
}
