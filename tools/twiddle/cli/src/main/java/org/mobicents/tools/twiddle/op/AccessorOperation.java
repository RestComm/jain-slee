package org.mobicents.tools.twiddle.op;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gnu.getopt.Getopt;

import org.jboss.console.twiddle.command.CommandContext;
import org.jboss.console.twiddle.command.CommandException;
import org.jboss.logging.Logger;
import org.mobicents.tools.twiddle.AbstractSleeCommand;

/**
 * Simple class for commom part of set/get ops. It should be used in case of
 * simple, bean like setters. User should not associate 'g' and 's' chars for
 * Getopt since those are reserved by this op.
 * 
 * @author baranowb
 * 
 */
public class AccessorOperation extends AbstractOperation {

	public static final char set = 's';
	public static final char get = 'g';
	/**
	 * Name of bean field, it will be used to create get/set method name.
	 */
	protected String beanFieldName;
    protected Class<?> fieldClass;
	protected boolean usJMXEditors;
	
    public AccessorOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand, String beanFieldName,Class<?> fieldClass, boolean useJMXEditors) {
		super(context, log, sleeCommand);
		this.beanFieldName = beanFieldName;
		if(Character.isLowerCase(this.beanFieldName.charAt(0)))
		{
			//TODO:replace first char. 
			char c = this.beanFieldName.charAt(0);
			c = Character.toUpperCase(c);
			StringBuffer sb =  new StringBuffer();
			sb.append(c);
			sb.append(this.beanFieldName.substring(1));
			this.beanFieldName = sb.toString();
		}
		this.fieldClass = fieldClass;
		this.usJMXEditors = useJMXEditors;

	}
    public AccessorOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand, String beanFieldName,Class<?> fieldClass) {
		this(context, log, sleeCommand, beanFieldName,fieldClass,false);
		

	}
    public AccessorOperation(CommandContext context, Logger log, AbstractSleeCommand sleeCommand, String beanFieldName) {
		this(context, log, sleeCommand, beanFieldName,null,false);
		

	}
    /**
     * User should overide it to provide different name, for instance for boolean \"is\" prefix
     */
	protected void makeGetter() {
		if(fieldClass.equals(boolean.class) || fieldClass.equals(Boolean.class))
		{
			super.operationName = "is" + this.beanFieldName;
		}else
		{
			super.operationName = "get" + this.beanFieldName;
		}
	}


	protected void makeSetter() {
		super.operationName = "set" + this.beanFieldName;
	}
	/**
	 * This method is called to convert optArg from string form, if no  conversion is needed it should return passed object.
	 * @param optArg
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 * @throws CommandException 
	 */
	protected Object convert(String optArg) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException, CommandException {
		if (fieldClass.isPrimitive()) {
	
			if (fieldClass.equals(int.class)) {
				return new Integer(optArg);
			} else if (fieldClass.equals(long.class)) {
				return new Long(optArg);
			} else if (fieldClass.equals(int.class)) {
				return new Integer(optArg);
			} else if (fieldClass.equals(byte.class)) {
				return new Byte(optArg);
			} else if (fieldClass.equals(short.class)) {
				return new Short(optArg);
			} else if (fieldClass.equals(float.class)) {
				return new Float(optArg);
			} else if (fieldClass.equals(double.class)) {
				return new Double(optArg);
			} else if (fieldClass.equals(boolean.class)) {
				return new Boolean(optArg);
			} else if(fieldClass.equals(char.class)){return new Character(optArg.charAt(0));} //?

			throw new CommandException("Unpredicted place. Please report.");
		} else if (isClassNumber()) {
			//Handle Long,Integer,....,Boolean
			Constructor<?> con = fieldClass.getConstructor(String.class);
			return con.newInstance(optArg);
		}

		return optArg;

	}
	
	private boolean isClassNumber()
	{
		@SuppressWarnings("rawtypes")
		Class[] all = fieldClass.getClasses();
		
		for(Class<?> c:all)
		{
			if(c.equals(Number.class) || c.equals(Boolean.class))
			{
				return true;
			}
		}
		return false;
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

				makeSetter();
				optArg = opts.getOptarg();
				try {
					addArg(convert(optArg), fieldClass, usJMXEditors);
				} catch (Exception e) {
					throw new CommandException("Failed to parse Integer: \"" + optArg + "\"", e);
				}

				break;
			case get:
				makeGetter();
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
