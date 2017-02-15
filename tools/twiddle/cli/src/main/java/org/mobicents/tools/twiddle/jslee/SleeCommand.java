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

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author baranowb
 * @author <a href="mailto:info@pro-ids.com">ProIDS sp. z o.o.</a>
 * 
 */
public class SleeCommand extends AbstractSleeCommand {

	/**
	 * This command performs operations on JSLEE SleeManagementMBean
	 */
	public SleeCommand() {
		super("slee", "This command performs operations on JSLEE SleeManagementMBean.");
		// TODO Auto-generated constructor stub
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
		out.println("usage: " + name + " <-operation [--option=arg]*]>");
		out.println();
		out.println("operation:");
		out.println("    -r, --start                     Starts container.");
		out.println("    -s, --stop                      Stops container.");
		out.println("    -g, --gracefulStop              Requests container to enter graceful stopping state.");
		out.println("            --time                  Optionally specifies graceful shutdown waiting maximum time value in seconds. ");
		out.println("            --ast                   Optionally specifies Active Sessions Threshold value (integer) as an argument ");
		out.println("                                    When the number of active RA related activities drops below ");
		out.println("                                    AST value then container is stopped in standard mode");
		out.println("    -d, --shutdown                  Shutdowns container.");
		out.println("    -i, --info                      Displays information about SLEE container(vendor, version, etc.).");
		//no more supported, since we dont have subsystems?
		out.flush();

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
		String sopts = ":rsgdi";

		LongOpt[] lopts = {
				new LongOpt("start", LongOpt.NO_ARGUMENT, null, 'r'),
				new LongOpt("stop", LongOpt.NO_ARGUMENT, null, 's'),
				new LongOpt("gracefulStop", LongOpt.NO_ARGUMENT, null, 'g'),
				//options
					new LongOpt("ast", LongOpt.REQUIRED_ARGUMENT, null, GracefulStopOperation.ast),
					new LongOpt("time", LongOpt.REQUIRED_ARGUMENT, null, GracefulStopOperation.time),
				new LongOpt("shutdown", LongOpt.NO_ARGUMENT, null, 'd'),
				new LongOpt("info", LongOpt.NO_ARGUMENT, null, 'i'),
				
				 };

		Getopt getopt = new Getopt(null, args, sopts, lopts);
		// getopt.setOpterr(false);

		int code;
		while ((code = getopt.getopt()) != -1) {
			switch (code) {
			case ':':
				throw new CommandException("Option requires an argument: " + args[getopt.getOptind() - 1]);

			case '?':
				throw new CommandException("Invalid (or ambiguous) option: " + args[getopt.getOptind() - 1]);

			case 'r':
				super.operation = new StartOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;

			case 's':
				super.operation = new StopOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;

			case 'g':
				super.operation = new GracefulStopOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;

			case 'd':
				super.operation = new ShutdownOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;

			case 'i':
				super.operation = new InfoOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#getBeanOName()
	 */
	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(Utils.SLEE_MANAGEMENT);
	}
	
	private class StartOperation extends AbstractOperation
	{
		private static final String OPERATION_start= "start";

		public StartOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName =OPERATION_start;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// TODO Auto-generated method stub
			
		}
	}

	private class StopOperation extends AbstractOperation
	{
		private static final String OPERATION_stop = "stop";

		public StopOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_stop;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// TODO Auto-generated method stub
			
		}
	}

	private class GracefulStopOperation extends AbstractOperation {
		private static final String OPERATION_graceful_stop = "gracefulStop";
		public static final char ast = 'a';
		public static final char time = 't';

		//ast which must be present with 'a';
		private String activeSessionThresholdStr = "-1";
		//time which must be present with 't';
		private String gracefulShutdownTimeStr = "-1";


		public GracefulStopOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_graceful_stop;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// not perfect, it will swallow everything that matches. but its ok.
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
					case ':':
						throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

					case '?':
						throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

					case ast:
						try {
							activeSessionThresholdStr = opts.getOptarg();
							Integer.parseInt(activeSessionThresholdStr);
						} catch (Exception e) {
								context.getErrorWriter().println("Failed to parse ast: " + activeSessionThresholdStr + " Exc: " + e.getMessage());
								activeSessionThresholdStr = "-1";
						}
						break;
					case time:
						try {
							gracefulShutdownTimeStr = opts.getOptarg();
							Long.parseLong(gracefulShutdownTimeStr);
						} catch (Exception e) {
							context.getErrorWriter().println("Failed to parse time: " + activeSessionThresholdStr + " Exc: " + e.getMessage());
							gracefulShutdownTimeStr = "-1";
						}
						break;
					default:
						throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName() + "\", found unexpected opt: "
								+ args[opts.getOptind() - 1]);
				}

			}
			//Always set arguments as to enable defaults when no options is defined.
			addArg(new Integer(activeSessionThresholdStr), Integer.class, false);
			addArg(new Long(gracefulShutdownTimeStr), Long.class, false);

			if (this.operationName == null) {
				throw new CommandException(sleeCommand.getName() + " command requires option to be passed.");
			}
		}

	}

	private class ShutdownOperation extends AbstractOperation
	{
		private static final String OPERATION_shutdown = "shutdown";

		public ShutdownOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = OPERATION_shutdown;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class InfoOperation extends AbstractOperation
	{
		private final String[] OPS=new String[]{
				"SleeName",
				"SleeVendor",
				"SleeVersion",
				"Subsystems",
				"State",
		};

		public InfoOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//no name, since its complicated op. It performs more than one call.
			super.operationName ="complicated!";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.slee.tools.twiddle.Operation#invoke()
		 */
		@Override
		public void invoke() throws CommandException {
			try {
				ObjectName on = sleeCommand.getBeanOName();
				MBeanServerConnection conn = context.getServer();
				Object[] parms = getOpArguments().toArray();
				String[] sig = new String[getOpSignature().size()];
				sig = getOpSignature().toArray(sig);
				//operationResult = conn.invoke(on, this.operationName, parms, sig);
				StringBuffer sb = new StringBuffer();
				sb.append("Info[");
				for(int index = 0;index<OPS.length;index++)
				{
					String op = OPS[index];
					sb.append(op);
					sb.append("=");
					
					Object res = conn.invoke(on, "get"+op, parms, sig);
					
					if(res instanceof String[])
					{
						String[] result = (String[]) res;
						sb.append(Arrays.toString(result));
					}else
					{
						sb.append(res);
					}
					
					if(index+1!=OPS.length)
					{
						sb.append(",");
					}
				}
				sb.append("]");
				super.operationResult = sb.toString();
				displayResult();
			}catch(javax.management.InstanceNotFoundException infe)
			{
				//this means no slee running/reployed
				super.operationResult = "No container deployed.";
				displayResult();
			}
			catch (Exception e) {
				//add handle error here?
				throw new CommandException("Failed to invoke \"" + this.operationName + "\" due to: ", e);
			}
		}
		
		
		
	}
	
}
