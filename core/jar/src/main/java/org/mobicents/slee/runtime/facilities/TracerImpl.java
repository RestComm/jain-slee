package org.mobicents.slee.runtime.facilities;

import java.util.StringTokenizer;

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

	private enum SpecificLevel { finer, config }
	
	private SpecificLevel specificLevel = null;
	
	private final Logger logger;
	
	private final MNotificationSource notificationSource;
	
	private final TraceMBeanImpl traceMBean;

	private final String name;
	
	private final String parentName;
		
	private boolean requestedBySource = false;

	/**
	 * 
	 */
	public TracerImpl(MNotificationSource notificationSource, TraceMBeanImpl traceMBean) {
		this(ROOT_TRACER_NAME, null, notificationSource, traceMBean);
		// specs require every root logger must have INFO level, but we don't
		// want that if log4j config imposes a WARN, ERROR or OFF level, even if
		// in that case we don't comply with the specs. this means tck will not
		// pass if log4j logger says INFO is not enabled (e.g. WARN level set)
		if (this.logger.isInfoEnabled()) {
			this.logger.setLevel(Level.INFO);
		}
	}
	
	/**
	 * 
	 */
	public TracerImpl(String name, String parentName, MNotificationSource notificationSource, TraceMBeanImpl traceMBean) {
		this.name = name;
		this.parentName = parentName;
		this.logger = Logger.getLogger(tracerNameToLog4JLoggerName(name, notificationSource.getNotificationSource()));
		this.notificationSource = notificationSource;
		this.traceMBean = traceMBean;
	}
	
	/**
	 * Generates the log4j logger name for the tracer with specified named and notification source.
	 * 
	 * @param tracerName
	 * @param notificationSource
	 * @return
	 */
	private String tracerNameToLog4JLoggerName(String tracerName, NotificationSource notificationSource) {
		return "javax.slee."+notificationSource.toString() + ( tracerName.equals(ROOT_TRACER_NAME) ? "" : ("." + tracerName) );
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
		return parentName;	
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#getTraceLevel()
	 */
	public TraceLevel getTraceLevel() throws FacilityException {
		final Level level = logger.getEffectiveLevel();
		
		if (level != null) {
			switch (level.toInt()) {
			case Level.DEBUG_INT:
				if (specificLevel == SpecificLevel.finer) {
					return TraceLevel.FINER;
				}
				else {
					return TraceLevel.FINE;
				}
			case Level.INFO_INT:
				if (specificLevel == SpecificLevel.config) {
					return TraceLevel.CONFIG;
				}
				else {
					return TraceLevel.INFO;
				}
			case Level.WARN_INT:
				specificLevel = null;
				return TraceLevel.WARNING;
			case Level.ERROR_INT:
				specificLevel = null;
				return TraceLevel.SEVERE;
			case Level.TRACE_INT:
				specificLevel = null;
				return TraceLevel.FINEST;
			case Level.OFF_INT:
				specificLevel = null;
				return TraceLevel.OFF;
			default:
				break;
			}
		};

		specificLevel = null;
		return DEFAULT_TRACE_LEVEL;
		
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
		return isTraceable(TraceLevel.CONFIG);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isFineEnabled()
	 */
	public boolean isFineEnabled() throws FacilityException {
		return isTraceable(TraceLevel.FINE);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isFinerEnabled()
	 */
	public boolean isFinerEnabled() throws FacilityException {
		return isTraceable(TraceLevel.FINER);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isFinestEnabled()
	 */
	public boolean isFinestEnabled() throws FacilityException {
		return isTraceable(TraceLevel.FINEST);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isInfoEnabled()
	 */
	public boolean isInfoEnabled() throws FacilityException {
		return isTraceable(TraceLevel.INFO);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isSevereEnabled()
	 */
	public boolean isSevereEnabled() throws FacilityException {
		return isTraceable(TraceLevel.SEVERE);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isTraceable(javax.slee.facilities.TraceLevel)
	 */
	public boolean isTraceable(TraceLevel traceLevel) throws NullPointerException,
			FacilityException {
		if (traceLevel == null) {
			throw new NullPointerException("null trace level");
		}
		return !this.getTraceLevel().isHigherLevel(traceLevel);
	}
	/* (non-Javadoc)
	 * @see javax.slee.facilities.Tracer#isWarningEnabled()
	 */
	public boolean isWarningEnabled() throws FacilityException {
		return isTraceable(TraceLevel.WARNING);
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
	 * @throws NullPointerException
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
		if (level.isConfig()) {
			specificLevel = SpecificLevel.config;
		}
		else if (level.isFiner()) {
			specificLevel = SpecificLevel.finer;
		}
		logger.setLevel(tracerToLog4JLevel(level));
	}

	/**
	 * 
	 */
	public void unsetTraceLevel() {
		logger.setLevel(null);
		specificLevel = null;
	}

	/**
	 * @return
	 */
	public boolean isExplicitlySetTracerLevel() {
		if (getParentTracerName() == null) {
			// root
			return false;
		}
		else {
			// get level set in log4j
			return logger.getLevel() != null;
		}
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
