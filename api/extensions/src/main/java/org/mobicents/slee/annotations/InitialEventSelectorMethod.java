/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A JAIN SLEE 1.1 annotation that tags a method to be used as initial event
 * selector method for one or more event types.
 * 
 * @author martins
 * 
 */
@Documented
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InitialEventSelectorMethod {

	/**
	 * the event types that use the method as initial event selector
	 * 
	 * @return
	 */
	EventTypeRef[] value();

}
