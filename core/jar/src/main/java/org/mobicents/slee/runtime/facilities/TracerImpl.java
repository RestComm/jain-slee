package org.mobicents.slee.runtime.facilities;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.slee.InvalidArgumentException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.TraceLevel;
import javax.slee.facilities.Tracer;
import javax.slee.management.NotificationSource;
import javax.slee.management.TraceNotification;

import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;

/**
 * 
 * Start time:21:04:04 2009-03-09<br>
 * Project: mobicents-jainslee-server-core<br>
 * Implementation of Tracer object that allows slee comopnents to send
 * notification on some interesting ocasions.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class TracerImpl implements Tracer {
	private static final Logger logger = Logger.getLogger(Tracer.class.getSimpleName());
	private String tracerName = null;
	private boolean isRoot = false;
	private boolean requestedBySource = false;

	// Default for root tracer
	private TraceLevel traceLevel = TraceLevel.INFO;
	private boolean explicitlySetTracerLevel = false;
	private MNotificationSource notificationSource = null;
	/**
	 * This reference us ised in case of removal of trace level, in that case it
	 * is inherited from parent, there is always parent. This reference is only
	 * null in case of root tracer
	 */
	private TracerImpl parentTracer = null;
	private TraceMBeanImpl traceFacility = null;

	/**
	 * 
	 * Creates tracer that is in tree. This will use parent tracer levels.
	 * 
	 * @param tracerName
	 *            - fqdn name of this tracer
	 * @param notificationSource
	 *            - notification source
	 * @param parentTracer
	 *            - parent, we need it to get
	 */
	public TracerImpl(String tracerName, MNotificationSource notificationSource, TracerImpl parentTracer, TraceMBeanImpl traceFacility) {
		super();
		this.tracerName = tracerName;
		this.notificationSource = notificationSource;
		this.parentTracer = parentTracer;
		this.traceFacility = traceFacility;
		this.setExplicitlySetTracerLevel(false);
	}

	/**
	 * THis is used to create root tracer.
	 * 
	 * @param notficationSource
	 */
	public TracerImpl(MNotificationSource notficationSource, TraceMBeanImpl traceFacility) {
		super();
		this.tracerName = "";
		this.isRoot = true;
		this.traceLevel = TraceLevel.INFO;
		this.setExplicitlySetTracerLevel(false);
		this.notificationSource = notficationSource;
		this.traceFacility = traceFacility;

	}

	boolean isRoot() {
		return isRoot;
	}

	boolean isRequestedBySource() {
		return requestedBySource;
	}

	void setRequestedBySource(boolean requestedBySource) {
		this.requestedBySource = requestedBySource;
	}

	boolean isExplicitlySetTracerLevel() {
		return explicitlySetTracerLevel;
	}

	void setExplicitlySetTracerLevel(boolean explicitlySetTracerLevel) {
		this.explicitlySetTracerLevel = explicitlySetTracerLevel;
	}

	void setTraceLevel(TraceLevel traceLevel) {

		// FIXME: add check for root ?
		this.traceLevel = traceLevel;
		this.setExplicitlySetTracerLevel(true);
	}

	void unsetTraceLevel() throws InvalidArgumentException {

		this.setExplicitlySetTracerLevel(false);
		this.traceLevel = this.parentTracer.getTraceLevel();
	}

	// Tracer Interface methods

	public void config(String trace) throws NullPointerException, FacilityException {
		if (!isConfigEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.CONFIG, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, null,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void config(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isConfigEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.CONFIG, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, cause,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void fine(String trace) throws NullPointerException, FacilityException {
		if (!isFineEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINE, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, null,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void fine(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isFineEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINE, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, cause,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void finer(String trace) throws NullPointerException, FacilityException {
		if (!isFinerEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINER, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, null,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void finer(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isFinerEnabled()) {
			return;
		}

		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINER, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, cause,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void finest(String trace) throws NullPointerException, FacilityException {
		if (!isFinestEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINEST, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, null,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void finest(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isFinestEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINEST, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, cause,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void info(String trace) throws NullPointerException, FacilityException {
		if (!isInfoEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.INFO, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, null,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void info(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isInfoEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.INFO, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, cause,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void severe(String trace) throws NullPointerException, FacilityException {
		if (!isSevereEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.SEVERE, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, null,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void severe(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isSevereEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.SEVERE, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, cause,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void trace(TraceLevel level, String trace) throws NullPointerException, IllegalArgumentException, FacilityException {
		if (!isTraceable(level)) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), level, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, null,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void trace(TraceLevel level, String trace, Throwable cause) throws NullPointerException, IllegalArgumentException, FacilityException {
		if (!isTraceable(level)) {
			return;
		}

		this.createTrace(this.notificationSource.getNotificationSource(), level, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, cause,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void warning(String trace) throws NullPointerException, FacilityException {
		if (!isWarningEnabled()) {
			return;
		}
		this.createTrace(this.notificationSource.getNotificationSource(), this.getTraceLevel(), this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				null, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void warning(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isWarningEnabled()) {

			return;
		}

		this.createTrace(this.notificationSource.getNotificationSource(), this.getTraceLevel(), this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				cause, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public boolean isConfigEnabled() throws FacilityException {
		return isTraceable(TraceLevel.CONFIG);
	}

	public boolean isFineEnabled() throws FacilityException {
		return isTraceable(TraceLevel.FINE);
	}

	public boolean isFinerEnabled() throws FacilityException {
		return isTraceable(TraceLevel.FINER);
	}

	public boolean isFinestEnabled() throws FacilityException {
		return isTraceable(TraceLevel.FINEST);
	}

	public boolean isInfoEnabled() throws FacilityException {
		return isTraceable(TraceLevel.INFO);
	}

	public boolean isSevereEnabled() throws FacilityException {
		return isTraceable(TraceLevel.SEVERE);
	}

	public boolean isTraceable(TraceLevel toTrace) throws NullPointerException, FacilityException {

		if (toTrace == null) {
			throw new NullPointerException("Passed trace level must not be null.");
		}

		return !this.getTraceLevel().isHigherLevel(toTrace);
	}

	public boolean isWarningEnabled() throws FacilityException {
		return isTraceable(TraceLevel.WARNING);
	}

	public String getParentTracerName() {
		if (this.isRoot) {
			// we are root, its ok to return null
			return null;
		} else {
			return this.parentTracer.getTracerName();
		}

	}

	public TraceLevel getTraceLevel() throws FacilityException {
		// FIXME: is this correct
		if (this.isExplicitlySetTracerLevel() || this.isRoot)
			return this.traceLevel;
		else
			return this.parentTracer.getTraceLevel();
	}

	public String getTracerName() {
		return this.tracerName;
	}

	/**
	 * THis is internaly called, by 1.1 tracers
	 * 
	 * @param src
	 */
	void createTrace(NotificationSource src, javax.slee.facilities.TraceLevel lvl, String traceType, String tracerName, String msg, Throwable cause, long seq, long timeStamp) {

		// Here we know that this trace level is logable;

		TraceNotification traceNotification = new TraceNotification(traceType, this.traceFacility, src, tracerName, lvl, msg, cause, seq, timeStamp);
		dumpMessage(traceNotification);
		this.traceFacility.sendNotification(traceNotification);
	}

	private void dumpMessage(TraceNotification traceNotification) {
		String msg = "Tracer[" + traceNotification.getTracerName() + "] Seq[" + traceNotification.getSequenceNumber() + "] Source[" + traceNotification.getNotificationSource() + "] Message: \n"
				+ traceNotification.getMessage() + "\nCause: " + makeStackTraceReadable(traceNotification.getCause());

		TraceLevel lvl = traceNotification.getTraceLevel();
		if (lvl.isFinest()) {
			logger.finest(msg);
		} else if (lvl.isFiner()) {
			logger.finer(msg);

		} else if (lvl.isFine()) {
			logger.fine(msg);

		} else if (lvl.isConfig()) {
			logger.config(msg);

		} else if (lvl.isInfo()) {
			logger.info(msg);

		} else if (lvl.isSevere()) {
			logger.severe(msg);

		} else if (lvl.isWarning()) {
			logger.warning(msg);

		}

	}

	private String makeStackTraceReadable(Throwable t) {
		if (t == null)
			return null;
		else {
			return null;
		}
	}

	/**
	 * This checks if tracer name is ok. It must not be null;
	 * 
	 * @param split
	 * @throws IllegalArgumentException
	 */
	public static void checkTracerName(String tracerName, NotificationSource notificationSource) throws IllegalArgumentException {

		if (tracerName.compareTo("") == 0) {
			// This is root
			return;
		}

		// String[] splitName = tracerName.split("\\.");
		StringTokenizer stringTokenizer = new StringTokenizer(tracerName, ".", true);

		int fqdnPartIndex = 0;

		// if(splitName.length==0)
		// {
		// throw new IllegalArgumentException("Passed tracer:" + tracerName +
		// ", name for source: " + notificationSource + ", is illegal");
		// }

		String lastToken = null;

		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (lastToken == null) {
				// this is start
				lastToken = token;
			}

			if (lastToken.compareTo(token) == 0 && token.compareTo(".") == 0) {
				throw new IllegalArgumentException("Passed tracer:" + tracerName + ", name for source: " + notificationSource + ", is illegal");
			}

			if (token.compareTo(".") != 0) {
				for (int charIndex = 0; charIndex < token.length(); charIndex++) {
					Character c = token.charAt(charIndex);
					if (c.isLetter(c) || c.isDigit(c)) {
						// Its ok?
					} else {
						throw new IllegalArgumentException("Passed tracer:" + tracerName + " Token[" + token + "], name for source: " + notificationSource
								+ ", is illegal, contains illegal character: " + charIndex);
					}

				}

				fqdnPartIndex++;
			}
			lastToken = token;

		}

		if (lastToken.compareTo(".") == 0) {
			throw new IllegalArgumentException("Passed tracer:" + tracerName + ", name for source: " + notificationSource + ", is illegal");
		}

	}

}
