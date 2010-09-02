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
 * Annotation for a JAIN SLEE 1.1 Sbb abstract class, defines a ref to another Sbb.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SbbRef {

	/**
	 * the sbb name
	 * @return
	 */
	String name();
	
	/**
	 * the sbb vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the sbb version
	 * @return
	 */
	String version();
	
	/**
	 * the alias for the sbb ref
	 * @return
	 */
	String alias();
}
