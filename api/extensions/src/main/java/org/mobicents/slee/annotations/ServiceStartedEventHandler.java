/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.serviceactivity.ServiceStartedEvent;

/**
 * Annotation for a JAIN SLEE 1.1 {@link ServiceStartedEvent} type, an alternative to the XML descriptor.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceStartedEventHandler {

	/**
	 * indicates if the event is defined as initial, default is true
	 * @return
	 */
	boolean initialEvent() default true;
	
	/**
	 * 
	 * @return
	 */
	InitialEventSelect[] initialEventSelect() default { InitialEventSelect.ActivityContext };
	
	/**
	 * 
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
