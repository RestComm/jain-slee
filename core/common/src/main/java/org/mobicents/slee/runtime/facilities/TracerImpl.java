package org.mobicents.slee.runtime.facilities;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.slee.InvalidArgumentException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.TraceLevel;
import javax.slee.facilities.Tracer;
import javax.slee.management.NotificationSource;
import javax.slee.management.TraceNotification;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;

/**
 * 
 * Start time:21:04:04 2009-03-09<br>
 * Project: mobicents-jainslee-server-core<br>
 * Implementation of Tracer object that allows slee comopnents to send
 * notification on some interesting ocasions.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski</a>
 * @author martins
 */
public class TracerImpl implements Tracer {

	public static final TraceLevel DEFAULT_TRACE_LEVEL = TraceLevel.INFO;

	public final static String ROOT_TRACER_NAME = "";
	
	private TraceLevel level;
	
	private final Logger logger;
	
	private final NotificationSourceWrapperImpl notificationSource;
	
	private final TraceMBeanImpl traceMBean;

	private final String name;
	
	private final TracerImpl parent;
		
	private boolean requestedBySource = false;
	
	private boolean configEnabled = false;
	private boolean infoEnabled = false;
	private boolean fineEnabled = false;
	private boolean finerEnabled = false;
	private boolean finestEnabled = false;
	private boolean warningEnabled = false;
	private boolean severeEnabled = false;
	
	private final ConcurrentLinkedQueue<TracerImpl> childs = new ConcurrentLinkedQueue<TracerImpl>(); 
	
	/**
	 * 
	 */
	public TracerImpl(String name, TracerImpl parent, NotificationSourceWrapperImpl notificationSource, TraceMBeanImpl traceMBean) {
		this.name = name;
		this.parent = parent;
		if (parent != null) {
			parent.childs.add(this);
		}
		this.logger = Logger.getLogger(tracerNameToLog4JLoggerName(name, notificationSource.getNotificationSource()));
		this.notificationSource = notificationSource;
		this.traceMBean = traceMBean;
		syncLevelWithLog4j();
	}
	
	/**
	 * Generates the log4j logger name for the tracer with specified named and notification source.
	 * 
	 * @param tracerName
	 * @param notificationSource
	 * @return
	 */
	private String tracerNameToLog4JLoggerName(String tracerName, NotificationSource notificationSource) {
		final StringBuilder sb = new StringBuilder("javax.slee.").append(notificationSource.toString());
		if(!tracerName.equals(ROOT_TRACER_NAME)) {
			sb.append('.').append(tracerName);
		}
		return sb.toString();
	}
	
	/**
	 * syncs the slee tracer level with the one that related logger has in log4j
	 */
	void syncLevelWithLog4j() {
		// get the level from log4j, only the root one uses effective level
		Level log4jLevel = parent == null ? logger.getEffectiveLevel() : logger.getLevel();
		if (level == null) {
			// set the level
			assignLog4JLevel(log4jLevel);
		}
		else {
			// set the level only if differs, otherwise we may loose levels not present in log4j
			if (tracerToLog4JLevel(level) != log4jLevel) {
				assignLog4JLevel(log4jLevel);				
			}
		}	
		// the root must always have a level
		if (parent == null && level == null) {
			// defaults to INFO
			logger.setLevel(Level.INFO);
			level = TraceLevel.INFO;			
		}
		// reset the flags
		resetCacheFlags(false);
	}
	
	/**
	 * assigns the equiv log4j level to the tracer
	 * @param log4jLevel
	 */
	private void assignLog4JLevel(Level log4jLevel) {
		if (log4jLevel == null) {
			return;
		}
		if (log4jLevel == Level.DEBUG) {
			level = TraceLevel.FINE;
		}
		else if (log4jLevel == Level.INFO) {
			level = TraceLevel.INFO;
		}
		else if (log4jLevel == Level.WARN) {
			level = TraceLevel.WARNING;
		}
		else if (log4jLevel == Level.ERROR) {
			level = TraceLevel.SEVERE;
		}
		else if (log4jLevel == Level.TRACE) {
			level = TraceLevel.FINEST;
		}
		else if (log4jLevel == Level.OFF) {
			level = TraceLevel.OFF;
		}
	}
	
