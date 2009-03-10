package org.mobicents.slee.runtime.facilities;

import javax.slee.InvalidArgumentException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.TraceLevel;
import javax.slee.facilities.Tracer;
import javax.slee.management.NotificationSource;

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
class TracerImpl implements Tracer {
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
	private TraceFacilityImpl traceFacility = null;

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
	public TracerImpl(String tracerName, MNotificationSource notificationSource, TracerImpl parentTracer, TraceFacilityImpl traceFacility) {
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
	public TracerImpl(MNotificationSource notficationSource, TraceFacilityImpl traceFacility) {
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
		traceFacility.createTrace(this.notificationSource.getNotificationSource(),TraceLevel.CONFIG, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				null, this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void config(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isConfigEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.CONFIG, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				cause, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void fine(String trace) throws NullPointerException, FacilityException {
		if (!isFineEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(),TraceLevel.FINE, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				null, this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void fine(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isFineEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINE, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				cause, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void finer(String trace) throws NullPointerException, FacilityException {
		if (!isFinerEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINER, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				null, this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void finer(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isFinerEnabled()) {
			return;
		}

		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINER, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				cause, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void finest(String trace) throws NullPointerException, FacilityException {
		if (!isFinestEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINEST, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				null, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void finest(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isFinestEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.FINEST, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				cause, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void info(String trace) throws NullPointerException, FacilityException {
		if (!isInfoEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.INFO, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				null, this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void info(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isInfoEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.INFO, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				cause, this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void severe(String trace) throws NullPointerException, FacilityException {
		if (!isSevereEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.SEVERE, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				null, this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void severe(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isSevereEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), TraceLevel.SEVERE, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				cause, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void trace(TraceLevel level, String trace) throws NullPointerException, IllegalArgumentException, FacilityException {
		if (!isTraceable(level)) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), level, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, null,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());
	}

	public void trace(TraceLevel level, String trace, Throwable cause) throws NullPointerException, IllegalArgumentException, FacilityException {
		if (!isTraceable(level)) {
			return;
		}

		traceFacility.createTrace(this.notificationSource.getNotificationSource(), level, this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace, cause,
				this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void warning(String trace) throws NullPointerException, FacilityException {
		if (!isWarningEnabled()) {
			return;
		}
		traceFacility.createTrace(this.notificationSource.getNotificationSource(), this.getTraceLevel(), this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
				null, this.notificationSource.getNextSequence(), System.currentTimeMillis());

	}

	public void warning(String trace, Throwable cause) throws NullPointerException, FacilityException {
		if (!isWarningEnabled()) {

			return;
		}

		traceFacility.createTrace(this.notificationSource.getNotificationSource(), this.getTraceLevel(), this.notificationSource.getNotificationSource().getTraceNotificationType(), this.tracerName, trace,
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

}
