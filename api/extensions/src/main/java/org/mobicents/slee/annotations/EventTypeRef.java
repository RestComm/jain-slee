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
 * Annotation for a JAIN SLEE 1.1 event type reference.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTypeRef {

	/**
	 * the event type name
	 * @return
	 */
	String name();
	
	/**
	 * the event type vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the event type version
	 * @return
	 */
	String version();
	
}