	/**
	 * manages the flags which cache if levels are enabled
	 */
	void resetCacheFlags(boolean resetChilds) {
		if (isTraceable(TraceLevel.FINEST)) {
			finestEnabled = true;
			finerEnabled = true;
			fineEnabled = true;
			configEnabled = true;
			infoEnabled = true;
			warningEnabled = true;
			severeEnabled = true;
		}
		else {
			finestEnabled = false;
			if (isTraceable(TraceLevel.FINER)) {
				finerEnabled = true;
				fineEnabled = true;
				configEnabled = true;
				infoEnabled = true;
				warningEnabled = true;
				severeEnabled = true;
			}
			else {
				finerEnabled = false;
				if (isTraceable(TraceLevel.FINE)) {
					fineEnabled = true;
					configEnabled = true;
					infoEnabled = true;
					warningEnabled = true;
					severeEnabled = true;
				}
				else {
					fineEnabled = false;
					if (isTraceable(TraceLevel.CONFIG)) {
						configEnabled = true;
						infoEnabled = true;
						warningEnabled = true;
						severeEnabled = true;
					}
					else {
						if (isTraceable(TraceLevel.INFO)) {
							infoEnabled = true;
							warningEnabled = true;
							severeEnabled = true;
						}
						else {
							infoEnabled = false;
							if (isTraceable(TraceLevel.WARNING)) {
								warningEnabled = true;
								severeEnabled = true;
							}
							else {
								warningEnabled = false;
								if (isTraceable(TraceLevel.SEVERE)) {
									severeEnabled = true;
								}
								else {
									severeEnabled = false;
								}
							}
						}
					}
				}
			}
		}
		if (resetChilds) {
			// implicit change of level demands that we update reset flags on childs without level
			for(TracerImpl child : childs) {
				if (child.level == null) {
					child.resetCacheFlags(true);
				}				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#config(java.lang.String)
	 */
	public void config(String message) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.CONFIG, message, null);
		logger.info(message);		
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#config(java.lang.String, java.lang.Throwable)
	 */
	public void config(String message, Throwable t)
			throws NullPointerException, FacilityException {
		sendNotification(TraceLevel.CONFIG, message, t);
		logger.info(message,t);		
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#fine(java.lang.String)
	 */
	public void fine(String message) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.FINE, message, null);
		logger.debug(message);
		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#fine(java.lang.String, java.lang.Throwable)
	 */
	public void fine(String message, Throwable t) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.FINE, message, t);
		logger.debug(message,t);		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#finer(java.lang.String)
	 */
	public void finer(String message) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.FINER, message, null);
		logger.debug(message);
		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#finer(java.lang.String, java.lang.Throwable)
	 */
	public void finer(String message, Throwable t) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.FINER, message, t);
		logger.debug(message,t);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#finest(java.lang.String)
	 */
	public void finest(String message) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.FINEST, message, null);
		logger.trace(message);
		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#finest(java.lang.String, java.lang.Throwable)
	 */
	public void finest(String message, Throwable t)
			throws NullPointerException, FacilityException {
		sendNotification(TraceLevel.FINEST, message, t);
		logger.trace(message,t);		
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#getParentTracerName()
	 */
	public String getParentTracerName() {
		return parent == null ? null : parent.getTracerName();	
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#getTraceLevel()
	 */
	public TraceLevel getTraceLevel() throws FacilityException {	
		return level != null ? level : parent.getTraceLevel();		
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#getTracerName()
	 */
	public String getTracerName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#info(java.lang.String)
	 */
	public void info(String message) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.INFO, message, null);
		logger.info(message);
		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#info(java.lang.String, java.lang.Throwable)
	 */
	public void info(String message, Throwable t) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.INFO, message, t);
		logger.info(message,t);
		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isConfigEnabled()
	 */
	public boolean isConfigEnabled() throws FacilityException {
		return configEnabled;
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isFineEnabled()
	 */
	public boolean isFineEnabled() throws FacilityException {
		return fineEnabled;
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isFinerEnabled()
	 */
	public boolean isFinerEnabled() throws FacilityException {
		return finerEnabled;
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isFinestEnabled()
	 */
	public boolean isFinestEnabled() throws FacilityException {
		return finestEnabled;
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isInfoEnabled()
	 */
	public boolean isInfoEnabled() throws FacilityException {
		return infoEnabled;
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isSevereEnabled()
	 */
	public boolean isSevereEnabled() throws FacilityException {
		return severeEnabled;
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isTraceable(javax.slee.facilities.TraceLevel)
	 */
	public boolean isTraceable(TraceLevel traceLevel) throws NullPointerException,
			FacilityException {
		return !getTraceLevel().isHigherLevel(traceLevel);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isWarningEnabled()
	 */
	public boolean isWarningEnabled() throws FacilityException {
		return warningEnabled;
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#severe(java.lang.String)
	 */
	public void severe(String message) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.SEVERE, message, null);
		logger.error(message);		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#severe(java.lang.String, java.lang.Throwable)
	 */
	public void severe(String message, Throwable t)
			throws NullPointerException, FacilityException {
		sendNotification(TraceLevel.SEVERE, message, t);
		logger.error(message,t);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#trace(javax.slee.facilities.TraceLevel, java.lang.String)
	 */
	public void trace(TraceLevel traceLevel, String message)
			throws NullPointerException, IllegalArgumentException,
			FacilityException {
		sendNotification(traceLevel, message, null);
		logger.log(tracerToLog4JLevel(traceLevel), message);		
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#trace(javax.slee.facilities.TraceLevel, java.lang.String, java.lang.Throwable)
	 */
	public void trace(TraceLevel traceLevel, String message, Throwable t)
			throws NullPointerException, IllegalArgumentException,
			FacilityException {
		sendNotification(traceLevel, message, t);
		logger.log(tracerToLog4JLevel(traceLevel), message,t);
		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#warning(java.lang.String)
	 */
	public void warning(String message) throws NullPointerException,
			FacilityException {
		sendNotification(TraceLevel.WARNING, message, null);
		logger.warn(message);		
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#warning(java.lang.String, java.lang.Throwable)
	 */
	public void warning(String message, Throwable t)
			throws NullPointerException, FacilityException {
		sendNotification(TraceLevel.WARNING, message, t);
		logger.warn(message,t);		
	}
	
	/**
	 * THis is internaly called, by 1.1 tracers
	 * 
	 * @param src
	 */
	void sendNotification(javax.slee.facilities.TraceLevel level, String message, Throwable t) {
		if (!isTraceable(level)) {
			return;
		}
		traceMBean.sendNotification(new TraceNotification(notificationSource.getNotificationSource().getTraceNotificationType(), traceMBean, notificationSource.getNotificationSource(), getTracerName(), level, message, t, notificationSource.getNextSequence(), System.currentTimeMillis()));
	}
	
	/**
	 * This checks if the specified tracer name is ok.
	 * 
	 * @param tracerName
	 * @param notificationSource
	 * @throws InvalidArgumentException
	 */
	public static void checkTracerName(String tracerName, NotificationSource notificationSource) throws NullPointerException,InvalidArgumentException {

		if (tracerName.equals("")) {
			// This is root
			return;
		}

		StringTokenizer stringTokenizer = new StringTokenizer(tracerName, ".", true);

		String lastToken = null;

		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (lastToken == null) {
				// this is start
				lastToken = token;
			}

			if (lastToken.equals(token) && token.equals(".")) {
				throw new InvalidArgumentException("Passed tracer:" + tracerName + ", name for source: " + notificationSource + ", is illegal");
			}

			lastToken = token;

		}

		if (lastToken.equals(".")) {
			throw new IllegalArgumentException("Passed tracer:" + tracerName + ", name for source: " + notificationSource + ", is illegal");
		}

	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tracer{ notificationSource = "+notificationSource.getNotificationSource()+" , name = "+getTracerName()+" , parent = "+getParentTracerName()+" , level = "+getTraceLevel()+" }";
	}

	private Level tracerToLog4JLevel(TraceLevel traceLevel) {
		if (traceLevel.isFine() || traceLevel.isFiner()) {
			return Level.DEBUG;
		}
		if (traceLevel.isInfo() || traceLevel.isConfig()) {
			return Level.INFO;
		}
		if (traceLevel.isFinest()) {
			return Level.TRACE;
		}
		if(traceLevel.isWarning()) {
			return Level.WARN;
		}
		if(traceLevel.isSevere()) {
			return Level.ERROR;
		}
		if(traceLevel.isOff()) {
			return Level.OFF;
		}
		return Level.INFO;
	}
	
	/**
	 * @param level
	 */
	public void setTraceLevel(TraceLevel level) {
		this.level = level;
		logger.setLevel(tracerToLog4JLevel(level));
		resetCacheFlags(true);
	}

	public void unsetTraceLevel() {
		this.level = null;
		logger.setLevel(null);
		resetCacheFlags(true);		
	}

	/**
	 * @return
	 */
	public boolean isExplicitlySetTracerLevel() {
		return level != null;		
	}
	
	/**
	 * Sets 
	 * @param requestedBySource the requestedBySource to set
	 */
	public void setRequestedBySource(boolean requestedBySource) {
		this.requestedBySource = requestedBySource;
	}
	
	/**
	 * Retrieves 
	 * @return the requestedBySource
	 */
	public boolean isRequestedBySource() {
		return requestedBySource;
	}
}
