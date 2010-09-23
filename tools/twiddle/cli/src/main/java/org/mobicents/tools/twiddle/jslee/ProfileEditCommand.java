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
import java.util.HashSet;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;
import org.mobicents.tools.twiddle.Utils;
import org.mobicents.tools.twiddle.op.AbstractOperation;

/**
 * Command performs operation on specific profile object.
 * @author baranowb
 *
 */
public class ProfileEditCommand extends AbstractSleeCommand {
	//TODO: add bussines method calls
	
	private ObjectName PROFILE_PROVISIONING_MBEAN;
	/**
	 * @param name
	 * @param desc
	 */
	public ProfileEditCommand() {
		super("profile.edit", "This command performs operations on JSLEE Profile MBean like: javax.slee.profile:type=Profile,profileTableName=CallControl,profileName=");
		
		try {
			this.PROFILE_PROVISIONING_MBEAN = new ObjectName(Utils.SLEE_PROFILE_PROVISIONING); //damn annoying.
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.tools.twiddle.AbstractSleeCommand#displayHelp()
	 */
	@Override
	public void displayHelp() {
		PrintWriter out = context.getWriter();

		out.println(desc);
		out.println();
		//out.println("usage: " + name + " <-tprofileTableName> [-pprofileName] <operation> <arg>*");
		out.println("usage: " + name + " <profileTableName> <profileName> <-operation[[arg] | [--option[=arg]]]*>");
		//out.println("This command requires atleast option -t to specify table name. If -p is not used, it performs operations on defualt profiles.");
		out.println();
		//out.println("config:");
		//out.println("    -t, --table                    Specifies table name from which profile should be accessed. Requires table name as argument.");
		//out.println("    -p, --profile                  Specifies profile name of profile to be accessed. Requires profile name as argument.");
		out.println("operation:");
		out.println("    -l, --list                     Returns list of available attributes and information associated with each.");
		out.println("    -d, --dirty                    Returns indication if profile is dirty. Does not require argument.");
		//isProfileDirty()
		out.println("    -w, --write                    Returns indication if profile is in write mode. Does not require argument.");
		//isProfileWriteable()
		out.println("    -e, --edit                     Marks profile as editable. Without this op any other will fail. Does not require argument.");
		//editProfile()
		out.println("    -c, --commit                   Commits changes done to profile. Can be invoked only after \"-e\". Does not require argument.");
		//commitProfile()
		out.println("    -r, --restore                  Revokes changes done to profile. Can be invoked only after \"-e\". Does not require argument.");
		//restoreProfile()
		out.println("    -o, --close                    De-registers MBean. Does not require argument.");
		//closeProfile()
		out.println("    -g, --get                      Returns value of profile attribute. Requires attribute name, ie. : \"voiceMailEnabled\".");
		out.println("    -s, --set                      Sets value of profile attribute. Supports mandatory options:.");
		out.println("         --name                    Specifies name of profile attribute. Requires attribute name as parameter.");
		out.println("         --value                   Specifies string representation of profile attribute value. Requires value as parameter.");
		out.println("                                   Command tries locally registered JMX Editor to parse value and optimize call, if editor is not found");
		out.println("                                   it dispatches call in hope that server side has better luck.");
		//out.println("    -b, --business                 Sets value of profile attribute. Supports mandatory options:.");
		//out.println("         --method                  Specifies business method name. Requires method name as parameter.");
		//out.println("         --name                    Specifies name of profile attribute. Requires attribute name as parameter.");
		//out.println("         --value                   Specifies string representation of profile attribute value. Requires value as parameter.");
		//out.println("                                   Command tries locally registered JMX Editor to parse value and optimize call, if editor is not found");
		//out.println("                                   it dispatches call in hope that server side has better luck.");
		out.println("Examples: ");
		out.println("");
		out.println("     1. Check if profile has been edited:");
		out.println("" + name + " CallControl mobile.user -w");
		out.println("");
		out.println("     2. Check if any changes were introduced to profile:");
		out.println("" + name + " CallControl mobile.user -d");
		out.println("");
		out.println("     3. Start editing:");
		out.println("" + name + " CallControl mobile.user -e");
		out.println("");
		out.println("     4. Set attribute:");
		out.println("" + name + " CallControl mobile.user -s --name=userPhone --value=sip:ala@ma.kota:5090");
		out.println("");
		out.println("     5. Commit changes:");
		out.println("" + name + " CallControl mobile.user -c");
		
		out.flush(); 
	}

	////////////////////////
	// Command essentials //
	////////////////////////
	
	private static final String DEFAULT_GET_OPERATION ="getDefaultProfile"; 
	private static final String GET_OPERATION ="getProfile"; 
	
	
	//object name for profile
	private ObjectName specificObjectName;
	//table name
	private String profileTableName;
	//profile name
	private String profileName;
	//info about bean. we will use it to get type of args, check editors etc.
	private MBeanInfo beanInfo;
	////////////////////////
	// Set     essentials //
	////////////////////////
	
	//list of fields, so we know what type they are?
	
	
	/* (non-Javadoc)
	 * @see org.mobicents.tools.twiddle.AbstractSleeCommand#getBeanOName()
	 */
	@Override
	public ObjectName getBeanOName() throws MalformedObjectNameException, NullPointerException {
		//tricky, its kind of dynamic :)
		//we get it from ProfileProvisioningMBean, we could create it by hand, but there is no need for that, since 
		//user calls this, it indicates there is interest in editing
		
		return specificObjectName;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.tools.twiddle.AbstractSleeCommand#processArguments(java.lang.String[])
	 */
	@Override
	protected void processArguments(String[] args) throws CommandException {
		//first, we must get atleast -t, possibly -p, depending on those we call different method on ProfileProvisioningMBean
		//String sopts = ":t:p:ldwecrog:s";
		String sopts = "-:ldwecrog:s";
	
		LongOpt[] lopts = { 
				//conf part
				//new LongOpt("table", LongOpt.REQUIRED_ARGUMENT, null, 't'),
				//new LongOpt("profile", LongOpt.REQUIRED_ARGUMENT, null, 'p'),
				 new LongOpt("noprefix", LongOpt.NO_ARGUMENT, null, 0x1000),
				//operration part
				new LongOpt("list", LongOpt.NO_ARGUMENT, null, 'l'),
				new LongOpt("dirty", LongOpt.NO_ARGUMENT, null, 'd'),
				new LongOpt("write", LongOpt.NO_ARGUMENT, null, 'w'),
				new LongOpt("edit", LongOpt.NO_ARGUMENT, null, 'e'),
				new LongOpt("commit", LongOpt.NO_ARGUMENT, null, 'c'),
				new LongOpt("restore", LongOpt.NO_ARGUMENT, null, 'r'),
				new LongOpt("close", LongOpt.NO_ARGUMENT, null, 'o'),
				//get,set,bussines
				new LongOpt("get", LongOpt.REQUIRED_ARGUMENT, null, 'g'),
				new LongOpt("set", LongOpt.NO_ARGUMENT, null, 's'),
					//options for set
					new LongOpt("name", LongOpt.REQUIRED_ARGUMENT, null, SetAttributeOperation.name),
					new LongOpt("value", LongOpt.OPTIONAL_ARGUMENT, null, SetAttributeOperation.value),
				//new LongOpt("bussines", LongOpt.NO_ARGUMENT, null, 'b'),	
				
					

				};

		Getopt getopt = new Getopt(null, args, sopts, lopts);
		getopt.setOpterr(false);
		int nonOptArgIndex = 0;
		int code;
		while ((code = getopt.getopt()) != -1) {
			switch (code) {
			case ':':
				throw new CommandException("Option requires an argument: " + args[getopt.getOptind() - 1]);

			case '?':
				throw new CommandException("Invalid (or ambiguous) option: " + args[getopt.getOptind() - 1]);

				//CONF PART
//			case 't':
//				this.profileTableName = getopt.getOptarg();
//				break;
//			case 'p':
//				this.profileName = getopt.getOptarg();
//				break;
				// OPERATION PART
			case 0x1000:
				break;
			case 1:
				//non opt args, table and profile name(maybe)
				switch(nonOptArgIndex)
				{
				case 0:
					profileTableName  = getopt.getOptarg();
					nonOptArgIndex++;
					break;
				case 1:
					profileName  = getopt.getOptarg();
					nonOptArgIndex++;
					break;
				default:
					throw new CommandException("Command: \"" + getName() + "\" expects at most two non opt arguments!");
				}
				break;
				
				
			case 'l':
				//list
				super.operation = new ListOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'd':
				//dirt
				super.operation = new SimpleInvokeOperation(super.context, super.log, this,"isProfileDirty");
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'w':
				//write
				super.operation = new SimpleInvokeOperation(super.context, super.log, this,"isProfileWriteable");
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'e':
				//edit
				super.operation = new SimpleInvokeOperation(super.context, super.log, this,"editProfile");
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'c':
				//comit
				super.operation = new SimpleInvokeOperation(super.context, super.log, this,"commitProfile");
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'r':
				//restore
				super.operation = new SimpleInvokeOperation(super.context, super.log, this,"restoreProfile");
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'o':
				//close
				super.operation = new SimpleInvokeOperation(super.context, super.log, this,"closeProfile");
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 'g':
				//get
				super.operation = new GetAttributeOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
			case 's':
				//set
				super.operation = new SetAttributeOperation(super.context, super.log, this);
				prepareCommand();
				super.operation.buildOperation(getopt, args);
				break;
//			case 'b':
//				//bussines
//				super.operation = new BussinesMethodOperation(super.context, super.log, this);
//				super.operation.buildOperation(getopt, args);
//				break;	
			
				
			default:
				throw new CommandException("Command: \"" + getName() + "\", found unexpected opt: " + args[getopt.getOptind() - 1]);

			}	
		}
	}
	
	private void prepareCommand() throws CommandException
	{
		//get bean name and bean info.
		if(profileTableName == null)
		{
			throw new CommandException("Command: \"" + getName() + "\", expects atleast \"-t\" to specify table name!");
		}
		String getOperationName;
		String[] sig;
		Object[] parms;
		MBeanServerConnection server = super.context.getServer();
		if(profileName == null)
		{
			//default;
			getOperationName = DEFAULT_GET_OPERATION;
			parms = new Object[]{profileTableName};
			sig = new String[]{"java.lang.String"};
		}else
		{
			getOperationName = GET_OPERATION;
			parms = new Object[]{profileTableName,profileName};
			sig = new String[]{"java.lang.String","java.lang.String"};
		}
		try {
			specificObjectName = (ObjectName) server.invoke(PROFILE_PROVISIONING_MBEAN, getOperationName, parms, sig);
		}catch (Exception e) {
			throw new CommandException("Command: \"" + getName()+"\" failed to obtain bean name for specified table name and profile. Table or profile does not exist.", e);
		} 
		
		//ok, now lets get bean info;
		try{
			this.beanInfo = server.getMBeanInfo(specificObjectName);
			
		}catch (Exception e) {
			throw new CommandException("Command: \"" + getName()+"\" failed to obtain bean name for specified table name and profile.", e);
		} 
	}
	
	private MBeanAttributeInfo findAttribute(String attr_name,
		      MBeanAttributeInfo[] attribute_info)
		   {
		      for (int i = 0; i < attribute_info.length; i++)
		      {
		         MBeanAttributeInfo mBeanAttributeInfo = attribute_info[i];
		         if (mBeanAttributeInfo.getName().equals(attr_name))
		            return mBeanAttributeInfo;
		      }
		      return null;
		   }
	
	private class ListOperation extends AbstractOperation {


		public ListOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//not set, since its complicated :)
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			//nothing
		}
		@Override
		public void invoke() throws CommandException {
			try {
				
				//we dont need to invoke anything, we have everything already :)
				displayResult();
			} catch (Exception e) {
				//add handle error here?
				throw new CommandException("Failed to invoke \"" + this.operationName + "\" due to: ", e);
			}
		}

		/* (non-Javadoc)
		 * @see org.mobicents.tools.twiddle.op.AbstractOperation#displayResult()
		 */
		@Override
		public void displayResult() {
			
			if (!context.isQuiet()) {
				Set<String> notDisplayed = new HashSet<String>();
				notDisplayed.add("ProfileDirty");
				notDisplayed.add("ProfileWriteable");
				// Translate the result to text
				String resultText = null;
				PrintWriter out = context.getWriter();
				//dont look at operationResult, we want info
			
				if (beanInfo != null) {
					MBeanAttributeInfo[] infos = beanInfo.getAttributes();
					for(MBeanAttributeInfo info:infos)
					{				
						if(notDisplayed.contains(info.getName()))
						{
							//skip
							continue;
						}
						out.println();
						//out.println("Class: "+info.getClass());
						out.println("Desc : "+info.getDescription());
						out.println("Name : "+info.getName());
						out.println("Type : "+info.getType());
						out.println("r/w  : "+info.isReadable()+"/"+info.isWritable());
						out.println("isIs : "+info.isIs()); // LOL :)
						
					}
				} else {
					resultText = "'success'";
					out.println(resultText);
				}
				// render results to out		
				out.flush();
			}
		}
		
	}
	/**
	 * Simple op to invoke some non arg methods.
	 * @author baranowb
	 *
	 */
	private class SimpleInvokeOperation extends AbstractOperation {


		public SimpleInvokeOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand,String operationName) {
			super(context, log, sleeCommand);
			super.operationName = operationName;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
		}
	}
	private class GetAttributeOperation extends AbstractOperation {

		//private String attributeName;
		//so we can access MBean info.
		private ProfileEditCommand editCommand;
		public GetAttributeOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//name is derived from type of attribute, usually its "get", but in case MBean info will reveal bool, it will be "is"
			//command for bean info
			this.editCommand = (ProfileEditCommand) sleeCommand;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			String attributeName =  opts.getOptarg();
			
			//now we have to build name of command.
			MBeanAttributeInfo info = findAttribute(attributeName, editCommand.beanInfo.getAttributes());
			if(info == null)
			{
				throw new CommandException("Attribute: "+attributeName+", does not exist in bean!");
			}
			if(!info.isReadable())
			{
				throw new CommandException("Attribute: "+attributeName+", is not readable!");
			}
			
			if(info.isIs())
			{
				super.operationName = "is"+attributeName;
			}else
			{
				super.operationName = "get"+attributeName;
			}
				
		}
	}
	private class SetAttributeOperation extends AbstractOperation {
		//TODO: allow no value to indicate set(null);
		public static final char name = 'n';
		public static final char value = 'v';
		
