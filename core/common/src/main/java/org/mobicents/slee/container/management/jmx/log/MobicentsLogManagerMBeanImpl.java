/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.management.jmx.log;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;

import java.util.regex.Pattern;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;

import javax.management.NotificationFilter;
import javax.management.NotificationListener;

import org.jboss.mx.util.JBossNotificationBroadcasterSupport;
import org.jboss.system.ServiceMBeanSupport;

public class MobicentsLogManagerMBeanImpl extends ServiceMBeanSupport implements
		MobicentsLogManagerMBeanImplMBean, NotificationBroadcaster {

	public static final String _NOTIFICATION_HANDLER_NAME = "NOTIFICATION";

	private final static Logger logger = Logger
			.getLogger(MobicentsLogManagerMBeanImpl.class.getCanonicalName());

	private HashMap<String, HashMap<String, Handler>> handlers = new HashMap<String, HashMap<String, Handler>>();

	private LogManager lManager = null;

	private LoggingMXBean bean = null;

	private Level defaultLoggerLevel = Level.INFO;

	private Level defaultHandlerLevel = Level.INFO;

	private int defultNotificationInterval = 150;

	public static final String _DEFAULT_MC_DOMAIN = "org.mobicents";

	// TODO: Remember old levels so we can reset to those values

	public boolean addLogger(String name, Level level) {

		if (name == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (this.bean.getLoggerNames().contains(name))
			return false;

		Level toSetLevel = level;
		if (toSetLevel == null)
			toSetLevel = this.defaultLoggerLevel;

		Logger add = Logger.getLogger(name);

		add.setLevel(toSetLevel);


		return true;
	}

	public boolean addLogger(String name, String level)
			throws IllegalArgumentException {

		Level parsed = null;
		if (level == null || level.equals(""))
			parsed = this.defaultLoggerLevel;
		else
			parsed = Level.parse(level);

		return this.addLogger(name, parsed);
	}

	public void addSocketHandler(String loggerName, Level handlerLevel,
			String handlerName, String formaterClassName,
			String filterClassName, String host, int port)
			throws IllegalArgumentException, NullPointerException,
			IllegalStateException, IOException {

		// With socket handlers we still want to use parent handlers... ;]

		// NPE SECTION
		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");
		if (handlerName == null)
			throw new NullPointerException("Handler name cant be null!!!");
		if (host == null)
			throw new NullPointerException("Host cant be null!!!");

		// IllegalArgument SECTION
		if (port <= 0)
			throw new IllegalArgumentException("Port cant be <=0");
		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException("No logger under this name!!!!");
		// TODO: add parse to host!!!!

		// STATE SECTION

		if (handlerName.equals(this._NOTIFICATION_HANDLER_NAME)
				|| (this.handlers.get(loggerName) != null && this.handlers.get(
						loggerName).containsKey(handlerName)))
			throw new IllegalStateException(
					"Duplicate or illegal handler name for this logger!!!");

		Level toSetLevel = handlerLevel;
		if (toSetLevel == null)
			toSetLevel = this.defaultHandlerLevel;

		// Dynamic classes load
		Filter filter = null;
		Formatter formatter = null;
		if (filterClassName != null && !filterClassName.equals("")) {
			Class filterClass = null;
			try {
				filterClass = this.getClass().forName(filterClassName);
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
				throw new IllegalArgumentException("Filter class["
						+ filterClassName + "] can not be found!!!");
			}
			try {
				Object o = filterClass.newInstance();
				filter = (Filter) o;
			} catch (Exception e) {
				throw new IllegalArgumentException("Cant create Filter class",
						e);
			}
		}

		if (formaterClassName != null && !formaterClassName.equals("")) {
			Class foratterClass = null;
			try {
				foratterClass = this.getClass().forName(formaterClassName);
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
				throw new IllegalArgumentException("Formatter class["
						+ filterClassName + "] can not be found!!!");
			}
			try {
				Object o = foratterClass.newInstance();
				formatter = (Formatter) o;
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Cant create Formatter class", e);
			}
		}
		SocketHandler handler = null;

		handler = new SocketHandler(host, port);
		handler.setLevel(toSetLevel);
		if (formatter != null)
			handler.setFormatter(formatter);
		if (filter != null)
			handler.setFilter(filter);

		if (this.handlers.get(loggerName) == null)
			this.handlers.put(loggerName, new HashMap<String, Handler>());
		Logger log = lManager.getLogger(loggerName);

		if (log != null) {
			log.addHandler(handler);
			this.handlers.get(loggerName).put(handlerName, handler);
		} else {
			handler.close();
		}

	}

	public void addSocketHandler(String loggerName, String handlerLevel,
			String handlerName, String formaterClassName,
			String filterClassName, String host, int port)
			throws IllegalArgumentException, NullPointerException,
			IllegalStateException, IOException {

		Level parsed = null;
		if (handlerLevel == null || handlerLevel.equals(""))
			parsed = this.defaultHandlerLevel;
		else
			parsed = Level.parse(handlerLevel);
		this.addSocketHandler(loggerName, parsed, handlerName,
				formaterClassName, filterClassName, host, port);

	}

	// public void addHandler(String loggerName, String handlerName,String
	// handlerLevel,
	// String handlerClassName, String[] handlerConstructorParameterTypes,
	// String[] handlerConstructorParamValues, String formaterClass,
	// String[] formatterConstructorParameterTypes,
	// String[] formatterConstructorParamValues, String filterClass,
	// String[] filterConstructorParameterTypes,
	// String[] filterConstructorParamValues) throws NullPointerException,
	// IllegalArgumentException, IllegalStateException {

	public void addHandler(String loggerName, String handlerName,
			String handlerLevel, String handlerClassName,
			Object handlerConstructorParameterTypes,
			Object handlerConstructorParamValues, String formaterClass,
			Object formatterConstructorParameterTypes,
			Object formatterConstructorParamValues, String filterClass,
			Object filterConstructorParameterTypes,
			Object filterConstructorParamValues) throws NullPointerException,
			IllegalArgumentException, IllegalStateException {

		// TEST

		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (handlerName == null)
			throw new NullPointerException("Handler name cant be null!!!");

		if (handlerClassName == null)
			throw new NullPointerException("Handler class name cant be null!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException("Logger with name ["
					+ loggerName + "] does not exist!!!");

		if (handlerLevel == null || handlerLevel.equals(""))
			handlerLevel = this.defaultHandlerLevel.toString();

		NotificationHandler nh = null;
		if (handlerName.equals(this._NOTIFICATION_HANDLER_NAME)) {

			try {
				nh = (NotificationHandler) getHandlerByName(loggerName,
						this._NOTIFICATION_HANDLER_NAME);
			} catch (IllegalStateException ies) {

			}

			if (nh != null) {
				throw new IllegalStateException(
						"Cant create notification handler while one still exists!!!!");
			}
		}

		Handler h = null;



		if (handlerConstructorParameterTypes != null
				&& handlerConstructorParamValues != null
				&& ((String[]) handlerConstructorParamValues).length > 0
				&& ((String[]) handlerConstructorParamValues).length == ((String[]) handlerConstructorParameterTypes).length) {
			

			h = (Handler) generateClassInstance(handlerClassName,
					(String[]) handlerConstructorParameterTypes,
					(String[]) handlerConstructorParamValues);
		} else {
			h = (Handler) generateClassInstance(handlerClassName);
		}

		if (h instanceof NotificationHandler && nh != null)
			throw new IllegalStateException(
					"Cant create notification handler while one still exists!!!!");

		if (formaterClass != null && !formaterClass.equals("")) {
			Formatter form = null;
			form = (Formatter) generateClassInstance(formaterClass);
			h.setFormatter(form);
		}

		if (filterClass != null && !filterClass.equals("")) {
			Filter fil = null;
			fil = (Filter) generateClassInstance(filterClass);
			h.setFilter(fil);
		}

		if (this.handlers.get(loggerName) == null)
			this.handlers.put(loggerName, new HashMap<String, Handler>());
		Logger log = lManager.getLogger(loggerName);

		if (log != null) {
			h.setLevel(Level.parse(handlerLevel));
			log.addHandler(h);
			this.handlers.get(loggerName).put(handlerName, h);
		} else {
			h.close();
		}

	}

	public void addNotificationHandler(String loggerName, int numberOfEntries,
			Level level, String formaterClassName, String filterClassName)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException {

		NotificationHandler nh = null;

		try {
			nh = (NotificationHandler) getHandlerByName(loggerName,
					this._NOTIFICATION_HANDLER_NAME);
		} catch (IllegalStateException ise) {
			// This is good, it says "THERE IS NO NH"
		}

		if (nh != null)
			throw new IllegalStateException(
					"Logger already has notification handler!!!");

		if(numberOfEntries<=0)
			numberOfEntries=defultNotificationInterval;
		
		nh = new NotificationHandler(numberOfEntries, this, loggerName);

		if (this.handlers.get(loggerName) == null)
			this.handlers.put(loggerName, new HashMap<String, Handler>());
		Logger log = lManager.getLogger(loggerName);

		if (log != null) {
			log.addHandler(nh);
			this.handlers.get(loggerName).put(this._NOTIFICATION_HANDLER_NAME,
					nh);
		} else {
			nh.close();
		}

	}

	public void addNotificationHandler(String loggerName, int numberOfEntries,
			String level, String formaterClassName, String filterClassName)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException {

		Level parsed = null;
		if (level == null || level.equals(""))
			parsed = this.defaultLoggerLevel;
		else
			parsed = Level.parse(level);
		this.addNotificationHandler(loggerName, numberOfEntries, parsed,
				formaterClassName, filterClassName);
	}

	public void clearLoggers() {

		// We dont want to clear loggers other than org.mobicents.*
		this.clearLoggers(_DEFAULT_MC_DOMAIN);
	}

	public void clearLoggers(String name) {

		Enumeration<String> names = this.lManager.getLoggerNames();

		while (names.hasMoreElements()) {
			String loggerName = names.nextElement();
			if (loggerName.startsWith(name)) {

				Logger l = this.lManager.getLogger(loggerName);
				l.setLevel(Level.OFF);
				// Now remove handlers
				HashMap<String, Handler> namedHandlers = this.handlers
						.get(loggerName);

				// Clear names if any
				if (namedHandlers != null)
					namedHandlers.clear();

				// Remove handlers
				for (Handler h : l.getHandlers()) {
					l.removeHandler(h);
					try {
						h.flush();
						h.close();

					} catch (Exception e) {
					}
				}

			}

		}

	}

	public String fetchLog(String loggerName) throws IllegalArgumentException,
			IllegalStateException, NullPointerException {

		NotificationHandler nh = (NotificationHandler) getHandlerByName(
				loggerName, this._NOTIFICATION_HANDLER_NAME);
		return nh.fetchLog();
	}

	public Level _getDefaultHandlerLevel() {

		return this.defaultHandlerLevel;
	}

	public Level _getDefaultLoggerLevel() {

		return this.defaultLoggerLevel;
	}

	public int getDefaultNotificationInterval() {

		return this.defultNotificationInterval;
	}

	public String getGenericHandlerFilterClassName(String loggerName, int index)
			throws NullPointerException, IllegalArgumentException {

		Handler h = getHandlerByIndex(loggerName, index);

		Filter f = h.getFilter();
		if (f == null) {
			return null;
		}

		return f.getClass().getCanonicalName();

	}

	public String getGenericHandlerFormatterClassName(String loggerName,
			int index) throws NullPointerException, IllegalArgumentException {

		Handler h = getHandlerByIndex(loggerName, index);

		java.util.logging.Formatter f = h.getFormatter();

		if (f == null)
			return null;

		return f.getClass().getCanonicalName();
	}

	public String getGenericHandlerLevel(String loggerName, int index)
			throws NullPointerException, IllegalArgumentException {

		Handler h = getHandlerByIndex(loggerName, index);

		return h.getLevel().toString();
	}

	public String getHandlerClassName(String loggerName, int index)
			throws IllegalArgumentException, NullPointerException,
			IllegalStateException {

		Handler h = getHandlerByIndex(loggerName, index);

		return h.getClass().getCanonicalName();
	}

	public String getHandlerClassName(String loggerName, String handlerName)
			throws IllegalArgumentException, NullPointerException,
			IllegalStateException {

		Handler namedHandler = getHandlerByName(loggerName, handlerName);

		return namedHandler.getClass().getCanonicalName();
	}

	public String getHandlerFilterClassName(String loggerName,
			String handlerName) throws IllegalArgumentException,
			NullPointerException {

		Handler namedHandler = getHandlerByName(loggerName, handlerName);

		Filter f = namedHandler.getFilter();

		if (f == null)
			return null;

		return f.getClass().getCanonicalName();
	}

	public String getHandlerFormaterClassName(String loggerName,
			String handlerName) throws IllegalArgumentException,
			NullPointerException {

		Handler namedHandler = getHandlerByName(loggerName, handlerName);

		java.util.logging.Formatter f = namedHandler.getFormatter();

		if (f == null)
			return null;

		return f.getClass().getCanonicalName();
	}

	public String getHandlerLevel(String loggerName, String handlerName)
			throws IllegalArgumentException, NullPointerException {

		Handler namedHandler = getHandlerByName(loggerName, handlerName);
		return namedHandler.getLevel().toString();
	}

	public String getHandlerName(String loggerName, int index) {

		Handler h = getHandlerByIndex(loggerName, index);

		if (this.handlers.get(loggerName) == null
				|| !this.handlers.get(loggerName).containsValue(h))
			return null;
		else {
			HashMap<String, Handler> hand = this.handlers.get(loggerName);
			for (String name : hand.keySet()) {
				Handler named = hand.get(name);

				if (named == h) {

					return name;
				}
			}

		}

		return null;
	}

	public List<String> getHandlerNamesForLogger(String loggerName)
			throws IllegalArgumentException {

		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException(
					"Logger with this name does not exist!!!");

		if (this.handlers.get(loggerName) == null)
			return new ArrayList<String>();

		Logger l = this.lManager.getLogger(loggerName);
		ArrayList<String> result = new ArrayList<String>();
		if (this.handlers.get(loggerName) == null)
			return result;
		for (int i = 0; i < l.getHandlers().length; i++) {

			String name = this.getHandlerName(loggerName, i);

			if (name != null)
				result.add(name);
			name = null;
		}

		if (result.size() != this.handlers.get(loggerName).keySet().size()) {
			// TODO: is this correct?
			this.handlers.get(loggerName).keySet().retainAll(result);
		}

		return result;
	}

	public int getHandlerNotificationInterval(String loggerName)
			throws IllegalArgumentException, NullPointerException,
			IllegalStateException {

		NotificationHandler nh = (NotificationHandler) getHandlerByName(
				loggerName, this._NOTIFICATION_HANDLER_NAME);

		return nh.getNotificationInterval();

	}

	public List<String> getLoggerNames(String regex) {

		List<String> result = new ArrayList<String>();
		boolean addAll = false;
		if (regex == null || regex.equals(""))
			addAll = true;

		for (String s : this.bean.getLoggerNames()) {
			if (!addAll && Pattern.matches(regex, s)) {

				result.add(s);
			} else if (addAll) {

				result.add(s);
			}
		}
		String[] o = result.toArray(new String[1]);
		Arrays.sort(o);
		List<String> tmpList = Arrays.asList(o);
		result.clear();
		result.addAll(tmpList);
		result.remove("");
		return result;
	}

	public String getDefaultHandlerLevel() {

		return this.defaultHandlerLevel.toString();
	}

	public String getDefaultLoggerLevel() {

		return this.defaultLoggerLevel.toString();
	}

	public boolean getUseParentHandlersFlag(String loggerName) {

		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException(
					"Logger with this name does not exist!!!");

		return this.lManager.getLogger(loggerName).getUseParentHandlers();
	}

	public int numberOfHandlers(String loggerName) throws NullPointerException,
			IllegalArgumentException {

		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException(
					"Logger with this name does not exist!!!");

		return this.lManager.getLogger(loggerName).getHandlers().length;
	}

	public boolean removeHandler(String loggerName, String handlerName) {

		try {
			Handler h = getHandlerByName(loggerName, handlerName);
			Logger l = this.lManager.getLogger(loggerName);
			l.removeHandler(h);

			return true;
		} catch (Exception e) {

		}

		return false;
	}

	public boolean removeHandler(String loggerName, int index) {

		try {
			Handler h = getHandlerByIndex(loggerName, index);
			Logger l = this.lManager.getLogger(loggerName);
			l.removeHandler(h);

			return true;
		} catch (Exception e) {

		}

		return false;

	}

	public void reReadConf(URI uri) throws IOException {

		if (uri == null)
			this.lManager.readConfiguration();
		else
			this.lManager.readConfiguration(uri.toURL().openStream());

	}

	public void resetLoggerLevel(String loggerName) {

		this.bean
				.setLoggerLevel(loggerName, this.defaultLoggerLevel.toString());

	}

	public void resetLoggerLevels() {
		this.bean.setLoggerLevel(_DEFAULT_MC_DOMAIN, this.defaultLoggerLevel
				.toString());
	}

	public void _setDefaultHandlerLevel(Level l) {
		if (l == null)
			throw new NullPointerException("Arg cant be null");
		this.defaultHandlerLevel = l;
	}

	public void _setDefaultLoggerLevel(Level l) {
		if (l == null)
			throw new NullPointerException("Arg cant be null");
		this.defaultLoggerLevel = l;
	}

	public void setDefaultNotificationInterval(int numberOfEntries) {
		if (numberOfEntries <= 0)
			throw new IllegalArgumentException(
					"Interval can tbe less or equal to zero");
		this.defultNotificationInterval = numberOfEntries;
	}

	public void setGenericHandlerFilterClassName(String loggerName, int index,
			String className) throws NullPointerException,
			IllegalArgumentException {

		Handler h = getHandlerByIndex(loggerName, index);
		Object o = generateClassInstance(className);
		h.setFilter((Filter) o);

	}

	public void setGenericHandlerFormatterClassName(String loggerName,
			int index, String className) throws NullPointerException,
			IllegalArgumentException {

		Handler h = getHandlerByIndex(loggerName, index);
		Object o = generateClassInstance(className);
		h.setFormatter((Formatter) o);

	}

	public void setGenericHandlerLevel(String loggerName, int index,
			String level) throws NullPointerException, IllegalArgumentException {

		Level parsed = null;
		if (level == null || level.equals(""))
			parsed = this.defaultHandlerLevel;
		else
			parsed = Level.parse(level);
		Handler h = getHandlerByIndex(loggerName, index);
		h.setLevel(parsed);
	}

	public void setHandlerFilterClassName(String loggerName,
			String handlerNamem, String className)
			throws IllegalArgumentException, NullPointerException {

		Handler h = getHandlerByName(loggerName, handlerNamem);
		Object o = generateClassInstance(className);

		h.setFilter((Filter) o);

	}

	public void setHandlerFormaterClassName(String loggerName,
			String handlerName, String className)
			throws IllegalArgumentException, NullPointerException {
		Handler h = getHandlerByName(loggerName, handlerName);
		Object o = generateClassInstance(className);
		h.setFormatter((Formatter) o);
	}

	public void setHandlerLevel(String loggerName, String handlerName,
			Level level) throws IllegalArgumentException, NullPointerException {
		if (level == null)
			throw new NullPointerException("Level cant be null");
		Handler h = getHandlerByName(loggerName, handlerName);
		h.setLevel(level);
	}

	public void setHandlerLevel(String loggerName, String handlerName,
			String level) throws IllegalArgumentException, NullPointerException {
		Handler h = getHandlerByName(loggerName, handlerName);

		h.setLevel(Level.parse(level));
	}

	public void setHandlerName(String loggerName, int index, String newName) {

		if (newName == null)
			throw new NullPointerException("newName cant be null!!!");

		if (newName.equals(_NOTIFICATION_HANDLER_NAME)
				|| (this.handlers.get(loggerName) != null && this.handlers.get(
						loggerName).containsKey(newName))
				|| this.handlers.get(loggerName).get(newName) instanceof NotificationHandler)
			throw new IllegalArgumentException(
					"Cant change name of notification handler or to this specific name!!!!");

		Handler h = getHandlerByIndex(loggerName, index);
		// if(h instanceof NotificationHandler)
		// throw new IllegalArgumentException(
		// "Cant change name of notification handler or to this specific
		// name!!!!");
		if (this.handlers.get(loggerName) == null) {
			this.handlers.put(loggerName, new HashMap<String, Handler>());
		} else {
			this.handlers.get(loggerName).values().remove(h);
		}
		this.handlers.get(loggerName).put(newName, h);

		logger.info("HANDLERS [" + this.handlers + "]");
	}

	public void setHandlerNotificationInterval(String loggerName,
			int numberOfEntries) throws IllegalArgumentException,
			NullPointerException, IllegalStateException {

		NotificationHandler nh = (NotificationHandler) getHandlerByName(
				loggerName, this._NOTIFICATION_HANDLER_NAME);
		nh.setNotificationInterval(numberOfEntries);
	}

	public void setLoggerLevel(String loggerName, Level level)
			throws IllegalArgumentException {
		if (loggerName == null || level == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException(
					"Logger with this name does not exist!!!");

		this.lManager.getLogger(loggerName).setLevel(level);

	}

	public void setLoggerLevel(String loggerName, String level)
			throws IllegalArgumentException {
		Level parsed = null;
		if (level == null || level.equals(""))
			parsed = this.defaultLoggerLevel;
		else
			parsed = Level.parse(level);
		this.setLoggerLevel(loggerName, parsed);

	}

	public void setDefaultHandlerLevel(String l) {
		if (l == null || l.equals(""))
			throw new NullPointerException("Arg cant be null");
		Level parsed = Level.parse(l);
		this._setDefaultHandlerLevel(parsed);
	}

	public void setDefaultLoggerLevel(String l) {
		if (l == null || l.equals(""))
			throw new NullPointerException("Arg cant be null");
		Level parsed = Level.parse(l);
		this._setDefaultLoggerLevel(parsed);
	}

	public void setUseParentHandlersFlag(String loggerName, boolean flag) {

		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException(
					"Logger with this name does not exist!!!");
		this.lManager.getLogger(loggerName).setUseParentHandlers(flag);
	}

	public String getLoggerLevel(String loggerName)
			throws IllegalArgumentException {
		if (this.bean.getLoggerNames().contains(loggerName)) {

			// Here we have to check, if return value is "" we have to go up
			// logger tree
			// org.mobicents.tmp -> org.mobicents -> org -> global -> "" and
			// return something :...

			//return this.bean.getLoggerLevel(loggerName);
			return this.lookupLoggerLevel(loggerName);
		} else {
			throw new IllegalArgumentException("No logger under name["
					+ loggerName + "]");
		}

	}

	private String lookupLoggerLevel(String loggerName) {

		if (loggerName == null) {
			// check global and ""

			if (this.bean.getLoggerLevel("global").equals("")) {
				return this.bean.getLoggerLevel("");
			} else {
				return this.bean.getLoggerLevel("global");
			}
		} else {
			if (this.bean.getLoggerNames().contains(loggerName)) {
				String level = this.bean.getLoggerLevel(loggerName);

				if (level.equals("")) {
					
					// call again
					
					return lookupLoggerLevel(stripFqdn(loggerName));
				} else {
					return level;
				}
			} else {
				
				// call again
				return lookupLoggerLevel(stripFqdn(loggerName));
			}
		}

	}

	private String stripFqdn(String fqdn)
	{
		
		
		if(fqdn.contains("."))
		{
			return fqdn.substring(0,fqdn.lastIndexOf('.'));
		}else
		{
			return null;
		}
		
	}
	
	public String getLoggerFilterClassName(String loggerName)
			throws NullPointerException, IllegalArgumentException {
		if (this.bean.getLoggerNames().contains(loggerName)) {
			String ret = "";

			if (this.lManager.getLogger(loggerName).getFilter() != null)
				ret = this.lManager.getLogger(loggerName).getFilter()
						.getClass().getCanonicalName();
			return ret;
		} else {
			throw new IllegalArgumentException("No logger under name["
					+ loggerName + "]");
		}
	}

	// public void setLoggerFilterClassName(String loggerName, String className,
	// String[] constructorParameters, String[] paramValues)
	// throws NullPointerException, IllegalArgumentException,
	// IllegalStateException {

	public void setLoggerFilterClassName(String loggerName, String className,
			Object constructorParameters, Object paramValues)
			throws NullPointerException, IllegalArgumentException,
			IllegalStateException {
		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (className == null)
			throw new NullPointerException("Class name cant be null!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException("Logger with name ["
					+ loggerName + "] does not exist!!!");
		Filter f = null;
		if (constructorParameters != null
				&& paramValues != null
				&& ((String[]) paramValues).length > 0
				&& ((String[]) paramValues).length == ((String[]) constructorParameters).length) {

			f = (Filter) generateClassInstance(className,
					(String[]) constructorParameters, (String[]) paramValues);
		} else {
			f = (Filter) generateClassInstance(className);
		}
		this.lManager.getLogger(loggerName).setFilter(f);

	}

	/**
	 * 
	 * start MBean service lifecycle method
	 * 
	 */
	public void startService() throws Exception {

		// this.notificationDelegate = new
		// JBossNotificationBroadcasterSupport();
		boolean hasRun = this.bean != null;
		this.bean = LogManager.getLoggingMXBean();
		this.lManager = LogManager.getLogManager();
		if (hasRun) {
		} else {
			// Eh..
			// logger.info("==========
			// HOME["+System.getProperty("JBOSS_HOME")+"]");
			// File lFile = new File(System.getProperty("JBOSS_HOME")
			// + File.separator + "server" + File.separator + "all"
			// + File.separator + "deploy" + File.separator
			// + "mobicents.sar" + File.separator + "logging.properties");
			// URI fURI=lFile.toURI();
			// this.reReadConf(fURI);

			// classes/org/mobicents/slee/container/management/jmx/log
			// URL
			// fURL=this.getClass().getResource("../../../../../../../logging.properties");

			// this.reReadConf(fURL.toURI());

		}

		// Do we have to reread conf here?
	}

	/**
	 * 
	 * stop MBean service lifecycle method does full shutdown - removes
	 * everything that has been done here
	 */
	protected void stopService() throws Exception {

		// this.notificationDelegate = new
		// JBossNotificationBroadcasterSupport();
		// this.bean = null;
		// this.lManager = null;

	}

	private Handler getHandlerByIndex(String loggerName, int index) {
		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException(
					"Logger with this name does not exist!!!");

		Logger l = this.lManager.getLogger(loggerName);

		if (index < 0 || index >= l.getHandlers().length)
			throw new IllegalArgumentException(
					"Wrong index, either negative or overshoots handler array size!!!");

		Handler h = l.getHandlers()[index];
		return h;

	}

	private Object generateClassInstance(String className, String[] paramTypes,
			String[] params) {

		Object ret = null;
		Class clazz = null;
		try {
			clazz = this.getClass().forName(className);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			throw new IllegalArgumentException("Handler class[" + className
					+ "] can not be found!!!");
		}

		Class[] paraTypes = new Class[paramTypes.length];
		int counter = 0;
		// GET TYPES
		for (String s : paramTypes) {
			try {
				paraTypes[counter] = this.getClass().forName(
						paramTypes[counter]);
			} catch (Exception e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"Cant load class for constructor parameter type on position["
								+ counter + "] type[" + paramTypes[counter]
								+ "]!!! ");
			}
			counter++;

		}

		// GET CONSTRUCTOR
		Constructor con;
		try {
			con = clazz.getConstructor(paraTypes);
		} catch (SecurityException e) {

			e.printStackTrace();
			throw new IllegalArgumentException(
					"Security exception while trying to fetch constructor!!! ");

		} catch (NoSuchMethodException e) {

			e.printStackTrace();
			throw new IllegalArgumentException(
					"Cant find constructor for specified params");

		}
		// BUILD TYPES

		Object[] values = new Object[paraTypes.length];
		counter = 0;
		for (Class ct : paraTypes) {
			try {
				values[counter] = ct.getConstructor(java.lang.String.class)
						.newInstance(params[counter]);
			} catch (InstantiationException e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"Cant instantiate one parameter[" + ct + "]");
			} catch (IllegalAccessException e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"Cant instantiate one parameter[" + ct
								+ "], no visible constructor!!!");
			} catch (SecurityException e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"Cant instantiate one parameter[" + ct
								+ "], no visible constructor!!!");

			} catch (InvocationTargetException e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"Cant instantiate one parameter[" + ct
								+ "], cant invoce!!!");

			} catch (NoSuchMethodException e) {

				e.printStackTrace();
				throw new IllegalArgumentException(
						"Cant instantiate one parameter[" + ct
								+ "], no such method!!!");

			}
			counter++;
		}

		try {
			ret = con.newInstance(values);
		} catch (InstantiationException e) {

			e.printStackTrace();
			throw new IllegalArgumentException(
					"Cant instantiate one parameter handler class");

		} catch (IllegalAccessException e) {

			e.printStackTrace();
			throw new IllegalArgumentException(
					"Cant instantiate handler, no visible constructor with specified parameters!!!");
		} catch (InvocationTargetException e) {

			e.printStackTrace();
			throw new IllegalArgumentException(
					"Cant instantiate handler, invocation exception!!!");

		}

		return ret;

	}

	private Object generateClassInstance(String className) {

		if (className != null && !className.equals("")) {
			Class clazz = null;
			try {
				clazz = this.getClass().forName(className);
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
				throw new IllegalArgumentException("Filter class[" + className
						+ "] can not be found!!!");
			}
			try {
				Object o = clazz.newInstance();
				return o;
			} catch (Exception e) {
				throw new IllegalArgumentException("Cant create  class", e);
			}
		}
		return null;

	}

	private Handler getHandlerByName(String loggerName, String handlerName) {

		if (loggerName == null)
			throw new NullPointerException("Logger name cant be null!!!");

		if (!this.bean.getLoggerNames().contains(loggerName))
			throw new IllegalArgumentException(
					"Logger with this name does not exist!!!");

		if (this.handlers.get(loggerName) != null
				&& !this.handlers.get(loggerName).containsKey(handlerName)) {
			throw new IllegalStateException(
					"There is no handler with specified name[" + handlerName
							+ "]");

		}
		if (this.handlers.get(loggerName) == null)
			this.handlers.put(loggerName, new HashMap<String, Handler>());
		Logger l = this.lManager.getLogger(loggerName);
		boolean found = false;

		Handler namedHandler = this.handlers.get(loggerName).get(handlerName);
		for (Handler h : l.getHandlers()) {
			if (h.equals(namedHandler)) {
				found = true;
				break;
			}
		}

		if (!found) {
			// BAD - this means something happened async
			this.handlers.get(loggerName).remove(handlerName);
			throw new IllegalStateException(
					"Handler has been removed async to this operation, or does not exist!!!");

		}

		return namedHandler;

	}

	// ** NOTIFICATION PART

	private JBossNotificationBroadcasterSupport notificationDelegate = new JBossNotificationBroadcasterSupport();

	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback) {
		notificationDelegate
				.addNotificationListener(listener, filter, handback);
	}

	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		notificationDelegate.removeNotificationListener(listener);
	}

	public void removeNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback)
			throws ListenerNotFoundException {
		notificationDelegate.removeNotificationListener(listener, filter,
				handback);
	}

	public MBeanNotificationInfo[] getNotificationInfo() {
		return notificationDelegate.getNotificationInfo();
	}

	public void sendNotification(Notification notification) {
		notificationDelegate.sendNotification(notification);
	}

	public void handleNotification(NotificationListener listener,
			Notification notification, Object handback) {
		notificationDelegate.handleNotification(listener, notification,
				handback);
	}

}
