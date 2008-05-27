package org.mobicents.slee.container.management.jmx.log;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.*;

import org.jboss.system.ServiceMBean;

public interface MobicentsLogManagerMBeanImplMBean extends ServiceMBean {

	// ========= PURE PROPS ========

	public void _setDefaultLoggerLevel(Level l);

	public Level _getDefaultLoggerLevel();

	public void setDefaultLoggerLevel(String l);

	public String getDefaultLoggerLevel();

	public void _setDefaultHandlerLevel(Level l);

	public Level _getDefaultHandlerLevel();

	public void setDefaultHandlerLevel(String l);

	public String getDefaultHandlerLevel();

	public void setDefaultNotificationInterval(int numberOfEntries);

	public int getDefaultNotificationInterval();

	// ========= END PROPS

	/**
	 * SImilar to LoggingMXBean, return list of available loggers. Filter is
	 * string that has to occur in loggers name.
	 * 
	 * @param -
	 *            specifies string that has to occur in loggers name, if null -
	 *            all names are returned. (Simply regex)
	 * @return
	 */
	public List<String> getLoggerNames(String regex);

	/**
	 * Same as LoggingMXBean - sets level for certain logger.
	 * 
	 * @param loggerName -
	 *            name of the logger
	 * @param level -
	 *            level to be set
	 * @throws IllegalArgumentException -
	 *             thrown when there is no logger under certain name.
	 */
	public void setLoggerLevel(String loggerName, Level level)
			throws IllegalArgumentException;

	/**
	 * Same as LoggingMXBean - sets level for certain logger. This method
	 * accepts String representation of logger level.
	 * 
	 * @param loggerName -
	 *            name of the logger
	 * @param level -
	 *            level to be set
	 * @throws IllegalArgumentException -
	 *             thrown when there is no logger under certain name or String
	 *             dose not represent valid logger level.
	 */
	public void setLoggerLevel(String loggerName, String level)
			throws IllegalArgumentException;

	public String getLoggerLevel(String loggerName)
			throws IllegalArgumentException;

	/**
	 * Resets all loggers level to default one
	 */
	public void resetLoggerLevels();

	/**
	 * Resets loggers under certain name to log on certain level.
	 * 
	 * @param loggerName
	 */
	public void resetLoggerLevel(String loggerName);

	/**
	 * Removes(set level to Level.OFF, handlers are removed) all loggers that
	 * were created by this MBean - this will usually include loggers for whole
	 * packages. It also removes all handlers - either NotificationHandler and
	 * SocketHandler for each defined logger(either by this MBean or by source
	 * code).
	 */
	public void clearLoggers();

	/**
	 * Removes all loggers under certain branch.
	 * 
	 * @param name -
	 *            logger name(branch name)
	 */
	public void clearLoggers(String name);

	/**
	 * Tries to add logger if it doesnt exist
	 * 
	 * @param name -
	 *            name of logger
	 * @param level -
	 *            level for this logger, if <b>null</b> than default level for
	 *            logger is used
	 * @return
	 *            <ul>
	 *            <li><b>true</b> - if logger didnt exist and it was created</li>
	 *            <li><b>false</b> - if logger did exist and it was not
	 *            created</li>
	 *            </ul>
	 * @throws NullPointerException -
	 *             when arg is null;
	 */
	public boolean addLogger(String name, Level level)
			throws NullPointerException;

	/**
	 * Tries to add logger if it doesnt exist
	 * 
	 * @param name -
	 *            name of logger
	 * @param level -
	 *            level for this logger, if <b>null</b> than default level for
	 *            logger is used
	 * @return
	 *            <ul>
	 *            <li><b>true</b> - if logger didnt exist and it was created</li>
	 *            <li><b>false</b> - if logger did exist and it was not
	 *            created</li>
	 *            </ul>
	 * 
	 * @throws IllegalArgumentException -
	 *             when level is not valid string representation of logging
	 *             level
	 * @throws NullPointerException -
	 *             when arg is null;
	 */
	public boolean addLogger(String name, String level)
			throws IllegalArgumentException, NullPointerException;

