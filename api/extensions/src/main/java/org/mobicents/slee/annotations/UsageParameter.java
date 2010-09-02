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
 * Annotation for a JAIN SLEE 1.1 Usage Parameter.
 * 
 * @author martins
 * 
 */
@Documented
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsageParameter {

	/**
	 * defines the initial state of the notification generation flag for the
	 * usage parameter
	 * 
	 * @return
	 */
	boolean notificationsEnabled() default false;

}
