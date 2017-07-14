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
public class CongestionCommand extends AbstractSleeCommand {
	
	private final static String FIELD_PeriodBetweenChecks ="PeriodBetweenChecks";
	private final static String FIELD_MinFreeMemoryToTurnOn ="MinFreeMemoryToTurnOn";
	private final static String FIELD_MinFreeMemoryToTurnOff ="MinFreeMemoryToTurnOff";
	private final static String FIELD_RefuseFireEvent ="RefuseFireEvent";
	private final static String FIELD_RefuseStartActivity ="RefuseStartActivity";

	
	public CongestionCommand() {
		super("congestion", "This command performs operations on Restcomm CongestionControlConfiguration MBean.");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		out.println();
		out.println("usage: " + name + " <-operation[[arg] | [--option[=arg]]*]>");
		out.println("Congestion mechanism starts acting when there is not enough memory available to JVM.");
		out.println("It stops");
		out.println();
		out.println("operation:");
		out.println("    -p, --period                    Performs operation on period between congestion checks. Exactly one of following options must be present:");
		out.println("            --get                   Returns number of seconds between congestion control checks. Does not require argument.");
		out.println("            --set                   Sets number of seconds, requires argument which is integer greater than zero.");
		out.println("    -d, --disable-level             Performs operation on minimal amount of memory free to turn off congestion. Once free memory reaches this level, congestion control stops.");
		out.println("                                    Exactly one of following options must be present:");
		out.println("            --get                   Returns number of MB required to be free to stop congestion. Does not require argument.");
		out.println("            --set                   Sets number of MB, requires argument which is integer greater than zero.");
		out.println("    -e, --enable-level              Performs operation on minimal amount of memory free to turn on congestion. Once free memory reaches this level, congestion control starts.");
		out.println("                                    Exactly one of following options must be present:");
		out.println("            --get                   Returns number of MB required to be free to start congestion. Does not require argument.");
		out.println("            --set                   Sets number of MB, requires argument which is integer greater than zero.");
		out.println("    -f, --refuse-event              Controls if events should not be fired during congestion period. Exactly one of following options must be present:");
		out.println("            --get                   Returns boolean value indicating if events should not be fired(true). Does not require argument.");
		out.println("            --set                   Sets value, \"true\" indicates that events wont be fired, requires argument which is valid boolean.");
		out.println("    -a, --refuse-activity           Controls if activities should not be created during congestion period. Exactly one of following options must be present:");
		out.println("            --get                   Returns boolean value indicating if activities should not be created(true). Does not require argument.");
		out.println("            --set                   Sets value, \"true\" indicates that activities wont be created, requires argument which is valid boolean.");
		out.println("arg:");
		out.println("Examples: ");
		out.println("");
		out.println("     1. Set period between congestion checks:");
		out.println("" + name + " -p set=95");
		out.println("");
		out.println("     2. Set high watermark. Memory that has to be free in JVM for congestion control to stop acting:");
		out.println("" + name + " -d --set=25");
		out.println("");
		out.println("     3. Set low watermark. Amount of free memory which will triger congestion control to act:");
		out.println("" + name + " -e --set=10");
		out.flush();
	}

	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.MC_CONGESTION_CONTROL);
	}

	@Override
	protected void processArguments(String[] args) throws CommandException {
		String sopts = ":pedfa";
		LongOpt[] lopts = { 
				new LongOpt("period", LongOpt.NO_ARGUMENT, null, 'p'),
					// options, used by all
					new LongOpt("set", LongOpt.REQUIRED_ARGUMENT, null, AccessorOperation.set),
					new LongOpt("get", LongOpt.NO_ARGUMENT, null, AccessorOperation.get),
				new LongOpt("enable-level", LongOpt.NO_ARGUMENT, null, 'e'),
				new LongOpt("disable-level", LongOpt.NO_ARGUMENT, null, 'd'),
				new LongOpt("refuse-event", LongOpt.NO_ARGUMENT, null, 'f'),
				new LongOpt("refuse-activity", LongOpt.NO_ARGUMENT, null, 'a'),
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

			case 'p':

				super.operation = new AccessorOperation(super.context, super.log, this,FIELD_PeriodBetweenChecks,int.class);
				super.operation.buildOperation(getopt, args);

				break;
			case 'e':

				super.operation = new AccessorOperation(super.context, super.log, this,FIELD_MinFreeMemoryToTurnOn,int.class);
				super.operation.buildOperation(getopt, args);

				break;
			case 'd':

				super.operation = new AccessorOperation(super.context, super.log, this,FIELD_MinFreeMemoryToTurnOff,int.class);
				super.operation.buildOperation(getopt, args);

				break;
			case 'f':

				super.operation = new AccessorOperation(super.context, super.log, this,FIELD_RefuseFireEvent,boolean.class);
				super.operation.buildOperation(getopt, args);

				break;
			case 'a':

				super.operation = new AccessorOperation(super.context, super.log, this,FIELD_RefuseStartActivity,boolean.class);
				super.operation.buildOperation(getopt, args);

				break;
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}

	}

	
}