	/**
	 * Adds SocketHandler to certain logger, this logger must exist prior this
	 * function is called
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null
	 * @param handlerLevel -
	 *            level for this handler, if its null default level for handlers
	 *            will be used
	 * @param handlerName -
	 *            name for this handler, cant be duplicate.
	 * @param formaterClassName -
	 *            name of formater class for this handler, can be null.
	 * @param filterClassName -
	 *            name of filter class for this handler, can be null.
	 * @param host -
	 *            host address
	 * @param port -
	 *            port address
	 * @throws IllegalArgumentException -
	 *             thrown when:
	 *             <ul>
	 *             <li>host is not a valid inet address</li>
	 *             <li>port <0 </li>
	 *             </ul>
	 * @throws NullPointerException -
	 *             if arg other than:
	 *             <ul>
	 *             <li>formaterClassName</li>
	 *             <li>filterClassName</li>
	 *             <li>handlerLevel</li>
	 *             </ul>
	 *             is null.
	 * @throws IllegalStateException :-
	 *             thrown when:
	 *             <ul>
	 *             <li>Logger under certain name doesnt exist</li>
	 *             <li>handler name is duplicate of another handler for this
	 *             logger or is reserved one ->NOTIFICATION</li>
	 *             </ul>
	 * @throw IOException - when host cant be reached
	 */
	public void addSocketHandler(String loggerName, Level handlerLevel,
			String handlerName, String formaterClassName,
			String filterClassName, String host, int port)
			throws IllegalArgumentException, NullPointerException, IOException;

	/**
	 * Adds SocketHandler to certain logger, this logger must exist prior this
	 * function is called
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null
	 * @param handlerLevel -
	 *            level for this handler, if its null default level for handlers
	 *            will be used
	 * @param handlerName -
	 *            name for this handler, cant be duplicate.
	 * @param formaterClassName -
	 *            name of formater class for this handler, can be null.
	 * @param filterClassName -
	 *            name of filter class for this handler, can be null.
	 * @param host -
	 *            host address
	 * @param port -
	 *            port address
	 * @throws IllegalArgumentException -
	 *             thrown when:
	 *             <ul>
	 *             <li>host is not a valid inet address</li>
	 *             <li>port <0 </li>
	 *             <li> handlerLevel!=null and does not represent valid logging
	 *             level </li>
	 *             <li>formaterClassName is not valid formater class</li>
	 *             <li>filterClassName is not valid filter class</li>
	 *             </ul>
	 * @throws NullPointerException -
	 *             if arg other than:
	 *             <ul>
	 *             <li>formaterClassName</li>
	 *             <li>filterClassName</li>
	 *             <li>handlerLevel</li>
	 *             </ul>
	 *             is null.
	 * @throws IllegalStateException :-
	 *             thrown when:
	 *             <ul>
	 *             <li>Logger under certain name doesnt exist</li>
	 *             <li>handler name is duplicate of another handler for this
	 *             logger or is reserved one ->NOTIFICATION</li>
	 *             </ul>
	 * @throw IOException - when host cant be reached
	 */
	public void addSocketHandler(String loggerName, String handlerLevel,
			String handlerName, String formaterClassName,
			String filterClassName, String host, int port)
			throws IllegalArgumentException, NullPointerException, IOException;

	/**
	 * Tries to remove handler from logger.
	 * 
	 * @param loggerName -
	 *            name of the logger
	 * @param handlerName -
	 *            handler name that has been added by this MBean
	 * @return
	 *            <ul>
	 *            <li><b>true</b> - if logger exists, and it was removed</li>
	 *            <li><b>false</b> - otherwise</li>
	 *            </ul>
	 */
	public boolean removeHandler(String loggerName, String handlerName);

	public boolean removeHandler(String loggerName, int index);

	/**
	 * Adds notification handler to logger if it exists. Its name is set to
	 * <b>NOTIFICATION</b>. There can be only one notification handler. This
	 * handler holds reference to up to numberOfEntries log entries and fires
	 * notification. Notification can be triggered prematurely in case when
	 * someone calls fetchLogContent function, this will cause notification to
	 * be fired along with log entries return as outcome of invocation.
	 * 
	 * @param loggerName -
	 *            name of the logger, it must exists prior this function call
	 * @param numberOfEntries -
	 *            number of log entries after notification is sent. If <=0
	 *            default value is used
	 * @param level -
	 *            level for this handler, if null, default value is used
	 * @param formaterClassName -
	 *            name of formater class for this handler, can be null.
	 * @param filterClassName -
	 *            name of filter class for this handler, can be null.
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger under certain name does not exist</li>
	 *             <li>formaterClassName is not valid formater class</li>
	 *             <li>filterClassName is not valid filter class</li>
	 *             </ul>
	 * @throws IllegalStateException -
	 *             logger under that name already has notification handler
	 * @throws NullPointerException -
	 *             loggerName is null
	 */
	public void addNotificationHandler(String loggerName, int numberOfEntries,
			Level level, String formaterClassName, String filterClassName)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException;

