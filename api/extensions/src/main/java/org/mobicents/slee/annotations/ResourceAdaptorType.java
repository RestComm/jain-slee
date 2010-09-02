/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.ActivityContextInterface;

/**
 * Annotation for a JAIN SLEE 1.1 RA Type.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAdaptorType {

	/**
	 * the RA Type name
	 * @return
	 */
	String name();
	
	/**
	 * the RA Type vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the RA Type version
	 * @return
	 */
	String version();
	
	/**
	 * 
	 * @return
	 */
	Class<?>[] activities() default {};
	
	/**
	 * 
	 * @return
	 */
	EventTypeRef[] events() default {};
	
	/**
	 * the optional (in case no activity objects defined) {@link ActivityContextInterface} factory interface
	 * @return
	 */
	Class<?> aciFactory() default Object.class;
	
	/**
	 * the optional sbb interface
	 * @return
	 */
	Class<?> sbbInterface() default Object.class;
	
	/**
	 * 
	 * @return
	 */
	LibraryRef[] libraryRefs() default {};
	
}
