package org.mobicents.slee.runtime.facilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.slee.InvalidArgumentException;
import javax.slee.facilities.TraceLevel;
import javax.slee.facilities.Tracer;
import javax.slee.management.NotificationSource;

import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;

/**
 * This class holds tracers for sources. By default it create root tracer. It
 * implicitly creates ancestor tracers for requested name. It holds reference
 * TraceName->TracerImpl. It also associates parent to a child.<br>
 * Start time:19:20:19 2009-03-09<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski</a>
 * 
 */
public class TracerStorage {
	private Map<String, TracerImpl> tracers = new HashMap<String, TracerImpl>();
	private MNotificationSource notificationSource = null;
	private TraceMBeanImpl traceFacility = null;

	public TracerStorage(NotificationSource notificationSource, TraceMBeanImpl traceFacility) {
		super();
		this.notificationSource = new MNotificationSource(notificationSource);
		this.traceFacility = traceFacility;
		TracerImpl rootTracer = new TracerImpl(this.notificationSource, this.traceFacility);
		
		tracers.put("", rootTracer);
	}

	public void setTracerLevel(TraceLevel lvl, String tracerName) throws InvalidArgumentException {
		// FIXME: JDOC of TraceMBean - description of class show example
		// that setTraceLevel creates tracer, we handle creation elswhere
		if (!isTracerDefined(tracerName)) {
			throw new InvalidArgumentException("No tracer definition with name: " + tracerName + ", for notification source: " + this.notificationSource + ". See section 13.3 of JSLEE 1.1 specs");
		}
		TracerImpl tracer = this.tracers.get(tracerName);
		if (tracer.isRoot()) {
			throw new InvalidArgumentException("Can not set root tracer level for source: " + this.notificationSource + ". See section 13.3 of JSLEE 1.1 specs");
		}
		tracer.setTraceLevel(lvl);

	}

	public void unsetTracerLevel(String tracerName) throws InvalidArgumentException {
		if (!isTracerDefined(tracerName)) {
			throw new InvalidArgumentException("No tracer definition with name: " + tracerName + ", for notification source: " + this.notificationSource + ". See section 13.3 of JSLEE 1.1 specs");
		}
		TracerImpl tracer = this.tracers.get(tracerName);
		if (tracer.isRoot()) {
			throw new InvalidArgumentException("Can not unset root tracer level for source: " + this.notificationSource + ". See section 13.3 of JSLEE 1.1 specs");
		}
		tracer.unsetTraceLevel();
	}

	public TraceLevel getTracerLevel(String tracerName) throws InvalidArgumentException {
		if (!isTracerDefined(tracerName)) {
			throw new InvalidArgumentException("No tracer definition with name: " + tracerName + ", for notification source: " + this.notificationSource + ". See section 13.3 of JSLEE 1.1 specs");
		}
		TracerImpl tracer = this.tracers.get(tracerName);
		
		return tracer.getTraceLevel();
	}

	/**
	 * This method raturns tracer names of tracers that have been requested
	 * directly from NotificationSource (like through SbbContext, RAContext,
	 * etc)
	 * 
	 * @return
	 */
	public String[] getRequestedTracerNames() {
		Set<String> names = new HashSet<String>();
		for (TracerImpl t : this.tracers.values()) {
			if (t.isRequestedBySource())
				names.add(t.getTracerName());
		}
		if(names.size()==0)
			return new String[0];
		return names.toArray(new String[names.size()]);
	}

	/**
	 * This method returns tracer names that have been defined explicitly via
	 * setTraceLevel from TraceMBean
	 * 
	 * @return
	 */
	public String[] getDefinedTracerNames() {
		Set<String> names = new HashSet<String>();
		for (TracerImpl t : this.tracers.values()) {
			if (t.isExplicitlySetTracerLevel())
				names.add(t.getTracerName());
		}
		if(names.size()==0)
			return new String[0];
		return names.toArray(new String[names.size()]);
	}

	/**
	 * This method can be called multiple times.
	 * 
	 * @param tracerName
	 * @param requestedBySource
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Tracer createTracer(String tracerName, boolean requestedBySource) throws InvalidArgumentException {
	

		// FIXME: this is double check, in some cases.
		TracerImpl.checkTracerName(tracerName, this.notificationSource.getNotificationSource());
		
		//Biut more efficient for cases we do hold tracers
		if (!tracers.containsKey(tracerName)) {
			String[] split = tracerName.split("\\.");
			String parentName = null;
			String currentName = "";
			for (String s : split) {
				if (parentName == null) {
					// first loop
					parentName = currentName;
					currentName = s;
				} else {
					parentName = currentName;
					currentName = currentName + "." + s;
				}
				// This could happen when we have org.mobicents tracers create,
				// and now request org.mobicents.Foo
				if (this.tracers.containsKey(currentName))
				{
					
					continue;
				}

				TracerImpl parent = tracers.get(parentName);
				TracerImpl child = new TracerImpl(currentName, this.notificationSource, parent, this.traceFacility);
				this.tracers.put(currentName, child);


			}
		}
		TracerImpl t = this.tracers.get(tracerName);

		if (requestedBySource)
			t.setRequestedBySource(requestedBySource);
		return t;
	}

	public boolean isTracerDefined(String tracerName) {
		return tracers.containsKey(tracerName);
	}
}