	public void addNotificationHandler(String loggerName, int numberOfEntries,
			String level, String formaterClassName, String filterClassName)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException;

	/**
	 * Returns names of handlers for logger
	 * 
	 * @param loggerName -
	 *            logger name, cant be null, must be valid
	 * @return List with names of handlers for this loggers
	 * @throws IllegalArgumentException -
	 *             logger does not exist
	 */
	public List<String> getHandlerNamesForLogger(String loggerName)
			throws IllegalArgumentException;

	/**
	 * Fetches level of particular handler
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @param handlerName -
	 *            valid name of handler, cant be null
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>handler does not exist</li>
	 *             </ul>
	 * 
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>arg is null</li>
	 *             </ul>
	 */
	public String getHandlerLevel(String loggerName, String handlerName)
			throws IllegalArgumentException, NullPointerException;

	/**
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @param handlerName -
	 *            name of the handler, cant be null and handler ust exist
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not exist</li>
	 *             <li>handler under specific name does not exist </li>
	 *             </ul>
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>arg is null</li>
	 *             </ul>
	 */
	public String getHandlerFilterClassName(String loggerName,
			String handlerName) throws IllegalArgumentException,
			NullPointerException;

	/**
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @param handlerName -
	 *            name of the handler, cant be null and handler ust exist
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not exist</li>
	 *             <li>handler under specific name does not exist </li>
	 *             </ul>
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>arg is null</li>
	 *             </ul>
	 */
	public String getHandlerFormaterClassName(String loggerName,
			String handlerName) throws IllegalArgumentException,
			NullPointerException;;

	/**
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not exist</li>
	 *             </ul>
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>args is null</li>
	 * @throws IllegalStateException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not have notification handler</li>
	 *             </ul>
	 */
	public int getHandlerNotificationInterval(String loggerName)
			throws IllegalArgumentException, NullPointerException,
			IllegalStateException;

	public String getHandlerClassName(String loggerName, String handlerName)
			throws IllegalArgumentException, NullPointerException,
			IllegalStateException;

	public String getHandlerClassName(String loggerName, int index)
			throws IllegalArgumentException, NullPointerException,
			IllegalStateException;

	/**
	 * Fetches level of particular handler
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @param handlerName -
	 *            valid name of handler, cant be null
	 * @param level -
	 *            new handelr level
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>handler does not exist</li>
	 *             <li>logger does not exist</li>
	 *             <li>level is not valid string representation of logging
	 *             level</li>
	 *             </ul>
	 * 
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>arg is null</li>
	 *             </ul>
	 */
	public void setHandlerLevel(String loggerName, String handlerName,
			String level) throws IllegalArgumentException, NullPointerException;

	/**
	 * Fetches level of particular handler
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @param handlerName -
	 *            valid name of handler, cant be null
	 * @param level -
	 *            new handelr level
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>handler does not exist</li>
	 *             <li>logger does not exist</li>
	 *             </ul>
	 * 
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>arg is null</li>
	 *             </ul>
	 */
	public void setHandlerLevel(String loggerName, String handlerName,
			Level level) throws IllegalArgumentException, NullPointerException;

	/**
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @param handlerName -
	 *            name of the handler, cant be null and handler ust exist
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not exist</li>
	 *             <li>handler under specific name does not exist </li>
	 *             <li>className is not valid filter class name</li>
	 *             </ul>
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>args is null</li>
	 *             </ul>
	 */
	public void setHandlerFilterClassName(String loggerName,
			String handlerNamem, String className)
			throws IllegalArgumentException, NullPointerException;

	/**
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @param handlerName -
	 *            name of the handler, cant be null and handler ust exist
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not exist</li>
	 *             <li>handler under specific name does not exist </li>
	 *             <li>className is not a valid formater class name </li>
	 *             </ul>
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>args is null</li>
	 *             </ul>
	 */
	public void setHandlerFormaterClassName(String loggerName,
			String handlerName, String className)
			throws IllegalArgumentException, NullPointerException;;

