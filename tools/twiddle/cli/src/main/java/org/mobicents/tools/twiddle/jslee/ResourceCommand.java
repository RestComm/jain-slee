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
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * @author baranowb
 *
 */
public class ResourceCommand extends AbstractSleeCommand {

	public ResourceCommand() {
		super("resource", "This command performs operations on JSLEE ResourceManagementMBean." );
	}

	/* (non-Javadoc)
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
		out.println("    -b, --bind                     Creates bind between RA entity and link name, supports following options(mandatory):");
		out.println("          --link-name              Specifies link name to be used, requires argument. ");
		out.println("          --entity-name            Specifies RA entity name to be used, requires argument. ");
		out.println("    -u, --unbind                   Unbinds a link name from a RA entity. Requires link name as argument.");
		out.println("    -a, --activate                 Activate a RA entity. Requires entity name as argument.");
		out.println("    -d, --deactivate               Deactivate a RA entity. Requires entity name as argument.");
		out.println("    -c, --create                   Creates a RA entity. Options specify mandatory arguments:");
		out.println("          --entity-name            Specifies RA entity name to be used, requires entity name as argument. ");
		out.println("          --ra-id                  Specifies ResourceAdaptorID to be used, requires argument. ");
		out.println("          --config                 Specifies ConfigurationProperties to be used, requires argument. ");
		out.println("    -r, --remove                   Removes a RA entity. Requires RA entity name as argument.");
		out.println("    -p, --update-config            Update RA entity configuration. Options specify mandatory arguments:");
		out.println("          --entity-name            Specifies RA entity name to be used, requires argument. ");
		out.println("          --config                 Specifies ConfigurationProperties to be used as new set, requires argument. ");
		out.println("    -l, --list                     Lists result. Result content depends on passed options. One option is required, supported:");
		out.println("          --ra-entities            Marks list operation to list entity names. Without argument it will list all entities. If ");
		out.println("                                   ResourceAdaptorID is passed as argument it will list entity names corresponding to argument. ");
		out.println("          --ra-entities-in-state   Marks list operation to list entity names of RAs in given state. Requires argument(ResourceAdaptorEntityState). ");
		out.println("          --ra-entities-by-link    Marks list operation to list entity name(s) of RAs bound to given link name(s). Requires argument which is a single link name or array. ");
		out.println("          --links                  Marks list operation to list link names. Without argument it will list all links. If ");
		out.println("                                   RA entity name is passed as argument it will list only corresponding link names. ");
		out.println("          --sbbs                   Marks list operation to list SbbIDs bound to passed link name. Requires link name as argument ");
		out.println("    -g, --get                      Fetches information from container based on passed option. One option is required, supported options:");
		out.println("          --ra-id                  Retrieves ResourceAdaptorID. Requires entity name as argument, RA ID is fetched for this name. ");
		out.println("          --state                  Retrieves state of RA. Requires entity name as argument. ");
		out.println("          --config-by-id           Retrieves ConfigurationProperties for given ResourceAdaptorID. Requires ResourceAdaptorID as argument.");
		out.println("          --config-by-name         Retrieves ConfigurationProperties for given RA entity name. Requires entity name as argument.");
		//out.println("          --usage-mbean            Retrieves ObjectName of ResourceAdaptorUsageMBean. Requires entity name as argument.");
//				
//		
		out.println("arg:");
		out.println("");
		out.println("NOTE: Config property has general form of: (name:java.type=value) and array has different form, than in components: [(cnf.prop),(cnf.prop)]");
		out.println("     Configuration property array: [(remotePort:java.lang.Integer=40001),(localPort:java.lang.Integer=40000),(localHost:java.lang.String=127.0.0.1),(remoteHost:java.lang.String=127.0.0.1)]");
		out.println("     ResourceAdaptorEntityState: [INACTIVE|STOPPING|ACTIVE]");
		
		out.println("");
		out.println("Examples: ");
		out.println("");
		out.println("     1. Create RA Entity:");
		out.println("" + name + " -c --entity-name=SipRA --ra-id=ResourceAdaptorID[name=JainSipResourceAdaptor,vendor=net.java.slee.sip,version=1.2] --config=[(javax.sip.TRANSPORT:java.lang.String=UDP),(javax.sip.IP_ADDRESS:java.lang.String=),(javax.sip.PORT:java.lang.Integer=5060)]");
		out.println("");
		out.println("     2. Bind RA Entity to Link:");
		out.println("" + name + " -b --link-name=SipRALink --entity-name=SipRA");
		out.println("");
		out.println("     3. Get state of RA Entity:");
		out.println("" + name + " -g --state=SipRA");
		out.println("");
		out.println("     4 List all RA Entities in container:");
		out.println("" + name + " -l --ra-entities");
		out.println("");
		out.println("     5. List all RA Entities created from specific ResourceAdaptorID:");
		out.println("" + name + " -l --ra-entities=ResourceAdaptorID[name=JainSipResourceAdaptor,vendor=net.java.slee.sip,version=1.2]");
		out.flush();
		
	}

	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.tools.twiddle.AbstractSleeCommand#processArguments(java.lang.String[])
	 */
	@Override
	protected void processArguments(String[] args) throws CommandException {
		String sopts = ":bu:a:d:cr:plg";
		
		LongOpt[] lopts = { 
				new LongOpt("bind", LongOpt.NO_ARGUMENT, null, 'b'),
					//common options
					new LongOpt("link-name", LongOpt.REQUIRED_ARGUMENT, null, BindOperation.ra_link_name),
					new LongOpt("entity-name", LongOpt.REQUIRED_ARGUMENT, null, BindOperation.ra_entity_name),
				new LongOpt("unbind", LongOpt.REQUIRED_ARGUMENT, null, 'u'),
				new LongOpt("activate", LongOpt.REQUIRED_ARGUMENT, null, 'a'),
				new LongOpt("deactivate", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
				new LongOpt("create", LongOpt.NO_ARGUMENT, null, 'c'),
					//entity-name is covered
					new LongOpt("ra-id", LongOpt.REQUIRED_ARGUMENT, null, CreateOperation.ra_id),
					new LongOpt("config", LongOpt.REQUIRED_ARGUMENT, null, CreateOperation.config),
				new LongOpt("remove", LongOpt.REQUIRED_ARGUMENT, null, 'r'),
				new LongOpt("update-config", LongOpt.NO_ARGUMENT, null, 'p'),
					//entity-name and config are already covered
				new LongOpt("list", LongOpt.NO_ARGUMENT, null, 'l'),
					new LongOpt("ra-entities", LongOpt.OPTIONAL_ARGUMENT, null, ListOperation.ra_entities),
					new LongOpt("ra-entities-in-state", LongOpt.REQUIRED_ARGUMENT, null, ListOperation.ra_entities_in_state),
					new LongOpt("ra-entities-by-link", LongOpt.REQUIRED_ARGUMENT, null, ListOperation.ra_entities_by_link),
					new LongOpt("links", LongOpt.OPTIONAL_ARGUMENT, null, ListOperation.links),
					new LongOpt("sbbs", LongOpt.REQUIRED_ARGUMENT, null, ListOperation.sbbs),
				new LongOpt("get", LongOpt.NO_ARGUMENT, null, 'g'),
					//ra-id is covered
					new LongOpt("state", LongOpt.REQUIRED_ARGUMENT, null, GetOperation.state),
					new LongOpt("config-by-id", LongOpt.REQUIRED_ARGUMENT, null, GetOperation.config_by_id),
					new LongOpt("config-by-name", LongOpt.REQUIRED_ARGUMENT, null, GetOperation.config_by_name),
					//new LongOpt("usage-mbean", LongOpt.REQUIRED_ARGUMENT, null, GetOperation.usage_mbean),
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
				
			case 'b':
	
				super.operation = new BindOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;

				
			case 'u':
			
				super.operation = new UnBindOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;

		
			case 'a':

				super.operation = new ActivateOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
		
		
			case 'd':
				
				super.operation = new DeactivateOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
		
			case 'c':
				
				super.operation = new CreateOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
			case 'r':
				
				super.operation = new RemoveOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
	
			case 'p':
				
				super.operation = new UpdateConfigOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
		
			case 'l':
				
				super.operation = new ListOperation(super.context, super.log, this);
				super.operation.buildOperation(getopt, args);
				break;
	
			case 'g':
				
				super.operation = new GetOperation(super.context, super.log, this);
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
		return new ObjectName(Utils.SLEE_RESOURCE_MANAGEMENT);
	}

	private class BindOperation extends AbstractOperation
	{
		public static final char ra_link_name = 'v';
		public static final char ra_entity_name = 'm';
		
		private String linkName;
		private String entityName;
		
		public BindOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "bindLinkName";
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

				case ra_link_name:

					this.linkName = opts.getOptarg();
					break;
				case ra_entity_name:

					this.entityName = opts.getOptarg();
					break;
				

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}
			if (this.linkName == null || this.entityName == null) {
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", expects both \"--link-name\" and \"--entity-name\" to be present");
			}
			
			super.addArg(this.entityName, String.class, false);
			super.addArg(this.linkName, String.class, false);
		}
		
	}
	
	private class UnBindOperation extends AbstractOperation
	{

		
		public UnBindOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "unbindLinkName";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			
			super.addArg(opts.getOptarg(), String.class, false);
		}
		
	}
	private class ActivateOperation extends AbstractOperation
	{

		
		public ActivateOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "activateResourceAdaptorEntity";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			
			super.addArg(opts.getOptarg(), String.class, false);
		}
		
	}
	private class DeactivateOperation extends AbstractOperation
	{

		
		public DeactivateOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "deactivateResourceAdaptorEntity";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			
			super.addArg(opts.getOptarg(), String.class, false);
		}
		
	}
	private class RemoveOperation extends AbstractOperation
	{

		
		public RemoveOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "removeResourceAdaptorEntity";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			
			super.addArg(opts.getOptarg(), String.class, false);
		}
		
	}
	private class CreateOperation extends AbstractOperation
	{
		
		//TODO: make config param optional
		private static final char config='i';
		private static final char ra_id='j';
		
		private String stringEntityName;
		private String stringConfig;
		private String stringRaID;
		public CreateOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "createResourceAdaptorEntity";
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

				case BindOperation.ra_entity_name:

					this.stringEntityName = opts.getOptarg();
					break;
				case config:
					this.stringConfig = opts.getOptarg();
					
					break;
				case ra_id:
					this.stringRaID = opts.getOptarg();
					
					break;
					

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}
			if (this.stringEntityName == null || this.stringConfig == null || this.stringRaID == null) {
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", expects all \"--config\" ,\"--entity-name\" and \"--ra-id\" to be present");
			}
			try {
				super.addArg(this.stringRaID, ResourceAdaptorID.class, true);
			} catch (Exception e) {
				throw new CommandException("Failed to parse ResourceAdaptorID: \"" + stringRaID + "\"", e);
			}
			
			super.addArg(this.stringEntityName, String.class, false);
			
			try {
				super.addArg(this.stringConfig, ConfigProperties.class, true);
			} catch (Exception e) {
				throw new CommandException("Failed to parse ConfigProperties: \"" + stringConfig + "\"", e);
			}
		}
		
	}
	
	private class ListOperation extends AbstractOperation
	{

		private static final char sbbs='s';
		private static final char links='k';
		private static final char ra_entities_by_link='e';
		private static final char ra_entities_in_state='x';
		private static final char ra_entities='q';

		public ListOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//operation is set based on arg.
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;

			while ((code = opts.getopt()) != -1) {
				if (super.operationName != null) {
					throw new CommandException("Command: \"" + sleeCommand.getName()
							+ "\", expects only one option!");
				}
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

			
				case sbbs:
					super.operationName = "getBoundSbbs";
					String optArg = opts.getOptarg(); // arg is mandatory
					super.addArg(optArg, String.class, false);
					break;
				case links:
					super.operationName = "getLinkNames";
					optArg = opts.getOptarg();
					if(optArg!=null)
					{
						//it must be link name
						super.addArg(optArg, String.class, false);
					}
					break;
				case ra_entities:
					super.operationName = "getResourceAdaptorEntities";
					 optArg = opts.getOptarg();
					if(optArg!=null)
					{
						//it must be RA ID
						try {
							super.addArg(optArg, ResourceAdaptorID.class, true);
						} catch (Exception e) {
							throw new CommandException("Failed to parse ResourceAdaptorID: \"" + optArg + "\"", e);
						}
					}
					break;
					
				case ra_entities_by_link:
					
					 optArg = opts.getOptarg();
					 if(optArg.contains(";"))
					 {
						 super.operationName = "getResourceAdaptorEntities";
						 super.addArg(optArg.split(";"), String[].class, false);
					 }else
					 {
						 super.operationName = "getResourceAdaptorEntity";
						 super.addArg(optArg, String.class, false);
					 }
					break;
				case ra_entities_in_state:
					super.operationName = "getResourceAdaptorEntities";
					optArg = opts.getOptarg();
					try {
						super.addArg(optArg, ResourceAdaptorEntityState.class, true);
					} catch (Exception e) {
						throw new CommandException("Failed to parse ResourceAdaptorEntityState: \"" + optArg + "\"", e);
					}
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}
			if (super.operationName == null) {
				throw new CommandException("Command: \"" + sleeCommand.getName()
						+ "\", expects one option!");
			}
		}
		
	}
	private class GetOperation extends AbstractOperation
	{
		private static final char usage_mbean='z';
		private static final char config_by_name='n';
		private static final char config_by_id='m';
		private static final char state='o';

		public GetOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//super.operationName = "removeResourceAdaptorEntity";
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;

			while ((code = opts.getopt()) != -1) {
				if (super.operationName != null) {
					throw new CommandException("Command: \"" + sleeCommand.getName()
							+ "\", expects only one option!");
				}
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1]);

				case CreateOperation.ra_id:
					super.operationName = "getResourceAdaptor";
					String optArg = opts.getOptarg();
					//it must be entity name
					super.addArg(optArg, String.class, false);
					break;
				case state:
					super.operationName = "getState";
					optArg = opts.getOptarg();
					//it must be entity name
					super.addArg(optArg, String.class, false);
					break;
				case config_by_id:
					super.operationName = "getConfigurationProperties"; 
					optArg = opts.getOptarg();
					//it must be RA ID
					try {
						super.addArg(optArg, ResourceAdaptorID.class, true);
					} catch (Exception e) {
						throw new CommandException("Failed to parse ResourceAdaptorID: \"" + optArg + "\"", e);
					}
					break;
				case config_by_name:
					super.operationName = "getConfigurationProperties"; 
					optArg = opts.getOptarg();
					//it must be RA entity name
	
					super.addArg(optArg, String.class, false);
					break;
				case usage_mbean:
					super.operationName = "getResourceUsageMBean"; 
					optArg = opts.getOptarg();
					//it must be RA entity name
	
					super.addArg(optArg, String.class, false);
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}
			
			if (super.operationName == null) {
				throw new CommandException("Command: \"" + sleeCommand.getName()
						+ "\", expects one option!");
			}
		}
		
	}
	
	private class UpdateConfigOperation extends AbstractOperation
	{
		
		private String stringEntityName;
		private String stringConfig;

		public UpdateConfigOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			super.operationName = "updateConfigurationProperties";
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

				case BindOperation.ra_entity_name:

					this.stringEntityName = opts.getOptarg();
					break;
				case CreateOperation.config:
					this.stringConfig = opts.getOptarg();
					
					break;
				

				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}

			}
			if (this.stringEntityName == null || this.stringConfig == null ) {
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", expects both \"--config\"  and \"--entity-name\" to be present");
			}
			
			
			super.addArg(this.stringEntityName, String.class, false);
			
			try {
				super.addArg(this.stringConfig, ConfigProperties.class, true);
			} catch (Exception e) {
				throw new CommandException("Failed to parse ConfigProperties: \"" + stringConfig + "\"", e);
			}
		}
		
	}
	
}
