/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.Sbb;

/**
 * Annotation for a JAIN SLEE 1.1 Service.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

	/**
	 * the service name
	 * @return
	 */
	String name();
	
	/**
	 * the service vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the service version
	 * @return
	 */
	String version();
	
	/**
	 * the ref to the root sbb
	 * @return
	 */
	Class<? extends Sbb> rootSbb();
	
	/**
	 * the default priority, 0 if not defined
	 * @return
	 */
	byte defaultPriority() default 0;
	
	/**
	 * optional, the address profile table
	 * @return
	 */
	String addressProfileTable() default "";
	
	/**
	 * optional, overriding sbb config properties
	 * @return
	 */
	ServiceConfigProperties[] properties() default {};
}