	/**
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @return
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not exist</li>
	 *             </ul>
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>args is null</li>
	 * @throws IllegalStateException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not have notification handler</li>
	 *             </ul>
	 */
	public void setHandlerNotificationInterval(String loggerName,
			int numberOfEntries) throws IllegalArgumentException,
			NullPointerException, IllegalStateException;

	/**
	 * 
	 * @param loggerName -
	 *            name of the logger, cant be null and logger must exist
	 * @throws IllegalArgumentException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not exist</li>
	 *             </ul>
	 * @throws NullPointerException
	 *             thrown when:
	 *             <ul>
	 *             <li>args is null</li>
	 * @throws IllegalStateException
	 *             thrown when:
	 *             <ul>
	 *             <li>logger does not have notification handler</li>
	 *             </ul>
	 */
	public String fetchLog(String loggerName) throws IllegalArgumentException,
			IllegalStateException, NullPointerException;

	// ** METHODS BELOW ARE GENERIC, THEY ALLOW TO CONTROLL ALL HANDLERS
	/**
	 * Triggers reareadigng configuration file.
	 * 
	 * @param uri -
	 *            target configuration file in Properties format, if null, local
	 *            file is read(jre local)
	 */
	public void reReadConf(URI uri) throws IOException;

	/**
	 * Adds handler, for now some parameters are ignored.
	 * 
	 * @param loggerName
	 * @param handlerName
	 * @param handlerClassName
	 * @param handlerConstructorParameterTypes
	 * @param handlerConstructorParamValues
	 * @param formaterClass
	 * @param formatterConstructorParameterTypes -
	 *            ignored
	 * @param formatterConstructorParamValues -
	 *            ignored
	 * @param filterClass
	 * @param filterConstructorParameterTypes -
	 *            ignored
	 * @param filterConstructorParamValues -
	 *            ignored
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 */
	//public void addHandler(String loggerName, String handlerName,String handlerLevel,
	//		String handlerClassName, String[] handlerConstructorParameterTypes,
	//		String[] handlerConstructorParamValues, String formaterClass,
	//		String[] formatterConstructorParameterTypes,
	//		String[] formatterConstructorParamValues, String filterClass,
	//		String[] filterConstructorParameterTypes,
	//		String[] filterConstructorParamValues) throws NullPointerException,
	//		IllegalArgumentException, IllegalStateException;
	
	public void addHandler(String loggerName, String handlerName,
			String handlerLevel, String handlerClassName,
			Object handlerConstructorParameterTypes,
			Object handlerConstructorParamValues, String formaterClass,
			Object formatterConstructorParameterTypes,
			Object formatterConstructorParamValues, String filterClass,
			Object filterConstructorParameterTypes,
			Object filterConstructorParamValues) throws NullPointerException,
			IllegalArgumentException, IllegalStateException;
	
	
	
	public int numberOfHandlers(String loggerName) throws NullPointerException,
			IllegalArgumentException;

	public String getGenericHandlerLevel(String loggerName, int index)
			throws NullPointerException, IllegalArgumentException;

	public String getGenericHandlerFilterClassName(String loggerName, int index)
			throws NullPointerException, IllegalArgumentException;

	public String getGenericHandlerFormatterClassName(String loggerName,
			int index) throws NullPointerException, IllegalArgumentException;

	public void setGenericHandlerLevel(String loggerName, int index,
			String level) throws NullPointerException, IllegalArgumentException;

	public void setGenericHandlerFilterClassName(String loggerName, int index,
			String className) throws NullPointerException,
			IllegalArgumentException;

	public void setGenericHandlerFormatterClassName(String loggerName,
			int index, String className) throws NullPointerException,
			IllegalArgumentException;

	public String getHandlerName(String loggerName, int index);

	public void setHandlerName(String loggerName, int index, String newName);

	public void setUseParentHandlersFlag(String loggerName, boolean flag);

	public boolean getUseParentHandlersFlag(String handlerName);

	public String getLoggerFilterClassName(String loggerName)
			throws IllegalArgumentException;

	//public void setLoggerFilterClassName(String loggerName, String className,
	//		String[] constructorParameters, String[] paramValues)
	//		throws NullPointerException, IllegalArgumentException,
	//		IllegalStateException;
	
	public void setLoggerFilterClassName(String loggerName, String className,
			Object constructorParameters, Object paramValues)
			throws NullPointerException, IllegalArgumentException,
			IllegalStateException;
}
