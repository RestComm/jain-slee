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

package org.mobicents.slee.runtime.facilities;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski</a>
 * 
 */
public class TracerStorage {
	
	private final ConcurrentHashMap<String, TracerImpl> tracers = new ConcurrentHashMap<String, TracerImpl>();
	
	private final NotificationSourceWrapperImpl notificationSource;
	
	private final TraceMBeanImpl traceFacility;
	private TracerImpl rootTracer;
	
	public TracerStorage(NotificationSource notificationSource, TraceMBeanImpl traceFacility) {
		super();
		this.notificationSource = new NotificationSourceWrapperImpl(notificationSource);
		this.traceFacility = traceFacility;
		this.rootTracer = new TracerImpl("",null,this.notificationSource, this.traceFacility);		
		tracers.put(rootTracer.getTracerName(), rootTracer);
	}

	public void syncTracersWithLog4J()  {
		for (TracerImpl tracer : tracers.values()) {
			tracer.syncLevelWithLog4j();
		}		
	}
	
	public void setTracerLevel(TraceLevel lvl, String tracerName) throws InvalidArgumentException {
		// FIXME: JDOC of TraceMBean - description of class show example
		// that setTraceLevel creates tracer, we handle creation elswhere
		if (!isTracerDefined(tracerName)) {
			throw new InvalidArgumentException("No tracer definition with name: " + tracerName + ", for notification source: " + this.notificationSource + ". See section 13.3 of JSLEE 1.1 specs");
		}
		TracerImpl tracer = this.tracers.get(tracerName);
		if (tracer.getParentTracerName() == null) {
			throw new InvalidArgumentException("Can not set root tracer level for source: " + this.notificationSource + ". See section 13.3 of JSLEE 1.1 specs");
		}
		tracer.setTraceLevel(lvl);

	}

	public void unsetTracerLevel(String tracerName) throws InvalidArgumentException {
		if (!isTracerDefined(tracerName)) {
			throw new InvalidArgumentException("No tracer definition with name: " + tracerName + ", for notification source: " + this.notificationSource + ". See section 13.3 of JSLEE 1.1 specs");
		}
		TracerImpl tracer = this.tracers.get(tracerName);
		if (tracer.getParentTracerName() == null) {
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
		if(names.isEmpty())
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
		if(names.isEmpty())
			return new String[0];
		return names.toArray(new String[names.size()]);
	}

	/**
	 * This method can be called multiple times.
	 * 
	 * @param tracerName
	 * @param requestedBySource
	 * @return
	 */
	public Tracer createTracer(String tracerName, boolean requestedBySource) {
	
		TracerImpl tparent = null;
		TracerImpl t = tracers.get(tracerName);
		if (t == null) {
			String[] split = tracerName.split("\\.");
			String currentName = "";
			for (String s : split) {
				if (tparent == null) {
					// first loop
					tparent = rootTracer;
					currentName = s;
				} else {
					currentName = currentName + "." + s;
				}
				t = tracers.get(currentName);
				if (t == null) {
					t = new TracerImpl(currentName, tparent, this.notificationSource, this.traceFacility);
					final TracerImpl u = tracers.putIfAbsent(t.getTracerName(), t);
					if (u != null) {
						t = u;
					}
				}
				tparent = t;
			}
		}

		if (requestedBySource)
			t.setRequestedBySource(requestedBySource);
		return t;
	}

	public boolean isTracerDefined(String tracerName) {
		return tracers.containsKey(tracerName);
	}
}
