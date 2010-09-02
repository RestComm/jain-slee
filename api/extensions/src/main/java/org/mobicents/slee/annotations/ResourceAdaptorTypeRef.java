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
 * Annotation for a JAIN SLEE 1.1 RA Type reference.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAdaptorTypeRef {

	/**
	 * the RA type name
	 * @return
	 */
	String name();
	
	/**
	 * the RA type vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the RA type version
	 * @return
	 */
	String version();
	
}