		private String attributeName;
		private String stringAttributeValue;
		//so we can access MBean info.
		private ProfileEditCommand editCommand;
		public SetAttributeOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand) {
			super(context, log, sleeCommand);
			//name is derived from type of attribute, usually its "get", but in case MBean info will reveal bool, it will be "is"
			//command for bean info
			this.editCommand = (ProfileEditCommand) sleeCommand;
		}

		@Override
		public void buildOperation(Getopt opts, String[] args) throws CommandException {
			int code;
			while ((code = opts.getopt()) != -1) {
				switch (code) {
				case ':':
					throw new CommandException("Option requires an argument: " + args[opts.getOptind() - 1]);

				case '?':
					throw new CommandException("Invalid (or ambiguous) option: " + args[opts.getOptind() - 1] + " --> " + opts.getOptopt());

				case name:
					attributeName = opts.getOptarg();
					break;
				case value:
					//this can be empty.
					stringAttributeValue = opts.getOptarg();
					break;
				default:
					throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
							+ "\", found unexpected opt: " + args[opts.getOptind() - 1]);

				}
			}
			
			if(attributeName == null)
			{
				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
						+ "\", requiers \"--name\".");
			}
//			if(stringAttributeValue == null)
//			{
//				throw new CommandException("Operation \"" + this.operationName + "\" for command: \"" + sleeCommand.getName()
//						+ "\", requiers \"--value\".");
//			}

			MBeanAttributeInfo info = findAttribute(attributeName, editCommand.beanInfo.getAttributes());
			
			//hmm
			super.operationName = "set"+info.getName();
			
			try{
				super.addArg(stringAttributeValue, info.getType(), true);
			}catch(CommandException ce)
			{
				//lets add, hope server knows how to handle
				super.addArg(stringAttributeValue, info.getType(), false);
			}
		}
	}
	
}
