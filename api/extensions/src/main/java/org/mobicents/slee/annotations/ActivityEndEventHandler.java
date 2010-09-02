/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.ActivityEndEvent;

/**
 * Annotation for a JAIN SLEE 1.1 {@link ActivityEndEvent} type, an alternative to the usage of generic {@link EventHandler}.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityEndEventHandler {

	/**
	 * indicates if the event is defined as initial, default is false
	 * @return
	 */
	boolean initialEvent() default false;
	
	/**
	 * defines the variables that should be included in the convergence name calculation performed by the SLEE when considering an initial event for a service's root SBB.
	 * @return
	 */
	InitialEventSelect[] initialEventSelect() default { InitialEventSelect.ActivityContext };
	
	/**
	 * contains the name of an initial event selector method defined in the SBB abstract class.
	 * @return
	 */
	String initialEventSelectorMethod() default "";
	
	/**
	 * 
	 * @return
	 */
	String eventResourceOption() default "";
	
	/**
	 * 
	 * @return
	 */
	boolean maskOnAttach() default false;
	
}
