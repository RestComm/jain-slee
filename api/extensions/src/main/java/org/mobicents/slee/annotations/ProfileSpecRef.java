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
 * Annotation for a JAIN SLEE 1.1 Profile Spec Reference, to be used in classes annotated by {@link Sbb} or ProfileAbstractClass.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileSpecRef {

	/**
	 * the profile spec name
	 * @return
	 */
	String name();
	
	/**
	 * the profile spec vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the profile spec version
	 * @return
	 */
	String version();
	
	/**
	 * the alias for the profile spec ref
	 * @return
	 */
	String alias();
}
