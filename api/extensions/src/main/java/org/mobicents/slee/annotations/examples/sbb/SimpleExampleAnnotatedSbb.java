package org.mobicents.slee.annotations.examples.sbb;

import javax.slee.ActivityContextInterface;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceStartedEvent;

import org.mobicents.slee.ConfigProperties;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.SbbExt;
import org.mobicents.slee.annotations.CMPField;
import org.mobicents.slee.annotations.Sbb;
import org.mobicents.slee.annotations.SbbContextExtField;
import org.mobicents.slee.annotations.Service;
import org.mobicents.slee.annotations.ServiceStartedEventHandler;
import org.mobicents.slee.annotations.TimerEventHandler;
import org.mobicents.slee.annotations.TracerField;

/**
 * Simple example for an annotated Sbb, on service start fires a 1 sec timer and
 * on timer event calculates real delay. Examples CMP Field and facilities
 * injection too.
 * 
 * A service is defined by applying the {@link Service} annotation too.
 * 
 * Extends {@link SbbExt} instead of implementing {@link Sbb}, besides access to
 * {@link SbbContextExt} and {@link ConfigProperties}, not used in the example,
 * there is no need to define sbb lifecycle methods not used.
 * 
 * Regarding field injection: 
 * - "default" tracer and SbbContextExt injected before setSbbContext()
 * - by applying CMPField annotation to field, value is
 * injected before sbbLoad(), and persisted after sbbStore(), in case applied to
 * method it identifies one of the CMP Field accessors.
 * 
 * A few more notes about event handler method annotations:
 * - initialEventSelect is { InitialEventSelect.ActivityContext } by default
 * - initialEvent is false by default
 * 
 * @author Eduardo Martins
 * 
 */
@Service(name="ExampleSbb",vendor="javax.slee",version="1.0",
		rootSbb=SimpleExampleAnnotatedSbb.class)
@Sbb(name="ExampleSbb",vendor="javax.slee",version="1.0")
public abstract class SimpleExampleAnnotatedSbb implements SbbExt {

	private static final long TIMER_DURATION = 1000;
	
	@TracerField
	private Tracer tracer;
	
	@SbbContextExtField
	private SbbContextExt sbbContextExt;
	
	@CMPField
	private Long startTime;
	
	@ServiceStartedEventHandler
	public void onServiceStartedEvent(ServiceStartedEvent event,
			ActivityContextInterface aci) {
		tracer.info("service started");
		sbbContextExt.getTimerFacility().setTimer(aci, null, TIMER_DURATION,
				new TimerOptions());
		startTime = Long.valueOf(System.currentTimeMillis());
	}
	
	@TimerEventHandler
	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		long delay = (System.currentTimeMillis()-TIMER_DURATION) - startTime;
		tracer.info("timer expired, delay = "+delay+" ms.");
	}
}
