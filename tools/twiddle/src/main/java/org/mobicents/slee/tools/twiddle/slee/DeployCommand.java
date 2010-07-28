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
package org.mobicents.slee.tools.twiddle.slee;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.PrintWriter;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.ComponentID;
import javax.slee.ServiceID;
import javax.slee.management.DeployableUnitID;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.slee.tools.twiddle.AbstractSleeCommand;
import org.mobicents.slee.tools.twiddle.JMXNameUtility;
import org.mobicents.slee.tools.twiddle.Operation;

/**
 * @author baranowb
 *
 */
public class DeployCommand extends AbstractSleeCommand {

	
	
	
	public DeployCommand() {
		super("deploy", "This command performs operations on JSLEE DeploymentMBean." );
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#displayHelp()
	 */
	@Override
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		out.println();
		out.println("usage: " + name + " <operation> <arg>*");
		out.println();
		out.println("operation:");
		out.println("    -l, --list                     Lists deployed components based on passed suboption:");
		out.println("            --sbbs                 Lists sbbs, if ServiceID is present as argument,");
		out.println("                                   sbbs are listed for given service.");
		out.println("            --services             Lists services, does not take argument.");
		out.println("            --libraries            Lists libraries, does not take argument.");
		out.println("            --events               Lists event types, does not take argument.");
		out.println("            --ra-types             Lists RA types, does not take argument.");
		out.println("            --ras                  Lists RAs, does not take argument.");
		out.println("            --dus                  Lists DUs, does not take argument.");
		out.println("            --profile-spec         Lists profile specifications, does not take argument.");
		out.println("    -y, --installed                Checks if SLEE component is installed. It accepts following suboptions:");
		out.println("            --duid                 Indicates check based on DeployableUnit ID. ");
		out.println("                                   It expects DeployableUnit ID as argument. It excludes \"--cid\".");
		out.println("            --cid                  Indicates check based on Component ID. ");
		out.println("                                   It expects Component ID as argument. It excludes \"--duid\".");
		out.println("    -i, --install                  Install DU which is identified by given path. ");
		out.println("                                   It expects path as argument.");
		out.println("    -u, --un-install               Uninstall DU which is identified by given DeployableUnit ID. ");
		out.println("                                   It expects DeployableUnit ID as argument.");
		out.println("    -d, --duid                     Fetches Deplouable Unit ID for given path.");
		out.println("                                   Requiers path as argument.");
		out.println("    -s, --desc                     Fetches descriptors for given SLEE component. It supports following suboptions:");
		out.println("            --duid                 Operation fetched descriptors based on DeployableUnit ID passed as arg.");
		out.println("                                   It expects DeployableUnit ID(single or array) as argument.");
		out.println("            --cid                  Operation fetched descriptors based on Component ID passed as arg.");
		out.println("    -r, --ref                      Fetches IDs of referring components. Expects ComponentID as argument.");
		

		out.println("arg:");
		out.println("    ComponentID:             Is any valid component id, for instance ServiceID[name=xxx,vendor=uuu,version=123.0.00]");
		out.println("    ComponentID Array:       ServiceID[name=xxx,vendor=uuu,version=123.0.00];ServiceID[name=xxx,vendor=uuu,version=123.0.00]");
		
		out.flush();


	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#processArguments(java.lang.String[])
	 */
	@Override
	protected void processArguments(String[] args) throws CommandException {
		String sopts = ":lyi:u:d:sr:"; 
		LongOpt[] lopts = {
				new LongOpt("list", LongOpt.NO_ARGUMENT, null, 'l'),
					//suboptions
					new LongOpt("sbbs", LongOpt.OPTIONAL_ARGUMENT, null, ListOperation.sbbs),
					new LongOpt("services", LongOpt.NO_ARGUMENT, null, ListOperation.services),
					new LongOpt("libraries", LongOpt.NO_ARGUMENT, null, ListOperation.libraries),
					new LongOpt("events", LongOpt.NO_ARGUMENT, null, ListOperation.events),
					new LongOpt("ra-types", LongOpt.NO_ARGUMENT, null, ListOperation.ra_types),
					new LongOpt("ras", LongOpt.NO_ARGUMENT, null, ListOperation.ras),
					new LongOpt("dus", LongOpt.NO_ARGUMENT, null, ListOperation.dus),
					new LongOpt("profile-spec", LongOpt.NO_ARGUMENT, null, ListOperation.profile_specs),
				new LongOpt("installed", LongOpt.NO_ARGUMENT, null, 'y'),
					new LongOpt("cid", LongOpt.REQUIRED_ARGUMENT, null, IsInstalledOperation.cid),
					new LongOpt("duid", LongOpt.REQUIRED_ARGUMENT, null, IsInstalledOperation.duid),
				new LongOpt("install", LongOpt.REQUIRED_ARGUMENT, null, 'i'),
				new LongOpt("un-install", LongOpt.REQUIRED_ARGUMENT, null, 'u'),
				new LongOpt("duid", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
				new LongOpt("desc", LongOpt.NO_ARGUMENT, null, 's'),
					//new LongOpt("cid", LongOpt.REQUIRED_ARGUMENT, null, GetDescriptorsOperation.cid),
					//new LongOpt("duid", LongOpt.REQUIRED_ARGUMENT, null, GetDescriptorsOperation.duid),
				new LongOpt("ref", LongOpt.REQUIRED_ARGUMENT, null, 'r'),
				

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

			case 'l':

				super.operation = new ListOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'y':

				super.operation = new IsInstalledOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'i':

				super.operation = new InstallOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'u':

				super.operation = new UninstallOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'd':

				super.operation = new DeployableUnitIDOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 's':

				super.operation = new GetDescriptorsOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;
			case 'r':

				super.operation = new GetReferringComponentsOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);

				break;

			
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}
		}
	}
	/* (non-Javadoc)
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#getBeanOName()
	 */
	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		return new ObjectName(JMXNameUtility.SLEE_DEPLOYMENT);
	}

	
	private class GetReferringComponentsOperation extends Operation
	{

		public GetReferringComponentsOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getReferringComponents";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();
			if (optArg.contains(";")) {
				// arrays for ServiceID

				throw new CommandException("Array parameter is not supported by: "+args[opts.getOptind()-1]);

			}
			try{
				addArg(optArg, ComponentID.class, true);
			}catch(Exception e)
			{
				throw new CommandException("Failed to parse ComponentID: \""+optArg+"\"",e);
			}
		}
		
	}
	
	private class GetDescriptorsOperation extends Operation
	{

		
		private String stringDUID;
		private String stringCID;
		public GetDescriptorsOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//op name not set, depends on args
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			
			
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind()-1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind()-1]+" --> "+opts.getOptopt());
				
				case IsInstalledOperation.duid:
	
					stringDUID = opts.getOptarg();
					super.operationName = "clearAlarms";
					break;
				case IsInstalledOperation.cid:
					stringCID = opts.getOptarg();
					super.operationName = "clearAlarms";
					break;
				
				default:
					throw new CommandException("Operation \""+this.operationName+"\" for command: \""+sleeCommand.getName()+"\", found unexpected opt: "+args[opts.getOptind()-1]);

				}
			}
			
			
			if( (stringCID == null && stringDUID == null) ||  (stringDUID != null && stringCID != null) )
			{
				throw new CommandException("Operation \""+this.operationName+"\" for command: \""+sleeCommand.getName()+"\", expects either \"--duid\" or \"--cid\" to be present");
			}
			
			if(stringCID!=null)
			{
				
				if (stringCID.contains(";")) {
					// arrays for ServiceID

					super.operationName="getDescriptors";

				}else
				{
					super.operationName="getDescriptor";
				}
				try{
					addArg(stringCID, ComponentID.class, true);
				}catch(Exception e)
				{
					throw new CommandException("Failed to parse ComponentID: \""+stringCID+"\"",e);
				}
				
			}else
			{
				if (stringDUID.contains(";")) {
					// arrays for ServiceID

					super.operationName="getDescriptors";

				}else
				{
					super.operationName="getDescriptor";
				}
				try{
					addArg(stringDUID, DeployableUnitID.class, true);
				}catch(Exception e)
				{
					throw new CommandException("Failed to parse DeployableUnitID: \""+stringDUID+"\"",e);
				}
				
			}
			
		}
		
	}
	
	private class DeployableUnitIDOperation extends Operation
	{
		
		public DeployableUnitIDOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "getDeployableUnit";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			addArg(opts.getOptarg(), String.class, false);
		}
		
	}
	
	private class UninstallOperation extends Operation
	{

		public UninstallOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "uninstall";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String optArg = opts.getOptarg();
			if (optArg.contains(";")) {
				// arrays for ServiceID

				throw new CommandException("Array parameter is not supported by: "+args[opts.getOptind()-1]);

			}
			try{
				addArg(optArg, DeployableUnitID.class, true);
			}catch(Exception e)
			{
				throw new CommandException("Failed to parse ComponentID: \""+optArg+"\"",e);
			}
		}
		
	}
	
	private class InstallOperation extends Operation
	{
		
		public InstallOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "install";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			addArg(opts.getOptarg(), String.class, false);
		}
		
	}

	private class IsInstalledOperation extends Operation {
		public static final char duid = 'n';
		public static final char cid = 'm';
		private String stringDUID;
		private String stringCID;

		public IsInstalledOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "isInstalled";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] );

				case duid:

					stringDUID = opts.getOptarg();
	
					break;
				case cid:
					stringCID = opts.getOptarg();

					break;

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
			}

			if ((stringCID == null && stringDUID == null) || (stringDUID != null && stringCID != null)) {
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", expects either \"--duid\" or \"--cid\" to be present");
			}

			if (stringCID != null) {

				if (stringCID.contains(";")) {
					throw new CommandException("Array parameter is not supported by: " + args[opts.getOptind() - 1]);

				}
				try {
					addArg(stringCID, ComponentID.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse ComponentID: \"" + stringCID + "\"", e);
				}

			} else {
				if (stringDUID.contains(";")) {
					// arrays for ServiceID

					throw new CommandException("Array parameter is not supported by: " + args[opts.getOptind() - 1]);

				}
				try {
					addArg(stringDUID, DeployableUnitID.class, true);
				} catch (Exception e) {
					throw new CommandException("Failed to parse DeployableUnitID: \"" + stringDUID + "\"", e);
				}

			}

		}

	}
	
	private class ListOperation extends Operation {
		
		public static final char sbbs = 'q';
		public static final char ra_types = 'w';
		public static final char ras = 'e';
		public static final char services = 'c';
		public static final char libraries = 't';
		public static final char events = 'a';
		public static final char dus = 'h';
		public static final char profile_specs = 'g';
		
		
		
		//service id which can be presen with 'q';
		private String stringServiceID;
		
		

		public ListOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			
			//not perfect, it will swallow everything that matches. but its ok.
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] );

				case sbbs:
					super.operationName = "getSbbs";
					stringServiceID = opts.getOptarg();
					System.err.println("ServiceID: "+stringServiceID);
					if(stringServiceID!=null)
					{
						//we have service id;
						try {
							addArg(stringServiceID, ServiceID.class, true);
						} catch (Exception e) {
							throw new CommandException("Failed to parse ServiceID: \"" + stringServiceID + "\"", e);
						}
					}
					break;		
				case ra_types:
					super.operationName = "getResourceAdaptorTypes";
					break;				
				case ras:
					super.operationName = "getResourceAdaptors";
					break;			
				case services:
					super.operationName = "getServices";
					break;				
				case libraries:
					super.operationName = "getLibraries";
					break;				
				case events:
					super.operationName = "getEventTypes";
					break;

				case dus:
					super.operationName = "getDeployableUnits";//FIXME: add editor for that
					break;
				case profile_specs:
					super.operationName = "getProfileSpecifications";
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
				if(operationName == null)
				{
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", requiers suboption to be present.");
				}
			}

			

		}

	}
}
